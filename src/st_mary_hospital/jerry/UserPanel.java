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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class UserPanel extends JDialog {

	private JLabel[] userLabel = new JLabel[3];
	private JTextField[] userField = new JTextField[3];
	private JButton[] userBtn = new JButton[3];
	private JTable table;
	private static DefaultTableModel model;
	private JTextField searchField;
	private TableRowSorter<DefaultTableModel> sorter;

	public UserPanel() {

		setSize(new Dimension(720, 600));
		setTitle("Login Dialog ");
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(3, 3));
		JPanel northPanel = new JPanel();
		// northPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		northPanel.setBackground(Color.GRAY);

		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/users.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		// center Panel containing text Field

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(null);
		centerPanel.setBackground(Color.WHITE);

		for (int i = 0; i < userLabel.length; i++) {
			userLabel[i] = new JLabel();
			userField[i] = new JTextField();
			userLabel[i].setBounds(100, 50 + (i * 35), 150, 30);
			userField[i].setBounds(250, 50 + (i * 35), 200, 30);
			userField[i].setFont(new Font("David", 1, 16));
			userLabel[i].setFont(new Font("David", 1, 16));

			centerPanel.add(userField[i]);
			centerPanel.add(userLabel[i]);

		}
		//
		userLabel[0].setText("StaffID:");
		userLabel[1].setText("UserName:");
		userLabel[2].setText("Password:");

		// user Button

		for (int i = 0; i < userBtn.length; i++) {

			userBtn[i] = new JButton();

			userBtn[i].setForeground(Color.BLACK);
			//userBtn[i].setBackground(new Color(0, 194, 255));
			userBtn[i].setFont(new Font("David", 1, 20));
			//

			centerPanel.add(userBtn[i]);
			userBtn[i].addActionListener(new UserBtnListener());
		}

		userBtn[0].setBounds(100, 157, 170, 30);
		userBtn[1].setBounds(280, 157, 170, 30);
		userBtn[2].setBounds(460, 157, 170, 30);

		userBtn[0].setText("Add New User");
		userBtn[1].setText("Modify");
		userBtn[2].setText("Delete");
		add(centerPanel, BorderLayout.CENTER);

		String[] columnName = { "ID", "UserName", "Password" };
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.RED);
		table.setToolTipText("users Credentials");
		table.addMouseListener(new RowClickedListener());
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(columnName);

		//
		// search ext field
		JLabel sechLabel = new JLabel("Search:");
		sechLabel.setBounds(50, 230, 100, 30);
		sechLabel.setFont(new Font("David", 1, 16));
		searchField = new JTextField();
		searchField.setBounds(120, 230, 200, 30);
		searchField.setFont(new Font("David", 1, 16));
		searchField.addKeyListener(new SearchListener());
		centerPanel.add(sechLabel);
		centerPanel.add(searchField);

		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setBounds(30, 270, 650, 150);

		centerPanel.add(tableScroll);

		// this method Call last row from user table

		lastStaffId();
		// get all user credential and set it on the table
		getAllUserDetails();
	}

	// button class
	private class UserBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			JButton btn = (JButton) ev.getSource();

			if (btn.getActionCommand().equals("Add New User")) {

				try {
					if (userField[0].getText().isEmpty() || userField[1].getText().isEmpty()
							|| userField[2].getText().isEmpty()) {

						JOptionPane.showMessageDialog(null, "One or all Textfields are empty");
					} else {
						saveUserCredential();
						// refresh the table
						model.setRowCount(0);
						getAllUserDetails();

					}
				} catch (NumberFormatException nfc) {
					JOptionPane.showMessageDialog(null, "Enter numeric key for staff ID");

				}
			}

			if (btn.getActionCommand().equals("Modify")) {

				if (userField[0].getText().isEmpty() || userField[1].getText().isEmpty()
						|| userField[2].getText().isEmpty()) {

					JOptionPane.showMessageDialog(null,
							"One or all Texfields are empty\n" + "Click on the table row you want to modify.");
				} else {
					updateUserTable();
				}

			}
			//
			if (btn.getActionCommand().equals("Delete")) {

				if (userField[0].getText().isEmpty() || userField[1].getText().isEmpty()
						|| userField[2].getText().isEmpty()) {

					JOptionPane.showMessageDialog(null,
							"One or all Texfields are empty\n" + "Click on the table row you want to delete.");
				} else {
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user ?");
					if (choice == JOptionPane.YES_OPTION) {
						deleteUser();
					}
				}

			}

		}

	}

	// update user table
	private void updateUserTable() {
		// -----------------------------------------------------------------------
		String SALT = "12/22513/UE|%/&";
		String saltedPassword = SALT + userField[2].getText().toString();

		String hasedPassword = UserPanel.generateHashKey(saltedPassword);
		// --------------------------------------------------------------------
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("" + "Update user_table set userName=?, password=? where u_id=?");

			ps.setString(1, userField[1].getText());
			ps.setString(2, hasedPassword);
			ps.setInt(3, Integer.parseInt(userField[0].getText()));
			int suces = ps.executeUpdate();
			if (suces > 0) {
				JOptionPane.showMessageDialog(null, "Data is modified successfully...");
				// refresh the table
				model.setRowCount(0);
				getAllUserDetails();
				// this method Call last row from user table
				// resetting userFields
				for (int i = 0; i < userField.length; i++) {
					userField[i].setText(null);
				}
				//
				lastStaffId();
				//
			} else {
				JOptionPane.showMessageDialog(null, "No modification is made, try again...");

			}

		} catch (SQLException exc) {
			System.out.println("Error in modifying user login info\n" + exc);
		}
	}

	// user credential insertion
	private void saveUserCredential() {
		PreparedStatement ps = null;
		// -----------------------------------------------------------------------
		String SALT = "12/22513/UE|%/&";
		String saltedPassword = SALT + userField[2].getText().toString();

		String hasedPassword = generateHashKey(saltedPassword);
		// --------------------------------------------------------------------

		String sql = "insert into user_table (u_id, userName, password) VALUES (?, ?, ?)";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(sql);

			ps.setString(1, userField[0].getText().toLowerCase());
			ps.setString(2, userField[1].getText().toString().trim().toLowerCase());
			ps.setString(3, hasedPassword);

			ps.execute();
			JOptionPane.showMessageDialog(null, "Data saved successfully...", "Message",
					JOptionPane.INFORMATION_MESSAGE);

			// resetting userFields
			for (int i = 0; i < userField.length; i++) {
				userField[i].setText(null);
			}
			// call last row method here
			lastStaffId();
		} catch (SQLException exc) {

			JOptionPane.showMessageDialog(null,

					"UserName enter: " + userField[1].getText() + " Already Taken. You can only modify or delete it\n"
							+ "Enter numeric key for staff ID");

		}
	}
	// This method query the last insert staff id and increment it by 1

	private void lastStaffId() {
		int lastId=0;
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String lastRow = "Select u_id from user_table order by u_id desc limit 1";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(lastRow);

			rSet = ps.executeQuery();
			if (rSet.next()) {
				lastId = Integer.parseInt(rSet.getString("u_id"));
				
			}
			userField[0].setText(""+ (lastId + 1));
		} catch (SQLException exc) {
			exc.printStackTrace();
		}

	}

	//
	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// search user details
	private class SearchListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			sorterProduct(searchField.getText().toString().toLowerCase());

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	// select all user info from the table
	public static void getAllUserDetails() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement("Select * from user_table;");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String userName = rs.getString("userName");
				String passw = rs.getString("password");
				int u_id = rs.getInt("u_id");
				model.addRow(new String[] { "" + u_id, userName, passw });
			}
		} catch (SQLException ex) {
			System.out.println("Error for getting login info and set on table\n" + ex);
		}
	}

	// generate hash key for password encryption
	public static String generateHashKey(String input) {

		StringBuilder hash = new StringBuilder();
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'i', 'J', '&', 'r', 'f', 'y' };

			for (int i = 0; i < hashedBytes.length; i++) {
				byte b = hashedBytes[i];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);

			}
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return hash.toString();
	}

	// mouse click listening class
	private class RowClickedListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {

			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();

				userField[0].setText((String) targetCell.getValueAt(row, 0));
				userField[1].setText((String) targetCell.getValueAt(row, 1));
			//	userField[2].setText((String) targetCell.getValueAt(row, 2));g
				
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

	// delete users fro the table
	private void deleteUser() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Delete From " + "user_table where u_id=?");

			ps.setInt(1, Integer.parseInt(userField[0].getText()));
			ps.execute();
			
			// refresh the table
			model.setRowCount(0);
			getAllUserDetails();
			// this method Call last row from user table
			// resetting userFields
			for (int i = 0; i < userField.length; i++) {
				userField[i].setText(null);
			}
			//
			lastStaffId();

		} catch (SQLException exc) {
			System.out.println("Error for deleting user data\n" + exc);
		}
	}
}
