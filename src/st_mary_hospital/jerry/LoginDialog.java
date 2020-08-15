package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class LoginDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel userNameLabel, passwordLabel;
	private JButton loginB, cancelB;
	private JTextField userField;
	private JPasswordField passField;
	private String admintex;
	private JLabel loginAs;
	private JLabel errorLabel;

	public LoginDialog(String adminText) {

		setSize(new Dimension(400, 300));
		setTitle("Login ");
		this.admintex = adminText;
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		setUndecorated(true);
		setLayout(new BorderLayout());
		getRootPane().setBorder(new LineBorder(Color.GRAY, 3));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		initUi();
	}

	private void initUi() {

		JPanel centerPanel = new JPanel();
		centerPanel.setBounds(10, 10, 390, 290);
		centerPanel.setBackground(Color.LIGHT_GRAY);
		centerPanel.setLayout(null);
		centerPanel.setBackground(Color.WHITE);
		loginAs = new JLabel("Login As: " + admintex);
		loginAs.setBounds(60, 10, 300, 30);
		loginAs.setFont(new Font("David", 1, 18));
		loginAs.setForeground(new Color(0, 194, 255));
		centerPanel.add(loginAs);

		userNameLabel = new JLabel("User Name:");
		userField = new JTextField();
		userNameLabel.setBounds(20, 40, 100, 30);
		userField.setBounds(105, 40, 200, 40);
		userField.setFont(new Font("David", 1, 30));
		userField.setBorder(new LineBorder(Color.BLACK, 2));
		centerPanel.add(userNameLabel);
		centerPanel.add(userField);
		//
		passwordLabel = new JLabel("Password:");
		passField = new JPasswordField();

		passwordLabel.setBounds(20, 85, 100, 30);
		passField.setBounds(105, 85, 200, 40);
		passField.setBorder(new LineBorder(Color.BLACK, 2));
		passField.setFont(new Font("David", 1, 30));
		centerPanel.add(passwordLabel);
		centerPanel.add(passField);
		//

		loginB = new JButton("Login");

		loginB.setBounds(105, 150, 200, 40);

		loginB.setForeground(Color.BLACK);
		//loginB.setBackground(new Color(0, 194, 255));
		loginB.setFont(new Font("David", 1, 30));
		loginB.addActionListener(new LoginListener());
		centerPanel.add(loginB);
		cancelB = new JButton("Cancel");
		cancelB.setBounds(105, 220, 200, 40);
		//cancelB.setBackground(new Color(0, 194, 255));
		cancelB.setForeground(Color.BLACK);
		cancelB.setFont(new Font("David", 1, 30));
		cancelB.addActionListener(new CancelListener());
		//
		errorLabel = new JLabel("");
		errorLabel.setBounds(20, 260, 300, 30);
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(new Font("David", 1, 16));
		centerPanel.add(cancelB);
		centerPanel.add(errorLabel);
		add(centerPanel, BorderLayout.CENTER);
	}

	// cancel button class
	private class CancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			Hospital_HomePage hhp = new Hospital_HomePage();
			hhp.setVisible(true);
			dispose();
		}

	}

	// login class
	private class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			try {
				String passW = new String(passField.getPassword());
				/*
				 * if (userField.getText().equals("admin") && passW.equals("admin") &&
				 * loginAs.getText().substring(10).equals("Admin")) {
				 * 
				 * AdminPanel adp = new AdminPanel(); adp.setVisible(true); dispose();
				 * 
				 * } else { errorLabel.setText("Access denied");
				 * 
				 * }
				 */
				//
				

				//
				if (login(userField.getText().toLowerCase(), passW)
						&& loginAs.getText().substring(10).equals("Sale Rep")) {
					HospitalSale ish1 = new HospitalSale();
					ish1.setVisible(true);
					dispose();
				} else {
					errorLabel.setText("Invalid user name or passsword...");
				}
				//
				if (login(userField.getText(), passW) && loginAs.getText().substring(10).equals("Receptionist")) {
					RegistrationPage ish1 = new RegistrationPage();
					ish1.setVisible(true);
					dispose();
				}

			} catch (Exception ec) {
				
				JOptionPane.showMessageDialog(null, "You cannot login this time. Ensure you are connected to database..");
			}
		}

	}

	// login method that fetch credential from database
	public Boolean login(String userName, String password) {
		boolean isAuthenticated = false;
		// -----------------------------------------------------------------------
		String SALT = "12/22513/UE|%/&";
		String saltedPassword = SALT + password.toString();

		String hasedPassword = UserPanel.generateHashKey(saltedPassword);
		// --------------------------------------------------------------------
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select * From" + " user_table where userName=? AND password=?");

			ps.setString(1, userName);
			ps.setString(2, hasedPassword);
			ResultSet rs = ps.executeQuery();
			//
			if (rs.next()) {
				String passw = rs.getString("password");
				if (hasedPassword.equals(passw)) {
					isAuthenticated = true;
				} else {

					isAuthenticated = false;
				}

			}
		} catch (SQLException exce) {
			System.out.println("Error for fetch password and userName\n" + exce);
		}
		return isAuthenticated;

	}
}
