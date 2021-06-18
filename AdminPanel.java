package st_mary.admin;

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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class AdminPanel extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton[] buttons = new JButton[4];
	private JPanel[]sidePanel = new JPanel[4];
	private JPanel centerPanel;
	private CardLayout cLayout;
	private JButton logOutB;

	public AdminPanel() {
		setSize(new Dimension(1200, 800));
		setTitle("Hospital management system");
		setLayout(new BorderLayout(6, 6));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		//
		JPanel northPanel = new JPanel(new GridLayout(1,2,2,3));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/med_drug.png"));
		
		
		JLabel northLabel = new JLabel("<html><body><div>"
				+ "<h2> PHARMACEUTICAL INVENTORY MANAGEMENT SYSTEM "
				+ ""
				+ "</h2><hr/><span style=color:red>ADMIN CONTTROL PANEL</span></div></body></html>"
				);
		
		northLabel.setFont(new Font("Aharoni",Font.BOLD,18));
		JLabel northIconLabel = new JLabel("Admin|",northBgIcon,JLabel.CENTER);
		
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

		JPanel westPanel = new JPanel(new GridLayout(2, 1, 8, 8));
		JPanel westPanel1 = new JPanel(new GridLayout(5, 1, 5, 5));
		JPanel westPanel3 = new JPanel(new BorderLayout());// log out button

		logOutB = new JButton();
		ImageIcon logOutIcon = new ImageIcon(getClass().getResource("/all/images/logout.png"));
		ImageIcon displayIcon = new ImageIcon(getClass().getResource("/all/images/harma_lada.png"));
		JLabel disIcon = new JLabel("",displayIcon,JLabel.CENTER);
		logOutB.setIcon(logOutIcon);
		logOutB.addActionListener(new LogOutListener());
		logOutB.setBackground(Color.GRAY);
		westPanel3.add(logOutB,BorderLayout.PAGE_END);
		westPanel3.add(disIcon,BorderLayout.CENTER);
		westPanel1.setBackground(Color.GRAY);
		westPanel3.setBackground(Color.GRAY);
		westPanel.add(westPanel1);
		westPanel.add(westPanel3);
		add(westPanel, BorderLayout.WEST);

		for (int j = 0; j < buttons.length; j++) {
			buttons[j] = new JButton();
			sidePanel[j] = new JPanel();
			buttons[j].setFont(new Font("David", 1, 16));
			sidePanel[j].add(buttons[j]);
			buttons[j].setPreferredSize(new Dimension(220,60));
			buttons[j].addActionListener(new ButtonListener());
			// buttons[j].addFocusListener(new HoverEffect());
			buttons[j].setBackground(Color.WHITE);
			buttons[j].setForeground(Color.BLACK);
			buttons[j].setBorder(new LineBorder(new Color(0, 194, 255), 1));
			//
			sidePanel[j].setPreferredSize(new Dimension(220,70));
			sidePanel[j].setBackground(Color.WHITE);
			sidePanel[j].setBorder(new EmptyBorder(10,10,10,10));
			westPanel1.add(sidePanel[j]);

		}
		buttons[0].setText("QUANTITY COMAPRISON");
		buttons[1].setText("TRANSACTION RECORD");
		buttons[2].setText("DELETED DRUGS");
		buttons[3].setText("USERS");
		buttons[3].addActionListener(new UserListener());

		

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

			Hospital_HomePageAdmin hhp = new Hospital_HomePageAdmin();
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

	/*
	 * // sales listener intent button private class SalesCorrectonListener
	 * implements ActionListener {
	 * 
	 * @Override public void actionPerformed(ActionEvent arg0) {
	 * SalesCorrectonDialog scd = new SalesCorrectonDialog(); scd.setVisible(true);
	 * 
	 * }
	 * 
	 * }
	 */

	// user listening class
	private class UserListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			UserPanel userP = new UserPanel();
			userP.setVisible(true);
		}

	}

}
