package st_mary.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JDialog;
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

public class InventoryDrugDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel[] addItemLabel = new JLabel[2];
	private JTextField[] inputField = new JTextField[2];
	private JTable table;
	private DefaultTableModel model;
	private JButton saveB, updateB, clearB;
	private JComboBox<String> supplierBox;
	private DefaultComboBoxModel<String> supplierModel;
	private JDateChooser invDate, expireDateChoser;
	private JLabel qttyLabel, unitLabel;
	private JTextField qttyField, unitField, alertField;
	private int inventoryid = 0;
	private JButton importBtn;
	private JTextField drugIdField, descriptionField, classField, searchField;
	private String drugId, dClass, dCat, dDescription;
	private TableRowSorter<DefaultTableModel> sorter;
	private JButton refreshB;

	private DefaultTableCellRenderer cellRenderer;

	//
	public InventoryDrugDialog(String drugId, String dDescription, String dClass, String dCat, String dDate) {

		// initialise the parameter in the constructor
		this.drugId = drugId;
		this.dDescription = dDescription;
		this.dClass = dClass;
		this.dCat = dCat;
		setSize(new Dimension(1200, 700));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);

		//
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/drug_inv.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		//
		JPanel mainCenterPanel = new JPanel(new GridLayout(2, 1));

		JPanel centerPanel = new JPanel(new GridLayout(6, 2, 8, 8));
		centerPanel.setBackground(Color.WHITE);
		// centerPanel.setBackground(Color.WHITE);
		centerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		JPanel addPanel2Center = new JPanel(new GridLayout(1, 3, 10, 10));
		addPanel2Center.add(centerPanel);
		addPanel2Center.setBorder(new LineBorder(new Color(204, 204, 204), 3));
		//
		JPanel operationPanel = new JPanel(new GridLayout(6, 2, 8, 8));// form field holder
		operationPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		operationPanel.setBackground(Color.WHITE);

		ImageIcon pharmaIcon = new ImageIcon(getClass().getResource("/all/images/3d_pharmacy.png"));
		JLabel pharIconLabel = new JLabel("", pharmaIcon, JLabel.CENTER);
		addPanel2Center.add(operationPanel);//
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		imagePanel.add(pharIconLabel);
		addPanel2Center.add(imagePanel);//
		//

		// import button

		importBtn = new JButton("Import Drug Details...");
		importBtn.addActionListener(new ImportBtnListener());
		importBtn.setFont(new Font("David", 1, 16));
		drugIdField = new JTextField(drugId);
		drugIdField.setFont(new Font("David", 1, 16));
		drugIdField.setEditable(false);
		centerPanel.add(drugIdField);
		centerPanel.add(importBtn);
		//

		//
		descriptionField = new JTextField(dDescription);
		JLabel describLabel = new JLabel("Description:");
		describLabel.setFont(new Font("David", 1, 16));
		descriptionField.setBorder(new LineBorder(Color.BLACK, 2));
		describLabel.setBorder(new LineBorder(Color.GRAY, 2));
		descriptionField.setFont(new Font("David", 1, 16));
		centerPanel.add(describLabel);
		descriptionField.setEditable(false);
		centerPanel.add(descriptionField);
		//
		classField = new JTextField(dClass);
		JLabel classLabel = new JLabel("Class:");
		classLabel.setFont(new Font("David", 1, 16));
		classField.setBorder(new LineBorder(Color.BLACK, 2));
		classLabel.setBorder(new LineBorder(Color.GRAY, 2));
		classField.setFont(new Font("David", 1, 16));
		classField.setEditable(false);
		centerPanel.add(classLabel);
		centerPanel.add(classField);
		//

		for (int i = 0; i < inputField.length; i++) {
			inputField[i] = new JTextField();
			addItemLabel[i] = new JLabel();
			addItemLabel[i].setForeground(new Color(0, 0, 0));
			addItemLabel[i].setFont(new Font("David", 1, 14));
			addItemLabel[i].setBorder(new LineBorder(Color.GRAY, 2));
			inputField[i].setFont(new Font("David", 1, 16));
			inputField[i].setBorder(new LineBorder(Color.BLACK, 2));
			centerPanel.add(addItemLabel[i]);
			centerPanel.add(inputField[i]);
		}

		addItemLabel[0].setText("Receipt Voucher No:");
		addItemLabel[1].setText("Batch Number:");

		//
		JLabel supplierLabel = new JLabel("Supplier:");
		supplierLabel.setBorder(new LineBorder(Color.GRAY, 2));

		supplierLabel.setFont(new Font("David", 1, 16));
		centerPanel.add(supplierLabel);
		supplierModel = new DefaultComboBoxModel<String>();
		supplierBox = new JComboBox<String>(supplierModel);
		supplierBox.setEditable(true);
		supplierBox.setFont(new Font("David", 1, 16));
		supplierBox.setBorder(new LineBorder(Color.BLACK, 2));
		AutoCompleteDecorator.decorate(supplierBox);
		centerPanel.add(supplierBox);
		//

		//
		expireDateChoser = new JDateChooser();
		expireDateChoser.setLocale(Locale.US);
		expireDateChoser.setBorder(new LineBorder(Color.BLACK, 2));
		JLabel expireLabel = new JLabel("Expiry Date");
		expireLabel.setBorder(new LineBorder(Color.GRAY, 2));
		expireLabel.setFont(new Font("David", 1, 16));
		operationPanel.add(expireLabel);
		operationPanel.add(expireDateChoser);

		//

		// total quantity supplied
		qttyLabel = new JLabel("Quantity Supplied:");
		qttyLabel.setFont(new Font("David", 1, 13));
		qttyField = new JTextField();
		qttyField.setFont(new Font("David", 1, 14));
		qttyField.setBorder(new LineBorder(Color.BLACK, 2));
		qttyLabel.setBorder(new LineBorder(Color.GRAY, 2));
		operationPanel.add(qttyLabel);
		operationPanel.add(qttyField);
		//
		alertField = new JTextField();
		JLabel alertLabel = new JLabel(
				"<html>Re-Order Level<br><p style = color:green;font-size:6px; font-family:italic;>"
						+ "(Notify Me At this Level)</p>" + "</html>");

		alertField.setFont(new Font("David", 1, 14));
		alertField.setBorder(new LineBorder(Color.BLACK, 2));
		alertLabel.setBorder(new LineBorder(Color.GRAY, 2));
		operationPanel.add(alertLabel);
		operationPanel.add(alertField);

		//

		unitLabel = new JLabel("Unit price:");
		unitLabel.setFont(new Font("David", 1, 14));
		unitField = new JTextField();
		unitField.setLocale(Locale.US);
		unitField.setFont(new Font("David", 1, 14));
		unitField.setBorder(new LineBorder(Color.BLACK, 2));
		unitLabel.setBorder(new LineBorder(Color.GRAY, 2));
		operationPanel.add(unitLabel);
		operationPanel.add(unitField);

		//

		invDate = new JDateChooser();
		invDate.setLocale(Locale.UK);
		invDate.setBorder(new LineBorder(Color.BLACK, 2));
		JLabel dateLabel = new JLabel("Inventory Date");
		dateLabel.setBorder(new LineBorder(Color.GRAY, 2));

		dateLabel.setFont(new Font("David", 1, 16));
		operationPanel.add(dateLabel);
		operationPanel.add(invDate);
		// operational button, save clear buttons
		JPanel operationBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		operationBtnPanel.setBackground(Color.WHITE);

		saveB = new JButton("Save");
		saveB.addActionListener(new SaveDataListener());
		updateB = new JButton("Update");
		clearB = new JButton("Delete");
		clearB.setEnabled(false);
		updateB.setEnabled(false);
		clearB.addActionListener(new DeleteSelectedDrug());
		updateB.addActionListener(new UpdateListener());

		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(250, 30));
		searchField.setFont(new Font("David", 1, 16));
		searchField.setBorder(new LineBorder(Color.BLACK));
		searchField.addKeyListener(new ItemSearchListener());
		JLabel searchLabel = new JLabel("Search:");
		searchLabel.setFont(new Font("David", 1, 16));
		operationBtnPanel.add(searchLabel);
		operationBtnPanel.add(searchField);
		operationBtnPanel.add(saveB);
		operationBtnPanel.add(updateB);
		operationBtnPanel.add(clearB);

		//
		//
		saveB.setForeground(Color.BLACK);
		saveB.setBackground(Color.WHITE);
		saveB.setFont(new Font("David", 1, 14));
		saveB.setPreferredSize(new Dimension(200, 30));
		saveB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		// saveB.addFocusListener(this);

		//
		clearB.setForeground(Color.BLACK);
		clearB.setBackground(Color.WHITE);
		clearB.setFont(new Font("David", 1, 14));
		clearB.setPreferredSize(new Dimension(200, 30));
		clearB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		// clearB.addFocusListener(this);
		// clearB.addActionListener(new ClearTable());
		//

		updateB.setPreferredSize(new Dimension(200, 30));
		updateB.setForeground(Color.BLACK);
		updateB.setBackground(Color.WHITE);
		updateB.setFont(new Font("David", 1, 14));
		updateB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		// updateB.addFocusListener(this);

		mainCenterPanel.add(addPanel2Center);

		// table for inserted data from input field
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 12));

		// table.setGridColor(Color.RED);
		// table.setShowVerticalLines(true);
		// table.setShowHorizontalLines(false);
		table.addMouseListener(new TextFieldGetTextListener());

		String tableColumn[] = { "S/No", "Description", "Class", "Category", "Quantity", "Re-Order_Lever", "UnitPrice",
				"BatchNo", "VoucherNo", "Supplier", "Expiry Date,", "Inv_Date" };

		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar = new JScrollPane();

		scrollBar.setViewportView(table);

		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(260);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		//
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(new LineBorder(Color.GRAY, 1));
		tablePanel.add(operationBtnPanel, BorderLayout.NORTH);
		tablePanel.add(scrollBar, BorderLayout.CENTER);
		mainCenterPanel.add(tablePanel);
		add(mainCenterPanel, BorderLayout.CENTER);

		// south panel holding refresh button
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(Color.WHITE);
		refreshB = new JButton("Refresh");
		refreshB.setForeground(Color.BLACK);
		refreshB.setBackground(Color.WHITE);
		refreshB.setBorder(new LineBorder(new Color(0, 194, 255), 1));
		refreshB.setFont(new Font("David", 1, 14));

		refreshB.setPreferredSize(new Dimension(200, 30));
		refreshB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		refreshB.addActionListener(new RefreshListener());
		southPanel.add(refreshB);
		add(southPanel, BorderLayout.SOUTH);
		lastInventoryId();// last inventory id
		getAllInventoryDrugs();// all stored inventory details
		suppliersName();//suppliers

	}

	// adding drugs and it expiring date to database
	private class SaveDataListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			if (inputField[0].getText().isEmpty() || inputField[1].getText().isEmpty()
					|| expireDateChoser.getDate() == null || alertField.getText().isEmpty()
					|| qttyField.getText().isEmpty() || unitField.getText().isEmpty() || invDate.getDate() == null
					|| drugIdField.getText().isEmpty() || classField.getText().isEmpty()
					|| descriptionField.getText().isEmpty() || supplierBox.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(null,
						"Some or all the text fields are empty.\n Enter appropriate data...");
			} else {
				saveData2InventoryTable();// save data to database table

				// all stored inventory details
				model.setRowCount(0);
				getAllInventoryDrugs();
				qttyField.setText(null);
				unitField.setText(null);
				descriptionField.setText(null);
				alertField.setText(null);
				inputField[0].setText(null);
				inputField[1].setText(null);

			}
		}
	}

	// import class
	private class ImportBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			DrugSlectionTable dSTable = new DrugSlectionTable();
			dSTable.setVisible(true);
			// reactivate save button
			saveB.setEnabled(true);
			clearB.setEnabled(false);
			updateB.setEnabled(false);
		}

	}

	// save data to inventory

	private void saveData2InventoryTable() {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateInvent = dFormat.format(invDate.getDate());
		SimpleDateFormat expireF = new SimpleDateFormat("yyyy-MM-01");
		//
		String expireDateFormat = expireF.format(expireDateChoser.getDate());
		PreparedStatement ps = null;
		String qry = "Insert into drug_inventory VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);

			ps.setInt(1, inventoryid);
			ps.setInt(2, Integer.parseInt(drugIdField.getText()));
			ps.setString(3, descriptionField.getText().toUpperCase());
			ps.setString(4, classField.getText().toUpperCase());
			ps.setString(5, dCat);
			ps.setInt(6, Integer.parseInt(qttyField.getText()));
			ps.setDouble(7, Double.parseDouble(unitField.getText()));
			ps.setString(8, inputField[1].getText().toUpperCase());
			ps.setString(9, inputField[0].getText().toUpperCase());
			ps.setString(10, supplierBox.getSelectedItem().toString().toUpperCase());
			ps.setInt(11, Integer.parseInt(alertField.getText()));
			ps.setString(12, expireDateFormat);
			ps.setString(13, dateInvent);
			ps.execute();
			lastInventoryId();// Last inventory id

		} catch (SQLException exc) {
			System.out.println("Saving data to inventory table error\n" + exc);
		} catch (NumberFormatException nfc) {
			JOptionPane.showMessageDialog(null, "Sorry, one of the text fields contain character. Check your entry...");
		}
	}

	// Getting last inventory id from inventory table
	private void lastInventoryId() {
		int inventId = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(
					"Select inventory_id From" + " drug_inventory order by inventory_id desc limit 1");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				inventId = rs.getInt("inventory_id");
			}
			inventoryid = ((inventId + 1));

		} catch (SQLException exc) {
			System.out.println("last inventory id from table error\n" + exc);
		}
	}

	//
	private void getAllInventoryDrugs() {

		String qry = "Select * From drug_inventory";
		String dDescribe1 = "", dCat1 = "", dClass1 = "", 
				batchNo1 = null, voucherNo1 = null, supplier1 = null,
				expireDate1 = null, invDate1 = null;
		int orderQtty1 = 0, unitPrice1 = 0, reOrderQtty1 = 0;
		int dId1 = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				dDescribe1 = rs.getString("description");
				dCat1 = rs.getString("category");
				dClass1 = rs.getString("class");
				batchNo1 = rs.getString("batchNo");
				voucherNo1 = rs.getString("voucherNo");
				supplier1 = rs.getString("supplier");
				expireDate1 = rs.getString("expiryDate");
				invDate1 = rs.getString("invDate");
				reOrderQtty1 = rs.getInt("Re_Order_Qtty");
				orderQtty1 = rs.getInt("quantity");
				dId1 = rs.getInt("inventory_id");
				unitPrice1 = rs.getInt("sellingPrice");
				model.addRow(new String[] { "" + dId1, dDescribe1, dClass1, dCat1, "" + orderQtty1, "" + reOrderQtty1,
						"" + unitPrice1, batchNo1, voucherNo1, supplier1, expireDate1, invDate1 });

								
			}

			// clear field on each table entry
			qttyField.setText("");
			unitField.setText(null);
			alertField.setText(null);
			inputField[0].setText(null);
			inputField[1].setText(null);
			// drugIdField.setText("Drug ID:Null");

		} catch (SQLException ex) {
			System.out.println("Getting all drug details errror\n" + ex);
		}
	}

	// select distinct supplier
	// who issued
	private void suppliersName() {
		String supplier1 = null;
		//
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Select DISTINCT supplier From drug_inventory");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				supplier1 = rs.getString("supplier");
				// set suppliers to it combo box
				supplierModel.addElement(supplier1);
			}
		} catch (SQLException ex) {
			System.out.println("Error getting suppliers on combo box" + ex);
		}
	}

	// setting all the fields with values when any table row is clicked
	private class TextFieldGetTextListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {

			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();

				drugIdField.setText((String) targetCell.getValueAt(row, 0));
				descriptionField.setText((String) targetCell.getValueAt(row, 1));
				classField.setText((String) targetCell.getValueAt(row, 2));
				dCat = ((String) targetCell.getValueAt(row, 3));
				qttyField.setText((String) targetCell.getValueAt(row, 4));
				alertField.setText((String) targetCell.getValueAt(row, 5));
				unitField.setText((String) targetCell.getValueAt(row, 6));
				inputField[1].setText((String) targetCell.getValueAt(row, 7));
				inputField[0].setText(targetCell.getValueAt(row, 8).toString());

				// deactivate save button
				saveB.setEnabled(false);
				updateB.setEnabled(true);
				clearB.setEnabled(true);
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

	// delete the selected drug
	private class DeleteSelectedDrug implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (descriptionField.getText().isEmpty() || qttyField.getText().isEmpty() || inventoryid == 0) {
				JOptionPane.showMessageDialog(null, "Drug ID is not found");
			} else {
				int choice = JOptionPane.showConfirmDialog(null,
						"Deleting this drug will affect your saved tally card.\n" + "Are you sure you want to delete "
								+ "" + descriptionField.getText().toUpperCase() + " ?");
				if (choice == JOptionPane.YES_OPTION) {
					try {
						// create a trigger immediately drug is deleted
						String triggerQry = " CREATE Trigger IF NOT EXISTS inventory_drug_delete_TriggerTable "
								+ "AFTER DELETE ON drug_inventory " + "FOR EACH ROW " + "BEGIN"
								+ " insert into inventory_deleteHistory VALUES (old.inventory_id, old.drugId,"
								+ "old.Description, old.Class,"
								+ "old.category,old.quantity, old.sellingPrice,old.batchNo,old.voucherNo,old.supplier,"
								+ "old.Re_order_Qtty,old.expiryDate,old.invDate); " + "END;";

						PreparedStatement ps1 = St_MaryConnection.getConnection().prepareStatement(triggerQry);
						ps1.executeUpdate();
						//
						PreparedStatement ps = St_MaryConnection.getConnection()
								.prepareStatement("Delete  from drug_inventory where inventory_id=?");
						ps.setInt(1, Integer.parseInt(drugIdField.getText().toString()));
						ps.executeUpdate();
						// --------------------------------------------------------------------------------
						PreparedStatement ps2 = St_MaryConnection.getConnection()
								.prepareStatement("Delete  from counterdrugordertableRemove where order_id=?");
						ps2.setInt(1, Integer.parseInt(drugIdField.getText().toString()));
						ps2.executeUpdate();
						//

						PreparedStatement ps3 = St_MaryConnection.getConnection()
								.prepareStatement("Delete  from counterdrugordertable where order_id=?");
						ps3.setInt(1, Integer.parseInt(drugIdField.getText().toString()));
						ps3.executeUpdate();
						//
						// all stored inventory details
						model.setRowCount(0);
						// refresh supplier combo box
						supplierModel.removeAllElements();
						getAllInventoryDrugs();

						JOptionPane.showMessageDialog(null, "Deleted successfully....");

					} catch (SQLException ex) {
						System.out.println("Deleting drug from inventory error\n");
						ex.printStackTrace();
					}
				}
			}
		}

	}

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// search for stored drug name
	private class ItemSearchListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub

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

	// update class
	private class UpdateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (inputField[0].getText().isEmpty() || inputField[1].getText().isEmpty()
					|| expireDateChoser.getDate() == null || alertField.getText().isEmpty()
					|| qttyField.getText().isEmpty() || unitField.getText().isEmpty() || invDate.getDate() == null
					|| drugIdField.getText().isEmpty() || descriptionField.getText().isEmpty()
					|| supplierBox.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(null,
						"Some or all the text fields are empty.\n Enter appropriate to update...");
			} else {
				updateInventoryTable();// update data to database table

				// all stored inventory details
				model.setRowCount(0);
				supplierModel.removeAllElements();// remove all items before refreshing
				getAllInventoryDrugs();
				qttyField.setText(null);
				unitField.setText(null);
				descriptionField.setText(null);
				alertField.setText(null);
				inputField[0].setText(null);
				inputField[1].setText(null);
			}
		}

	}

	// update table of inventory

	private void updateInventoryTable() {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateInvent = dFormat.format(invDate.getDate());
		SimpleDateFormat expireF = new SimpleDateFormat("yyyy-MM-01");
		String expireDateFormat = expireF.format(expireDateChoser.getDate());
		PreparedStatement ps = null;
		String qry = "Update drug_inventory SET quantity=?, Description=?, expiryDate=?,sellingPrice=?"
				+ ",Re_Order_Qtty=?,invDate=?,batchNo=?, voucherNo=?,supplier=? where inventory_id=?";

		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);

			ps.setInt(1, Integer.parseInt(qttyField.getText()));
			ps.setString(2, descriptionField.getText().toUpperCase());
			ps.setString(3, expireDateFormat);
			ps.setDouble(4, Double.parseDouble(unitField.getText()));
			ps.setInt(5, Integer.parseInt(alertField.getText()));
			ps.setString(6, dateInvent);
			//
			ps.setString(7, inputField[1].getText().toUpperCase());
			ps.setString(8, inputField[0].getText().toUpperCase());
			ps.setString(9, supplierBox.getSelectedItem().toString().toUpperCase());
			ps.setInt(10, Integer.parseInt(drugIdField.getText()));

			ps.execute();
			JOptionPane.showMessageDialog(null, descriptionField.getText().toUpperCase() + " Updated successfully...");
			lastInventoryId();// Last inventory id

		} catch (SQLException exc) {
			System.out.println("Saving data to inventory table error\n" + exc);
		}
	}

	// refresh the inventory table becaus when new data is entered from another
	// computer, you might not know
	// I dont want to use while loop becuase of it forever looping
	private class RefreshListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// all stored inventory details
			model.setRowCount(0);
			supplierModel.removeAllElements();// remove all items before refreshing
			getAllInventoryDrugs();
		}

	}
}
