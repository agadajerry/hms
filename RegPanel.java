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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.toedter.calendar.JDateChooser;

public class RegPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField[] inputField = new JTextField[3];
	private JLabel[] inputLabel = new JLabel[3];
	private JLabel addrLabel;
	private JTextArea addrArea;
	private JTable table;
	private DefaultTableModel model;
	private JDateChooser regDate;
	private JComboBox<String> sexBox;
	private JTextField teleField, ageField;
	private JButton[] actionBtn = new JButton[5];
	private JTextField idField, searchField;
	private TableRowSorter<DefaultTableModel> sorter;

	public RegPanel() {

		setLayout(new BorderLayout(3, 3));
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/pat_reg.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);

		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//

		JPanel centerPanel = new JPanel(new GridLayout(1, 2));
		centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JPanel leftSidePanel = new JPanel(new GridLayout(7, 2, 3, 3));
		JPanel rightSidePanel = new JPanel();
		rightSidePanel.setBorder(new LineBorder(Color.GRAY, 1));
		leftSidePanel.setBorder(new LineBorder(Color.GRAY, 1));
		rightSidePanel.setBackground(Color.WHITE);
		leftSidePanel.setBackground(Color.WHITE);
		centerPanel.setBackground(Color.WHITE);

		rightSidePanel.setLayout(null);
		JPanel lastRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lastRightPanel.setBackground(Color.WHITE);

		centerPanel.add(leftSidePanel);
		centerPanel.add(rightSidePanel);
		// centerPanel.add(lastRightPanel);
		// centerPanel.add(new JPanel());
		//
		ageField = new JTextField();
		ageField.setBorder(new LineBorder(Color.GRAY, 1));
		//
		idField = new JTextField();
		idField.setPreferredSize(new Dimension(200, 30));
		idField.setBorder(new LineBorder(Color.GRAY, 1));
		idField.setFont(new Font("David", 1, 16));
		//
		JLabel idLabel = new JLabel("Patient ID:");
		idLabel.setForeground(new Color(0, 0, 0));
		idLabel.setFont(new Font("David", 1, 16));

		leftSidePanel.add(idLabel);
		leftSidePanel.add(idField);
		//
		for (int i = 0; i < inputLabel.length; i++) {
			inputField[i] = new JTextField();

			inputLabel[i] = new JLabel();
			inputLabel[i].setForeground(new Color(0, 0, 0));
			inputLabel[i].setFont(new Font("David", 1, 16));
			inputField[i].setFont(new Font("David", 1, 16));
			inputField[i].setBorder(new LineBorder(Color.GRAY, 1));
			inputField[i].setPreferredSize(new Dimension(200, 30));
			inputLabel[i].setPreferredSize(new Dimension(200, 30));
			inputLabel[i].setForeground(new Color(0, 0, 0));
			inputLabel[i].setFont(new Font("David", 1, 16));
			leftSidePanel.add(inputLabel[i]);
			leftSidePanel.add(inputField[i]);
		}
		inputLabel[0].setText("Surname:");
		inputLabel[1].setText("Other Name:");
		inputLabel[2].setText("Occupation:");

		//
		JLabel dobLabel = new JLabel("Age");
		dobLabel.setPreferredSize(new Dimension(200, 30));
		dobLabel.setForeground(new Color(0, 0, 0));
		dobLabel.setFont(new Font("David", 1, 16));
		leftSidePanel.add(dobLabel);
		leftSidePanel.add(ageField);
		//
		String[] genda = { "Gender", "Male", "Female" };
		sexBox = new JComboBox<String>(genda);
		JLabel sexLabel = new JLabel("Gender:");
		sexLabel.setPreferredSize(new Dimension(200, 30));
		sexLabel.setForeground(new Color(0, 0, 0));
		sexLabel.setFont(new Font("David", 1, 16));
		//
		sexBox.setPreferredSize(new Dimension(200, 30));
		sexBox.setForeground(new Color(0, 0, 0));
		sexBox.setFont(new Font("David", 1, 16));
		sexBox.setBorder(new LineBorder(Color.GRAY, 1));
		//
		leftSidePanel.add(sexLabel);
		leftSidePanel.add(sexBox);
		//
		teleField = new JTextField();
		teleField.setBorder(new LineBorder(Color.GRAY, 1));
		JLabel phoneLabel = new JLabel("Phone Number:");
		phoneLabel.setPreferredSize(new Dimension(200, 30));
		phoneLabel.setForeground(new Color(0, 0, 0));
		teleField.setFont(new Font("David", 1, 16));
		phoneLabel.setFont(new Font("David", 1, 16));
		leftSidePanel.add(phoneLabel);
		leftSidePanel.add(teleField);
		//
		regDate = new JDateChooser();
		regDate.setLocale(Locale.US);
		JLabel regDateLabel = new JLabel("Date:");
		regDateLabel.setPreferredSize(new Dimension(200, 30));
		regDateLabel.setForeground(new Color(0, 0, 0));
		regDateLabel.setFont(new Font("David", 1, 16));
		regDateLabel.setBounds(10, 10, 100, 30);
		regDate.setBounds(111, 10, 200, 30);
		regDate.setBorder(new LineBorder(Color.GRAY, 1));
		rightSidePanel.add(regDateLabel);
		rightSidePanel.add(regDate);
		//
		// image here
		ImageIcon regIcon = new ImageIcon(getClass().getResource("/all/images/reg_p.png"));
		JLabel imageLabel = new JLabel("", regIcon, JLabel.CENTER);
		imageLabel.setBounds(350, 30, 250, 142);
		imageLabel.setBorder(new LineBorder(Color.GRAY, 1));
		rightSidePanel.add(imageLabel);
		//

		addrLabel = new JLabel("Address:");
		addrLabel.setForeground(new Color(0, 0, 0));
		addrLabel.setFont(new Font("David", 1, 16));
		addrArea = new JTextArea("");
		addrArea.setLineWrap(true);
		addrArea.setWrapStyleWord(true);
		addrArea.setBorder(new LineBorder(Color.GRAY, 1));
		addrLabel.setBounds(10, 50, 100, 30);

		JScrollPane areaFieldScroll = new JScrollPane(addrArea);
		areaFieldScroll.setBounds(110, 50, 200, 100);
		rightSidePanel.add(addrLabel);
		rightSidePanel.add(areaFieldScroll);
		//

		// search field for a patient
		searchField = new JTextField();
		searchField.setFont(new Font("David", 1, 16));
		searchField.setPreferredSize(new Dimension(140, 30));
		searchField.addKeyListener(new SearchListener());
		// label for search field
		JLabel sechLabel = new JLabel("Search");
		sechLabel.setFont(new Font("David", 1, 16));

		lastRightPanel.add(sechLabel);
		lastRightPanel.add(searchField);
		// Button initialization
		for (int j = 0; j < actionBtn.length; j++) {
			actionBtn[j] = new JButton();
			actionBtn[j] = new JButton();
			actionBtn[j].setForeground(Color.BLACK);
			actionBtn[j].setBackground(Color.WHITE);
			actionBtn[j].setFont(new Font("David", 1, 18));
			actionBtn[j].setBorder(new LineBorder(new Color(0, 194, 255), 1));
			actionBtn[j].setPreferredSize(new Dimension(180, 30));
			actionBtn[j].addActionListener(new ButtonOperationListener());
			lastRightPanel.add(actionBtn[j]);

		}

		actionBtn[0].setText("Filter");
		actionBtn[1].setText("Save");
		actionBtn[2].setText("Update");
		actionBtn[3].setText("Delete Record");
		actionBtn[4].setText("Search Patient");

		add(centerPanel, BorderLayout.CENTER);
		JPanel westPanel = new JPanel();
		westPanel.setBorder(new LineBorder(Color.GRAY, 1));
		JLabel statusLabel = new JLabel("This contains daily progress");
		westPanel.add(statusLabel);
		// add(westPanel, BorderLayout.WEST);

		//
		String[] columnName = { "PID", "SurName", "otherName", "Occupation", "Age", "Gender", "Tel", "Addr", "Date" };
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 14));
		table.addMouseListener(new MouseSelectionListener());

		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(columnName);

		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(1200, 170));
		//
		JPanel southPanel = new JPanel(new BorderLayout());
		// southPanel.setBorder(new LineBorder(Color.GRAY, 1));
		southPanel.setBackground(Color.WHITE);
		southPanel.add(lastRightPanel, BorderLayout.NORTH);
		southPanel.add(tableScroll, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);

		patient_Data_Selection();// getting all patient id from table and set it on
		// combo box

	}

