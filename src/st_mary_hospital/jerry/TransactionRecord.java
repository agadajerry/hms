package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import net.proteanit.sql.DbUtils;

public class TransactionRecord extends JPanel {
	private JComboBox<String> dateBox;
	public static DefaultComboBoxModel<String> comboModel;
	private JTable table;
	private DefaultTableModel model;
	private JLabel totalLabel;
	private double amountTotal = 0;
	private JButton okB;

	//
	public TransactionRecord() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/daily.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		// center panel for date and table for record display
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Color.WHITE);
		JPanel cNorthPanel = new JPanel(new FlowLayout());
		cNorthPanel.setBackground(Color.WHITE);
		JLabel dateComboLabel = new JLabel("Daily Report Date");
		dateComboLabel.setFont(new Font("David", 1, 18));
		dateComboLabel.setForeground(Color.BLACK);
		cNorthPanel.add(dateComboLabel);
		//
		comboModel = new DefaultComboBoxModel<String>();
		dateBox = new JComboBox<String>(comboModel);
		dateBox.setPreferredSize(new Dimension(200, 30));
		dateBox.setBorder(new LineBorder(Color.GRAY,1));
		AutoCompleteDecorator.decorate(dateBox);
		dateBox.addItemListener(new DateSelectionListener());
		cNorthPanel.add(dateBox);
		//
		okB = new JButton("Calculate");
		okB.setPreferredSize(new Dimension(100,30));
		//okB.setBackground(Color.GRAY);
		okB.setForeground(Color.BLACK);
		okB.addActionListener(new SumAllPurchaseListener());
		cNorthPanel.add(okB);
		//
		centerPanel.add(cNorthPanel, BorderLayout.NORTH);
		// table for inserted data from input field
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 16));

		// table.addMouseListener(new TextFieldGetTextListener());

		String tableColumn[] = { "PATIENT ID", "NAME", "AMOUNT PAID", "VISITED DATE" };
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar = new JScrollPane(table);
		scrollBar.setPreferredSize(new Dimension(1100, 230));
		//
		JPanel tablePanel = new JPanel(new GridLayout());
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(new EmptyBorder(10,10,10,10));
		// tablePanel.add(operationBtnPanel,BorderLayout.NORTH);
		tablePanel.add(scrollBar);
		centerPanel.add(tablePanel, BorderLayout.CENTER);

		// south panel containing all the information the transaction made
		JPanel southPanel = new JPanel(new GridLayout(1, 2));
		southPanel.setBorder(new EmptyBorder(10,10,10,10));

		JPanel totalPanel = new JPanel();
		totalLabel = new JLabel("Total");
		totalLabel.setFont(new Font("David", Font.BOLD, 23));
		totalLabel.setForeground(Color.WHITE);
		totalPanel.add(totalLabel);
		totalPanel.setBackground(Color.GRAY);

		southPanel.add(totalPanel);
		//
		centerPanel.add(southPanel, BorderLayout.SOUTH);

		//

		add(centerPanel, BorderLayout.CENTER);

		// visit date
		patientDateVisted();
	}// end of constructor

	// show patient daily amount paid
	private void patientAmountPaid(String dateVis) {
		String dateV = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		double amount = 0.0;
		String qry = "Select patient_id, patient_Name,amount_paid, visit_Date from patient_invoice "
				+ " where visit_Date=?  order by visit_Date desc";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ps.setString(1, dateVis);
			rs = ps.executeQuery();

			while (rs.next()) {
				String pId = rs.getString("patient_id");
				String pName = rs.getString("patient_Name");
				amount = rs.getDouble("amount_paid");
				dateV = rs.getString("visit_Date");
				model.addRow(new String[] { pId, pName, "" + amount, dateV });

			}
			//
			
		} catch (SQLException ex) {
			System.out.println("Error in getting customer pay");
		}
	}

	//
	// date selection class
	private class DateSelectionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg0) {

			if (dateBox.getSelectedIndex() != -1) {
				amountTotal = 0;// reset
				model.setRowCount(0);// reset table first
				patientAmountPaid(dateBox.getSelectedItem().toString());
				totalLabel.setText("Total amount: " + amountTotal);

			}
		}

	}

	// show patient daily amount paid
	public static void patientDateVisted() {
		// String dateV="";
		PreparedStatement ps = null;
		ResultSet rs = null;
		String qry = "Select Distinct visit_Date from patient_invoice order by visit_Date desc";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);

			rs = ps.executeQuery();
			// table.setModel(DbUtils.resultSetToTableModel(rs));

			while (rs.next()) {

				String dateV = rs.getString("visit_Date");

				comboModel.addElement(dateV);

			}
		} catch (SQLException ex) {
			System.out.println("Error in getting customer date visit");
		}
	}

	// sum all item purchase on a particular date
	private class SumAllPurchaseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			double totalsum = 0.0;
			if (table.getRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "The table is empty. No transaction is made yet.");
			} else {

				for (int i = 0; i < table.getRowCount(); i++) {

					totalsum += Double.parseDouble((String) table.getValueAt(i, 2));
					//
					//
					NumberFormat nf = NumberFormat.getInstance();
				//	amountTotal += amount;
					totalLabel.setText("Total amount: " + nf.format(totalsum) + " [Naira]");
				}

			}
		}

	}
}
