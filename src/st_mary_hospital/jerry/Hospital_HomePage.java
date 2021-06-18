package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import st_mary.admin.Hospital_HomePageAdmin.LoginDialog;

public class Hospital_HomePage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel userNameLabel, passwordLabel;
	private JButton loginB;
	private JTextField userField;
	private JPasswordField passField;
	private JLabel errorLabel;
	private JComboBox<String> userBox;
	private DefaultComboBoxModel<String> cModel;

	public Hospital_HomePage() {
		setSize(new Dimension(1100, 550));
		setTitle("Hospital management System");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		// dispose();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/med_drug.png"));
		
		
		JLabel northLabel = new JLabel("<html><body><div>"
				+ "<h2> PHARMACEUTICAL INVENTORY MANAGEMENT SYSTEM "
				+ ""
				+ "</h2><hr/><span style=color:red>USERS AUTHENTICATION</span></div></body></html>"
				);
		
		northLabel.setFont(new Font("Aharoni",Font.BOLD,18));
		JLabel northIconLabel = new JLabel("",northBgIcon,JLabel.CENTER);
		
		//This panel hold display images
		JPanel dispPanel = new JPanel();
		dispPanel.setBackground(Color.WHITE);
		dispPanel.add(northIconLabel);
		northPanel.add(dispPanel);
		
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//
		Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/all/images/add1.png"));
		setIconImage(iconImage);

		//
		//
		JPanel centerPanel = new JPanel(new GridLayout(1, 3));
		// photo  panel
		ImageIcon leftIcon = new ImageIcon(getClass().getResource("/all/images/loginbackg.png"));
		JLabel leftLabel = new JLabel("", leftIcon, JLabel.CENTER);
		leftLabel.setBounds(0, 0, 400, 300);
		leftLabel.setBackground(Color.WHITE);
		centerPanel.add(leftLabel);
		//
		centerPanel.add(new LoginDialog());

	// photo  panel
		ImageIcon leftIcon1 = new ImageIcon(getClass().getResource("/all/images/splash1.png"));
		JLabel leftLabel1 = new JLabel("", leftIcon1, JLabel.CENTER);
		leftLabel1.setBounds(0, 0, 400, 300);
		leftLabel1.setBackground(Color.WHITE);
		
		centerPanel.add(leftLabel1);
		// add centerPanel to the center
		add(centerPanel, BorderLayout.CENTER);
		//
		
		ImageIcon sIcon = new ImageIcon(getClass().getResource("/all/images/splash1.png"));
		JLabel southIcon = new JLabel("", sIcon, JLabel.CENTER);
		
		// add(southPanel, BorderLayout.SOUTH);

		setVisible(true);// set hospital management page visible

	}// end of constructor

	// Login UI and logic

	public class LoginDialog extends JPanel {

		private static final long serialVersionUID = 1L;

		public LoginDialog() {

			setSize(new Dimension(400, 300));

			setLayout(new BorderLayout());
			setBorder(new LineBorder(Color.GRAY, 3));

			
			initialiseUi();
		}

		private void initialiseUi() {
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(null);
			centerPanel.setBounds(10, 10, 390, 290);
			centerPanel.setBackground(Color.LIGHT_GRAY);

			centerPanel.setBackground(Color.WHITE);
			
			//
			
			// admin registration dialog
			JLabel adLabel = new JLabel("<html><h2 style= color:rgb(0,130,230);>Users Login</h2></html>");
			adLabel.setBounds(105, 8, 200, 30);
			centerPanel.add(adLabel);
			//
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
			// user selection
			// Select combo box b either admin or users
			String userArrsy[] = {"Cashier", "Pharmacy", "Receptionist" };
			cModel = new DefaultComboBoxModel<>(userArrsy);
			userBox = new JComboBox<>(cModel);
			JLabel userTypeLabel = new JLabel("User Type:");
			userTypeLabel.setBounds(20, 155, 100, 16);
			userTypeLabel.setFont(new Font("David", Font.BOLD, 18));
			userBox.setFont(new Font("David", Font.BOLD, 18));
			userBox.setBounds(105, 150, 200, 40);
			centerPanel.add(userTypeLabel);
			centerPanel.add(userBox);
			///
			loginB = new JButton("Login");

			loginB.setBounds(105, 210, 200, 40);

			loginB.setForeground(Color.BLACK);
			loginB.setBackground(Color.PINK);
			loginB.setFont(new Font("David", 1, 18));
			loginB.addActionListener(new LoginListener());
			centerPanel.add(loginB);
			//

			errorLabel = new JLabel("");
			errorLabel.setBounds(30, 280, 300, 30);
			errorLabel.setForeground(Color.RED);
			errorLabel.setFont(new Font("David", 1, 16));
			centerPanel.add(errorLabel);
			
			
			ImageIcon dpt3 = new ImageIcon(getClass().getResource("/all/images/department.png"));
			JLabel dptLabel = new JLabel("", dpt3, JLabel.CENTER);
			dptLabel.setBounds(120, 250, 150, 150);
			centerPanel.add(dptLabel);
			
			add(centerPanel, BorderLayout.CENTER);


		}
		//
		// login logic

		// login class
		private class LoginListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//create drug order log on pressing the button
				//------------------------------------------
				
				try {
					String passW = new String(passField.getPassword());

					//
					boolean suc = (login(userField.getText().toLowerCase(), passW,
							userBox.getSelectedItem().toString()));

				} catch (Exception ec) {

					JOptionPane.showMessageDialog(null,
							"You cannot login this time. Ensure you are connected to database..");
					ec.printStackTrace();
				}
			}

		}

		// login method that fetch credential from database
		public Boolean login(String userName, String password, String positionHeld) {
			boolean isAuthenticated = false;

			// -----------------------------------------------------------------------
			String SALT = "12/22513/UE|%/&";
			String saltedPassword = SALT + password.toString();

			String hasedPassword = UserPanel.generateHashKey(saltedPassword);
			// --------------------------------------------------------------------
			try {
				PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(
						"Select * From" + " user_table where userName=? AND password=? and position=?");

				ps.setString(1, userName);
				ps.setString(2, hasedPassword);
				ps.setString(3, positionHeld);
				ResultSet rs = ps.executeQuery();
				//
				if (rs.next()) {
					String passw = rs.getString("password");
					if (hasedPassword.equals(passw) && userBox.getSelectedIndex() == 0) {

						HospitalSale ish1 = new HospitalSale();
						ish1.setVisible(true);
						isAuthenticated = true;
						dispose();
					}
					//
					else if (hasedPassword.equals(passw) && userBox.getSelectedIndex() == 1) {

						InnerStoreHome ish1 = new InnerStoreHome();
						ish1.setVisible(true);

						isAuthenticated = true;
						dispose();
					}
					//
					else if (hasedPassword.equals(passw) && userBox.getSelectedIndex() == 2) {
						RegistrationPage ish1 = new RegistrationPage();
						ish1.setVisible(true);
						isAuthenticated = true;
						dispose();
					}

				} else {

					isAuthenticated = false;
					errorLabel.setText("Access denied");

				}
				errorLabel.setText("Username or password incorrect!");

			} catch (SQLException exce) {
				System.out.println("Error for fetch password and userName\n" + exce);
			}
			return isAuthenticated;

		}

	}
	
	
}
