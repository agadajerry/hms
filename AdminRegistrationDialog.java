package st_mary.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import st_mary_hospital.jerry.St_MaryConnection;

public class AdminRegistrationDialog extends JDialog {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel userNameLabel, passwordLabel;
	private JButton cancelB, regB;
	private JTextField userField;
	private JPasswordField passField;
	private JLabel errorLabel, errorLabel1;

	public AdminRegistrationDialog() {

		setSize(new Dimension(400, 300));
		
		setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		
		this.setLocationRelativeTo(null);
		setBackground(Color.LIGHT_GRAY);
		this.setResizable(false);
		/*
		 * ImageIcon northBgIcon = new
		 * ImageIcon(getClass().getResource("/all/images/loginBackg.png")); JLabel
		 * northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		 * northLabel.setBounds(0, 0, 400, 300); // add(northLabel);
		 */		
		Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/all/images/add1.png"));
		setIconImage(iconImage);
		initialiseUi();
		

	}

	private void initialiseUi() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(null);
		centerPanel.setBounds(10, 10, 390, 290);
		centerPanel.setBackground(Color.LIGHT_GRAY);

		centerPanel.setBackground(Color.WHITE);

		// admin registration dialog
		JLabel adLabel = new JLabel("<html><h2 style= color:rgb(0,130,230);>Admin Registration</h2></html>");
		adLabel.setBounds(105, 8, 200, 30);
		centerPanel.add(adLabel);
		///

		userNameLabel = new JLabel("User Name:");
		userNameLabel.setFont(new Font("David", 1, 16));
		userField = new JTextField();
		userNameLabel.setBounds(20, 45, 100, 30);
		userField.setBounds(105, 40, 200, 40);
		userField.setFont(new Font("David", 1, 16));
		userField.setBorder(new LineBorder(Color.BLACK, 2));
		centerPanel.add(userNameLabel);
		centerPanel.add(userField);
		//
		passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("David", 1, 16));
		passField = new JPasswordField();
		passwordLabel.setLabelFor(passField);
		passwordLabel.setBounds(20, 110, 100, 16);
		passField.setBounds(105, 100, 200, 40);
		passField.setBorder(new LineBorder(Color.BLACK, 2));
		passField.setFont(new Font("David", 1, 30));
		centerPanel.add(passwordLabel);
		centerPanel.add(passField);
		//

		//
		cancelB = new JButton("Exit");
		cancelB.setBounds(105, 210, 200, 40);

		regB = new JButton("Register");
		regB.setBounds(105, 140, 200, 40);

		cancelB.setForeground(Color.BLACK);
		// loginB.setBackground(new Color(0, 194, 255));
		cancelB.setFont(new Font("David", 1, 18));
		regB.addActionListener(new RegisterListener());

		regB.setForeground(Color.BLACK);
		cancelB.addActionListener(new ExitListener());
		// loginB.setBackground(new Color(0, 194, 255));
		regB.setFont(new Font("David", 1, 18));
		centerPanel.add(cancelB);
		centerPanel.add(regB);
		//

		errorLabel = new JLabel("");
		errorLabel.setBounds(40, 305, 300, 30);
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(new Font("David", 1, 16));
		centerPanel.add(errorLabel);
		errorLabel1 = new JLabel("");
		errorLabel1.setBounds(40, 330, 300, 30);
		errorLabel1.setForeground(Color.RED);
		errorLabel1.setFont(new Font("David", 1, 16));
		centerPanel.add(errorLabel1);
		add(centerPanel, BorderLayout.CENTER);

	
	}

	// exit listener class
	private class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			dispose();
		}

	}

	// register admin class
	private class RegisterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			String passW = new String(passField.getPassword());
			if (userField.getText().isEmpty() || passW.isEmpty()) {
				
				JOptionPane.showMessageDialog(null, "One of the Text Fields is empty...");


			} else {
				saveUserCredential(passW);
			}
		}

		// user credential insertion
		private void saveUserCredential(String passW) {
			PreparedStatement ps = null;
			// -----------------------------------------------------------------------

			String sql = "insert into adminTable values( ?,?)";
			try {
				ps = St_MaryConnection.getConnection().prepareStatement(sql);

				ps.setString(1, userField.getText().toLowerCase());
				ps.setString(2, passW);

				ps.execute();
				JOptionPane.showMessageDialog(null, "Data saved successfully...", "Message",
						JOptionPane.INFORMATION_MESSAGE);
				
				userField.setText(null);
				passW="";
			} catch (SQLException exc) {

				exc.printStackTrace();

			}
		}
	}

}