//	// sorter focus listener class
//	private void sorterProduct(String qry) {
//		sorter = new TableRowSorter<DefaultTableModel>(model);
//		table.setRowSorter(sorter);
//		sorter.setRowFilter(RowFilter.regexFilter(qry));
//	}

	// search listener class
	private class SearchListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {}

		@Override
		public void keyReleased(KeyEvent arg0) {
		//sorterProduct(searchField.getText().toString().toUpperCase());
		fetchPatient();
		}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}

	

	// Setting back the patient data for update
	private class MouseSelectionListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {

			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();

				idField.setText((String) targetCell.getValueAt(row, 0));
				;
				inputField[0].setText((String) targetCell.getValueAt(row, 1));
				inputField[1].setText((String) targetCell.getValueAt(row, 2));
				inputField[2].setText((String) targetCell.getValueAt(row, 3));
				ageField.setText((String) targetCell.getValueAt(row, 4));
				teleField.setText((String) targetCell.getValueAt(row, 6));
				addrArea.setText((String) targetCell.getValueAt(row, 7));

			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}

	}

	// Buttons operation class
	private class ButtonOperationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			JButton btn = (JButton) ev.getSource();
			if (btn.getActionCommand().equals("Search Patient")) {
				SearchPatientDialog spd = new SearchPatientDialog();
				spd.setVisible(true);
			}

			if (btn.getActionCommand().equals("Delete Record")) {
				DeleteDialog dsd = new DeleteDialog();
				dsd.setVisible(true);

				// Refresh the id in combo box and the table
				model.setRowCount(0);
				patient_Data_Selection();
				//
				// refresh the age display bar by the left
				RegistrationPage.setAgeOnButtons();// call this method for refreshing age bar---

			}
			
			if (btn.getActionCommand().equals("Filter")) {
				// Refresh the table in combo box and the table
				model.setRowCount(0);
				fetchPatient();
			}
			if (btn.getActionCommand().equals("Save")) {

				savePatientData();
			}

			if (btn.getActionCommand().equals("Update")) {
				if (idField.getText().isEmpty() || inputField[0].getText().isEmpty()
						|| inputField[1].getText().isEmpty() || ageField.getText().isEmpty()
						|| sexBox.getSelectedIndex() == -1 || regDate.getDate() == null
						|| addrArea.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Click on the table row you want to update.");
				} else {
					int choice = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to update " + idField.getText().toString());
					if (choice == JOptionPane.YES_OPTION) {
						updatePatientInfo();

						// Refresh the table in combo box and the table
						model.setRowCount(0);
						patient_Data_Selection();
						//
						// refresh the age display bar by the left
						RegistrationPage.setAgeOnButtons();// call this method for refreshing age bar---

					}
				}
			}

		}

		private void updatePatientInfo() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String p_regDate = null;
			p_regDate = dateFormat.format(regDate.getDate());// date object

			try {
				String qry = "Update patient_register set surname=?, other_name=?, occupation=?,"
						+ "address=?, phone_no=?, age=?, gender=?, reg_date=? where patient_id=?";
				PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(qry);
				//
				ps.setString(1, inputField[0].getText().trim().toUpperCase());
				ps.setString(2, inputField[1].getText().trim().toUpperCase());
				ps.setString(3, inputField[2].getText().trim().toUpperCase());
				ps.setString(4, addrArea.getText().trim().toUpperCase());
				ps.setString(5, teleField.getText().trim().toUpperCase());
				ps.setString(6, ageField.getText().trim().toUpperCase());

				ps.setString(7, sexBox.getSelectedItem().toString().toUpperCase());
				ps.setString(8, p_regDate);
				ps.setString(9, idField.getText());

				ps.execute();
				JOptionPane.showMessageDialog(null, idField.getText().toString() + "" + " UpdatedSuccessfully...");
				// clear text fields
				for (int i = 0; i < inputField.length; i++) {
					inputField[i].setText("");
				}
				ageField.setText("");
				teleField.setText(null);
				addrArea.setText("");
				//
				// refresh the age display bar by the left
				RegistrationPage.setAgeOnButtons();// call this method for refreshing age bar---
			} catch (SQLException ex) {
				System.out.println("patient update error\n" + ex);
			}
		}

		private void savePatientData() {

			try {
				String p_regDate = "";
				// ||idBox.getSelectedIndex()==-1
				if (inputField[0].getText().isEmpty() || inputField[0].getText().isEmpty()
						|| inputField[1].getText().isEmpty() || ageField.getText().isEmpty()
						|| sexBox.getSelectedIndex() == -1 || regDate.getDate() == null
						|| addrArea.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"One or all  the text fields are empty. Enter appropriate data");
				} else {

					// Adding data to the database
					savePatientReg(p_regDate);

				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Check your input. Enter number for telephone and age fields only");
			}

		}

		private void savePatientReg(String p_regDate) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String regDatenow = sdf.format(regDate.getDate());
			PreparedStatement ps = null;
			String qry = "INSERT INTO patient_register VALUES(?,?,?,?,?,?,?,?,?)";
			try {
				ps = St_MaryConnection.getConnection().prepareStatement(qry);
				// text field info is collected and save in the db
				ps.setString(1, idField.getText().toString().toUpperCase());
				ps.setString(2, inputField[0].getText().trim().toUpperCase());
				ps.setString(3, inputField[1].getText().trim().toUpperCase());
				ps.setString(4, inputField[2].getText().trim().toUpperCase());
				ps.setString(5, "" + Integer.parseInt(ageField.getText().trim()));
				ps.setString(6, sexBox.getSelectedItem().toString().toUpperCase());
				ps.setString(7, teleField.getText().trim());
				ps.setString(8, addrArea.getText().trim().toUpperCase());
				ps.setString(9, regDatenow);

				ps.execute();

				model.setRowCount(0);
				patient_Data_Selection();
				// clear text fields
				for (int i = 0; i < inputField.length; i++) {
					inputField[i].setText("");
				}
				ageField.setText("");
				teleField.setText(null);
				addrArea.setText("");

				// refresh the age display bar by the left
				RegistrationPage.setAgeOnButtons();// call this method for refreshing age bar---
			} catch (SQLException exc) {
				JOptionPane.showMessageDialog(null,
						idField.getText().toString()
								+ " already taken by another patient. You can update the specified ID.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/*
	 * this method handle patient id selection from db and set it on combo box
	 */
	private void patient_Data_Selection() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select *  From patient_register");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String p_Id = rs.getString("patient_id");
				String surName = rs.getString("surname");
				String otherName = rs.getString("other_name");
				String occupation = rs.getString("occupation");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String phone = rs.getString("phone_no");
				String address = rs.getString("address");
				String regDate = rs.getString("reg_date");

				model.addRow(new String[] { p_Id, surName, otherName, occupation, "" + age, gender, phone, address,
						regDate });

			}
		} catch (SQLException ex) {
			System.out.println("Selecting all patient id from it table error");
		}
	}
	
//	Filter patient name
	private void fetchPatient() {

		/*
		 * this method handle patient data selection from db and set on table
		 */
		String inputQuery = searchField.getText().toString().toUpperCase();
		String splitS = inputQuery.replaceAll("\\s+", "");
		
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select *  From patient_register WHERE "
							+ "CONCAT(surname, other_name, occupation, address, phone_no) LIKE ?");
			
			ps.setString(1, "%"+splitS+"%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String p_Id = rs.getString("patient_id");
				String surName = rs.getString("surname");
				String otherName = rs.getString("other_name");
				String occupation = rs.getString("occupation");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String phone = rs.getString("phone_no");
				String address = rs.getString("address");
				String regDate = rs.getString("reg_date");

				model.addRow(new String[] { p_Id, surName, otherName, occupation, "" + age, gender, phone, address,
						regDate });

			}
		} catch (SQLException ex) {
			System.out.println("Selecting all patient id from it table error 2");
			ex.printStackTrace();
		}
	}
}
