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
import java.text.SimpleDateFormat;

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

import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;

public class DispensaryComparisionPanel extends JPanel {

	private JDateChooser chooser;
	private JTable table, table2;
	private DefaultTableModel model, model2;
	private DefaultComboBoxModel<String> expireModel;
	public static DefaultComboBoxModel<String> drugModel, yearModel;
	private JComboBox<String> expireBox, drugBox, yearBox;
	private JButton okBtn;
	private int totalQttyDemanded = 0, totalQttySold = 0;
	private JPanel qPanel[] = new JPanel[5];
	private JLabel[] statusLabel = new JLabel[10];

	public DispensaryComparisionPanel() {

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/dispensary_comp.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel, BorderLayout.NORTH);
		add(northPanel, BorderLayout.NORTH);

		//

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setBackground(Color.WHITE);
		drugModel = new DefaultComboBoxModel<String>();
		drugBox = new JComboBox<String>(drugModel);
		drugBox.setBorder(new LineBorder(Color.GRAY, 1));

		AutoCompleteDecorator.decorate(drugBox);
		drugBox.setPreferredSize(new Dimension(230, 30));
		drugBox.addItemListener(new DrugSelectionListener());
		centerPanel.add(drugBox);
		//
		expireModel = new DefaultComboBoxModel<String>();
		expireBox = new JComboBox<String>(expireModel);
		expireBox.setBorder(new LineBorder(Color.GRAY, 1));

		expireBox.setPreferredSize(new Dimension(200, 30));
		AutoCompleteDecorator.decorate(expireBox);

		//

		//
		JLabel dateLabel = new JLabel("Expiry Date");
		centerPanel.add(dateLabel);
		centerPanel.add(expireBox);

		//

		yearModel = new DefaultComboBoxModel<String>();
		yearBox = new JComboBox<String>(yearModel);
		yearBox.setPreferredSize(new Dimension(200, 30));
		yearBox.setBorder(new LineBorder(Color.GRAY, 1));

		AutoCompleteDecorator.decorate(yearBox);
		JLabel yearLabel = new JLabel("Select Year");
		centerPanel.add(yearLabel);
		centerPanel.add(yearBox);

		okBtn = new JButton("Check");
		okBtn.setBackground(Color.WHITE);
		okBtn.setBorder(new LineBorder(new Color(0, 194, 255)));
		okBtn.setForeground(Color.BLACK);
		okBtn.setFont(new Font("David", 1, 16));
		okBtn.setPreferredSize(new Dimension(100, 30));
		okBtn.addActionListener(new DailySaleListenr());
		centerPanel.add(okBtn);

		//
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
//

		String tableColumn[] = { "Quantity Demanded", "Unit Price", " order Date" };

		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar = new JScrollPane(table);
		scrollBar.setPreferredSize(new Dimension(550, 330));
		//
		JPanel tablePanel = new JPanel(new FlowLayout());
		tablePanel.setBackground(Color.WHITE);
		// tablePanel.add(operationBtnPanel,BorderLayout.NORTH);
		tablePanel.add(scrollBar);

		//
		table2 = new JTable();
		table2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table2.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table2.getTableHeader().setBackground(new Color(0, 194, 255));
		table2.getTableHeader().setForeground(Color.WHITE);
		table2.getTableHeader().setOpaque(false);
		table2.setRowHeight(30);
		table2.setForeground(Color.BLACK);
		table2.setFont(new Font("David", Font.BOLD, 16));

		// table.addMouseListener(new TextFieldGetTextListener());
//

		String tableColumn2[] = { "Quanity Sold", "Unit Price", "Total Price", "Date Sold" };

		model2 = (DefaultTableModel) table2.getModel();
		model2.setColumnIdentifiers(tableColumn2);
		JScrollPane scrollBar2 = new JScrollPane(table2);
		scrollBar2.setPreferredSize(new Dimension(550, 330));

		JPanel table2Panel = new JPanel(new BorderLayout());
		table2Panel.setBackground(Color.WHITE);
		table2Panel.add(scrollBar2, BorderLayout.NORTH);
		// info panel

		tablePanel.add(table2Panel);

