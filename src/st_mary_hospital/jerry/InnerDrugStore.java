package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.toedter.calendar.JDateChooser;

public class InnerDrugStore extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField[] inputField = new JTextField[1];
	private JLabel[] inputLabel = new JLabel[3];
	private JLabel drugCat, locationLabel;
	private JComboBox<String> catBox, locationBox, classBox, unitBox;
	public static DefaultComboBoxModel<String> catModel;
	public static DefaultComboBoxModel<String> locationModel;
	public static DefaultComboBoxModel<String> unitModel;
	private JTextField discribeDrugField, searchField, strengthField;
	private JTable table;
	private DefaultTableModel model;
	private JButton saveB, deleteB, refreshb;
	private JDateChooser createDate;
	private DefaultTableCellRenderer cellRenderer;
	private TableRowSorter<DefaultTableModel> sorter;

	public InnerDrugStore() {

		initialiseUi();
	}

	private void initialiseUi() {

		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/drug.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);

		northPanel.add(northLabel);

		add(northPanel, BorderLayout.NORTH);
		//

		JPanel centerPanel1, centerPanel2, centerPanel3;
		// ------------------------------------------------------

		// Center Panel is divided into three, the middle is used for drug computation
		JPanel centerPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		centerPanel1 = new JPanel(new GridLayout(4, 2, 8, 8));// text fields panel
		centerPanel2 = new JPanel(new GridLayout(4, 2, 8, 8));
		centerPanel2.setBorder(new EmptyBorder(10, 10, 10, 10));
		centerPanel1.setBorder(new EmptyBorder(10, 10, 10, 10));
		centerPanel3 = new JPanel(new GridLayout(1, 2));// button panel
		centerPanel1.setBackground(Color.WHITE);
		centerPanel2.setBackground(Color.WHITE);
		centerPanel3.setBackground(Color.WHITE);
		centerPanel.add(centerPanel2);
		//
		// ImageIcon cmpIcon = new
		// ImageIcon(getClass().getResource("/all/images/harma_lada.png"));
		// JLabel iconLabel = new JLabel("", cmpIcon, JLabel.CENTER);
		// centerPanel1.add(iconLabel);
		centerPanel.add(centerPanel1);
		// centerPanel.add(centerPanel3);
		add(centerPanel, BorderLayout.CENTER);

		// adding components to centerpanel2

		for (int i = 0; i < inputField.length; i++) {
			inputField[i] = new JTextField();
			inputLabel[i] = new JLabel();
			inputLabel[i].setFont(new Font("David", 1, 14));
			inputField[i].setFont(new Font("David", 1, 14));
			inputField[i].setBorder(new LineBorder(Color.GRAY, 1));
			inputField[i].setPreferredSize(new Dimension(250, 30));
			inputLabel[i].setPreferredSize(new Dimension(200, 30));
			inputLabel[i].setBorder(new LineBorder(Color.GRAY, 1));

			inputLabel[i].setForeground(new Color(0, 0, 0));
			inputLabel[i].setFont(new Font("David", 1, 14));
			centerPanel2.add(inputLabel[i]);
			centerPanel2.add(inputField[i]);

		}
		inputField[0].setEditable(false);
		// set label name on JLabels
		inputLabel[0].setText("DrugId:");

		//
		discribeDrugField = new JTextField();
		discribeDrugField.setBorder(new LineBorder(Color.GRAY, 1));
		discribeDrugField.setFont(new Font("David", 1, 14));
		JLabel discribeLabel = new JLabel("Description:");
		discribeLabel.setBorder(new LineBorder(Color.GRAY, 1));
		discribeLabel.setFont(new Font("David", 1, 14));
		centerPanel2.add(discribeLabel);
		centerPanel2.add(discribeDrugField);
		//
		//
		JLabel classLabel = new JLabel("Class:");
		classLabel.setBorder(new LineBorder(Color.GRAY, 1));

		String[] drugClass = { "Generic", "Scientific" };
		classBox = new JComboBox<String>(drugClass);
		classBox.setFont(new Font("David", 1, 16));
		classBox.setForeground(Color.BLACK);
		classBox.setBorder(new LineBorder(Color.GRAY, 1));
		classBox.setEditable(true);
		AutoCompleteDecorator.decorate(classBox);
		centerPanel2.add(classLabel);
		centerPanel2.add(classBox);
		//
		strengthField = new JTextField();
		strengthField.setBorder(new LineBorder(Color.GRAY, 1));
		strengthField.setFont(new Font("David", 1, 14));
		JLabel strengthLabel = new JLabel("Strength:");
		strengthLabel.setBorder(new LineBorder(Color.GRAY, 1));

		discribeLabel.setFont(new Font("David", 1, 14));
		centerPanel2.add(strengthLabel);
		centerPanel2.add(strengthField);
		strengthField.addFocusListener(new refreshDrugIdListener());
		//

		drugCat = new JLabel("Category");
		drugCat.setBorder(new LineBorder(Color.GRAY, 1));
		drugCat.setFont(new Font("David", 1, 16));
		drugCat.setForeground(Color.BLACK);
		catModel = new DefaultComboBoxModel<String>();
		catBox = new JComboBox<String>(catModel);
		catBox.setBorder(new LineBorder(Color.GRAY, 1));
		catBox.setEditable(true);
		AutoCompleteDecorator.decorate(catBox);
		catBox.setFont(new Font("David", 1, 14));
		centerPanel1.add(drugCat);
		centerPanel1.add(catBox);
		//

		//
		unitModel = new DefaultComboBoxModel<>();
		unitBox = new JComboBox<String>(unitModel);
		unitBox.setFont(new Font("David", 1, 14));
		unitBox.setBorder(new LineBorder(Color.GRAY, 1));
		unitBox.setEditable(true);
		AutoCompleteDecorator.decorate(unitBox);
		JLabel drugUnitLabel = new JLabel("Unit:");
		drugUnitLabel.setBorder(new LineBorder(Color.GRAY, 1));
		drugUnitLabel.setFont(new Font("David", 1, 14));
		centerPanel1.add(drugUnitLabel);
		centerPanel1.add(unitBox);

		//
		locationLabel = new JLabel("Drug Location:");
		locationLabel.setFont(new Font("David", 1, 14));
		locationLabel.setBorder(new LineBorder(Color.GRAY, 1));


		locationModel = new DefaultComboBoxModel<>();
		locationBox = new JComboBox<String>(locationModel);
		locationBox.setFont(new Font("David", 1, 14));
		locationBox.setBorder(new LineBorder(Color.GRAY, 1));
		locationBox.setEditable(true);
		AutoCompleteDecorator.decorate(locationBox);
		centerPanel1.add(locationLabel);
		centerPanel1.add(locationBox);
		//

		createDate = new JDateChooser();
		createDate.setLocale(Locale.UK);
		createDate.setBorder(new LineBorder(Color.GRAY, 1));
		JLabel dateLabel = new JLabel(" Date:");
		dateLabel.setAlignmentX(JLabel.RIGHT);

		dateLabel.setBorder(new LineBorder(Color.GRAY, 1));
		dateLabel.setFont(new Font("David", 1, 16));
		centerPanel1.add(dateLabel);
		centerPanel1.add(createDate);

		// centerPanel3 with button for save and Update

		JPanel southPanel2 = new JPanel();
		southPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

		southPanel2.setBackground(Color.WHITE);

		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(250, 30));
		searchField.setFont(new Font("David", 1, 16));
		searchField.setBorder(new LineBorder(Color.GRAY));
		searchField.addKeyListener(new ItemSearchListener());
		JLabel searchLabel = new JLabel("Search:");
		searchLabel.setBorder(new LineBorder(Color.GRAY));
		searchLabel.setFont(new Font("David", 1, 18));
		southPanel2.add(searchLabel);
		southPanel2.add(searchField);
		// adding components to panel31
		saveB = new JButton("Save");
		deleteB = new JButton("Delete");
		refreshb = new JButton("Refresh");
		saveB.setBackground(Color.WHITE);
		deleteB.setBackground(Color.WHITE);
		refreshb.setBackground(Color.WHITE);
		saveB.setForeground(Color.BLACK);
		deleteB.setForeground(Color.BLACK);
		refreshb.setForeground(Color.BLACK);
		saveB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		deleteB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		refreshb.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		saveB.setFont(new Font("David", 1, 16));
		deleteB.setFont(new Font("David", 1, 16));
		refreshb.setFont(new Font("David", 1, 16));
		saveB.setPreferredSize(new Dimension(200, 40));
		deleteB.setPreferredSize(new Dimension(200, 40));
		refreshb.setPreferredSize(new Dimension(100, 40));
		saveB.addActionListener(new SaveToDatabaseListener());
		deleteB.addActionListener(new DeleteDrugDetailsListener());
		refreshb.addActionListener(new RefreshListener());
		southPanel2.add(saveB);
		southPanel2.add(deleteB);
		southPanel2.add(refreshb);

		//

		String[] columnName = { "Id_No", "Description", "Class", "Unit", "Category", "Drug \nLocation", "Date" };
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 12));
		table.addMouseListener(new RowClickedListener());
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(columnName);

		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(1100, 200));
		JPanel tableScrollPanel = new JPanel();
		tableScrollPanel.setBackground(Color.WHITE);

		table.getColumnModel().getColumn(0).setPreferredWidth(4);
		table.getColumnModel().getColumn(1).setPreferredWidth(320);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		// tableScrollPanel.setBorder(new EmptyBorder(5,5,5,5));
		tableScrollPanel.add(tableScroll);
		//
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setBorder(new LineBorder(Color.GRAY, 1));
		southPanel.setBackground(Color.LIGHT_GRAY);
		southPanel.add(tableScrollPanel, BorderLayout.CENTER);
		southPanel.add(southPanel2, BorderLayout.NORTH);
		add(southPanel, BorderLayout.SOUTH);
		lastDrugId();// last drug id
		// get added drugs from the table
		getAllDrugs();// check this becuase of start up problem
		//
		/*
		 * getting all drugs unit, location and category on combo box
		 */
		catUnitLoaction();
	}

	// setting drug id on the the text field for id, each drug save
	private void lastDrugId() {
		int drugId = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("SELECT drugId FROM `drug_details` order by drugId desc LIMIT 1");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				drugId = rs.getInt("drugId");
			}
			inputField[0].setText("" + (drugId + 1));


		} catch (SQLException ex) {
			System.out.println("last drug id error\n no connection to drug Table");
		}
	}

	// save drug to db inner class
	private class SaveToDatabaseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			//
			if (inputField[0].getText().isEmpty() || classBox.getSelectedItem() == null
					|| catBox.getSelectedItem() == null || discribeDrugField.getText().isEmpty()
					|| locationBox.getSelectedItem().toString().isEmpty()
					|| unitBox.getSelectedItem().toString().isEmpty() || createDate.getDate() == null) {
				JOptionPane.showMessageDialog(null, "Some or all the text fields " + "are empty");
			} else {

				model.setRowCount(0);
				saveDrugInfo();// save text field data

				// set all to null
				discribeDrugField.setText(null);
				// locationBox.removeAllItems();
				// unitBox.removeAllItems();
			}
		}

		private void saveDrugInfo() {

			SimpleDateFormat sDateF = new SimpleDateFormat("yyyy-MM-dd");
			String todayDate = sDateF.format(createDate.getDate());
			try {
				PreparedStatement ps = St_MaryConnection.getConnection()
						.prepareStatement("INSERT INTO drug_details VALUES (?,?,?,?,?,?,?)");

				ps.setInt(1, Integer.parseInt(inputField[0].getText()));
				ps.setString(2, discribeDrugField.getText().toUpperCase() + ", " + strengthField.getText());
				ps.setString(3, classBox.getSelectedItem().toString().toUpperCase());
				ps.setString(4, unitBox.getSelectedItem().toString().toUpperCase());
				ps.setString(5, catBox.getSelectedItem().toString().toUpperCase());
				ps.setString(6, locationBox.getSelectedItem().toString().toUpperCase());
				ps.setString(7, todayDate);
				//
				ps.execute();
				// last drug id when the save button is clicked, it get incremented
				lastDrugId();
				// get added drugs from the table
				getAllDrugs();

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "The drug you are trying to save already  in the table...");
				// get added drugs from the table
				getAllDrugs();
			}
		}

	}
	// select all drugs from db table and set on app table

	private void getAllDrugs() {
		String qry = "Select * From drug_details";
		String dDescribe = "", dLocation = "", dCat = "", dUnit = null, dDate = "", dClass = "";
		int dId = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				dDescribe = rs.getString("description");
				dLocation = rs.getString("drug_location");
				dCat = rs.getString("category");
				dClass = rs.getString("class");
				dUnit = rs.getString("Unit");
				dDate = rs.getString("Daily_Date");
				dId = rs.getInt("drugId");
				model.addRow(new String[] { "" + dId, dDescribe, dClass, dUnit, dCat, dLocation, dDate });
			}

		} catch (SQLException ex) {
			System.out.println("Getting all drug details errror\n" + ex);
		}
	}

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	private class ItemSearchListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			sorterProduct(searchField.getText().toString().toUpperCase());

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	/*
	 * this method select drug category, unit location from drug details table
	 * 
	 */
	public static void catUnitLoaction() {

		dLoaction(); // drug location method
		//
		dUnitM();// drug unit model
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Select DISTINCT Category From drug_details");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String category = rs.getString("Category");
				catModel.addElement(category);
			}
		} catch (SQLException ex) {
			System.out.println("Error in setting drug category on combo box" + ex);
		}
	}

	//
	public static void dLoaction() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Select DISTINCT drug_location From drug_details");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String locationD = rs.getString("drug_location");
				locationModel.addElement(locationD);
			}
		} catch (SQLException ex) {
			System.out.println("Error in setting in location on combo box" + ex);
		}
	}

	//
	//
	public static void dUnitM() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Select DISTINCT Unit From drug_details");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String locationD = rs.getString("Unit");
				unitModel.addElement(locationD);
			}
		} catch (SQLException ex) {
			System.out.println("Error in setting unit drug category on combo box" + ex);
		}
	}

	// this class delete drug details from drug details table
	private class DeleteDrugDetailsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (table.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(null,
						"Drug Name / ID is unknown. Select drug from the table and hit delete...");
			} else {
				int choice = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete the selected drug with ID: " + inputField[0].getText() + "?");
				if (choice == JOptionPane.YES_OPTION) {
					// remove table data first

					deleteDrugDetails();
					//
					model.setRowCount(0);
					getAllDrugs();// refresh drug table again
					//
					// last drug id when the save button is clicked, it get incremented
					lastDrugId();
					saveB.setEnabled(true);//

				}
			}
		}
	}

	private void deleteDrugDetails() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Delete From" + " drug_details where drugId =?");
			ps.setInt(1, Integer.parseInt(inputField[0].getText().trim().toString()));
			ps.executeUpdate();
		} catch (SQLException ex) {
			System.out.println("Error drug drug details deletion from the table\n" + ex);
		}
	}

	// mouse click listener for the drug details table
	private class RowClickedListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {

			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();

				//
				inputField[0].setText(targetCell.getValueAt(row, 0) + "");
				// discribeDrugField.setText(targetCell.getValueAt(row, 1) + "");
				saveB.setEnabled(false);

			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

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
	// update drug info

	private void updateDrugInfo() {

		SimpleDateFormat sDateF = new SimpleDateFormat("yyyy-MM-dd");
		String todayDate = sDateF.format(createDate.getDate());
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Update drug_details SET Description=?, class=?, unit=?, category=?,drug_location"
							+ "=?, daily_Date=? WHERE drugId=?");

			ps.setString(1, discribeDrugField.getText().toUpperCase() + ", " + strengthField.getText());
			ps.setString(2, classBox.getSelectedItem().toString().toUpperCase());
			ps.setString(3, unitBox.getSelectedItem().toString().toUpperCase());
			ps.setString(4, catBox.getSelectedItem().toString().toUpperCase());
			ps.setString(5, locationBox.getSelectedItem().toString().toUpperCase());
			ps.setString(6, todayDate);
			ps.setInt(7, Integer.parseInt(inputField[0].getText()));
			//
			ps.executeUpdate();

			// lastDrugId();
			// get updated drugs from the table
			model.setRowCount(0);
			getAllDrugs();

		} catch (SQLException ex) {
			System.out.println("update drug error\n" + ex);

		}
	}

	// refresh drug id when string field get focus
	private class refreshDrugIdListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {
			// last drug id when the save button is clicked, it get incremented
			lastDrugId();
			model.setRowCount(0);
			getAllDrugs();// refresh drug table again

		}

		@Override
		public void focusLost(FocusEvent arg0) {

		}

	}

	// fresh drug details table
	private class RefreshListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// last drug id when the save button is clicked, it get incremented
			lastDrugId();
			model.setRowCount(0);
			getAllDrugs();// refresh drug table again
			saveB.setEnabled(true);

		}

	}
}
