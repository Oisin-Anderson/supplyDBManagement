DROP DATABASE IF EXISTS dbYear4Proj;
CREATE DATABASE IF NOT EXISTS dbYear4Proj;
USE dbYear4Proj;

DROP TABLE IF EXISTS stock;

DROP TABLE IF EXISTS employees;

DROP TABLE IF EXISTS orders;

CREATE TABLE stock (
	pap_id INTEGER AUTO_INCREMENT NOT NULL,
	size VARCHAR(2) NOT NULL,
	colour VARCHAR(20) NOT NULL,
    price VARCHAR(20) NOT NULL,
	PRIMARY KEY(pap_id));
    
    
INSERT INTO stock VALUES ( null, 'A4', 'White', 5.00);
INSERT INTO stock VALUES ( null, 'A4', 'Cream', 6.00);
INSERT INTO stock VALUES ( null, 'A3', 'White', 8.00);
INSERT INTO stock VALUES ( null, 'A3', 'Cream', 9.00);
INSERT INTO stock VALUES ( null, 'A5', 'White', 3.00);
INSERT INTO stock VALUES ( null, 'A5', 'Cream', 4.00);

select * from stock;

CREATE TABLE employees (
	emp_id INTEGER AUTO_INCREMENT NOT NULL,
	empfName VARCHAR(20) NOT NULL,
	emplName VARCHAR(20) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    age int NOT NULL,
	PRIMARY KEY(emp_id));
    
    
INSERT INTO employees VALUES ( null, 'Jim', 'Halpert', 'Male', 28);
INSERT INTO employees VALUES ( null, 'Dwight', 'Schrute', 'Male', 35);
INSERT INTO employees VALUES ( null, 'Stanley', 'Hudson', 'Male', 57);
INSERT INTO employees VALUES ( null, 'Philis', 'Vance', 'Female', 45);
INSERT INTO employees VALUES ( null, 'Andy', 'Bernard', 'Male', 33);

select * from employees;


CREATE TABLE orders (
	ord_id INTEGER AUTO_INCREMENT NOT NULL,
    emp_id int,
    pap_id int,
	cusfName VARCHAR(20) NOT NULL,
	cuslName VARCHAR(20) NOT NULL,
    cusNumber VARCHAR(20) NOT NULL,
	quantity int NOT NULL,
	PRIMARY KEY(ord_id, emp_id, pap_id),
	FOREIGN KEY(emp_id) references employees(emp_id),
	FOREIGN KEY(pap_id) references stock(pap_id));

SELECT 'INSERTING DATA INTO DATABASE' as 'INFO';

INSERT INTO orders VALUES ( null, 1, 2, 'Jan', 'Levingsum', '0838172344', 20);
INSERT INTO orders VALUES ( null, 2, 4, 'Toby', 'Flenderson', '0906778798', 40);
INSERT INTO orders VALUES ( null, 4, 5, 'Kelly', 'Kapour', '0875436718', 10);
INSERT INTO orders VALUES ( null, 3, 6, 'Ryan', 'Howard', '0892347685', 5);
INSERT INTO orders VALUES ( null, 3, 1, 'Bob', 'Vance', '0835556518 ', 30);
INSERT INTO orders VALUES ( null, 2, 6, 'Micheal', 'Scott', '0875490454 ', 40);
INSERT INTO orders VALUES ( null, 5, 3, 'Creed', 'Bratton', '0851114748', 60);
INSERT INTO orders VALUES ( null, 1, 3, 'Meridith', 'Palmer', '0839125815', 24);
INSERT INTO orders VALUES ( null, 1, 3, 'Angela', 'Martin', '0873051277', 50);
INSERT INTO orders VALUES ( null, 2, 5, 'Kevin', 'Malone', '0833153419', 8);
INSERT INTO orders VALUES ( null, 4, 1, 'Daryll', 'Philbin', '0836946317', 15);
INSERT INTO orders VALUES ( null, 5, 2, 'Roy', 'Anderson', '0890454829', 4);
INSERT INTO orders VALUES ( null, 3, 2, 'David', 'Wallace', '0837089182', 29);
INSERT INTO orders VALUES ( null, 2, 1, 'Oscar', 'Martinez', '0852402884', 55);
INSERT INTO orders VALUES ( null, 1, 5, 'Erin', 'Hannon', '0833792467', 60);


select * from orders;

select * from orders where emp_id IN(select emp_id from employees) AND emp_id = 1;

SELECT CONCAT(employees.empfName,' ',employees.emplName) as Name, sum(orders.quantity * stock.price) as Sales 
FROM employees
INNER JOIN orders ON employees.emp_id = orders.emp_id
INNER JOIN stock ON stock.pap_id = orders.pap_id
GROUP BY employees.emp_id;

DROP PROCEDURE IF EXISTS getEmpOrders//
DELIMITER // 
CREATE PROCEDURE getEmpOrders (
          IN inord_id INTEGER,
          OUT out_EmpName VARCHAR(20)
          )
       BEGIN
          SELECT CONCAT(empfName, ' ', emplName) as EmployeeName
          INTO out_EmpName
          FROM employees
		  INNER JOIN orders ON employees.emp_id = orders.emp_id
		  where ord_id = inord_id;
       END //
DELIMITER ;

call getEmpOrders(1, @emp);

DROP PROCEDURE IF EXISTS getCostOrder//
DELIMITER // 
CREATE PROCEDURE getCostOrder (
          IN inord_id INTEGER,
          OUT out_TotPrice Integer
          )
       BEGIN
          SELECT sum(stock.price * orders.quantity) as TotalPrice
          INTO out_TotPrice
          FROM stock
		  INNER JOIN orders ON stock.pap_id = orders.pap_id
		  where ord_id = inord_id;
       END //
DELIMITER ;

call getCostOrder(1, @pri);
