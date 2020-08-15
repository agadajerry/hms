package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PrintingDialog extends JDialog {

	private JButton printB, cancelB, printAllB;
	private JComboBox<String> idBox, dateBox;
	private DefaultComboBoxModel<String> idModel, dateModel;
	private JTable table = SearchPatientDialog.PatientTable;

	int sno = 0;

	public PrintingDialog() {
		setSize(new Dimension(462, 243));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setLayout(null);
		this.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.GRAY));

		this.setLocationRelativeTo(null);
		setBackground(Color.WHITE);
		setModal(true);
		initUi();

	}

	private void initUi() {

		

		//
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setLayout(null);
		centerPanel.setBounds(10, 20, 452, 233);

		//

		idModel = new DefaultComboBoxModel<String>();
		idBox = new JComboBox<>(idModel);
		AutoCompleteDecorator.decorate(idBox);
		// idBox.addItemListener(new ItemSelectListener());
		JLabel selectId = new JLabel("Select ID:");

		selectId.setBounds(20, 20, 70, 30);
		idBox.setBounds(95, 30, 200, 30);

		centerPanel.add(selectId);
		centerPanel.add(idBox);
		//
		printB = new JButton("Print");
		ImageIcon logout = new ImageIcon(getClass().getResource("/all/images/logout.png"));

		cancelB = new JButton(logout);
		cancelB.setBackground(Color.WHITE);
		printB.setBounds(95, 70, 100, 30);
		//printB.setBackground(Color.GRAY);
		printB.setForeground(Color.BLACK);
		cancelB.setBounds(330, 170, 64, 30);
		centerPanel.add(printB);
		centerPanel.add(cancelB);
		//

		dateModel = new DefaultComboBoxModel<String>();
		dateBox = new JComboBox<String>(dateModel);
		AutoCompleteDecorator.decorate(dateBox);

		printAllB = new JButton("Print All");
		dateBox.setBounds(95, 130, 170, 30);
		printAllB.setBounds(265, 130, 100, 30);
		printAllB.addActionListener(new PrintAllListListener());
		centerPanel.add(dateBox);
		centerPanel.add(printAllB);
		//
		add(centerPanel);
		printB.addActionListener(new PrintListener());
		cancelB.addActionListener(new CancelOperation());

		// populate date cpmboBox with date from db
		comboDate();
		patietIds();
	}

	// printing inner class
	private class CancelOperation implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			SearchPatientDialog adm = new SearchPatientDialog();

			adm.setVisible(true);
			dispose();
		}

	}

	// printing job class
	private class PrintListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// printAllPatientList();
			if (idBox.getSelectedIndex() != -1) {

				if (idBox.getSelectedIndex() == -1) {
					System.out.println("error, Empty box");
				} else {
					printPatientBio_Data(idBox.getSelectedItem().toString());
				}
			}
		}

	}

	// populate date cpmboBox with date from db

	private void patietIds() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlQuery = "Select DISTINCT patient_id From patient_register";
		try {

			ps = St_MaryConnection.getConnection().prepareStatement(sqlQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				String dates = rs.getString("patient_id");

				idModel.addElement(dates);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Date from db " + ex.getMessage());
			ex.printStackTrace();

		}

	}

	private void comboDate() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sqlQuery = "Select DISTINCT YEAR(reg_date) AS year From patient_register";
		try {

			ps = St_MaryConnection.getConnection().prepareStatement(sqlQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				String yearDate = rs.getString("year");
				dateModel.addElement(yearDate);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Date from db " + ex.getMessage());
			ex.printStackTrace();

		}

	}

	// print patients info from the table

	private void printAllPatientList(String reg_Dates) {
		Document doc = new Document();
		// ArrayList<PatientTableFields>pTableArray;
		try {
			PdfWriter.getInstance(doc, new FileOutputStream("C:\\St_Mary_Flies\\TallyCard\\"
					+ "\\InvoiceFiles\\registerList\\" + dateBox.getSelectedItem().toString() + ".pdf"));
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
			com.itextpdf.text.Font dateFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
					20, com.itextpdf.text.Font.UNDERLINE, new BaseColor(0, 0, 0));

			Paragraph datePara = new Paragraph(sformat.format(calendar.getTime()), dateFont);
			datePara.setAlignment(Element.ALIGN_CENTER);
			datePara.setSpacingBefore(20);
			datePara.setSpacingAfter(50);
			doc.add(datePara);
			//
			// table for address of the hospital
			com.itextpdf.text.Font blackFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,
					24, com.itextpdf.text.Font.NORMAL, new BaseColor(0, 0, 0));
			Paragraph blakParag = new Paragraph("ST. MARY'S HOSPITAL,OKPOGA", blackFont);
			blakParag.setAlignment(Element.ALIGN_CENTER);
			blakParag.setSpacingAfter(10);
			doc.add(blakParag);

			//
			// table for address of the hospital
			com.itextpdf.text.Font blackFont2 = new com.itextpdf.text.Font(
					com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18, com.itextpdf.text.Font.UNDERLINE,
					new BaseColor(0, 0, 0));
			Paragraph blakParag2 = new Paragraph(" LIST OF REGISTERED PATIENTS", blackFont2);
			blakParag2.setAlignment(Element.ALIGN_CENTER);
			blakParag2.setSpacingAfter(10);
			doc.add(blakParag2);
			// .............................................................................

			/*
			 * Here i will get get all patients details in the table for a particular date
			 * of registration
			 */
			// .............................................................................

			// LoadPatientTable p = new LoadPatientTable();
			// pTableArray = p.allPatientList(reg_Dates);

			// table of value for patient
			float[] columnWidth = { 2.2f, 2.7f, 2.7f, 2.2f, 2f, 2.5f, 3.3f, 3f, 2.4f };

			// pdf table
			PdfPTable pdfTable = new PdfPTable(columnWidth);
			// table width percentage of the page width
			pdfTable.setWidthPercentage(100f);
			pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			pdfTable.addCell("PID");
			pdfTable.addCell("SurName");
			pdfTable.addCell("OtherName");
			pdfTable.addCell("Occupation");
			pdfTable.addCell("Age");
			pdfTable.addCell("Sex");
			pdfTable.addCell("PhoneNo");
			pdfTable.addCell("Address");
			pdfTable.addCell("Date");

			pdfTable.setHeaderRows(1);
			pdfTable.setSpacingBefore(20);
			PdfPCell[] cells = pdfTable.getRow(0).getCells();
			for (int i = 0; i < cells.length; i++) {
				cells[i].setBackgroundColor(BaseColor.WHITE);
			}

			// loop through the table and get it info
			for (int i = 0; i < table.getRowCount(); i++) {

				pdfTable.addCell((String) table.getValueAt(i, 0));
				pdfTable.addCell((String) table.getValueAt(i, 1));
				pdfTable.addCell((String) table.getValueAt(i, 2));
				pdfTable.addCell((String) table.getValueAt(i, 3));
				pdfTable.addCell((String) table.getValueAt(i, 4));
				pdfTable.addCell((String) table.getValueAt(i, 5));
				pdfTable.addCell((String) table.getValueAt(i, 6));
				pdfTable.addCell((String) table.getValueAt(i, 7) + "\n");
				pdfTable.addCell((String) table.getValueAt(i, 8));

			}

			Paragraph tableP = new Paragraph();
			tableP.setAlignment(Element.ALIGN_CENTER);
			tableP.add(pdfTable);
			doc.add(tableP);

			//
			doc.close();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Open created pdf file immediately
		if (Desktop.isDesktopSupported()) {
			//
			File myFile = new File("C:\\St_Mary_Flies\\TallyCard\\" + "\\InvoiceFiles\\registerList\\"
					+ dateBox.getSelectedItem().toString() + ".pdf");
			try {
				Desktop.getDesktop().open(myFile);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// print patient's card
	private void printPatientBio_Data(String p_id) {

		String pId = "", surname = "", otherName = "", occupation = "", address = "", phoneNo = "", age = "",
				genda = "", regDate = "";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Select * From patient_register where patient_id=?");
			ps.setString(1, p_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				pId = rs.getString("patient_id");
				surname = rs.getString("surname");
				otherName = rs.getString("other_name");
				occupation = rs.getString("occupation");
				age = rs.getString("age");
				genda = rs.getString("gender");
				phoneNo = rs.getString("phone_no");
				address = rs.getString("address");
				regDate = rs.getString("reg_date");
			}

			//

			Document doc = new Document();
			try {

				PdfWriter.getInstance(doc, new FileOutputStream("C:\\St_Mary_Flies\\TallyCard\\"
						+ "\\InvoiceFiles\\registerList\\" + "" + idBox.getSelectedItem().toString() + ".pdf"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "The file is already opened. Close it and try again...");
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			doc.open();

			try {
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd");
				com.itextpdf.text.Font dateFont = new com.itextpdf.text.Font(
						com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 20, com.itextpdf.text.Font.UNDERLINE,
						new BaseColor(0, 0, 0));

				Paragraph datePara = new Paragraph(sformat.format(calendar.getTime()), dateFont);
				datePara.setAlignment(Element.ALIGN_RIGHT);
				datePara.setSpacingBefore(20);
				datePara.setSpacingAfter(50);
				doc.add(datePara);
				//
				// table for address of the hospital
				com.itextpdf.text.Font blackFont = new com.itextpdf.text.Font(
						com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 24, com.itextpdf.text.Font.UNDERLINE,
						new BaseColor(0, 0, 0));
				Paragraph blakParag = new Paragraph("ST. MARY'S HOSPITAL,OKPOGA", blackFont);
				blakParag.setAlignment(Element.ALIGN_CENTER);
				blakParag.setSpacingAfter(20);
				doc.add(blakParag);
				//
				Paragraph patientTitelParag = new Paragraph("Patient Bio-Data",dateFont);
				
				patientTitelParag.setAlignment(Element.ALIGN_CENTER);
				patientTitelParag.setSpacingAfter(30);
				doc.add(patientTitelParag);

				// all paragraphs are added to this paragraph
				Paragraph allParagraph = new Paragraph();
				// Adding a particular patient info on a paragraphs
				Paragraph pIdPara1 = new Paragraph("Patient ID:");
				Paragraph pIdPara2 = new Paragraph(pId + "\n________________________________________________________");
				pIdPara1.setAlignment(Element.ALIGN_LEFT);
				pIdPara2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(pIdPara1);
				allParagraph.add(pIdPara2);
				//
				Paragraph surNamePara1 = new Paragraph("Surname:");
				Paragraph surNamePara2 = new Paragraph(
						surname + "\n ________________________________________________________");

				surNamePara1.setAlignment(Element.ALIGN_LEFT);
				surNamePara2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(surNamePara1);
				allParagraph.add(surNamePara2);

				//

				Paragraph otherNamePara1 = new Paragraph("OtherName:");
				Paragraph otherNamePara2 = new Paragraph(
						otherName + "\n ________________________________________________________");

				otherNamePara1.setAlignment(Element.ALIGN_LEFT);
				otherNamePara2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(otherNamePara1);
				allParagraph.add(otherNamePara2);

				//
				Paragraph occupationP1 = new Paragraph("Occupation:");
				Paragraph occupationP2 = new Paragraph(
						occupation + "\n ________________________________________________________");

				occupationP1.setAlignment(Element.ALIGN_LEFT);
				occupationP2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(occupationP1);
				allParagraph.add(occupationP2);

				//
				//
				Paragraph ageP = new Paragraph("Age:");
				Paragraph ageP2 = new Paragraph(age + "\n ________________________________________________________");

				ageP.setAlignment(Element.ALIGN_LEFT);
				ageP2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(ageP);
				allParagraph.add(ageP2);

				//
				Paragraph gendaP = new Paragraph("Gender:");
				Paragraph gendaP2 = new Paragraph(
						genda + "\n ________________________________________________________");

				gendaP.setAlignment(Element.ALIGN_LEFT);
				gendaP2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(gendaP);
				allParagraph.add(gendaP2);

				//
				Paragraph phoneP = new Paragraph("Phone No:");
				Paragraph phoneP2 = new Paragraph(
						phoneNo + "\n ________________________________________________________");

				phoneP.setAlignment(Element.ALIGN_LEFT);
				phoneP2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(phoneP);
				allParagraph.add(phoneP2);

				//
				//

				//
				Paragraph addressP = new Paragraph("Address:");
				Paragraph addressP2 = new Paragraph(
						address + "\n ________________________________________________________");

				addressP.setAlignment(Element.ALIGN_LEFT);
				addressP2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(addressP);
				allParagraph.add(addressP2);
				//
				//
				Paragraph regP = new Paragraph("Registration Date:");
				Paragraph regP2 = new Paragraph(
						regDate + "\n ________________________________________________________");

				regP.setAlignment(Element.ALIGN_LEFT);
				regP2.setAlignment(Element.ALIGN_CENTER);
				allParagraph.add(regP);
				allParagraph.add(regP2);
				//

				doc.add(allParagraph);
				allParagraph.setSpacingAfter(30);

			} catch (DocumentException e1) {
				System.out.println("Error in opening file already opened" + e1);
			} finally {
				if (doc != null) {
					doc.close();
				}

			}

			// Open created pdf file immediately
			if (Desktop.isDesktopSupported()) {
			
				File myFile = new File("C:\\St_Mary_Flies\\TallyCard\\" + "\\InvoiceFiles\\registerList\\"
						+ idBox.getSelectedItem().toString() + ".pdf");
				try {
					Desktop.getDesktop().open(myFile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (SQLException ex) {
			System.out.println("Error in getting patient with a given id\n" + ex);
		} finally {
			try {
				if (rs != null && ps != null) {
					St_MaryConnection.getConnection().close();
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//

	// print all patient list on a given year
	private class PrintAllListListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			printAllPatientList(dateBox.getSelectedItem().toString());
		}

	}
}
