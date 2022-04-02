package dbYear4;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.sql.*;

@SuppressWarnings("serial")
public class JDBCMainWindowContent extends JInternalFrame implements ActionListener
{	
	String cmd = null;

	// DB Connectivity Attributes
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;

	private Container content;

	private JPanel charactersPanel;
	private JPanel exportButtonPanel;
	private JPanel extraButtonPanel;
	//private JPanel exportConceptDataPanel;
	private JScrollPane dbContentsPanel;

	private Border lineBorder;
	
	String op[] = {"Orders", "Employees","Stock"};
	String table = "Orders";
	private JComboBox options = new JComboBox(op);

	private JLabel head1 =new JLabel("Orders");
	private JLabel head2 =new JLabel("");
	private JLabel IDLabel=new JLabel("OrderID:");
	private JLabel secondLabel=new JLabel("EmployeeID:");
	private JLabel thirdLabel=new JLabel("PaperID:");
	private JLabel fourthLabel=new JLabel("FirstName:");
	private JLabel fifthLabel=new JLabel("LastName:");
	private JLabel sixthLabel=new JLabel("Phone No");
	private JLabel seventhLabel=new JLabel("Quantity:");

	private JTextField IDTF= new JTextField(10);
	private JTextField secondTF= new JTextField(10);
	private JTextField thirdTF=new JTextField(10);
	private JTextField fourthTF=new JTextField(10);
	private JTextField fifthTF=new JTextField(10);
	private JTextField sixthTF=new JTextField(10);
	private JTextField seventhTF=new JTextField(10);


	private static QueryTableModel TableModel = new QueryTableModel();
	//Add the models to JTabels
	private JTable TableofDBContents=new JTable(TableModel);
	//Buttons for inserting, and updating members
	//also a clear button to clear characters panel
	private JButton updateButton = new JButton("Update");
	private JButton insertButton = new JButton("Insert");
	private JButton exportButton  = new JButton("Export");
	private JButton deleteButton  = new JButton("Delete");
	private JButton clearButton  = new JButton("Clear");
	private JButton optionButton  = new JButton("Show");
	private JButton empChart = new JButton("Employee Sales Chart");
	private JButton papChart = new JButton("Paper Sales Chart");

	private JButton  EmpClients = new JButton("FindEmpClientsByID:");
	private JTextField EmpClientsTF  = new JTextField(12);
	private JButton sortOrders  = new JButton("FindBiggerOrderQuantity");
	private JTextField sortOrdersTF  = new JTextField(12);
	private JButton ListAllEmp  = new JButton("ListAllEmp");
	private JButton ListAllStock  = new JButton("ListAllStock");
	private JButton EmpOrders  = new JButton("GetEmpByOrdID");
	private JTextField EmpOrdersTF  = new JTextField(12);
	private JTextField EmpOrdersResult  = new JTextField(12);
	private JButton SalePrice  = new JButton("GetSaleByOrdID");
	private JTextField SalePriceTF  = new JTextField(12);
	private JTextField SalePriceResult  = new JTextField(12);