		centerPanel.add(tablePanel);
		add(centerPanel, BorderLayout.CENTER);
		//

		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPanel.setBorder(new EmptyBorder(10,10,10,10));
		southPanel.setBackground(Color.WHITE);
		
		// panels for sold demanded and qtty difference

		for (int i = 0; i < qPanel.length; i++) {
			qPanel[i] = new JPanel(new GridLayout(2, 1,8,8));
			 qPanel[i].setPreferredSize(new Dimension(200,70));
			southPanel.add(qPanel[i]);
		}
		qPanel[0].setBackground(Color.RED);
		qPanel[2].setBackground(new Color(0,194,255));
		qPanel[4].setBackground(new Color(0,130,230));

		qPanel[1].setBackground(Color.CYAN);
		qPanel[3].setBackground(Color.PINK);
		//

		//southPanel.setBackground(Color.GRAY);

		//
		for (int i = 0; i < statusLabel.length; i++) {
			statusLabel[i] = new JLabel("", JLabel.CENTER);
			statusLabel[i].setFont(new Font("David", Font.BOLD, 16));
			statusLabel[i].setForeground(Color.BLACK);
			statusLabel[i].setBorder(new LineBorder(new Color(0, 194, 255), 1));
		}
		// adding label to panels
		qPanel[0].add(statusLabel[0]);
		qPanel[0].add(statusLabel[1]);
		//
		qPanel[2].add(statusLabel[2]);
		qPanel[2].add(statusLabel[3]);
		//
		qPanel[4].add(statusLabel[4]);
		qPanel[4].add(statusLabel[5]);
		//
		qPanel[1].add(statusLabel[6]);
		qPanel[1].add(statusLabel[7]);
		//
		qPanel[3].add(statusLabel[8]);
		qPanel[3].add(statusLabel[9]);
		
		
		// southPanel.add(statusLabel);
		add(southPanel, BorderLayout.SOUTH);

