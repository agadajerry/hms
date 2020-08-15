package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;

public class DrugTallyPanel extends JPanel {

	private JLabel[] inputLabel = new JLabel[12];
	private JTable table, table2;
	private DefaultTableModel model, model2;
	private DefaultComboBoxModel<String> expireModel;
	public static DefaultComboBoxModel<String> drugModel;
	private JComboBox<String> expireBox, drugBox;
	private JButton okBtn, printB;

	public DrugTallyPanel() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/tally_card.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel, BorderLayout.NORTH);
		add(northPanel, BorderLayout.NORTH);
		//

		//

		JPanel centerPanel = new JPanel(new FlowLayout());
		//centerPanel.setBorder(new EmptyBorder(10,10,10,10));
		centerPanel.setBackground(Color.WHITE);
		drugModel = new DefaultComboBoxModel<String>();
		drugBox = new JComboBox<String>(drugModel);
		AutoCompleteDecorator.decorate(drugBox);
		drugBox.setPreferredSize(new Dimension(230, 30));
		drugBox.addItemListener(new DrugSelectionListener());
		centerPanel.add(drugBox);
		//
		expireModel = new DefaultComboBoxModel<String>();
		expireBox = new JComboBox<String>(expireModel);
		expireBox.setPreferredSize(new Dimension(230, 30));

		AutoCompleteDecorator.decorate(expireBox);

		//

		//
		JLabel dateLabel = new JLabel("Expiry Date");
		centerPanel.add(dateLabel);
		centerPanel.add(expireBox);

		//
		okBtn = new JButton("Go");
		okBtn.setPreferredSize(new Dimension(100, 30));
		okBtn.setBackground(Color.WHITE);
		okBtn.setForeground(Color.BLACK);
		okBtn.addActionListener(new TallyCardListenr());
		centerPanel.add(okBtn);

		//
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.RED);
		table.setFont(new Font("David", Font.BOLD, 16));

//

		String tableColumn[] = { "Date", "From_Whom_Rec", "To_Whom_Issued", "Issued_Qtty" };
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar = new JScrollPane(table);
		scrollBar.setPreferredSize(new Dimension(550, 100));
		//
		JPanel tablePanel = new JPanel(new GridLayout(1,2,9,8));
		tablePanel.setPreferredSize(new Dimension(1100, 330));
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
		table2.setForeground(Color.RED);
		table2.setFont(new Font("David", Font.BOLD, 16));

		// table.addMouseListener(new TextFieldGetTextListener());
