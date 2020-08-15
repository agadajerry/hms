package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class AdminPanel extends JFrame {
	private JButton[] buttons = new JButton[5];
	private JPanel centerPanel;
	private CardLayout cLayout;
	private JButton logOutB;

	public AdminPanel() {
		setSize(new Dimension(1200, 800));
		setTitle("Hospital management system");
		setLayout(new BorderLayout(6,6));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		//
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.GRAY);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/background_gray1.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);

		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//
		Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/all/images/add1.png"));
		setIconImage(iconImage);

		initialiseUi();
	}

	private void initialiseUi() {
		// menu bar and items
		JMenuBar mBar = new JMenuBar();
		JMenu helpMenu = new JMenu("HELP");
		helpMenu.setForeground(Color.BLACK);
		//
		mBar.add(helpMenu);
		JMenuItem aboutDev = new JMenuItem("About");
		helpMenu.add(aboutDev);
		aboutDev.setMnemonic(KeyEvent.VK_A);
		aboutDev.addActionListener(new AboutPanelPanel());

		setJMenuBar(mBar);

		// Center panel holding all information concerning sales

		centerPanel = new JPanel();

		cLayout = new CardLayout();
		centerPanel.setLayout(cLayout);
		centerPanel.add(new DispensaryComparisionPanel(), "daily");
		centerPanel.add(new TransactionRecord(), "year");
		centerPanel.add(new DeleteDrugReportPanel(), "delete_drug");
		cLayout.show(centerPanel, "daily");

		add(centerPanel, BorderLayout.CENTER);

		JPanel westPanel = new JPanel(new GridLayout(3, 1, 8, 8));
		JPanel westPanel1 = new JPanel(new GridLayout(5, 1, 5, 5));
		JPanel westPanel2 = new JPanel();// image for admin
		JPanel westPanel3 = new JPanel();// log out button

		logOutB = new JButton();
		ImageIcon logOutIcon = new ImageIcon(getClass().getResource("/all/images/logout.png"));
		logOutB.setIcon(logOutIcon);
		logOutB.addActionListener(new LogOutListener());
		logOutB.setBackground(Color.GRAY);
		westPanel3.add(logOutB);
		westPanel1.setBackground(Color.GRAY);
		westPanel2.setBackground(Color.WHITE);
		westPanel3.setBackground(Color.GRAY);
		westPanel.add(westPanel1);
		westPanel.add(westPanel2);
		westPanel.add(westPanel3);
		add(westPanel, BorderLayout.WEST);

		for (int j = 0; j < buttons.length; j++) {
			buttons[j] = new JButton();
			buttons[j].setFont(new Font("David", 1, 16));
			westPanel1.add(buttons[j]);
			buttons[j].addActionListener(new ButtonListener());
			// buttons[j].addFocusListener(new HoverEffect());
			buttons[j].setBackground(Color.WHITE);
			buttons[j].setForeground(Color.BLACK);
			buttons[j].setBorder(new LineBorder(new Color(0, 194, 255), 1));
		}
		buttons[0].setText("QUANTITY COMAPRISON");
		buttons[1].setText("TRANSACTION RECORD");
		buttons[2].setText("DELETED DRUGS");
		buttons[3].setText("SALES CORRECTION");
		buttons[4].setText("USERS");
		buttons[3].addActionListener(new SalesCorrectonListener());
		buttons[4].addActionListener(new UserListener());

		ImageIcon patientDetail = new ImageIcon(getClass().getResource("/all/images/patientdetals.png"));
		JLabel patLabel = new JLabel("", patientDetail, JLabel.CENTER);

		westPanel2.add(patLabel);

	}

	// button class
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			JButton bt = (JButton) ev.getSource();
			if (bt.getActionCommand().equals("QUANTITY COMAPRISON")) {
				cLayout.show(centerPanel, "daily");
			}

			if (bt.getActionCommand().equals("TRANSACTION RECORD")) {

				cLayout.show(centerPanel, "year");
				TransactionRecord.comboModel.removeAllElements();
				TransactionRecord.patientDateVisted();
			}
			//

			if (bt.getActionCommand().equals("DELETED DRUGS")) {

				cLayout.show(centerPanel, "delete_drug");
				DeleteDrugReportPanel.deleteTableModel.setRowCount(0);
				DeleteDrugReportPanel.fetchDeletedDrugs();
			}

		}

	}

	// logout class
	private class LogOutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			Hospital_HomePage hhp = new Hospital_HomePage();
			hhp.setVisible(true);
			dispose();
		}

	}

	// about dev class
	private class AboutPanelPanel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			AboutDeveloperDialog abt = new AboutDeveloperDialog();
			abt.setVisible(true);
		}

	}

	// sales listener intent button
	private class SalesCorrectonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			SalesCorrectonDialog scd = new SalesCorrectonDialog();
			scd.setVisible(true);

		}

	}

	// user listening class
	private class UserListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			UserPanel userP = new UserPanel();
			userP.setVisible(true);
		}

	}

	// admin reset password
	private class ResetButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			String userName = JOptionPane.showInputDialog("Enter new User Name");
			String password = JOptionPane.showInputDialog("Enter new Password");

			JOptionPane.showMessageDialog(null, "Your user name is: " + userName + "and the password is: " + password);
		}

	}
}