		// all drug order list
		allDrugList();
		// get all years that you want to display transaction partain to
		yearOfTransaction();
	}

	//
	// get all the drug name and set it on combo box
	private void allDrugList() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select Distinct drugNameOrder From counterdrugordertable");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String drugDescription = rs.getString("drugNameOrder");
				// drugModel.removeAllElements();
				drugModel.addElement(drugDescription);
			}

		} catch (SQLException ex) {
			System.out.println("Error in getting all drug list and set on drug box\n" + ex);
		}
	}

	//
	// get year of the transaction
	// get all the drug name and set it on combo box
	private void yearOfTransaction() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select Distinct YEAR(orderDate) as Year" + " From" + " counterdrugordertable");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String yearlyDetail = rs.getString("Year");

				yearModel.addElement(yearlyDetail);
			}

		} catch (SQLException ex) {
			System.out.println("Error in getting all year list and set on drug box\n" + ex);
		}
	}

	// get expire date
	private void allExpiryDateList(String drugs) {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(
					"Select Distinct " + "expiryDate From" + " counterdrugordertable" + " where drugNameOrder=?");
			ps.setString(1, drugs);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String expireDateTime = rs.getString("expiryDate");

				// drugModel.removeAllElements();
				expireModel.addElement(expireDateTime);
			}

		} catch (SQLException ex) {
			System.out.println("Error in getting all expiry date list and set on drug box\n" + ex);
		}
	}

	// drug selection class
	private class DrugSelectionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg0) {

			if (drugBox.getSelectedIndex() != -1) {
				expireModel.removeAllElements();
				allExpiryDateList(drugBox.getSelectedItem().toString());
				//
				// reset the qtty variables to zero
				totalQttyDemanded = 0;
				totalQttySold = 0;
				statusLabel[0].setText("Quantity Demanded");
				statusLabel[2].setText("Quantity sold");
				statusLabel[4].setText("Quantity Remaining");
				statusLabel[1].setText("");
				statusLabel[3].setText("");
				statusLabel[5].setText("");
				statusLabel[7].setText("");
				statusLabel[9].setText("");
				statusLabel[6].setText("Expiry Date");
				statusLabel[8].setText("Drug Name");
				//
				
				 
				 

			}
		}

	}

	// getting the drug order and sold information
	private class DailySaleListenr implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			// reset the qtty variables to zero
			totalQttyDemanded = 0;
			totalQttySold = 0;
			//
			if (expireBox.getSelectedIndex() == -1 || drugBox.getSelectedIndex() == -1
					|| yearBox.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(null, "Please select drug,expire date and year you are in.");
			} else {

				model.setRowCount(0);
				model2.setRowCount(0);
				getDrugOrderQttyDiff(drugBox.getSelectedItem().toString(), expireBox.getSelectedItem().toString(),
						yearBox.getSelectedItem().toString());

				//
				getSaleHistory(drugBox.getSelectedItem().toString(), expireBox.getSelectedItem().toString(),
						yearBox.getSelectedItem().toString());

				// totalQuantityInventory(drugBox.getSelectedItem().toString(),
				// expireBox.getSelectedItem().toString());

			}
		}

		private void getDrugOrderQttyDiff(String drug, String expire, String year) {

			String qry2 = "SELECT quantityDemanded, unitPrice,orderDate from counterdrugordertable "
					+ "where expiryDate=? AND drugNameOrder =? AND YEAR(orderDate) =?" + " AND whom_Recieved = ?";
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = St_MaryConnection.getConnection().prepareStatement(qry2);

				ps.setString(1, expire);
				ps.setString(2, drug);
				ps.setString(3, year);
				ps.setString(4, "dispensary unit");
				rs = ps.executeQuery();

				while (rs.next()) {
					int totalqDemande = rs.getInt("quantityDemanded");
					double unitp = rs.getDouble("unitPrice");
					String orderDate = rs.getString("orderDate");
					totalQttyDemanded += totalqDemande;

					statusLabel[0].setText("Quanitity Demanded:");
					statusLabel[1].setText("" + totalQttyDemanded);
					//
					/*
					 * qPanel[1].add(new JLabel("Expiry Date:")); qPanel[1].add(new JLabel(expire));
					 * qPanel[3].add(new JLabel("Drug Name:")); qPanel[3].add(new JLabel(drug));
					 */
					// qPanel[0].add(statusLabel[1]);
					
					statusLabel[6].setText("Expiry Date");
					
					String exp_date =expire.substring(0,7);//get year and month part only
					statusLabel[7].setText(exp_date);
					statusLabel[8].setText("Drug Name");
					statusLabel[9].setText(drug);



					model.addRow(new String[] { "" + totalqDemande, "" + unitp, orderDate });

				}
			} catch (SQLException ex) {
				System.out.println("Getting drug details when expire, year and drug name is selected error\n" + ex);
			}
		}

		// get the history of sale and compare to order qtty

		private void getSaleHistory(String drug, String expire, String year) {

			String qry2 = "SELECT quantitySold, unitPrice,total_per_item, invoiceDate" + " from saleinvoice_table "
					+ "where expiryDate=? AND drugNameSold =? AND YEAR(invoiceDate) =?";
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			try {
				ps1 = St_MaryConnection.getConnection().prepareStatement(qry2);

				ps1.setString(1, expire);
				ps1.setString(2, drug);
				ps1.setString(3, year);
				rs1 = ps1.executeQuery();

				while (rs1.next()) {

					int totalQ = rs1.getInt("quantitySold");
					double unitp = rs1.getDouble("unitPrice");
					double totalP = rs1.getDouble("total_per_item");
					String invoiceDate = rs1.getString("invoiceDate");

					totalQttySold += totalQ;
					//

					model2.addRow(new String[] { "" + totalQ, "" + unitp, "" + totalP, invoiceDate });

					statusLabel[5].setText("" + (totalQttyDemanded - totalQttySold));
					//
					statusLabel[2].setText("Quantity Sold");
					statusLabel[3].setText("" + totalQttySold);
//

				}
				//
				if (totalQttySold <= 0) {
					JOptionPane.showMessageDialog(null,
							"'Dispensary Unit' was not used in the tally" + " card during drug Request.\n\n"
									+ "'Dispensary unit should be used alawys as receipient for outer counter.");
				}

			} catch (SQLException ex) {
				System.out.println("Getting drug details when expire, year and drug name is selected"
						+ "from saleinoice_table error\n" + ex);
			}
		}
	}

}
