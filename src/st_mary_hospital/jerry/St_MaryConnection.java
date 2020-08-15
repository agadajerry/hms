package st_mary_hospital.jerry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class St_MaryConnection {

	public static Connection getConnection() {
		Connection conn = null;
		try {

			//String dbURL = "jdbc:sqlite:C:\\St_MAry_Flies\\InvoiceFiles\\St_MaryIctDatabase.db";
			String mysqlUrl ="jdbc:mysql://192.168.111.3:3306/st_MaryDatabase";
			 String name ="stmary";
			String pass ="stmary";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(mysqlUrl,name,pass);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println(" DB connection in connection class error");
			}

		} catch (SQLException ex) {
			System.out.println(" DB connection");
			JOptionPane.showMessageDialog(null, "Sorry, your computer system is not yet connected to database.\n"
			+ "Ensure you are connected and try again","Connection Error", JOptionPane.ERROR_MESSAGE);
		}
		return conn;
	}

}
