package st_mary_hospital.jerry;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SearchPatientDialog extends JDialog {

	private JTextField qryfield;
	public  static JTable PatientTable;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;
	private JButton printB;

	public SearchPatientDialog() {
		setSize(new Dimension(1100, 600));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel northPanel = new JPanel();
		northPanel.setBackground(new Color(255, 255, 255));
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/patient-list.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		setModal(true);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		//
		// Center panel, having two fractions of panels, for button and table
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnPanel.setBackground(Color.LIGHT_GRAY);
		//
		qryfield = new JTextField();
		qryfield.addKeyListener(new ItemSorterListener());
		

		qryfield.setForeground(new Color(0, 0, 0));
		qryfield.setFont(new Font("David", 1, 16));
		qryfield.setBorder(new LineBorder(new Color(204, 204, 204), 3));
		qryfield.setPreferredSize(new Dimension(200, 40));
		//

		JLabel patientSearch = new JLabel("SEARCH PATIENT WITH ANY BIO-DATA");
		patientSearch.setForeground(new Color(0, 0, 0));
		patientSearch.setFont(new Font("David", 1, 12));
		//

		btnPanel.add(patientSearch);
		btnPanel.add(qryfield);

		//

		JPanel tablePanel = new JPanel(new GridLayout());
		// table for inserted data from input field
		PatientTable = new JTable();
		PatientTable.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		PatientTable.getTableHeader().setBackground(new Color(0, 194, 255));
		PatientTable.getTableHeader().setForeground(Color.WHITE);
		PatientTable.getTableHeader().setOpaque(false);
		PatientTable.setRowHeight(25);
		PatientTable.setForeground(Color.BLACK);
		PatientTable.setFont(new Font("David", Font.BOLD, 14));

		String[] columnName = { "PID", "SurName", "otherName", "Occupation", "Age", "Gender", "Tel", "Addr", "Date" };
		model = (DefaultTableModel) PatientTable.getModel();
		model.setColumnIdentifiers(columnName);
		JScrollPane scrollBar = new JScrollPane(PatientTable);
		scrollBar.setPreferredSize(new Dimension(830, 500));

		tablePanel.add(scrollBar);
		centerPanel.add(btnPanel, BorderLayout.NORTH);
		centerPanel.add(tablePanel, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
		ImageIcon exitIcon = new ImageIcon(getClass().getResource("/all/images/logout.png"));
		JButton exitB = new JButton(exitIcon);
		exitB.setBackground(Color.GRAY);
		exitB.setForeground(Color.WHITE);

		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(Color.GRAY);
		//
		printB = new JButton("Print");
		//printB.setBackground(Color.GRAY);
		printB.setForeground(Color.BLACK);
		printB.addActionListener(new PrintDialogDisplay());
		
		southPanel.add(printB);
		southPanel.add(exitB);
		add(southPanel, BorderLayout.SOUTH);
		exitB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				dispose();// exit the dialog
			}

		});

		getTableData();
	}

	// query patient table and select all the bio data and se it on table
	private void getTableData() {
		String pId = "", surname = "", otherName = "", occupation = "", address = "", phoneNo = "", age = "",
				genda = "", regDate = "";
		PreparedStatement ps = null;
		String qry = "Select * From patient_register";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);

			ResultSet rs = ps.executeQuery();
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
				//
				model.addRow(
						new String[] { pId, surname, otherName, occupation, age, genda, phoneNo, address, regDate });
			}

		} catch (SQLException ex) {
			System.out.println("Select registered patient from database error\n" + ex);
		}
	}

	// adding key listener to text field for filtering patient bio data
	private class ItemSorterListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent arg0) {

			sorter = new TableRowSorter<DefaultTableModel>(model);
			PatientTable.setRowSorter(sorter);
			sorter.setRowFilter(RowFilter.regexFilter(qryfield.getText().trim().toString().toUpperCase()));
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}

	}
	// display the print dialog
	private class PrintDialogDisplay implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			PrintingDialog pd = new PrintingDialog();
			pd.setVisible(true);
			
			
		}
		
	}
}
