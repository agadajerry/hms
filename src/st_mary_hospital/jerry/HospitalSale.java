package st_mary_hospital.jerry;

import java.util.Calendar;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class HospitalSale extends JFrame {

	public JTable table;
	public DefaultTableModel model;
	double qtty, qAndP, uPrice, grandTotal;
	public JTextField idField;
	private JDateChooser chooser;
	private JTextField drugDescriptionField;
	private JLabel[] addItemLabel = new JLabel[4];
	private JTextField[] inputField = new JTextField[4];
	private JButton[] buttons = new JButton[4];
	public int saleDrugId = 0;
	private JLabel totalLabel;
	public double patientAmount = 0.0, totalPaid = 0.0;
	private JTable inventoryTable;
	private static DefaultTableModel invTablemodel;
	private TableColumn tColumn;
	private JTextField searchField;
	private DefaultTableCellRenderer cellRenderer, cellRenderer1;
	private String expireDate = "", drugIdNo = "";
	private TableRowSorter<DefaultTableModel> sorter;
	private JButton exitB;
	private JButton refreshB;
	public int lastIdNo = 000001;

	public HospitalSale() {

		setSize(new Dimension(970, 650));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		// setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/all/images/add1.png"));
		setIconImage(iconImage);

		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.GRAY);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/dispensary_banna.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		JPanel centerMainPanel = new JPanel(new GridLayout(2, 1,6,6));
		centerMainPanel.setBackground(Color.WHITE);

		// table for inserted data from input field
		inventoryTable = new JTable();
		inventoryTable.getTableHeader().setFont(new Font("David", Font.BOLD, 16));
		inventoryTable.getTableHeader().setBackground(new Color(0, 194, 255));
		inventoryTable.getTableHeader().setForeground(Color.WHITE);
		inventoryTable.getTableHeader().setOpaque(false);
		inventoryTable.setRowHeight(25);
		inventoryTable.setForeground(Color.BLACK);
		inventoryTable.setFont(new Font("David", Font.BOLD, 12));
		inventoryTable.addMouseListener(new SetBackTextFieldValueListener());

		// table.setShowVerticalLines(true);

		// table.addMouseListener(new TextFieldGetTextListener());

		//
		String tableColumn2[] = { "S/No", "Description", "Category", "Re-Order_Lever", "UnitPrice", "BatchNo",
				"Expiry Date,", "Inv_Date" };
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

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchPanel.setBackground(Color.WHITE);
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(250, 30));
		searchField.setFont(new Font("David", 1, 16));
		searchField.setBorder(new LineBorder(Color.GRAY));
		searchField.addKeyListener(new ItemSearchListener());
		JLabel searchLabel = new JLabel("Search:");
		searchLabel.setFont(new Font("David", 1, 16));
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		//
		refreshB = new JButton("Refresh");
		refreshB.setForeground(Color.BLACK);
		refreshB.setBackground(Color.WHITE);
		refreshB.setFont(new Font("David", 1, 18));
		refreshB.setBorder(new LineBorder(new Color(0,194,255)));
		refreshB.setPreferredSize(new Dimension(200, 30));
		refreshB.setBorder(new LineBorder(new Color(0, 194, 255), 2));
		refreshB.addActionListener(new RefreshListener());
		searchPanel.add(refreshB);

		//
		//
		JPanel centerPanel1 = new JPanel(new BorderLayout());
		centerPanel1.setBorder(new EmptyBorder(10, 10, 10, 10));
		centerPanel1.setBackground(Color.WHITE);
		centerPanel1.add(searchPanel, BorderLayout.NORTH);
		centerPanel1.add(scrollBar, BorderLayout.CENTER);
		//
		ImageIcon saleIcon = new ImageIcon(getClass().getResource("/all/images/pharma_buyer.png"));
		JLabel saleIconLabel = new JLabel("", saleIcon, JLabel.CENTER);
		centerPanel1.add(saleIconLabel, BorderLayout.EAST);
		centerMainPanel.add(centerPanel1);

		// setBorder(new LineBorder(Color.BLACK, 2));

		JPanel centerPanel2 = new JPanel(new GridLayout(6, 2, 5, 5));
		centerPanel2.setBackground(Color.WHITE);
		centerPanel2.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel dateLabel2 = new JLabel("DATE:");
		dateLabel2.setForeground(new Color(0, 0, 0));
		dateLabel2.setFont(new Font("David", 1, 16));
		dateLabel2.setBorder(new LineBorder(Color.GRAY,1));


		centerPanel2.add(dateLabel2);
		//
		chooser = new JDateChooser();
		chooser.setLocale(Locale.US);
		chooser.setFont(new Font("David", 1, 16));
		chooser.setForeground(new Color(0, 0, 0));
		centerPanel2.add(chooser);

		drugDescriptionField = new JTextField();
		JLabel drugNameLabel = new JLabel();
		drugNameLabel.setBorder(new LineBorder(Color.GRAY,1));
		drugNameLabel.setText("Drug Name:");
		drugNameLabel.setForeground(new Color(0, 0, 0));
		drugNameLabel.setFont(new Font("David", 1, 16));
		drugDescriptionField.setFont(new Font("David", 1, 16));
		drugDescriptionField.setForeground(new Color(0, 0, 0));

		centerPanel2.add(drugNameLabel);
		centerPanel2.add(drugDescriptionField);

		centerPanel2.setBorder(new LineBorder(new Color(204, 204, 204), 3));

		for (int i = 0; i < inputField.length; i++) {
			inputField[i] = new JTextField();
			addItemLabel[i] = new JLabel();
			addItemLabel[i].setForeground(new Color(0, 0, 0));
			addItemLabel[i].setFont(new Font("David", 1, 16));
			inputField[i].setFont(new Font("David", 1, 16));
			inputField[i].setBorder(new LineBorder(new Color(204, 204, 204), 3));
			addItemLabel[i].setBorder(new LineBorder(new Color(204, 204, 204), 3));
			centerPanel2.add(addItemLabel[i]);
			centerPanel2.add(inputField[i]);
		}

		addItemLabel[0].setText("Quantity:");
		addItemLabel[1].setText("Selling Price:");
		addItemLabel[2].setText("Patient_ID:");
		addItemLabel[3].setText("Patient Name:");

		JPanel operationBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		operationBtnPanel.setBackground(Color.WHITE);
		centerPanel1.add(operationBtnPanel, BorderLayout.SOUTH);

		//
		for (int j = 0; j < buttons.length; j++) {
			buttons[j] = new JButton();
			buttons[j].setForeground(new Color(0, 0, 0));
			buttons[j].setFont(new Font("David", 1, 18));
			operationBtnPanel.add(buttons[j]);
			buttons[j].addActionListener(new ButtonListener());
			// buttons[j].addFocusListener(new HoverEffect());
			buttons[j].setPreferredSize(new Dimension(200, 30));
			buttons[j].setBackground(Color.WHITE);
			buttons[j].setForeground(Color.BLACK);
			buttons[j].setBorder(new LineBorder(new Color(0,194,255),1));
		}
		buttons[0].setText("Add");
		buttons[1].setText("Save/Print");
		buttons[2].setText("Delete Row");
		buttons[3].setText("Clear Table");

		JPanel tableAndFieldsPanel = new JPanel(new GridLayout(1, 2));
		tableAndFieldsPanel.setBackground(Color.WHITE);
		tableAndFieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		tableAndFieldsPanel.add(centerPanel2);
		centerMainPanel.add(tableAndFieldsPanel);
		// table
		// table for inserted data from input field
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 16));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 12));

		String tableColumn[] = { "drugId", "Description", "Quantity", "UnitPrice", "TotalPrice", "ExpiryDate", "Date" };
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar3 = new JScrollPane(table);
		//
		cellRenderer1 = new DefaultTableCellRenderer();
		cellRenderer1.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(320);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		//
		tColumn = table.getColumnModel().getColumn(3);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.YELLOW, Color.BLACK));
		//
		tColumn = table.getColumnModel().getColumn(4);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.PINK, Color.BLACK));
		//
		JPanel tablePanel = new JPanel(new GridLayout());
		tablePanel.add(scrollBar3);
		tablePanel.setBackground(Color.WHITE);
		tableAndFieldsPanel.add(tablePanel);
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalLabel = new JLabel("Grand Total: ");
		totalLabel.setFont(new Font("Aharoni", Font.BOLD, 30));
		totalLabel.setForeground(Color.WHITE);
		southPanel.setBackground(Color.GRAY);
		southPanel.add(totalLabel);
		southPanel.add(new JLabel(""));
		southPanel.add(new JLabel(""));
		southPanel.add(new JLabel(""));

		//
		exitB = new JButton();
		exitB.setBackground(Color.GRAY);
		ImageIcon logout = new ImageIcon(getClass().getResource("/all/images/logout.png"));

		exitB.setIcon(logout);
		exitB.addActionListener(new ExitListener());

		southPanel.add(exitB);
		add(southPanel, BorderLayout.SOUTH);

		add(centerMainPanel, BorderLayout.CENTER);
		// this.setVisible(true);
		getAllInventoryDrugs();// drug in inventory table
		lastPatientId();// last patient id
	}

	// exit listener class
	private class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			Hospital_HomePage hhp = new Hospital_HomePage();
			hhp.setVisible(true);
			dispose();
		}

	}

	// Button action listener class
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			double totalBought = 0.0;
			JButton btn = (JButton) ev.getSource();
			if (btn.getActionCommand().equals("Add")) {

				if (inputField[0].getText().isEmpty() || inputField[0].getText().isEmpty()
						|| inputField[1].getText().isEmpty() || inputField[2].getText().isEmpty()
						|| inputField[3].getText().isEmpty() || drugDescriptionField.getText().toString().isEmpty()
						|| chooser.getDate() == null) {
					JOptionPane.showMessageDialog(null, "Some or all the text fields " + "are empty");
				} else {
					try {
						totalBought = (Integer.parseInt(inputField[0].getText().trim().toString())
								* Double.parseDouble(inputField[1].getText()));

						// NumberFormat numberfmt = NumberFormat.getInstance();
						// date format
						SimpleDateFormat dFormat = new SimpleDateFormat("yyyy/MM/dd");
						String bus_date = dFormat.format(chooser.getDate());
						model.addRow(new String[] { drugIdNo, drugDescriptionField.getText().toString(),
								inputField[0].getText(), inputField[1].getText(), String.format("%2f", totalBought),
								expireDate, bus_date });

						inputField[0].setText(null);
						inputField[1].setText(null);
						drugDescriptionField.setText(null);

						//

					} catch (NumberFormatException nfc) {
						JOptionPane.showMessageDialog(null,
								"Number format exception error has occur." + "\nPlease enter the require data...");

					}
				}
			} //
			if (btn.getActionCommand().equals("Clear Table")) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Do you want to clear the selected items " + "from the table?");
				if (choice == JOptionPane.YES_OPTION) {
					model.setRowCount(0);
					// reset the amount variable to zero
					patientAmount = 0;
					totalLabel.setText("AMOUNT: " + patientAmount + " (Naira)");// display the total amount on jLabel at
																				// south panel

				}

			}
			if (btn.getActionCommand().equals("Delete Row")) {
				int row = table.getSelectedRow();
				if (row < 0) {
					JOptionPane.showMessageDialog(null, "No table row is selected....");
				} else {
					model.removeRow(row);

					totalPaid = 0;// reset amount paid
					NumberFormat numberfmt = NumberFormat.getInstance();

					totalLabel.setText("AMOUNT: " + numberfmt.format(totalPaid) + "(Naira)");// display the total amount
																								// on jLabel at south

				}
			} //

			if (btn.getActionCommand().equals("Save/Print")) {

				if (table.getRowCount() != 0) {
					totalPaid();// total Paid by customer

					int choice = JOptionPane.showConfirmDialog(null,
							"You are about to save/send the table data." + "\n Do you want to to so?");
					if (choice == JOptionPane.YES_OPTION) {
						// Save item bought to table in db
						saveSaleInvoice();
						//
						lastPatientId();// last patient id

						getAllInventoryDrugs();// refresh table
						invTablemodel.setRowCount(0);
						getAllInventoryDrugs();// refresh table
						model.setRowCount(0);// clear order table

						int choice2 = JOptionPane.showConfirmDialog(null,
								"Click Yes to" + " print the invoice or otherwise.");
						if (choice2 == JOptionPane.YES_OPTION) {
							printSaleInvoice();
						}
					}
				}
			}
		}

		private void saveSaleInvoice() {

			SimpleDateFormat dFormat = new SimpleDateFormat("yyyy/MM/dd");
			String bus_date = dFormat.format(chooser.getDate());
			PreparedStatement ps = null, pPs = null;
			String qry = "Insert into saleInvoice_Table VALUES(?,?,?,?,?,?,?)";
			String patientInvoice = "Insert into Patient_Invoice Values (?,?,?,?,?)";

			try {
				for (int i = 0; i < table.getRowCount(); i++) {

					String drugName = (String) table.getValueAt(i, 1);
					int qttySold = Integer.parseInt(table.getValueAt(i, 2) + "");
					double unitPrice = Double.parseDouble(table.getValueAt(i, 3) + "");
					double totalPrice = Double.parseDouble((String) table.getValueAt(i, 4));
					String expirTime = (String) table.getValueAt(i, 5);
					String saleDate = (String) table.getValueAt(i, 6);
					patientAmount += (unitPrice * qttySold);

					ps = St_MaryConnection.getConnection().prepareStatement(qry);

					ps.setInt(1, Integer.parseInt((String) table.getValueAt(i, 0)));
					ps.setString(2, drugName);
					ps.setInt(3, qttySold);
					ps.setDouble(4, unitPrice);
					ps.setDouble(5, totalPrice);
					ps.setString(6, expirTime);
					ps.setString(7, saleDate);

					ps.execute();
				}

				pPs = St_MaryConnection.getConnection().prepareStatement(patientInvoice);
				pPs.setInt(1, lastIdNo);
				pPs.setString(2, inputField[2].getText().toUpperCase());
				pPs.setString(3, inputField[3].getText().toUpperCase());
				pPs.setDouble(4, patientAmount);
				pPs.setString(5, bus_date);
				pPs.execute();
				NumberFormat numberfmt = NumberFormat.getInstance();

				totalLabel.setText("AMOUNT: " + numberfmt.format(patientAmount) + "(Naira)");// display the total amount
																								// on jLabel at south
																								// panel

			} catch (SQLException exc) {
				System.out.println("Sale invoice entry error\n" + exc);
			}

		}

	}

	// mouse Listener
	private class SetBackTextFieldValueListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {

			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();
				// String []text = (String[]) table.getinventoryTableAt(row, col);

				drugIdNo = (String) targetCell.getValueAt(row, 0);
				String dDescription = (String) targetCell.getValueAt(row, 1);
				String sellingPrice = (String) targetCell.getValueAt(row, 4);
				expireDate = (String) targetCell.getValueAt(row, 6);

				drugDescriptionField.setText(dDescription);
				inputField[1].setText(sellingPrice);

				// Reset the amount paid by each customer when the table row is clicked
				patientAmount = 0;
				totalLabel.setText("AMOUNT: " + patientAmount + " (Naira)");// display the total amount on jLabel at
				//
				totalPaid = 0;// reset amount paid
				NumberFormat numberfmt = NumberFormat.getInstance();

				totalLabel.setText("AMOUNT: " + numberfmt.format(totalPaid) + "(Naira)");// display the total amount
																							// on jLabel at south
																							// panel

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

	private void getAllInventoryDrugs() {

		String qry = "Select * From drug_inventory";
		String dDescribe1 = "", dCat1 = "", batchNo1 = null, expireDate1 = null, invDate1 = null;
		int reOrderQtty1 = 0;
		int dId1 = 0;
		double unitPrice1 = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				dDescribe1 = rs.getString("description");
				dCat1 = rs.getString("category");
				batchNo1 = rs.getString("batchNo");
				expireDate1 = rs.getString("expiryDate");
				invDate1 = rs.getString("invDate");
				reOrderQtty1 = rs.getInt("Re_Order_Qtty");
				dId1 = rs.getInt("inventory_id");
				unitPrice1 = rs.getDouble("sellingPrice");
				invTablemodel.addRow(new String[] { "" + dId1, dDescribe1, dCat1, "" + reOrderQtty1, "" + unitPrice1,
						batchNo1, expireDate1, invDate1 });
			}

		} catch (SQLException ex) {
			System.out.println("Getting all drug details from inventory errror\n");
		}
	}

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(invTablemodel);
		inventoryTable.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	//
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

	private void totalPaid() {
		for (int i = 0; i < table.getRowCount(); i++) {

			int qttySold = Integer.parseInt(table.getValueAt(i, 2) + "");
			double unitPrice = Double.parseDouble(table.getValueAt(i, 3) + "");
			totalPaid += (unitPrice * qttySold);
		}
		NumberFormat numberfmt = NumberFormat.getInstance();

		totalLabel.setText("AMOUNT: " + numberfmt.format(totalPaid) + "(Naira)");// display the total amount
																					// on jLabel at south
																					// panel
	}

	// Print sale invoice
	private void printSaleInvoice() {

		// generate random no for invoice references
		int serialNo = 0;
		int seed;
		Document doc = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sformat = null;
		PdfWriter pdfWriter = null;
		Rectangle paperSize = new Rectangle(168.41f, 300);
		//
		// generate random no for invoice references

		for (seed = 0; seed < 1000; seed++) {
			serialNo = (int) (Math.random() * 1000);
		}
		//
		//
		//
		try {
			doc = new Document(paperSize);

			pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream("C:\\St_Mary_Flies\\StMarySale\\InvoiceFiles\\"
					+ "" + inputField[2].getText().toString() + "-" + serialNo + ".pdf"));
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "The file is already opened. Close it and try again...");
		} catch (DocumentException doE) {
			doE.printStackTrace();
		}
		// open the document instance and write in the necessary files

		doc.open();

		try {

			// Time for the invoice creation
			com.itextpdf.text.Font red = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 8,
					com.itextpdf.text.Font.NORMAL, new BaseColor(0, 0, 0));
			//
			sformat = new SimpleDateFormat("yyyy-MM-dd");

			//
			// invoice para
			Paragraph invoiceParagraph = new Paragraph("INVOICE", red);
			invoiceParagraph.setAlignment(Element.ALIGN_CENTER);
			doc.add(invoiceParagraph);
			invoiceParagraph.setSpacingAfter(30);

			//
			com.itextpdf.text.Font blueFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 6,
					com.itextpdf.text.Font.UNDERLINE, BaseColor.BLACK);
			Paragraph blueParag = new Paragraph("ST. MARY'S HOSPITAL, OKPOGA", blueFont);
			blueParag.setAlignment(Element.ALIGN_LEFT);
			blueParag.setSpacingBefore(20);
			blueParag.setSpacingAfter(10);
			doc.add(blueParag);
			//

			// Overall paragraph
			Paragraph allParagraph = new Paragraph();
			// font size and color patient details
			com.itextpdf.text.Font patientFont = new com.itextpdf.text.Font(
					com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 7, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
			//
			// font size and color patient details
			com.itextpdf.text.Font pRefont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
					7, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);

			Paragraph patient_Id = new Paragraph("Patient Ref: ", pRefont);
			patient_Id.setAlignment(Element.ALIGN_LEFT);
			Paragraph patient_Id2 = new Paragraph(inputField[2].getText() + "\n" + "___________________________",
					pRefont);
			patient_Id2.setAlignment(Element.ALIGN_RIGHT);
			//
			allParagraph.add(patient_Id);
			allParagraph.add(patient_Id2);
			//
			Paragraph patient_name_p = new Paragraph("Patient Name: ", patientFont);
			patient_name_p.setAlignment(Element.ALIGN_LEFT);

			//
			Paragraph patient_name_p2 = new Paragraph(
					inputField[3].getText().toUpperCase() + "\n" + "___________________________", patientFont);
			patient_name_p2.setAlignment(Element.ALIGN_RIGHT);
			//
			allParagraph.add(patient_name_p);
			allParagraph.add(patient_name_p2);
			//
			// total amount paid
			NumberFormat numberfmt = NumberFormat.getInstance();

			Paragraph amountPara = new Paragraph("Total Amount Paid:\n ___________________________ ", patientFont);
			Paragraph amountPara2 = new Paragraph(numberfmt.format(totalPaid) + "[Naira]\n__________________",
					patientFont);
			amountPara.setAlignment(Element.ALIGN_LEFT);
			amountPara2.setAlignment(Element.ALIGN_RIGHT);
			//
			allParagraph.add(amountPara);
			allParagraph.add(amountPara2);
			doc.add(allParagraph);
			//
			// adding barcode
			Barcode128 code128 = new Barcode128();
			code128.setGenerateChecksum(true);
			code128.setCode("ST. MARY");
			Paragraph barP = new Paragraph();
			barP.setAlignment(Element.ALIGN_CENTER);
			barP.add(code128.createImageWithBarcode(pdfWriter.getDirectContent(), null, null));
			doc.add(barP);

			// add transaction date to the doc
			// font size and color patient details
			com.itextpdf.text.Font dateFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
					5, com.itextpdf.text.Font.ITALIC, BaseColor.BLACK);
			//
			sformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			Paragraph datePara = new Paragraph(sformat.format(calendar.getTime()), dateFont);
			datePara.setAlignment(Element.ALIGN_CENTER);
			doc.add(datePara);

			// doc.close();

		} catch (DocumentException e1) {
			e1.printStackTrace();
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (pdfWriter != null) {
				pdfWriter.close();
			}
		}

		//
		// Open the file created immediately

		if (Desktop.isDesktopSupported()) {

			File myFile = new File("C:\\St_Mary_Flies\\StMarySale\\InvoiceFiles\\" + ""
					+ inputField[2].getText().toString() + "-" + serialNo + ".pdf");

			try {
				Desktop.getDesktop().open(myFile);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// refresh table that contains inventory for sales
	private class RefreshListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			invTablemodel.setRowCount(0);
			getAllInventoryDrugs();// refresh table

		}

	}
	// last patient invoice table

	// Getting last inventory id from inventory table
	private void lastPatientId() {
		int inventId = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select idNo From" + " patient_invoice order by idNo desc limit 1");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				inventId = rs.getInt("idNo");
				lastIdNo = ((inventId + 000001));
			}
		} catch (SQLException exc) {
			System.out.println("last inventory id from table error\n" + exc);
		}
	}
}
