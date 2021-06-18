package st_mary_hospital.jerry;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class St_MaryDriver {

	public static void main(String[] args) {

		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

			if ("Windows Classic".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		new Hospital_HomePage();

		createDatabase();
		createTables();
		// drugOrderUPdate();
	}

	private static void createDatabase() {
		Connection dbConn = null;
		String mysqlUrl ="jdbc:mysql://192.168.111.3:3306";
		 String name ="stmary";
		String pass ="stmary";
		
//		String mysqlUrl ="jdbc:mysql://127.0.0.1:3305";
//		String name ="root";
//		String pass ="";
		String useDB = "USE st_MaryDatabase";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

			dbConn = DriverManager.getConnection(mysqlUrl, name, pass);
			Statement st = dbConn.createStatement();
			st.execute("Create Database if not exists st_MaryDatabase");
			st.execute(useDB);

			System.out.println("Database created successfully...");
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println("Database creation error\n");
		}

	}

	private static void createTables() {

		// folders
		String dbURL_location = "C:\\St_Mary_Flies\\TallyCard\\InvoiceFiles\\registerList\\";
		String saleInvoiceLocation = "C:\\St_Mary_Flies\\StMarySale\\InvoiceFiles\\";

		File newFile = new File(dbURL_location);
		File invoiceFile = new File(saleInvoiceLocation);
		if (!newFile.exists()) {
			newFile.mkdirs();
		}
		//
		if (!invoiceFile.exists()) {
			invoiceFile.mkdirs();
		}

		// table creation
		PreparedStatement regPs = null, drugPs = null, invPs = null, orderPs = null, soldPs, patientSalePs = null,
				deleteInv_his_ps = null, userPs = null,orderRmPs=null;
		String regTable = "Create Table if not exists patient_register ( " + "patient_id Varchar(45) NOT NULL ,"
				+ "surname VARCHAR(45) NOT NULL, other_name VARCHAR(45) NOT NULL, occupation VARCHAR(45),"
				+ " age INT NOT NULL, gender VARCHAR(45) NOT NULL, phone_no VARCHAR(45), address VARCHAR(100) NOT NULL"
				+ ",  reg_date VARCHAR(45) NOT NULL, PRIMARY KEY(patient_id));";
		//
		// -----------------------------------------------------------------------------------------------------------

		// drug details
		String drugDetail = "CREATE TABLE if not exists `st_marydatabase`.`drug_details` "
				+ "( `drugId` INT NOT NULL , `Description` VARCHAR(45) , Class VARCHAR(45) NOT NULL, Unit VARCHAR(45)"
				+ " NOT NULL, "
				+ "`category` VARCHAR(45) NOT NULL ,`drug_location` VARCHAR(45) NOT NULL, Daily_Date VARCHAR(45) NOT NULL,"
				+ " PRIMARY KEY (`drugId`))";
		// -----------------------------------------------------------------------------------------------------------

		String invTable = "create Table if Not exists drug_inventory (inventory_id Int not null, drugId INT NOT NULL,"
				+ "Description "
				+ " VARCHAR (45) NOT NULL, Class VARCHAR(45) NOT NULL,category VARCHAR(45) NOT NULL, quantity INT NOT NULL, "
				+ "sellingPrice double,"
				+ "batchNo VARCHAR(45) , voucherNo VARCHAR(45) NOT NULL,Supplier VARCHAR(45) NOT NULL,"
				+ " Re_Order_Qtty INT," + " expiryDate VARCHAR(45) NOT NULL, " + "invDate VARCHAR(45) NOT NULL,"
				+ " PRIMARY KEY(inventory_id), FOREIGN KEY(drugId) REFERENCES drug_details(drugId) "
				+ "ON DELETE CASCADE ON UPDATE CASCADE)";

		// ----------------------------------------------------------------------------------------------------
		String inv_historyQry = "create Table if Not exists inventory_deleteHistory (s_No INT NOT NULL,"
				+ "drugId INT NOT NULL," + "Description "
				+ " VARCHAR (45) NOT NULL, Class VARCHAR(45) NOT NULL,category VARCHAR(45) NOT NULL, quantity INT NOT NULL, "
				+ "sellingPrice double,"
				+ "batchNo VARCHAR(45) , voucherNo VARCHAR(45) NOT NULL,Supplier VARCHAR(45) NOT NULL,"
				+ " Re_Order_Qtty INT," + " expiryDate VARCHAR(45) NOT NULL, " + "invDate VARCHAR(45) NOT NULL)";
		// -----------------------------------------------------------------------------------------------------------

		String counterOrderQry = "Create table if not exists counterDrugOrderTable (order_id int, "
				+ "drugNameorder VARCHAR(45) NOT NULL, category VARCHAR(45) NOT NULL,"
				+ "unitPrice double NOT NULL,quantityDemanded INT NOT NULL,sold_qtty INT NOT NULL," + " whom_Recieved VARCHAR(45) NOT NULL,"
				+ " to_whom_issued VARCHAR (45) NOT NULL, expiryDate VARCHAR(45) NOT NULL, "
				+ "orderDate VARCHAR (45) NOT NULL,"
				+ "FOREIGN KEY(order_id) REFERENCES drug_inventory(inventory_id)"
				+ " ON DELETE CASCADE ON UPDATE CASCADE)";
		//-------------------------------------------------------------
		String counterOrderQryRemove = "Create table if not exists counterDrugOrderTableRemove (order_id int, "
				+ "drugNameorder VARCHAR(45) NOT NULL, category VARCHAR(45) NOT NULL,"
				+ "unitPrice double NOT NULL,quantityDemanded INT NOT NULL,sold_qtty INT NOT NULL,"
				+ "" + " whom_Recieved VARCHAR(45) NOT NULL,"
				+ " to_whom_issued VARCHAR (45) NOT NULL,"
				+ " expiryDate VARCHAR(45) NOT NULL, "
				+ "orderDate VARCHAR (45) NOT NULL, FOREIGN KEY(order_id) "
				+ "REFERENCES drug_inventory(inventory_id)"
				+ "ON DELETE CASCADE ON UPDATE CASCADE)";
		// -----------------------------------------------------------------------------------------------------------
		String saleInvoiceQry = "Create Table If Not Exists saleInvoice_Table ( drugId INT NOT NULL,"
				+ "drugNameSold VARCHAR (45) NOT NULL," + " quantitySold INTEGER NOT NULL, unitPrice DOUBLE NOT NULL,"
				+ "total_Per_Item DOUBLE NOT NULL,expiryDate VARCHAR(45)" + " NOT NULL, "
				+ "invoiceDate VARCHAR(45) NOT NULL,FOREIGN KEY(drugId) REFERENCES drug_inventory(inventory_id)"
				+ " ON DELETE CASCADE ON UPDATE CASCADE)";
		// -----------------------------------------------------------------------------------------------------------
		String patientPaid = "Create Table if not exists Patient_Invoice (idNo INT NOT NULL,"
				+ "patient_Id VARCHAR(45) NOT NULL, patient_Name"
				+ " VARCHAR(45) NOT NULL, amount_paid DOUBLE NOT NULL, visit_Date VARCHAR(45) NOT NULL,"
				+ " PRIMARY KEY(idNo))";
		// -----------------------------------------------------------------------------------------------------------

		String users = "Create table if not exists user_table (u_id int not null, userName VARCHAR(45) NOT NULL UNIQUE,"
				+ "position VARCHAR (45) NOT NULL, password VARCHAR(45) NOT NULL, PRIMARY KEY(u_id))";
		// -----------------------------------------------------------------------------------------------------------

		try {
			orderPs = St_MaryConnection.getConnection().prepareStatement(counterOrderQry);
			orderRmPs = St_MaryConnection.getConnection().prepareStatement(counterOrderQryRemove);
			regPs = St_MaryConnection.getConnection().prepareStatement(regTable);
			drugPs = St_MaryConnection.getConnection().prepareStatement(drugDetail);
			invPs = St_MaryConnection.getConnection().prepareStatement(invTable);
			soldPs = St_MaryConnection.getConnection().prepareStatement(saleInvoiceQry);
			patientSalePs = St_MaryConnection.getConnection().prepareStatement(patientPaid);
			deleteInv_his_ps = St_MaryConnection.getConnection().prepareStatement(inv_historyQry);
			userPs = St_MaryConnection.getConnection().prepareStatement(users);

			drugPs.execute();// should come first
			invPs.execute(); // should come second
			orderPs.execute();//
			orderRmPs.execute();//
			
			soldPs.execute();//
			deleteInv_his_ps.execute();
			patientSalePs.execute();//
			regPs.execute();

			userPs.execute();//
		} catch (SQLException ex) {
			System.out.println("Table creation error\n table linking in driver class");
		} catch (Exception exc) {
			System.out.println("could not... driver class page" + exc);
		}
	}

}
