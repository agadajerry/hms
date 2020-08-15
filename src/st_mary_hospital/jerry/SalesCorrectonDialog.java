package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class SalesCorrectonDialog extends JDialog {

	private JTextField[] infoField = new JTextField[2];
	private JTextField[] inputField = new JTextField[2];
	private JLabel[] inputLabel = new JLabel[2];
	private JLabel[] boxLabel = new JLabel[2];
	private JButton updateB;
	private JTextField searchField;
	private JTable table;
	private TableRowSorter<DefaultTableModel> sorter;
	private static DefaultTableModel tablemodel;
	private DefaultTableCellRenderer cellRenderer;
	private TableColumn tColumn;
	public String tableId;
	// public int lastId = 00000;

	public SalesCorrectonDialog() {
		setSize(new Dimension(870, 620));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/background_gray1.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//
		// center panel for holding other panel and text fields
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(null);
		centerPanel.setBounds(0, 198, 870, 450);
		centerPanel.setBackground(new Color(0, 192, 255));
		//
		ImageIcon correctionIcon = new ImageIcon(getClass().getResource("/all/images/sale_corect.png"));
		JLabel correctionLabel = new JLabel("", correctionIcon, JLabel.CENTER);
		correctionLabel.setBounds(0, 0, 870, 120);
		centerPanel.add(correctionLabel);
		//

		// This panel hold all the fields
		JPanel holderPanel = new JPanel();
		holderPanel.setLayout(null);
		holderPanel.setBounds(20, 123, 820, 420);
		holderPanel.setBackground(Color.WHITE);
		//
		for (int i = 0; i < inputLabel.length; i++) {
			//
			boxLabel[i] = new JLabel();
			boxLabel[i].setBounds(20, 40 + (i * 35), 100, 30);
			boxLabel[i].setFont(new Font("David", 1, 16));

			//
			infoField[i] = new JTextField();
			infoField[i].setBounds(120, 40 + (i * 35), 200, 30);
			infoField[i].setFont(new Font("David", 1, 16));

			//
			// input field and label
			inputLabel[i] = new JLabel();
			inputField[i] = new JTextField();
			inputLabel[i].setBounds(320, 40 + (i * 35), 120, 30);
			inputField[i].setBounds(445, 40 + (i * 35), 200, 30);
			inputField[i].setFont(new Font("David", 1, 16));
			inputLabel[i].setFont(new Font("David", 1, 16));
			holderPanel.add(boxLabel[i]);
			holderPanel.add(infoField[i]);
			holderPanel.add(inputLabel[i]);
			holderPanel.add(inputField[i]);
		}
		boxLabel[0].setText("Visit Date:");
		boxLabel[1].setText("Patient ID:");
		inputLabel[0].setText("Patient Name:");
		inputLabel[1].setText("Amout Paid:");
		// update button at the right side
		updateB = new JButton("Update");
		updateB.setBounds(650, 65, 200, 30);
		updateB.setFont(new Font("David", 1, 16));
		//updateB.setBackground(Color.GRAY);
		updateB.setForeground(Color.BLACK);
		updateB.addActionListener(new UpdateCustomerListener());
		holderPanel.add(updateB);

		centerPanel.add(holderPanel);
		add(centerPanel, BorderLayout.CENTER);
		//
		// next is table that display patient information regarding drug purchases

		// table for inserted data from input field
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 16));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 12));
		table.addMouseListener(new SetBackTextFieldValueListener());

		//
		String tableColumn2[] = { "S/No", "Patient ID", "Patient Name", "Amount Paid", " Visit Date" };
		tablemodel = (DefaultTableModel) table.getModel();
		tablemodel.setColumnIdentifiers(tableColumn2);
		JScrollPane scrollBar = new JScrollPane(table);

		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(320);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		//
		tColumn = table.getColumnModel().getColumn(4);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.YELLOW, Color.BLACK));
		//
		tColumn = table.getColumnModel().getColumn(3);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.PINK, Color.BLACK));

		// search ext field
		JLabel sechLabel = new JLabel("Search:");
		sechLabel.setBounds(20, 110, 100, 30);
		sechLabel.setFont(new Font("David", 1, 16));
		searchField = new JTextField();
		searchField.setBounds(120, 110, 200, 30);
		searchField.setFont(new Font("David", 1, 16));
		searchField.addKeyListener(new SearchListener());

		scrollBar.setBounds(10, 140, 800, 300);
		holderPanel.add(searchField);
		holderPanel.add(sechLabel);
		holderPanel.add(scrollBar);

		// table.setShowVerticalLines(true);
		// Getting last id id from sale table table
		// lastPatientId();
		getAllPatientInvoice();// get patient invoice lists
	}
	// get last serial number

	/// this method select all patient invoice from table

	private void getAllPatientInvoice() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select * From patient_invoice" + " order by idNo asc");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int idNo = rs.getInt("idNo");
				String patientId = rs.getString("patient_Id");
				String patientName = rs.getString("patient_Name");
				double amountPaid = rs.getDouble("amount_paid");
				String dateVisit = rs.getString("visit_Date");

				tablemodel.addRow(new String[] { "" + idNo, patientId, patientName, "" + amountPaid, dateVisit });
			}
		} catch (SQLException ex) {
			System.out.println("Error in getting patient sales info from table" + ex);
		}
	}

	//
	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(tablemodel);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// search listener class for invoice correction
	private class SearchListener implements KeyListener {

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

	// add mouse listener to table and set back the selected row t fields
	private class SetBackTextFieldValueListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {
			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();

				infoField[1].setText((String) targetCell.getValueAt(row, 1));
				inputField[0].setText((String) targetCell.getValueAt(row, 2));
				inputField[1].setText((String) targetCell.getValueAt(row, 3));
				infoField[0].setText((String) targetCell.getValueAt(row, 4));

				tableId = (String) targetCell.getValueAt(row, 0);

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

	// update listener class for patient purchase
	private class UpdateCustomerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (infoField[0].getText().isEmpty() || infoField[1].getText().isEmpty()
					|| inputField[0].getText().isEmpty() || inputField[1].getText().isEmpty()) {


				JOptionPane.showMessageDialog(null, "One or all the text fields are empty.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				int choice = JOptionPane.showConfirmDialog(null,
						"Are you you sure you want to update this information ?");
				if (choice == JOptionPane.YES_OPTION) {
					updatePatientSaleTable();
				}
			}
		}

	}

	// update the table for customer purchases
	private void updatePatientSaleTable() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement("Update patient_invoice SET"
					+ " patient_Id=?," + "visit_Date=?,patient_Name=?,amount_paid=? where idNo=?");
			//
			ps.setString(1, infoField[1].getText().trim().toUpperCase());
			ps.setString(2, infoField[0].getText().trim().toUpperCase());
			ps.setString(3, inputField[0].getText().toUpperCase());
			ps.setDouble(4, Double.parseDouble(inputField[1].getText()));
			ps.setInt(5, Integer.parseInt(tableId));
			ps.executeUpdate();
			//
			JOptionPane.showMessageDialog(null, "Updated successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
			tablemodel.setRowCount(0);
			getAllPatientInvoice();// call all sale invoice table
			inputField[0].setText("");
			inputField[1].setText("");
			infoField[0].setText("");
			infoField[1].setText("");
			//
		} catch (SQLException ex) {
			System.out.println("Error in updating customer's id info\n" + ex);
		}
	}
}
