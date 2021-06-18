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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class PatientDrugListDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField searchField;
	private JLabel[] addItemLabel = new JLabel[3];
	private JTextField[] inputField = new JTextField[3];
	private TableColumn tColumn;
	private DefaultTableCellRenderer cellRenderer, cellRenderer1;
	private TableRowSorter<DefaultTableModel> sorter;
	private JTable table;
	private DefaultTableModel model;
	private JButton updateB, searchB;
	private JComboBox<String> dChooser;
	private DefaultComboBoxModel<String> cModel;
	private double totalPaid = 0.0;
	private double unitPric=0;
	private int drugIdNo = 0, sNo = 0;;
	private int orderQtty = 0;
	private String drugName1 = null;
	private JLabel totalVisitNo;

	public PatientDrugListDialog() {

		this.setTitle("Patient Drug Lists");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setSize(new Dimension(990, 600));
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setLayout(new BorderLayout(8, 8));
		initUi();

	}

	private void initUi() {

		JPanel centerPanel = new JPanel();
		JPanel westPanel = new JPanel(new BorderLayout(8, 8));
		add(centerPanel, BorderLayout.CENTER);
		add(westPanel, BorderLayout.WEST);
		// -------------------------------------------------------
		centerPanel.setBackground(Color.WHITE);
		westPanel.setBackground(Color.WHITE);
		// ------------------North panel component-----------------

		JPanel northPanel = new JPanel(new GridLayout(1, 2, 2, 3));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/med_drug.png"));

		JLabel northLabel = new JLabel("<html><body><div>" + "<h2> PHARMACEUTICAL INVENTORY MANAGEMENT SYSTEM " + ""
				+ "</h2><hr/><span style=color:red>UPDATE UNIT</span></div></body></html>");

		northLabel.setFont(new Font("Aharoni", Font.BOLD, 18));
		JLabel northIconLabel = new JLabel("Drug Reversal|", northBgIcon, JLabel.CENTER);

		// This panel hold display images
		JPanel dispPanel = new JPanel();
		dispPanel.setBackground(Color.WHITE);
		dispPanel.add(northIconLabel);
		northPanel.add(dispPanel);

		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		// adding component to west panel

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchPanel.setBackground(Color.WHITE);
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(150, 30));
		searchField.setFont(new Font("David", 1, 16));
		searchField.setBorder(new LineBorder(Color.BLACK));
		JLabel searchLabel = new JLabel("Patient ID:");
		searchLabel.setFont(new Font("David", 1, 16));
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		//
		searchB = new JButton("Enter");
		searchB.setForeground(Color.BLACK);
		searchB.setBackground(Color.WHITE);
		searchB.setFont(new Font("David", 1, 18));
		searchB.setPreferredSize(new Dimension(90, 30));
		searchB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		searchB.addActionListener(new DrugSearchListener());
		searchPanel.add(searchB);

		westPanel.add(searchPanel, BorderLayout.NORTH);
		// -------------------------------------------------------

		JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));

		JLabel dateLabel2 = new JLabel("Select Visited Date:");
		dateLabel2.setForeground(new Color(0, 0, 0));
		dateLabel2.setFont(new Font("David", 1, 16));
		dateLabel2.setBorder(new LineBorder(Color.GRAY, 1));

		inputPanel.add(dateLabel2);
		//
		cModel = new DefaultComboBoxModel<String>();
		dChooser = new JComboBox<String>(cModel);
		dChooser.addItemListener(new DateSelectionListener());
		dChooser.setFont(new Font("David", 1, 16));
		dChooser.setForeground(new Color(0, 0, 0));
		dChooser.setBorder(new LineBorder(Color.BLACK, 2));
		inputPanel.add(dChooser);

		// -----------------------------------------------------------------c
		for (int i = 0; i < inputField.length; i++) {
			inputField[i] = new JTextField();
			addItemLabel[i] = new JLabel();
			addItemLabel[i].setHorizontalTextPosition(SwingConstants.RIGHT);
			addItemLabel[i].setForeground(new Color(0, 0, 0));
			addItemLabel[i].setFont(new Font("David", 1, 16));
			inputField[i].setFont(new Font("David", 1, 16));
			inputField[i].setBorder(new LineBorder(Color.BLACK, 1));
			inputField[i].setEditable(false);
			addItemLabel[i].setBorder(new LineBorder(new Color(204, 204, 204), 3));
			inputPanel.add(addItemLabel[i]);
			inputPanel.add(inputField[i]);
		}
		addItemLabel[0].setText("Amount Paid:");
		addItemLabel[1].setText("Drug ID:");
		addItemLabel[2].setText("Patient Name:");

		inputPanel.setBackground(Color.WHITE);
		westPanel.add(inputPanel, BorderLayout.CENTER);

		/// -------------------Buttons in west panel--------------------

		totalVisitNo = new JLabel();
		totalVisitNo.setFont(new Font("David",1,24));
		
		//
		updateB = new JButton("Update Quantity");
		updateB.addActionListener(new UpdateReturnedQttyListener());
		updateB.setToolTipText("Enter the amount of unit you want to returned.");

		JPanel southPanelW = new JPanel(new GridLayout(3, 1, 3, 3));
		southPanelW.setBackground(Color.WHITE);
		southPanelW.setBorder(new EmptyBorder(10, 10, 10, 10));
		southPanelW.add(totalVisitNo);
		southPanelW.add(updateB);
		//
		updateB.setForeground(Color.BLACK);
		updateB.setBackground(Color.WHITE);
		updateB.setFont(new Font("David", 1, 18));
		updateB.setPreferredSize(new Dimension(200, 30));
		updateB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		// -----------------------------------------------

		/*
		 * deleteB.setForeground(Color.BLACK); deleteB.setBackground(Color.WHITE);
		 * deleteB.setFont(new Font("David", 1, 18)); deleteB.setPreferredSize(new
		 * Dimension(200, 30)); deleteB.setBorder(new LineBorder(new Color(0, 194, 255),
		 * 2)); deleteB.addActionListener(new DeleteDrugAbdReplcListener());
		 */
		//
		westPanel.add(southPanelW, BorderLayout.SOUTH);
		/// --------------Center Panel component-----------------------

		// table for inserted data from input field
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 16));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 12));
		table.addMouseListener(new RowClickListener());

		String tableColumn[] = { "S/No", "drugId", "Description", "Quantity", "UnitPrice", "TotalPrice", "ExpiryDate",
				"OrderDate" };
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar3 = new JScrollPane(table);
		scrollBar3.setPreferredSize(new Dimension(600, 400));
		//
		cellRenderer1 = new DefaultTableCellRenderer();
		cellRenderer1.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(320);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		//
		tColumn = table.getColumnModel().getColumn(3);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.YELLOW, Color.BLACK));
		//
		tColumn = table.getColumnModel().getColumn(4);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.PINK, Color.BLACK));
		/*
		 * // JPanel tablePanel = new JPanel(new GridLayout()); //
		 * tablePanel.add(scrollBar3);
		 */
		centerPanel.add(scrollBar3);

		JLabel myName = new JLabel("Designed By: JerrySoft Inc.");
		myName.setFont(new Font("Forte", 1, 18));
		myName.setForeground(Color.BLACK);
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(Color.GRAY);
		southPanel.add(myName);
		add(southPanel, BorderLayout.SOUTH);

	}

	// patient drug search class
	private class DrugSearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (searchField.getText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "No patient ID found");

			} else {
				model.setRowCount(0);
				cModel.removeAllElements();
				searchPaitentId();// Load drug List from db
				dateOfvisit();// populate visit date on combo box
				countPatientVisit();//total visit per patient

				//
				// delete qtty<=0
				deleteZeroQtty();
				//
				totalPaid = 0.0;
				totalPaid();// Amount paid

			}
		}

	}

	// search patients drug list
	private void searchPaitentId() {

		String drugName = "";
		int qtty = 0;
		double unitPrice = 0.0;
		double totalPrice = 0.0;
		int drugId = 0, idNo = 0;

		PreparedStatement ps = null;
		ResultSet rs = null;
		String expireDate = "", orderDate = "";
		try {
			ps = St_MaryConnection.getConnection()
					.prepareStatement("Select * From patientdruglisttable where patientId=?");

			ps.setString(1, searchField.getText());
			rs = ps.executeQuery();
			while (rs.next()) {
				drugName = rs.getString("drugNameorder");
				qtty = rs.getInt("quantityDemanded");
				unitPrice = rs.getDouble("unitPrice");
				totalPrice = rs.getDouble("totalprice");
				drugId = rs.getInt("drugId");
				idNo = rs.getInt("ID");
				expireDate = rs.getString("expiryDate");
				orderDate = rs.getString("orderDate");

				model.addRow(new String[] { "" + idNo, "" + drugId, drugName, "" + qtty, "" + unitPrice,
						"" + totalPrice, expireDate, orderDate });

			}

		} catch (SQLException exc) {
			exc.printStackTrace();
		}
	}
	// select all date of visit

	private void dateOfvisit() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select DISTINCT orderDate From patientdruglisttable where patientId=?");

			ps.setString(1, searchField.getText());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String visitDate = rs.getString("orderDate");

				cModel.addElement(visitDate);
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
	}

	// Listening class combo box for date selection
	private class DateSelectionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg0) {

			if (dChooser.getSelectedIndex() != -1) {

				model.setRowCount(0);
				// searchPaitentId();// Load drug List from db
				searchPaitentDateAndId(searchField.getText(), cModel.getSelectedItem().toString());

				// delete qtty<=0
				deleteZeroQtty();
				//
				totalPaid = 0.0;
				totalPaid();// Amount paid

			} else {
				System.out.println("No date is selected...");

			}
		}

	}

	// search patients drug list
	private void searchPaitentDateAndId(String patientId, String date1) {

		String drugName = "";
		int qtty = 0;
		double unitPrice = 0.0;
		double totalPrice = 0.0;
		int drugId = 0, idNo = 0;
		String expireDate = "";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = St_MaryConnection.getConnection()
					.prepareStatement("Select * From patientdruglisttable " + "where patientId=? AND orderDate=?");

			ps.setString(1, patientId);
			ps.setString(2, date1);
			rs = ps.executeQuery();
			while (rs.next()) {
				drugName = rs.getString("drugNameorder");
				qtty = rs.getInt("quantityDemanded");
				unitPrice = rs.getDouble("unitPrice");
				totalPrice = rs.getDouble("totalprice");
				drugId = rs.getInt("drugId");
				idNo = rs.getInt("ID");
				expireDate = rs.getString("expiryDate");

				model.addRow(new String[] { "" + idNo, "" + drugId, drugName, "" + qtty, "" + unitPrice,
						"" + totalPrice, expireDate, date1 });
			}

		} catch (SQLException exc) {
			exc.printStackTrace();
		}
	}

	// delete quantity that is <=0 from patient drug list table
	private void deleteZeroQtty() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Delete from patientdruglisttable where quantityDemanded<=0");

			ps.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Total Amount paid on a given date

	private void totalPaid() {
		for (int i = 0; i < table.getRowCount(); i++) {

			int qttySold = Integer.parseInt(table.getValueAt(i, 3) + "");
			double unitPrice = Double.parseDouble(table.getValueAt(i, 4) + "");
			totalPaid += (unitPrice * qttySold);
		}
		NumberFormat numberfmt = NumberFormat.getInstance();

		inputField[0].setText("0.00");
		inputField[0].setText("" + numberfmt.format(totalPaid) + " (Naira)");// display the total amount
																				// on jLabel at south
																				// panel
		searchPaitentName();// call patient name
	}

	// search patients name
	private void searchPaitentName() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String patientNam = "";
		try {
			ps = St_MaryConnection.getConnection()
					.prepareStatement("SELECT patient_Name FROM `patient_invoice` where patient_Id=?");

			ps.setString(1, searchField.getText());
			rs = ps.executeQuery();
			while (rs.next()) {

				patientNam = rs.getString("patient_Name");
			}
			inputField[2].setText(patientNam);
			inputField[2].setEditable(false);

		} catch (SQLException exc) {
			exc.printStackTrace();
		}
	}
	// Row click listener class of table

	private class RowClickListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {

			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();
				// String []text = (String[]) table.getinventoryTableAt(row, col);
				try {
				sNo = Integer.parseInt((String) targetCell.getValueAt(row, 0));
				drugIdNo = Integer.parseInt((String) targetCell.getValueAt(row, 1));
				drugName1 = ((String) targetCell.getValueAt(row, 2));
				orderQtty = Integer.parseInt((String) targetCell.getValueAt(row, 3));
				unitPric = Double.parseDouble((String) targetCell.getValueAt(row, 4));
				inputField[1].setText("" + drugIdNo);
				}catch(NumberFormatException nfe) {
					System.out.println(nfe.getMessage());
				}

			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	// update already sold drug quantity back
	private class UpdateReturnedQttyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			int row = table.getSelectedRow();
			if (row < 0) {

				JOptionPane.showMessageDialog(null, "Click on the drug you want to update....");
			} else {
				OrderQuantityUpdate oqu = new OrderQuantityUpdate(sNo, drugIdNo, drugName1,orderQtty,unitPric);
				oqu.setVisible(true);

			}
		}

	}



	// count the number of visit of a patient to the hospital, using his/her id
	private void countPatientVisit() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(""
					+ "Select COUNT(orderDate) as visit from patientdruglisttable where patientId =?");
			
			ps.setString(1, searchField.getText());
			ResultSet rs =ps.executeQuery();
			while(rs.next()) {
				int visitNo = rs.getInt("visit");
				totalVisitNo.setText("Total Visit: "+visitNo);
			}
		}catch(SQLException exc) {
			exc.printStackTrace();
		}
	}
}
