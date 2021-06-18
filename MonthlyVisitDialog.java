package st_mary.admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class MonthlyVisitDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton showB;
	private JComboBox<String> monthBox, dateBox;
	private DefaultComboBoxModel<String> monthModel, dateModel;
	private JLabel dateLabel;

	public MonthlyVisitDialog() {
		setSize(new Dimension(462, 270));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// setUndecorated(true);
		setLayout(null);
		this.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.GRAY));

		this.setLocationRelativeTo(null);
		setBackground(Color.WHITE);
		setModal(true);
		this.setResizable(false);
		initUi();

	}

	private void initUi() {


		dateModel = new DefaultComboBoxModel<String>();
		dateBox = new JComboBox<String>(dateModel);
		AutoCompleteDecorator.decorate(dateBox);

		//
		monthModel = new DefaultComboBoxModel<String>();
		monthBox = new JComboBox<String>(monthModel);
		AutoCompleteDecorator.decorate(monthBox);
		//
		showB = new JButton("Enter");
		showB.addActionListener(new MonthYearListener());
		dateLabel = new JLabel("Patients monthly visit appear here!");
		JLabel monthStat = new JLabel("<html><body><div><h2 style=color:red>"
				+ " MONTHLY VISITATION STATS<hr/></h2></div></body></html>");
		dateLabel.setForeground(Color.BLACK);
		dateLabel.setFont(new Font("DAVID", Font.BOLD, 20));
		JLabel yrLabel = new JLabel("Year");
		yrLabel.setBounds(20,5,70,30);
		dateBox.setBounds(20, 30, 150, 40);
		monthBox.setBounds(170, 30, 150, 40);
		showB.setBounds(325, 30, 100, 40);
		
		JLabel monthLabel = new JLabel("Month");
		monthLabel.setBounds(170,5,70,40);
		monthStat.setBounds(50, 100, 400, 40);
		dateLabel.setBounds(50, 160, 400, 40);
		add(dateBox);
		add(monthBox);
		add(dateLabel);
		add(monthStat);
		add(showB);
		add(yrLabel);
		add(monthLabel);
		comboDate();
		comboDate2();
	}

	private void comboDate() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlQuery = "Select DISTINCT YEAR(orderDate) AS year"
				+ " From patientmonthlyvisit1  order by YEAR(orderDate) DESC";
		try {

			ps = St_MaryConnection.getConnection().prepareStatement(sqlQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				String yearDate = rs.getString("year");
				dateModel.addElement(yearDate);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "TEAR from db " + ex.getMessage());
			ex.printStackTrace();

		}

	}

	//
	private void comboDate2() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlQuery = "Select DISTINCT MONTH(orderDate) as month"
				+ " From patientmonthlyvisit1  order by MONTH(orderDate) ASC";
		try {

			ps = St_MaryConnection.getConnection().prepareStatement(sqlQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				String monthDate = rs.getString("month");
				monthModel.addElement(monthDate);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "month from db " + ex.getMessage());
			ex.printStackTrace();

		}

	}
	
	//get all patient visit from the table of patientmonthly visit
	private void getmonthlyVist() {
		
		
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("SELECT COUNT(CASE WHEN MONTH(orderDate)=? THEN patientId END) AS month,"
							+ "COUNT(CASE WHEN YEAR(orderDate)=? THEN patientId END) AS year "
							+ " from patientmonthlyvisit1 WHERE YEAR(orderDate)=?");
			
			ps.setString(1, monthBox.getSelectedItem().toString());
			ps.setString(2, dateBox.getSelectedItem().toString());
			ps.setString(3, dateBox.getSelectedItem().toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String month = rs.getString("month");
				String yearb = rs.getString("year");
				int monthValue = Integer.parseInt(monthBox.getSelectedItem().toString());
				switch(monthValue) {
				case 1:
					dateLabel.setText(null);
					dateLabel.setText(" January Total = " + month+", "
							+ ""+dateBox.getSelectedItem().toString()+" Sum Total   =  "+yearb);
					break;
				case 2:
					dateLabel.setText(null);
					dateLabel.setText(" February Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total   =  "+yearb);
					break;
				case 3:
					dateLabel.setText(null);
					dateLabel.setText(" March Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total   =  "+yearb);
					break;
				case 4:
					dateLabel.setText(null);
					dateLabel.setText(" April Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total   =  "+yearb);
					break;
				case 5:
					dateLabel.setText(null);
					dateLabel.setText(" May Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total =  "+yearb);
					break;
				case 6:
					dateLabel.setText(null);
					dateLabel.setText(" June Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total =  "+yearb);
					break;
				case 7:
					dateLabel.setText(null);
					dateLabel.setText(" July Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total =  "+yearb);
					break;
				case 8:
					dateLabel.setText(null);
					dateLabel.setText(" August Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total =  "+yearb);
					break;
				case 9:
					dateLabel.setText(null);
					dateLabel.setText(" September Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total =  "+yearb);
					break;
				case 10:
					dateLabel.setText(null);
					dateLabel.setText(" October Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total =  "+yearb);
					break;
				case 11:
					dateLabel.setText(null);
					dateLabel.setText(" November Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total =  "+yearb);
					break;
				case 12:
					dateLabel.setText(null);
					dateLabel.setText(" December Total = " + month+","
							+ " "+dateBox.getSelectedItem().toString()+" Sum Total  =  "+yearb);
					break;
				default:
					dateLabel.setText(null);
					dateLabel.setText(" No month selected");
					
				}
				
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private class MonthYearListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if(dateBox.getSelectedIndex()==-1 || monthBox.getSelectedIndex()==-1) {
				JOptionPane.showMessageDialog(null, "One of the text fields is empty!!!!");

				
			}else {
				getmonthlyVist();
			}
			
		}
		
	}
}
