package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Hospital_HomePage extends JFrame {

	private JButton[] loginBtn = new JButton[4];
	private JLabel btnLabel[] = new JLabel[4];

	public Hospital_HomePage() {
		setSize(new Dimension(870, 550));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/background_gray1.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);

		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//
		Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/all/images/add1.png"));
		setIconImage(iconImage);

		//

		JPanel centerPanel1 = new JPanel();
		centerPanel1.setLayout(null);
		centerPanel1.setBackground(Color.WHITE);

		for (int i = 0; i < loginBtn.length; i++) {
			loginBtn[i] = new JButton();
			btnLabel[i] = new JLabel();
			loginBtn[i].setBackground(Color.WHITE);
			loginBtn[i].setBorder(new LineBorder(Color.WHITE, 1));
			btnLabel[i].setForeground(new Color(0, 194, 255));
			btnLabel[i].setBounds(130 + (i * 150), 100, 200, 150);
			btnLabel[i].setFont(new Font("David", 1, 16));
			centerPanel1.add(loginBtn[i]);
			centerPanel1.add(btnLabel[i]);

		}
		loginBtn[0].addActionListener(new LoginAdminactionListener());
		loginBtn[1].addActionListener(new LoginSaleactionListener());
		loginBtn[2].addActionListener(new LoginInventactionListener());
		loginBtn[3].addActionListener(new LoginP_RegctionListener());

		btnLabel[0].setText("ADMIN");
		btnLabel[1].setText("PHARMACY");
		btnLabel[2].setText("CASHIER");
		btnLabel[3].setText("PATIENT REGISTER");
		
		

		loginBtn[0].setBounds(80, 10, 150, 150);
		loginBtn[1].setBounds(400, 10, 150, 150);
		loginBtn[2].setBounds(240, 10, 150, 150);
		loginBtn[3].setBounds(570, 10, 150, 150);

		ImageIcon adminIcon = new ImageIcon(getClass().getResource("/all/images/admin.png"));
		ImageIcon pat_icon = new ImageIcon(getClass().getResource("/all/images/patient_icon.png"));
		ImageIcon sale_icon = new ImageIcon(getClass().getResource("/all/images/employee.png"));
		ImageIcon patient_icon = new ImageIcon(getClass().getResource("/all/images/pharmacy.png"));

		loginBtn[0].setIcon(adminIcon);
		loginBtn[1].setIcon(patient_icon);
		loginBtn[2].setIcon(sale_icon);
		loginBtn[3].setIcon(pat_icon);

		//

		add(centerPanel1, BorderLayout.CENTER);

		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(300, 220));
		southPanel.setBackground(new Color(210, 210, 210));
		ImageIcon sIcon = new ImageIcon(getClass().getResource("/all/images/splash1.png"));
		JLabel southIcon = new JLabel("", sIcon, JLabel.CENTER);
		southPanel.add(southIcon);
		add(southPanel, BorderLayout.SOUTH);

		setVisible(true);// set hospital management page visible

	}// end of constructor

	// Login listener class
	private class LoginAdminactionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			String adminText = "Admin";
			LoginDialog loginD = new LoginDialog(adminText);
			loginD.setVisible(true);
			dispose();

		}

	}

	private class LoginSaleactionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			String saleText = "Sale Rep";
			LoginDialog loginD = new LoginDialog(saleText);
			loginD.setVisible(true);
			dispose();

		}

	}

	// inventory class
	private class LoginInventactionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			String pharma = "Pharmacist";
			LoginDialogPhar loginD = new LoginDialogPhar(pharma);
			loginD.setVisible(true);
			dispose();

		}

	}

	// patient registration class
	private class LoginP_RegctionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String pReg = "Receptionist";
			LoginDialog loginD = new LoginDialog(pReg);
			loginD.setVisible(true);
			dispose();

		}

	}

}
