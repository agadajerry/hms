package st_mary.admin;

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

public class Hospital_HomePageAdmin extends JFrame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel userNameLabel, passwordLabel;
	private JButton loginB, regB;
	private JTextField userField;
	private JPasswordField passField;
	private JComboBox<String> userBox;
	private DefaultComboBoxModel<String> cModel;

	public Hospital_HomePageAdmin() {
		setSize(new Dimension(1100, 550));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		// dispose();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/med_drug.png"));
		
		
		JLabel northLabel = new JLabel("<html><body><div>"
				+ "<h2> PHARMACEUTICAL INVENTORY MANAGEMENT SYSTEM "
				+ ""
				+ "</h2><hr/><span style=color:red>CONTROL PANEL</span></div></body></html>"
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
		JPanel centerPanel = new JPanel(new GridLayout(1, 3));
		// photo  panel
		ImageIcon leftIcon = new ImageIcon(getClass().getResource("/all/images/loginbackg.png"));
		JLabel leftLabel = new JLabel("", leftIcon, JLabel.CENTER);
		leftLabel.setBounds(0, 0, 400, 300);
		
		centerPanel.add(leftLabel);
		//
		centerPanel.add(new LoginDialog());

	// photo  panel
		ImageIcon leftIcon1 = new ImageIcon(getClass().getResource("/all/images/splash1.png"));
		JLabel leftLabel1 = new JLabel("", leftIcon1, JLabel.CENTER);
		leftLabel1.setBounds(0, 0, 400, 300);
		
		centerPanel.add(leftLabel1);
		// add centerPanel to the center
		add(centerPanel, BorderLayout.CENTER);
		//
		

		setVisible(true);// set hospital management page visible

	}// end of constructor

	// Login UI and logic

	public class LoginDialog extends JPanel {

		/**
		 * 
		 */
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
			JLabel adLabel = new JLabel("<html><h2 style= color:rgb(0,130,230);>Admin Login</h2></html>");
			adLabel.setBounds(105, 8, 200, 30);
			centerPanel.add(adLabel);
			//
			userNameLabel = new JLabel("User Name:");
			userNameLabel.setFont(new Font("David", 1, 16));
			userField = new JTextField();
			userNameLabel.setBounds(20, 45, 100, 30);
			userField.setBounds(105, 45, 200, 40);
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
			String userArrsy[] = { "Admin", "Cashier", "Pharmacy", "Receptionist" };
			cModel = new DefaultComboBoxModel<>(userArrsy);
			userBox = new JComboBox<>(cModel);
			JLabel userTypeLabel = new JLabel("UserType:");
			userTypeLabel.setBounds(18, 155, 100, 16);
			userTypeLabel.setFont(new Font("David", Font.BOLD, 18));
			userBox.setFont(new Font("David", Font.BOLD, 18));
			userBox.setBounds(105, 150, 200, 40);
			centerPanel.add(userTypeLabel);
			centerPanel.add(userBox);
			///
			loginB = new JButton("Login");

			loginB.setBounds(105, 210, 200, 40);
			regB = new JButton("Register");
			regB.setBounds(105, 260, 200, 40);

			loginB.setForeground(Color.BLACK);
			loginB.setBackground(Color.PINK);
			loginB.setFont(new Font("David", 1, 18));
			loginB.addActionListener(new LoginListener());

			regB.setForeground(Color.BLACK);
			regB.addActionListener(new RegisterListener());
			// loginB.setBackground(new Color(0, 194, 255));
			regB.setFont(new Font("David", 1, 18));
			centerPanel.add(loginB);
			centerPanel.add(regB);
			//
			ImageIcon dpt3 = new ImageIcon(getClass().getResource("/all/images/admin.png"));
			JLabel dptLabel = new JLabel("", dpt3, JLabel.CENTER);
			dptLabel.setBounds(120, 290, 150, 150);
			centerPanel.add(dptLabel);
			//
			
			add(centerPanel, BorderLayout.CENTER);

		}
		//
		// login logic

		// login class
		private class LoginListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					String passW = new String(passField.getPassword());

					if (userBox.getSelectedIndex() == 1) {

						HospitalSale adp = new HospitalSale();
						adp.setVisible(true);
						dispose();

					} else if (userBox.getSelectedIndex() == 2) {

						InnerStoreHome adp = new InnerStoreHome();
						adp.setVisible(true);
						dispose();

					} else if (userBox.getSelectedIndex() == 3) {

						RegistrationPage adp = new RegistrationPage();
						adp.setVisible(true);
						dispose();

					}

					else if (userBox.getSelectedIndex() == 0) {

						if (userField.getText().isEmpty() || passW.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Empty field(s) or wrong password.");
						} else {
							// admin login
							boolean suc = (login(userField.getText().toLowerCase(), passW,
									userBox.getSelectedItem().toString()));
						}

					}
				} catch (Exception ec) {
					ec.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"You cannot login this time. Ensure you are connected to database..");
				}
			}

		}

		// login method that fetch credential from database
		public boolean login(String userName, String password, String userType) {

			boolean auth = false;
			try {
				PreparedStatement ps = St_MaryConnection.getConnection()
						.prepareStatement("Select * From" + " adminTable where userName=? and password=?");

				ps.setString(1, userName);
				ps.setString(2, password);
				ResultSet rs = ps.executeQuery();
				//
				if (rs.next()) {
					String passw = rs.getString("password");
					String userNam = rs.getString("userName");
					if (password.equals(passw) && userNam.equals(userName)) {

						auth = true;
						AdminPanel ish1 = new AdminPanel();
						ish1.setVisible(true);
						dispose();
					} 

				}else {
					auth = false;
					JOptionPane.showMessageDialog(null, "Invalid login credentials...");
				}

			} catch (SQLException exce) {
				System.out.println("Error for fetch password and userName\n" + exce);
			}
			return auth;

		}

	}

	// New registration panel for admin registration
	private class RegisterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			AdminRegistrationDialog ard = new AdminRegistrationDialog();
			ard.setVisible(true);
			
		}

	}
}