//

		String tableColumn2[] = { "Receipt Qtty", "Balance", "Voucher_No", "BatchNo", "Remark" };

		model2 = (DefaultTableModel) table2.getModel();
		model2.setColumnIdentifiers(tableColumn2);
		JScrollPane scrollBar2 = new JScrollPane(table2);
		scrollBar2.setPreferredSize(new Dimension(550, 60));

		JPanel table2Panel = new JPanel(new BorderLayout());
		table2Panel.add(scrollBar2, BorderLayout.NORTH);
		// info panel

		JPanel displayPanel = new JPanel(new GridLayout(12, 1));
		displayPanel.setBackground(Color.WHITE);
		
		displayPanel.add(new JLabel(""));
		displayPanel.add(new JLabel(""));
		displayPanel.add(new JLabel(""));
		displayPanel.add(new JLabel(""));
		for (int i = 0; i < inputLabel.length; i++) {
			inputLabel[i] = new JLabel();
			inputLabel[i].setForeground(new Color(0, 0, 0));
			inputLabel[i].setFont(new Font("David", 1, 14));
			inputLabel[i].setPreferredSize(new Dimension(200, 30));
			displayPanel.add(inputLabel[i]);
		}

		inputLabel[0].setText("Discription");
		inputLabel[1].setText("Unit Of Issue");
		inputLabel[2].setText("Unit Price");
		inputLabel[3].setText("Catalogue Number");

		table2Panel.add(displayPanel, BorderLayout.CENTER);
		tablePanel.add(table2Panel);

		centerPanel.add(tablePanel);
		add(centerPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		printB = new JButton("Print");
		printB.setPreferredSize(new Dimension(100, 30));
		printB.setBackground(Color.WHITE);
		printB.setForeground(Color.BLACK);
		printB.addActionListener(new PrintTallyCard());
		southPanel.add(printB);
		add(southPanel, BorderLayout.SOUTH);

		allDrugList();// drug name method
		// allExpiryDateList();// expiry date list
	}

	// tally card methods

	private void TallyCard(String drugName, String expirationDate) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		String qry2 = "SELECT  counterdrugordertable.orderDate, "
				+ "counterdrugordertable.to_whom_issued, counterdrugordertable.whom_Recieved,"
				+ "counterdrugordertable.quantityDemanded, "
				+ "unitPrice from counterdrugordertable where drugNameOrder =? AND "
				+ "expiryDate =? ORDER BY counterdrugordertable.orderDate asc";
		int issueQttyDemanded = 0;
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry2);
			ps.setString(1, drugName);
			ps.setString(2, expirationDate);
			rs = ps.executeQuery();
			// table.setModel(DbUtils.resultSetToTableModel(rs));
			while (rs.next()) {

				String orderDate = rs.getString("orderDate");
				String from = rs.getString("to_whom_issued");
				String to = rs.getString("whom_Recieved");
				issueQttyDemanded = rs.getInt("quantityDemanded");
				double unitP = rs.getDouble("unitPrice");

				model.addRow(new String[] { orderDate, from, to, "" + issueQttyDemanded });
				inputLabel[0].setText("Description:__________" + drugName+"____");
				inputLabel[1].setText("Unit Price:____________" + unitP+"____");
			}
		} catch (SQLException exc) {
			System.out.println("inventory  and order table joining error\n" + exc);
		}
	}
	// drug Selection listeneing class

	// get all the drug name and set it on combo box
	public static void allDrugList() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select Distinct drugNameOrder" + " From" + " counterdrugordertable");
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

	// display the information relating to the selected year and expire date

	private class TallyCardListenr implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
				
			if (expireBox.getSelectedIndex()==-1 || drugBox.getSelectedIndex()==-1) {
				JOptionPane.showMessageDialog(null, "Please select drug and it expire date");
			} else {

				model.setRowCount(0);
				model2.setRowCount(0);
				TallyCard(drugBox.getSelectedItem().toString(), expireBox.getSelectedItem().toString());
				totalQuantityInventory(drugBox.getSelectedItem().toString(), expireBox.getSelectedItem().toString());

				// qttyTotalFromOrder(drugBox.getSelectedItem().toString(),
				// expireBox.getSelectedItem().toString());

			}
		}

	}

	// get the expire date when a particular drug is selected
	private class DrugSelectionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg0) {

			if (drugBox.getSelectedIndex() != -1) {
				expireModel.removeAllElements();
				allExpiryDateList(drugBox.getSelectedItem().toString());
			}
		}

	}
	// getting a particular drug relating to a particular expire date,.....method

	private void totalQuantityInventory(String drugName, String expireDate) {

		PreparedStatement ps = null, ps2 = null;
		ResultSet rs = null, rs2 = null;
		int qttyDemandedTotal2 = 0;
		String qry = "Select quantity, batchNo, voucherNo, Re_Order_Qtty,category From drug_inventory where expiryDate=? and"
				+ " description=?";

		String qry2 = "SELECT SUM(quantityDemanded) as quantitd from counterdrugordertable "
				+ "where expiryDate=? AND drugNameOrder =?";

		try {

			try {
				ps = St_MaryConnection.getConnection().prepareStatement(qry);
				ps.setString(1, expireDate);
				ps.setString(2, drugName);

				//
				ps2 = St_MaryConnection.getConnection().prepareStatement(qry2);
				ps2.setString(1, expireDate);
				ps2.setString(2, drugName);
				rs2 = ps2.executeQuery();

				while (rs2.next()) {
					qttyDemandedTotal2 = rs2.getInt("quantitd");
					System.out.println(qttyDemandedTotal2);
				}

			} catch (SQLException exc) {
				System.out.println("getting the remaining qtty from inventory table error\n" + exc);
			}

			rs = ps.executeQuery();
			while (rs.next()) {

				String batchNo = rs.getString("batchNo");
				String voucherNo = rs.getString("voucherNo");
				int qttyReamaining = rs.getInt("quantity");
				String reOrderQ = rs.getString("Re_Order_Qtty");
				String category = rs.getString("category");

				inputLabel[2].setText("Re-Order Quantity:____________ " + reOrderQ+"____");
				inputLabel[3].setText("Unit Of Issue:____________" + category+"____");

				model2.addRow(new String[] { "" + (qttyDemandedTotal2 + qttyReamaining), "" + qttyReamaining, voucherNo,
						batchNo });

			}
		} catch (SQLException exc) {
			System.out.println("getting the remaining qtty from inventory table error\n" + exc);
		}
	}

	

	// print tally card
	private class PrintTallyCard implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (table.getRowCount() == 0 || table2.getRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "Nothing to print. The table is empty...");
			} else {
				PrintTableInfo();
			}
		}

	}

	private void PrintTableInfo() {

		Document doc = new Document();

		try {

			PdfWriter.getInstance(doc, new FileOutputStream(
					"C:\\St_Mary_Flies\\TallyCard\\" + drugBox.getSelectedItem().toString()+
					"_"+expireBox.getSelectedItem().toString() + ".pdf"));
		} catch (FileNotFoundException e1) {
		JOptionPane.showMessageDialog(null, "The file is already opened. Close it and try again...");	

		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		doc.open();
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd");
			Paragraph datePara = new Paragraph(sformat.format(calendar.getTime()));
			datePara.setAlignment(Element.ALIGN_RIGHT);
			doc.add(datePara);

			// table for address of the hospital
			com.itextpdf.text.Font blackFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
					24, com.itextpdf.text.Font.NORMAL, new BaseColor(0,0,0));
			Paragraph blakParag = new Paragraph("ST. MARY'S HOSPITAL,OKPOGA", blackFont);
			blakParag.setAlignment(Element.ALIGN_CENTER);
			blakParag.setSpacingAfter(10);
			doc.add(blakParag);
			//
			// table for address of the hospital
			com.itextpdf.text.Font blackFont2 = new com.itextpdf.text.Font(
					com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18, com.itextpdf.text.Font.UNDERLINE,
					new BaseColor(0,0,0));
			Paragraph blakParag2 = new Paragraph("PHARMACY DEPARTMENT", blackFont2);
			blakParag2.setAlignment(Element.ALIGN_CENTER);
			blakParag2.setSpacingAfter(10);
			doc.add(blakParag2);
			//
			//
			com.itextpdf.text.Font blackFont3 = new com.itextpdf.text.Font(
					com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 16, com.itextpdf.text.Font.UNDERLINE,
					new BaseColor(0,0,0));
			Paragraph blakParag3 = new Paragraph("TALLY CARD", blackFont3);
			blakParag3.setAlignment(Element.ALIGN_CENTER);
			doc.add(blakParag3);
			blakParag3.setSpacingAfter(30);
			//
			Paragraph descriParag = new Paragraph(inputLabel[0].getText());

			descriParag.setSpacingBefore(20);
			doc.add(descriParag);
			//
			Paragraph unitPara = new Paragraph(inputLabel[3].getText());
			doc.add(unitPara);
			//
			Paragraph unitPricePara = new Paragraph(inputLabel[1].getText());
			doc.add(unitPricePara);
			//
			Paragraph reOrderPara = new Paragraph(inputLabel[2].getText());
			doc.add(reOrderPara);
			//

			// column width
			float[] columnWidth = { 2.5f, 2.3f, 2.3f, 2.2f,2.8f,2f,2f,2f,2f };
			// pdf table
			PdfPTable pdfTable = new PdfPTable(columnWidth);
			// table width percentage of the page width
			pdfTable.setWidthPercentage(100f);
			pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell("Date");
			pdfTable.addCell("From Who Received");
			pdfTable.addCell("To Whom Issued");
			pdfTable.addCell("Issued Qtty");
			//
			pdfTable.addCell("Receipt Qtty");
			pdfTable.addCell("Balance");
			pdfTable.addCell("Voucher No");
			pdfTable.addCell("Batch No");
			pdfTable.addCell("Remark");
		
			pdfTable.setHeaderRows(1);
			pdfTable.setSpacingBefore(20);
			PdfPCell[] cells = pdfTable.getRow(0).getCells();

			for (int i = 0; i < cells.length; i++) {
				cells[i].setBackgroundColor(BaseColor.WHITE);

				
			}
			//
			for (int i = 0; i< table.getRowCount(); i++) {

				
				//
				if(i==0) {
					
					String issueDate = (String) table.getValueAt(0, 0);
					String fromWho = (String) table.getValueAt(0, 1);
					String toWho = (String) table.getValueAt(0, 2);
					int issQtty = Integer.parseInt((String) table.getValueAt(0, 3));

					pdfTable.addCell("" + issueDate);
					pdfTable.addCell("" + fromWho);
					pdfTable.addCell("" + toWho);
					pdfTable.addCell("" + issQtty);
					//
					pdfTable.addCell(table2.getValueAt(0, 0) + "");
					pdfTable.addCell(table2.getValueAt(0, 1) + "");
					pdfTable.addCell(table2.getValueAt(0, 2) + "");
					pdfTable.addCell(table2.getValueAt(0, 3) + "");
					
					pdfTable.addCell("Expiry: "+expireBox.getSelectedItem().toString().substring(0,7));
					//
				}if(i>0) {
					String issueDate = (String) table.getValueAt(i, 0);
					String fromWho = (String) table.getValueAt(i, 1);
					String toWho = (String) table.getValueAt(i, 2);
					int issQtty = Integer.parseInt((String) table.getValueAt(i, 3));

					pdfTable.addCell("" + issueDate);
					pdfTable.addCell("" + fromWho);
					pdfTable.addCell("" + toWho);
					pdfTable.addCell("" + issQtty);
					//
					pdfTable.addCell("");
					pdfTable.addCell("");
					pdfTable.addCell("");
					pdfTable.addCell("");
					pdfTable.addCell("");
				}
				
				
			}
			//
			
			
			Paragraph tableP = new Paragraph();
			
			com.itextpdf.text.Font tableFont = new com.itextpdf.text.Font(
					com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10, com.itextpdf.text.Font.UNDERLINE,
					BaseColor.RED);
			tableP.setFont(tableFont);
			tableP.setAlignment(Element.ALIGN_CENTER);
			tableP.add(pdfTable);
			doc.add(tableP);
			//


			doc.close();

		} catch (DocumentException ex) {
			System.out.println("error i open pdf file\n" + ex);
		}
		if (Desktop.isDesktopSupported()) {

			File myFile = new File("C:\\St_Mary_Flies\\TallyCard\\" + drugBox.getSelectedItem().toString()+
					"_"+expireBox.getSelectedItem().toString() + ".pdf");
			try {
				Desktop.getDesktop().open(myFile);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
