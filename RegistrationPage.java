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
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class RegistrationPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JButton[] pageBtn = new JButton[8];
	private JPanel centerPanel;

	public RegistrationPage() {

		setSize(new Dimension(1320, 800));
		setLayout(new BorderLayout(4, 4));
		// setBackground(Color.WHITE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initialiseUi();
		Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/all/images/add1.png"));
		setIconImage(iconImage);

	}

	private void initialiseUi() {

		// menu bar and items
		JMenuBar mBar = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		filemenu.setMnemonic(KeyEvent.VK_F);
		JMenu editMenu = new JMenu("Edit");
		editMenu.setForeground(Color.BLACK);
		editMenu.setMnemonic(KeyEvent.VK_E);
		//
		filemenu.setForeground(Color.BLACK);
		mBar.add(filemenu);
		mBar.add(editMenu);
		//mBar.setBackground(new Color(0, 194, 255));

		JMenuItem printAll = new JMenuItem("PRINT");
		JMenuItem saveFile = new JMenuItem("OPEN SAVED FILES");
		JMenuItem aboutDev = new JMenuItem("ABOUT US");
		aboutDev.addActionListener(new AboutListener());
		printAll.addActionListener(new PrintingListener());
		saveFile.setMnemonic(KeyEvent.VK_S);
		saveFile.addActionListener(new OpenSavedFiles());
		printAll.setMnemonic(KeyEvent.VK_P);
		aboutDev.setMnemonic(KeyEvent.VK_A);

		filemenu.add(printAll);
		filemenu.addSeparator();
		filemenu.add(saveFile);
		filemenu.add(aboutDev);
		setJMenuBar(mBar);
		// ------------------------------------------------------
		JPanel northPanel = new JPanel(new GridLayout(1,2,2,3));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/med_drug.png"));
		
		
		JLabel northLabel = new JLabel("<html><body><div>"
				+ "<h2> PHARMACEUTICAL INVENTORY MANAGEMENT SYSTEM "
				+ ""
				+ "</h2><hr/><span style=color:red>PATIENT REGISTRATION</span></div></body></html>"
				);
		
		northLabel.setFont(new Font("Aharoni",Font.BOLD,18));
		JLabel northIconLabel = new JLabel("Register|",northBgIcon,JLabel.CENTER);
		
		//This panel hold display images
		JPanel dispPanel = new JPanel();
		dispPanel.setBackground(Color.WHITE);
		dispPanel.add(northIconLabel);
		northPanel.add(dispPanel);
		
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//
		JPanel westPanel1 = new JPanel(new GridLayout(9, 1, 10, 10));
		westPanel1.setBorder(new LineBorder(Color.GRAY, 2));
		westPanel1.setBackground(Color.GRAY);

		JLabel sumaryLabel = new JLabel("REGISTRATION SUMMARY", JLabel.CENTER);
		sumaryLabel.setForeground(Color.WHITE);
		westPanel1.add(sumaryLabel);

		for (int i = 0; i < pageBtn.length; i++) {
			pageBtn[i] = new JButton();
			pageBtn[i].setForeground(Color.BLACK);
			pageBtn[i].setBackground(Color.WHITE);
			pageBtn[i].setFont(new Font("David", 1, 18));
			pageBtn[i].setBorder(new LineBorder(new Color(0, 194, 255)));
			setBackground(new Color(0, 194, 255));
			// pageBtn[i].setPreferredSize(new Dimension(220,40));

			westPanel1.add(pageBtn[i]);
		}
		add(westPanel1, BorderLayout.WEST);
		//
		pageBtn[0].setText("");
		pageBtn[1].setText("");
		pageBtn[2].setText("");
		pageBtn[3].setText("");
		pageBtn[4].setText("");
		pageBtn[5].setText("");
		pageBtn[6].setText("");
		ImageIcon logout = new ImageIcon(getClass().getResource("/all/images/logout.png"));

		pageBtn[7].setIcon(logout);
		pageBtn[7].addActionListener(new ExitListener());

	//	ImageIcon centerbgIcon = new ImageIcon(getClass().getResource("/all/images/adminpanel.png"));
//		JPanel bgPanel = new JPanel();
		//bgPanel.add(new JLabel("", centerbgIcon, JLabel.CENTER));

		centerPanel = new JPanel();
		centerPanel.add(new RegPanel());

		add(centerPanel, BorderLayout.CENTER);
		//
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.GRAY);
		add(southPanel,BorderLayout.SOUTH);
		setAgeOnButtons();// patient age range method. setting it for display
//
	}
	// Button click Listener

	// set age on various patient age on the panel
	public static void setAgeOnButtons() {
		
		//reset the button to null
		for (int i = 0; i < pageBtn.length; i++) {
			pageBtn[i].setText("");
		}
		//
		int age10 = 0, age20 = 0, age30 = 0, age40 = 0, age50 = 0, ageAbove = 0, totalRange = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select age from patient_register");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int age = rs.getInt("age");
				if (age >= 0) {
					totalRange++;
					pageBtn[0].setText("All Patients: " + totalRange);
				}
				if (age >= 0 && age < 10) {
					age10++;
					pageBtn[1].setText("0-10 yr= " + age10);
				}
				if (age >= 11 && age < 21) {
					age20++;
					pageBtn[2].setText("11-20 yr= " + age20);
				}
				if (age >= 21 && age < 31) {
					age30++;
					pageBtn[3].setText("21-30 yr= " + age30);
				}
				if (age >= 31 && age < 41) {
					age40++;
					pageBtn[4].setText("31-40 yr= " + age40);
				}
				if (age >= 41 && age < 51) {
					age50++;
					pageBtn[5].setText("41-50 yr= " + age50);
				}
				if (age >= 51 && age < 200) {
					ageAbove++;
					pageBtn[6].setText("51-above yr= " + ageAbove);
				}
			}
		} catch (SQLException exc) {
			System.out.println("getting  all patient age range and" + " set it for display");
		}
	}

	//
	// exit class
	private class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Hospital_HomePageAdmin hhp = new Hospital_HomePageAdmin();
			hhp.setVisible(true);
			dispose();

		}

	}

	// printing all registered patients
	private class PrintingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			PrintingDialog pd = new PrintingDialog();
			pd.setVisible(true);
		

		}
	}

	private class OpenSavedFiles implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			try {
				String receiptFolder = "C:\\St_Mary_Flies\\TallyCard\\InvoiceFiles\\registerList\\";

				Runtime.getRuntime().exec("explorer  /select," + receiptFolder);
			} catch (IOException ex) {
				Logger.getLogger(RegistrationPage.class.getName()).log(Level.SEVERE, null, ex);

			}
		}

	}

	// about listener class
	private class AboutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			AboutDeveloperDialog abtD = new AboutDeveloperDialog();
			abtD.setVisible(true);

		}

	}
}