	public JDBCMainWindowContent( String aTitle)
	{	
		//setting up the GUI
		super(aTitle, false,false,false,false);
		setEnabled(true);

		initiate_db_conn();
		//add the 'main' panel to the Internal Frame
		content=getContentPane();
		content.setLayout(null);
		content.setBackground(Color.lightGray);
		lineBorder = BorderFactory.createEtchedBorder(15, Color.red, Color.black);

		//setup characters panel and add the components to it
		charactersPanel=new JPanel();
		charactersPanel.setLayout(new GridLayout(11,2));
		charactersPanel.setBackground(Color.lightGray);
		charactersPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "CRUD Actions"));

		charactersPanel.add(head1);
		charactersPanel.add(head2);
		charactersPanel.add(IDLabel);			
		charactersPanel.add(IDTF);
		charactersPanel.add(secondLabel);			
		charactersPanel.add(secondTF);
		charactersPanel.add(thirdLabel);		
		charactersPanel.add(thirdTF);
		charactersPanel.add(fourthLabel);		
		charactersPanel.add(fourthTF);
		charactersPanel.add(fifthLabel);	
		charactersPanel.add(fifthTF);
		charactersPanel.add(sixthLabel);		
		charactersPanel.add(sixthTF);
		charactersPanel.add(seventhLabel);
		charactersPanel.add(seventhTF);
		charactersPanel.add(options);
		charactersPanel.add(optionButton);
		
		fifthLabel.show();
		sixthLabel.show();
		seventhLabel.show();
		fifthTF.show();
		sixthTF.show();
		seventhTF.show();

		//setup characters panel and add the components to it
		exportButtonPanel=new JPanel();
		exportButtonPanel.setLayout(new GridLayout(4,2));
		exportButtonPanel.setBackground(Color.lightGray);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));
		exportButtonPanel.add(EmpClients);
		exportButtonPanel.add(EmpClientsTF);
		exportButtonPanel.add(sortOrders);
		exportButtonPanel.add(sortOrdersTF);
		exportButtonPanel.add(ListAllEmp);
		exportButtonPanel.add(ListAllStock);

		EmpOrdersResult.setEnabled(false);
		SalePriceResult.setEnabled(false);

		extraButtonPanel=new JPanel();
		extraButtonPanel.setLayout(new GridLayout(3,3));
		extraButtonPanel.setBackground(Color.lightGray);
		extraButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Extra Data"));
		extraButtonPanel.add(EmpOrders);
		extraButtonPanel.add(EmpOrdersTF);
		extraButtonPanel.add(EmpOrdersResult);
		extraButtonPanel.add(SalePrice);
		extraButtonPanel.add(SalePriceTF);
		extraButtonPanel.add(SalePriceResult);
		extraButtonPanel.add(empChart);
		extraButtonPanel.add(papChart);

		insertButton.setSize(100, 30);
		updateButton.setSize(100, 30);
		exportButton.setSize (100, 30);
		deleteButton.setSize (100, 30);
		clearButton.setSize (100, 30);

		insertButton.setLocation(370, 10);
		updateButton.setLocation(370, 110);
		exportButton.setLocation (370, 160);
		deleteButton.setLocation (370, 60);
		clearButton.setLocation (370, 210);

		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		exportButton.addActionListener(this);
		deleteButton.addActionListener(this);
		clearButton.addActionListener(this);
		optionButton.addActionListener(this);
		empChart.addActionListener(this);
		papChart.addActionListener(this);

		this.ListAllEmp.addActionListener(this);
		this.ListAllStock.addActionListener(this);
		this.EmpClients.addActionListener(this);
		this.sortOrders.addActionListener(this);
		this.EmpOrders.addActionListener(this);
		this.SalePrice.addActionListener(this);


		content.add(insertButton);
		content.add(updateButton);
		content.add(exportButton);
		content.add(deleteButton);
		content.add(clearButton);


		TableofDBContents.setPreferredScrollableViewportSize(new Dimension(900, 300));

		dbContentsPanel=new JScrollPane(TableofDBContents,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dbContentsPanel.setBackground(Color.lightGray);
		dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder,"Database Content"));

		charactersPanel.setSize(360, 300);
		charactersPanel.setLocation(3,0);
		dbContentsPanel.setSize(700, 300);
		dbContentsPanel.setLocation(477, 0);
		exportButtonPanel.setSize(500, 200);
		exportButtonPanel.setLocation(3, 300);
		extraButtonPanel.setSize(500, 200);
		extraButtonPanel.setLocation(670, 300);
		
		content.add(exportButtonPanel);
		content.add(charactersPanel);
		content.add(dbContentsPanel);
		content.add(extraButtonPanel);

		setSize(982,645);
		setVisible(true);

		TableModel.refreshFromDB(stmt, table);
	}

	public void initiate_db_conn()
	{
		try
		{
			// Load the JConnector Driver
			Class.forName("com.mysql.jdbc.Driver");
			// Specify the DB Name
			String url="jdbc:mysql://localhost:3306/dbYear4Proj";
			// Connect to DB using DB URL, Username and password
			con = DriverManager.getConnection(url, "root", "sedo123O");
			//Create a generic statement which is passed to the TestInternalFrame1
			stmt = con.createStatement();
		}
		catch(Exception e)
		{
			System.out.println("Error: Failed to connect to database\n"+e.getMessage());
		}
	}
	
	
	public  void pieGraph(ResultSet rs, String title) {
		try {
			DefaultPieDataset dataset = new DefaultPieDataset();

			while (rs.next()) {
				String name = rs.getString(1);
				String sales = rs.getString(2);
				dataset.setValue(name+ " "+sales, new Double(sales));
			}
			JFreeChart chart = ChartFactory.createPieChart(
					title,  
					dataset,             
					false,              
					true,
					true
			);

			ChartFrame frame = new ChartFrame(title, chart);
			frame.pack();
			frame.setVisible(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {

		IDTF.setText("");
		secondTF.setText("");
		thirdTF.setText("");
		fourthTF.setText("");
		fifthTF.setText("");
		sixthTF.setText("");
		seventhTF.setText("");
	}

	//event handling 
	public void actionPerformed(ActionEvent e)
	{
		Object target=e.getSource();
		if (target == optionButton)
		{
			table = options.getSelectedItem().toString();
			
			if(table == "Orders") {
				IDLabel.setText("OrderID:");
				secondLabel.setText("EmployeeID:");
				thirdLabel.setText("PaperID:");
				fourthLabel.setText("FirstName:");
				fifthLabel.setText("LastName:");
				sixthLabel.setText("Phone No:");
				seventhLabel.setText("Quantity:");
				
				fifthLabel.show();
				sixthLabel.show();
				seventhLabel.show();
				fifthTF.show();
				sixthTF.show();
				seventhTF.show();
				
				clear();
				
			}else if(table == "Employees") {
				IDLabel.setText("EmployeeID:");
				secondLabel.setText("FirstName:");
				thirdLabel.setText("LastName:");
				fourthLabel.setText("Gender:");
				fifthLabel.setText("Age:");
				sixthLabel.setText("");
				seventhLabel.setText("");
				
				fifthLabel.show();
				sixthLabel.hide();
				seventhLabel.hide();
				fifthTF.show();
				sixthTF.hide();
				seventhTF.hide();
				
				clear();
			}else if(table == "Stock") {
				IDLabel.setText("PaperID:");
				secondLabel.setText("Size:");
				thirdLabel.setText("Colour:");
				fourthLabel.setText("Price:");
				fifthLabel.setText("");
				sixthLabel.setText("");
				seventhLabel.setText("");
				
				fifthLabel.hide();
				sixthLabel.hide();
				seventhLabel.hide();
				fifthTF.hide();
				sixthTF.hide();
				seventhTF.hide();
				
				clear();
			}
			
			head1.setText(table);
			
			TableModel.refreshFromDB(stmt, table);

		}
		
		if (target == empChart)
		{
			try
			{
				cmd = "SELECT CONCAT(employees.empfName,' ',employees.emplName) as Name, sum(orders.quantity * stock.price) as Sales \r\n" + 
						"FROM employees\r\n" + 
						"INNER JOIN orders ON employees.emp_id = orders.emp_id\r\n" + 
						"INNER JOIN stock ON stock.pap_id = orders.pap_id\r\n" + 
						"GROUP BY employees.emp_id;";
				rs= stmt.executeQuery(cmd);
				pieGraph(rs, "Employee Sales in Euros");	
			}
			catch (SQLException sqle)
			{
				System.err.println("Error with  insert:\n"+sqle.toString());
			}
		}
		
		if (target == papChart)
		{
			try
			{
				cmd = "SELECT CONCAT(stock.size,' ',stock.colour) as Type, sum(orders.quantity * stock.price) as Sales \r\n" + 
						"FROM stock\r\n" + 
						"INNER JOIN orders ON stock.pap_id = orders.pap_id group by Type;";
				rs= stmt.executeQuery(cmd);
				pieGraph(rs, "Paper Sales in Euros");	
			}
			catch (SQLException sqle)
			{
				System.err.println("Error with  insert:\n"+sqle.toString());
			}
		}
		
		if (target == clearButton)
		{
			IDTF.setText("");
			secondTF.setText("");
			thirdTF.setText("");
			fourthTF.setText("");
			fifthTF.setText("");
			sixthTF.setText("");
			seventhTF.setText("");

		}

		if (target == insertButton)
		{		 
			try
				{
				String updateTemp = "";
				
				if(table == "Orders") {
					updateTemp ="INSERT INTO "+table+" VALUES("+
							null +",'"+secondTF.getText()+"','"+thirdTF.getText()+"','"+fourthTF.getText()+"','"
							+fifthTF.getText()+"','"+sixthTF.getText()+"','"+seventhTF.getText()+"');";
					
				}else if(table == "Employees") {
					updateTemp ="INSERT INTO "+table+" VALUES("+
							null +",'"+secondTF.getText()+"','"+thirdTF.getText()+"','"+fourthTF.getText()+"','"
							+fifthTF.getText()+"');";
				}else if(table == "Stock") {
					updateTemp ="INSERT INTO "+table+" VALUES("+
							null +",'"+secondTF.getText()+"','"+thirdTF.getText()+"','"+fourthTF.getText()+"');";
				}
				

				stmt.executeUpdate(updateTemp);
			}
			catch (SQLException sqle)
			{
				System.err.println("Error with  insert:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt, table);
			}
		}
		if (target == deleteButton)
		{

			try
			{
				String updateTemp = "";
				
				if(table == "Orders") {
					updateTemp ="DELETE FROM "+table+" WHERE ord_id = "+IDTF.getText()+";"; 
				}else if(table == "Employees") {
					updateTemp ="DELETE FROM "+table+" WHERE emp_id = "+IDTF.getText()+";"; 
				}else if(table == "Stock") {
					updateTemp ="DELETE FROM "+table+" WHERE pap_id = "+IDTF.getText()+";"; 
				}
				stmt.executeUpdate(updateTemp);

			}
			catch (SQLException sqle)
			{
				System.err.println("Error with delete:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt, table);
			}
		}
		if (target == updateButton)
		{	 	
			try
			{
				String updateTemp = "";
				
				if(table == "Orders") {
					updateTemp ="UPDATE "+table+" SET " +
							"emp_id = '"+secondTF.getText()+
							"',pap_id = '"+thirdTF.getText()+
							"', cusFName = '"+fourthTF.getText()+
							"', cusLName = '"+fifthTF.getText()+
							"', cusNumber ='"+sixthTF.getText()+
							"', quantity = '"+seventhTF.getText()+
							"' where ord_id = "+IDTF.getText(); 
				}else if(table == "Employees") {
					updateTemp ="UPDATE "+table+" SET " +
							"empFName = '"+secondTF.getText()+
							"',empLName = '"+thirdTF.getText()+
							"', gender = '"+fourthTF.getText()+
							"', age = '"+fifthTF.getText()+
							"' where emp_id = "+IDTF.getText(); 
				}else if(table == "Stock") {
					updateTemp ="UPDATE "+table+" SET " +
							"size = '"+secondTF.getText()+
							"',colour = '"+thirdTF.getText()+
							"', price = '"+fourthTF.getText()+
							"' where pap_id = "+IDTF.getText(); 
				}

				stmt.executeUpdate(updateTemp);
				//these lines do nothing but the table updates when we access the db.
				rs = stmt.executeQuery("SELECT * from "+table);
				rs.next();
				rs.close();	
			}
			catch (SQLException sqle){
				System.err.println("Error with  update:\n"+sqle.toString());
			}
			finally{
				TableModel.refreshFromDB(stmt, table);
			}
		}
		if(target == exportButton){
			cmd = "select * from  "+table;

			System.out.println(cmd);
			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		} 

		/////////////////////////////////////////////////////////////////////////////////////
		//I have only added functionality of 2 of the button on the lower right of the template
		///////////////////////////////////////////////////////////////////////////////////

		if(target == this.ListAllStock){

			cmd = "select CONCAT(size, ' ', colour) as PaperType, price from stock order by price ASC;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}

		if(target == this.ListAllEmp){

			cmd = "select  distinct CONCAT(empfName, ' ', emplName) as EmployeeName from employees;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}

		if(target == this.EmpClients){
			try{
				ps = con.prepareStatement("select CONCAT(cusfName, ' ', cuslName) as CustomerName from orders where emp_id = ?");

				String empID = this.EmpClientsTF.getText();
				ps.setString(1,empID);
				rs = ps.executeQuery();	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		} 

		if(target == this.sortOrders){
			try{
				ps = con.prepareStatement("select * from orders where quantity >=  ?");

				String x = this.sortOrdersTF.getText();
				int amount = Integer.parseInt(x);
				ps.setInt(1,amount);
				rs = ps.executeQuery();	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		} 

		if(target == this.EmpOrders){
			try{
				int ordID = Integer.parseInt(this.EmpOrdersTF.getText());
				CallableStatement cstmt = con.prepareCall("{call getEmpOrders(?, ?)}");
			      cstmt.setInt(1, ordID);
			      cstmt.registerOutParameter(2, Types.VARCHAR);
			      cstmt.executeUpdate();
			      String empName = cstmt.getString(2);
				System.out.print(empName);
				
				EmpOrdersResult.setText(empName + "");
			}
			catch(Exception e1){e1.printStackTrace();}

		} 

		if(target == this.SalePrice){
			try{
				int ordID = Integer.parseInt(this.SalePriceTF.getText());
				CallableStatement cstmt = con.prepareCall("{call getCostOrder(?, ?)}");
			      cstmt.setInt(1, ordID);
			      cstmt.registerOutParameter(2, Types.INTEGER);
			      cstmt.executeUpdate();
			      int salePrice = cstmt.getInt(2);
				System.out.print(salePrice);
				
				SalePriceResult.setText(salePrice + "");
			}
			catch(Exception e1){e1.printStackTrace();}

		} 

	}

	///////////////////////////////////////////////////////////////////////////

	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == options) {
			
			table = options.getSelectedItem() +"";
			System.out.print("here");
			
			TableModel.refreshFromDB(stmt, table);
			
		}
	}
	
	
	private void writeToFile(ResultSet rs){
		try{
			System.out.println("In output.csv");
			FileWriter outputFile = new FileWriter("output.csv");
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for(int i=0;i<numColumns;i++){
				printWriter.print(rsmd.getColumnLabel(i+1)+",");
			}
			printWriter.print("\n");
			while(rs.next()){
				for(int i=0;i<numColumns;i++){
					printWriter.print(rs.getString(i+1)+",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}
