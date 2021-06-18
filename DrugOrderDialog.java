package st_mary.admin;

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
import java.util.Date;
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
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.toedter.calendar.JDateChooser;

public class DrugOrderDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel[] addItemLabel = new JLabel[2];
	private JTextField[] inputField = new JTextField[2];
	private TableRowSorter<DefaultTableModel> sorter;
	private JTextField drugDescriptionField;
	private JLabel drugLabel;
	private JTable inventoryTable, table;
	private DefaultTableModel invTablemodel, model;
	private JDateChooser invDate;
	private JButton[] buttons = new JButton[4];
	private DefaultTableCellRenderer cellRenderer, cellRenderer1;
	private JLabel drugSelectedInfoLabel;
	private String dCat;
	private JTextField searchField;
	private String drugIdNo, expireDate;
	private TableColumn tColumn;
	private JComboBox<String> receiverBox, issuerBox;
	private DefaultComboBoxModel<String> recevaModel, issuaModel;
	private String drugN = "";
	private int qty = 0;

	public DrugOrderDialog() {

		setSize(new Dimension(1200, 700));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/drug_order.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		//
		JPanel mainCenterPanel = new JPanel(new GridLayout(2, 1));

		JPanel addPanel2Center = new JPanel(new GridLayout(1, 2, 5, 5));
		addPanel2Center.setBorder(new LineBorder(new Color(204, 204, 204), 3));

		JPanel centerPanel = new JPanel(new GridLayout(7, 2, 5, 5));
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		// centerPanel.setBackground(Color.WHITE);

		invDate = new JDateChooser();
		invDate.setLocale(Locale.UK);
		invDate.setBorder(new LineBorder(Color.BLACK, 2));

		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setBorder(new LineBorder(Color.GRAY, 2));
		dateLabel.setFont(new Font("David", 1, 16));
		centerPanel.add(dateLabel);
		centerPanel.add(invDate);

		//
		drugDescriptionField = new JTextField();
		drugLabel = new JLabel("Drug/Description:");
		drugLabel.setBorder(new LineBorder(Color.GRAY, 2));
		drugLabel.setFont(new Font("David", 1, 16));
		drugDescriptionField.setBorder(new LineBorder(Color.BLACK, 1));
		drugDescriptionField.setEditable(false);
		centerPanel.add(drugLabel);
		centerPanel.add(drugDescriptionField);

		//

		for (int i = 0; i < inputField.length; i++) {
			inputField[i] = new JTextField();
			addItemLabel[i] = new JLabel();
			addItemLabel[i].setForeground(new Color(0, 0, 0));
			addItemLabel[i].setFont(new Font("David", 1, 14));
			inputField[i].setFont(new Font("David", 1, 16));
			inputField[i].setBorder(new LineBorder(Color.BLACK, 2));
			addItemLabel[i].setBorder(new LineBorder(Color.GRAY, 2));
			centerPanel.add(addItemLabel[i]);
			centerPanel.add(inputField[i]);
		}
		// initialise unitField
		inputField[0].setEditable(false);

		// focuslistener on quantity field, this to sugest that the quantity demanded is
		// higher or lower than what in stored

		inputField[1].addFocusListener(new QttyIsBelowListener());
		addItemLabel[0].setText("Price Per Unit:");
		addItemLabel[1].setText("Quantity Issued:");
		//

		recevaModel = new DefaultComboBoxModel<String>();
		issuaModel = new DefaultComboBoxModel<String>();
		//
		receiverBox = new JComboBox<String>(recevaModel);
		receiverBox.setEditable(true);
		AutoCompleteDecorator.decorate(receiverBox);
		//
		issuerBox = new JComboBox<String>(issuaModel);
		issuerBox.setEditable(true);
		AutoCompleteDecorator.decorate(issuerBox);
		receiverBox.setFont(new Font("David", 1, 16));
		receiverBox.setBorder(new LineBorder(Color.BLACK, 2));
		//
		issuerBox.setFont(new Font("David", 1, 16));
		issuerBox.setBorder(new LineBorder(Color.BLACK, 2));
		JLabel recevaLabel = new JLabel("From Who Received:");
		JLabel IssuaLabel = new JLabel(" To Whom Issued:");
		recevaLabel.setBorder(new LineBorder(Color.GRAY, 2));
		IssuaLabel.setBorder(new LineBorder(Color.GRAY, 2));

		//
		recevaLabel.setForeground(new Color(0, 0, 0));
		recevaLabel.setFont(new Font("David", 1, 14));
		//
		IssuaLabel.setForeground(new Color(0, 0, 0));
		IssuaLabel.setFont(new Font("David", 1, 14));
		//
		centerPanel.add(recevaLabel);
		centerPanel.add(receiverBox);
		//
		centerPanel.add(IssuaLabel);
		centerPanel.add(issuerBox);

		// table selection table panel for displaying info
		JPanel selectionPanel = new JPanel(new GridLayout());
		selectionPanel.setBorder(new LineBorder(Color.GRAY, 1));

		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);

		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 12));

		String tableColumn[] = { "ID", "Description", "Category", "Unit_Price", "Qtty_Issued", "From", "To", "ExpiryD",
				"Date" };

		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar1 = new JScrollPane(table);

		cellRenderer1 = new DefaultTableCellRenderer();
		cellRenderer1.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer1);

		tColumn = table.getColumnModel().getColumn(4);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.YELLOW, Color.BLACK));
		selectionPanel.add(scrollBar1);

		// add the two panel to main panel
		addPanel2Center.add(centerPanel);
		addPanel2Center.add(selectionPanel);//

		//
		// this is button panel
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.setBackground(Color.WHITE);
		for (int j = 0; j < buttons.length; j++) {
			buttons[j] = new JButton();
			buttons[j].setForeground(Color.WHITE);

			buttons[j].setFont(new Font("David", 1, 18));
			buttons[j].setBorder(new LineBorder(new Color(0, 194, 255), 2));
			buttons[j].setPreferredSize(new Dimension(200, 30));
			btnPanel.add(buttons[j]);
			buttons[j].addActionListener(new ButtonListener());
			// buttons[j].addFocusListener(new HoverEffect());
			buttons[j].setBackground(Color.WHITE);
			buttons[j].setForeground(Color.BLACK);

		}
		buttons[0].setText("Add");
		buttons[1].setText("Save/Send");
		buttons[2].setText("Delete Row");
		buttons[3].setText("Clear Table");

		//
		// table for inserted data from input field
		inventoryTable = new JTable();
		inventoryTable.getTableHeader().setFont(new Font("David", Font.BOLD, 16));
		inventoryTable.getTableHeader().setBackground(new Color(0, 194, 255));
		inventoryTable.getTableHeader().setForeground(Color.WHITE);
		inventoryTable.getTableHeader().setOpaque(false);
		inventoryTable.setRowHeight(25);
		inventoryTable.setForeground(Color.BLACK);
		inventoryTable.setFont(new Font("David", Font.BOLD, 12));
		inventoryTable.addMouseListener(new RowClickedListener());

		// table.setShowVerticalLines(true);

		// table.addMouseListener(new TextFieldGetTextListener());

		//
		String tableColumn2[] = { "S/No", "Description", "Class", "Category", "Quantity", "Re-Order_Lever", "UnitPrice",
				"BatchNo", "VoucherNo", "Supplier", "Expiry Date,", "Inv_Date" };
		invTablemodel = (DefaultTableModel) inventoryTable.getModel();
		invTablemodel.setColumnIdentifiers(tableColumn2);
		JScrollPane scrollBar = new JScrollPane(inventoryTable);

		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(10);
		inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(320);
		inventoryTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		//
		tColumn = inventoryTable.getColumnModel().getColumn(4);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.YELLOW, Color.BLACK));
		//
		tColumn = inventoryTable.getColumnModel().getColumn(5);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.PINK, Color.BLACK));

		//

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchPanel.setBackground(Color.WHITE);
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(250, 30));
		searchField.setFont(new Font("David", 1, 16));
		searchField.setBorder(new LineBorder(Color.BLACK));
		searchField.addKeyListener(new ItemSearchListener());
		JLabel searchLabel = new JLabel("Search:");
		searchLabel.setFont(new Font("David", 1, 16));
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		//

		drugSelectedInfoLabel = new JLabel("this label shows information about the selected drug.. watch and see",
				JLabel.CENTER);
		drugSelectedInfoLabel.setFont(new Font("David", Font.BOLD, 19));
		drugSelectedInfoLabel.setForeground(Color.RED);
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(new LineBorder(Color.GRAY, 1));
		tablePanel.add(searchPanel, BorderLayout.NORTH);
		tablePanel.add(scrollBar, BorderLayout.CENTER);
		tablePanel.add(btnPanel, BorderLayout.SOUTH);

		// adding the two panels to parent panel
		mainCenterPanel.add(tablePanel);
		mainCenterPanel.add(addPanel2Center);

		add(mainCenterPanel, BorderLayout.CENTER);

		// totalDrugBought();//quantity recieved total
		getAllInventoryDrugs();// all inventory data
		who_to_Reciever();// to whom recieved and issue method here
	}

	// select the corresponding date for the particular drug and the selling price

	// button class for saving deleting ---- operation
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {
			JButton btn = (JButton) ev.getSource();
			if (btn.getActionCommand().equals("Add")) {

				if (inputField[0].getText().isEmpty() || inputField[1].getText().isEmpty()
						|| receiverBox.getSelectedItem() == null || issuerBox.getSelectedItem() == null
						|| invDate.getDate() == null) {
					JOptionPane.showMessageDialog(null,
							"Some or all the input fields are empty\n" + "Enter appropriate information.");
					
				} else {

					SimpleDateFormat dFormat = new SimpleDateFormat("yyyy/MM/dd");
					String bus_date = dFormat.format(invDate.getDate());
					model.addRow(new String[] { "" + drugIdNo, drugDescriptionField.getText().toString(), dCat,
							inputField[0].getText(), inputField[1].getText(), receiverBox.getSelectedItem().toString(),
							issuerBox.getSelectedItem().toString(), expireDate, bus_date

					});
					
					avoidHighQttFromLowDrug();
					//model.setRowCount(0);//Clear tablef
					//
					inputField[0].setText(null);
					inputField[1].setText(null);
					drugDescriptionField.setText(null);

				}
			} // 

			if (btn.getActionCommand().equals("Save/Send")) {

				if (table.getRowCount() != 0) {
					int choice = JOptionPane.showConfirmDialog(null,
							"You are about to save/send the table data." + "\n Do you want to to so?");
					if (choice == JOptionPane.YES_OPTION) {
						saveCounterOrderInfo();

						getAllInventoryDrugs();// refresh table
						invTablemodel.setRowCount(0);
						getAllInventoryDrugs();// refresh table
						model.setRowCount(0);// clear order table

						//
						issuaModel.removeAllElements();// mpty the combo box first
						recevaModel.removeAllElements();// empty the combo box first
						who_to_Reciever();// to whom recieved and issue method here

					}
				}

			}
			if (btn.getActionCommand().equals("Clear Table")) {

				int choice = JOptionPane.showConfirmDialog(null, "Do you want to clear the table data?");
				if (choice == JOptionPane.YES_OPTION) {
					model.setRowCount(0);
				}
			}
			if (btn.getActionCommand().equals("Delete Row")) {
				int row = table.getSelectedRow();
				if (row < 0) {
					JOptionPane.showMessageDialog(null, "No table row is selected....");
				} else {
					model.removeRow(row);

				}
			}
		}

	}
	// save pharmacist a the count drug request information

	private void saveCounterOrderInfo() {

		int soldQtty=0;
		PreparedStatement ps = null, pre = null,ps2=null;
		String insertQry = "Insert into counterDrugOrderTable VALUES (?,?,?,?,?,?,?,?,?,?)";
		String insertQryRemove = "Insert into counterDrugOrderTableRemove"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
		String updateQry = "Update drug_inventory SET quantity= quantity-? "
				+ "where description=? AND inventory_id=?"
				+ " ";
		
		//counterDrugOrderTableRemove
		try {
			for (int i = 0; i < table.getRowCount(); i++) {
				int drugOrderId = Integer.parseInt((String) table.getValueAt(i, 0));
				String drugName = (String) table.getValueAt(i, 1);
				String drugCateg = (String) table.getValueAt(i, 2);
				double unitPrice = Double.parseDouble(table.getValueAt(i, 3) + "");
				int qttyIssued = Integer.parseInt((String) table.getValueAt(i, 4));
				String whomReceived = (String) table.getValueAt(i, 5);
				String whoGive = (String) table.getValueAt(i, 6);
				String expirationTime = (String) table.getValueAt(i, 7);
				String biz_date = (String) table.getValueAt(i, 8);

				ps = St_MaryConnection.getConnection().prepareStatement(insertQry);
				ps.setInt(1, drugOrderId);
				ps.setString(2, drugName);
				ps.setString(3, drugCateg);
				ps.setDouble(4, unitPrice);
				ps.setInt(5, qttyIssued);
				ps.setInt(6, soldQtty);
				ps.setString(7, whomReceived);
				ps.setString(8, whoGive);
				ps.setString(9, expirationTime);
				ps.setString(10, biz_date);
				//---------------------------------------------------------
				
				ps2 = St_MaryConnection.getConnection().prepareStatement(insertQryRemove);
				ps2.setInt(1, drugOrderId);
				ps2.setString(2, drugName);
				ps2.setString(3, drugCateg);
				ps2.setDouble(4, unitPrice);
				ps2.setInt(5, qttyIssued);
				ps2.setInt(6, soldQtty);
				ps2.setString(7, whomReceived);
				ps2.setString(8, whoGive);
				ps2.setString(9, expirationTime);
				ps2.setString(10, biz_date);
				
				
				// updating drug inventory table now
				pre = St_MaryConnection.getConnection().prepareStatement(updateQry);
				pre.setInt(1, qttyIssued);
				pre.setString(2, drugName);
				pre.setInt(3, drugOrderId);
				
				pre.executeUpdate();
				ps2.execute();
				ps.execute();
				

				
			}
			getAllInventoryDrugs();// refresh table

			// update label for status display quantity from drug_inventory.....
			// quantity received method

		} catch (SQLException ex) {
			System.out.println("Counter requesting drug error\n" + ex);
			ex.printStackTrace();
		}
	}

	// Focus Listener for input field
	private class QttyIsBelowListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {

			
			checkDrugNameOnJtable();
				
				//model.removeRow(model.getRowCount()-1);
			
			// Check for existing drug in the order table and prevent same drug addition

			avoidSameDrugsOrderTable(drugDescriptionField.getText());
			
				
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {

			try {

				if (Integer.parseInt(inputField[1].getText().trim()) <= 0) {
					JOptionPane.showMessageDialog(null, "Quantity require must not less  or equal zero...");
					inputField[1].setText("");

				}
				// alert for quantity depletion
				alertForOrderLevelQtty(drugDescriptionField.getText(), expireDate);
			} catch (NumberFormatException nfc) {
				System.out.println("you enter String value");
			}
		}

	}

	// Notification when the re-order qtty is equal or below the level

	private void alertForOrderLevelQtty(String drugNam, String expirationTime) {

		String qry = "Select Re_Order_Qtty, quantity From drug_inventory "
				+ "where Description=? " + "AND expiryDate=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int qttyAlert = 0;
		int quantit = 0;
		try {

			ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ps.setString(1, drugNam);
			ps.setString(2, expirationTime);
			rs = ps.executeQuery();
			while (rs.next()) {
				qttyAlert = rs.getInt("Re_Order_Qtty");
				quantit = rs.getInt("quantity");
			}
			if (quantit <= qttyAlert) {
				JOptionPane.showMessageDialog(null,
						"Be inform that " + drugNam + "" + " has reached it Re-Order Level of [" + qttyAlert + "]");
				// JOptionPane.showMessageDialog(null, , arg2, arg3, arg4);
			}
			if (Integer.parseInt(inputField[1].getText().toString().trim()) > quantit) {
				JOptionPane.showMessageDialog(null, "Amount of quantity entered is more than the total inventory");
				inputField[1].setText("");
			}
		} catch (SQLException exc) {
			System.out.println("Checking reorder level alert error\n" + exc);
		}
	}

	//

	private void getAllInventoryDrugs() {

		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-01");
		Date dNow = new Date();
		String todayDate = dFormat.format(dNow.getTime());
		//
		String qry = "Select DISTINCT description, category, class, batchNo, voucherNo, supplier,"
				+ "expiryDate, invDate, Re_Order_Qtty, quantity,inventory_id, sellingPrice"
				+ " From drug_inventory where expiryDate > ? and quantity>0";
		String dDescribe1 = "", dCat1 = "", dClass1 = "", batchNo1 = null, voucherNo1 = null, supplier1 = null,
				expireDate1 = null, invDate1 = null;
		int orderQtty1 = 0, reOrderQtty1 = 0;
		int dId1 = 0;
		double unitPrice1 = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ps.setString(1, todayDate);
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
				unitPrice1 = rs.getDouble("sellingPrice");
				invTablemodel.addRow(new String[] { "" + dId1, dDescribe1, dClass1, dCat1, "" + orderQtty1,
						"" + reOrderQtty1, "" + unitPrice1, batchNo1, voucherNo1, supplier1, expireDate1, invDate1 });
			}

		} catch (SQLException ex) {
			System.out.println("Getting all drug details errror\n" + ex);
		}
	}

	// row click event for drug details
	private class RowClickedListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {

			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();
				// String []text = (String[]) table.getinventoryTableAt(row, col);

				drugIdNo = (String) targetCell.getValueAt(row, 0);
				String dDescription = (String) targetCell.getValueAt(row, 1);
				dCat = (String) targetCell.getValueAt(row, 3);
				String sellingPrice = (String) targetCell.getValueAt(row, 6);
				expireDate = (String) targetCell.getValueAt(row, 10);

				drugDescriptionField.setText(dDescription);
				inputField[0].setText(sellingPrice);

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

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(invTablemodel);
		inventoryTable.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// searching for a specific drug
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
		}
	}

	// select from whom reciev from drug inventory

	private void who_to_Reciever() {

		//
		who_to_Issued();// call issuermethod here
		//
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Select DISTINCT whom_Recieved From counterdrugordertable");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String recVa = rs.getString("whom_Recieved");
				recevaModel.addElement(recVa);
			}
		} catch (SQLException ex) {
			System.out.println("Error in setting drug whom_Recieved on combo box" + ex);
		}
	}

	//-------------------------------------------------------------------------------------

	// who issued
	private void who_to_Issued() {

		//
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Select DISTINCT to_whom_Issued From counterdrugordertable");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String recVa = rs.getString("to_whom_Issued");
				issuaModel.addElement(recVa);
			}
		} catch (SQLException ex) {
			System.out.println("Error in setting drug whom_Recieved on combo box" + ex);
		}
	}

	//-------------------------------------------------------------------------------------
	// This function prevent duplicate of drugs in order table
	private void avoidSameDrugsOrderTable(String textFielddrugName) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int soldQtty=0;
		try {
			ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Select * from counterdrugordertableremove" + " where drugNameorder=? ");
			ps.setString(1, textFielddrugName);

			rs = ps.executeQuery();
			while (rs.next()) {
				drugN = rs.getString("drugNameorder");
				soldQtty = rs.getInt("sold_qtty");
				qty = rs.getInt("quantityDemanded");


			}
			if (drugN!= null && drugN.equals(textFielddrugName.toString())&&soldQtty<qty) {

				JOptionPane.showMessageDialog(null, "You cannot add this drug to"
						+ " the order table because\n [" + qty
						+ "]  unsold quantity still remaining.");
				
				model.setRowCount(0);//clear table
				
				//
				inputField[0].setText(null);
				inputField[1].setText(null);
				drugDescriptionField.setText(null);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// This function check the order drug list table if drug already added to the table
	
	private void checkDrugNameOnJtable() {
		
		for(int i=0;i<table.getRowCount();i++) {
			
			String drugName2 = (String) table.getValueAt(i, 1);
			if(drugName2.equals(drugDescriptionField.getText())) {

				JOptionPane.showMessageDialog(null, "You have already added the selected drug.\n"
						+ "You can remove it and modify...");
				
				inputField[0].setText(null);
				inputField[1].setText(null);
				drugDescriptionField.setText(null);

			}
		}
		
	}
	
	//
	// Compare quantity on the table and the entered amount
	
	private void avoidHighQttFromLowDrug() {

		
		//
		String qry = "Select quantity  from drug_inventory where inventory_id=?";
		int orderQtty1 = 0;
		
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ps.setInt(1, Integer.parseInt(drugIdNo));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				orderQtty1 = rs.getInt("quantity");
			}
			if(Integer.parseInt(inputField[1].getText())>orderQtty1) {
				
//				JOptionPane.showMessageDialog(null, "Amount of quantity entered is more than the total inventory");
//				inputField[1].setText("");
				model.setRowCount(model.getRowCount()-1);
//				System.out.println(model.getRowCount()-1);
				
			}
		} catch (SQLException ex) {
			System.out.println("Getting all drug details errror\n" + ex);
		}
	}
}
