package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class InnerStoreHome extends JFrame {

	private JButton[] actionBtn = new JButton[7];
	private JPanel centerPanel;
	private CardLayout cLayout;
	private JButton logOutB;

	public InnerStoreHome() {

		setSize(new Dimension(1200, 800));
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(8,9));
		// setBackground(Color.WHITE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/all/images/add1.png"));
		setIconImage(iconImage);

		initialiseUi();

	}

	private void initialiseUi() {
		// menu bar and items
		JMenuBar mBar = new JMenuBar();
		JMenu helpMenu = new JMenu("HELP");
		helpMenu.setForeground(Color.WHITE);

		//
		mBar.add(helpMenu);
		mBar.setBackground(new Color(0, 194, 255));

		JMenuItem aboutDev = new JMenuItem("About");
		helpMenu.add(aboutDev);
		aboutDev.setMnemonic(KeyEvent.VK_A);
		aboutDev.addActionListener(new AboutPanelPanel());

		setJMenuBar(mBar);
		initialiseGui();
	}

	private void initialiseGui() {

		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.setBackground(Color.GRAY);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/background_gray1.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);

		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//

		// WestPanel holding order Table,displaying all drugs table,

		JPanel westPanel = new JPanel(new GridLayout(2, 1));
		JPanel westPanel1 = new JPanel(new GridLayout(7, 1));
		JPanel westPanel2 = new JPanel();// dummy panel
		westPanel1.setBackground(Color.GRAY);
		westPanel2.setBackground(Color.GRAY);
		westPanel.add(westPanel1);
		westPanel.add(westPanel2);
		// adding components to panel one

		for (int j = 0; j < actionBtn.length; j++) {
			actionBtn[j] = new JButton();
			actionBtn[j] = new JButton();
			actionBtn[j].setForeground(Color.BLACK);
			actionBtn[j].setBackground(Color.WHITE);
			actionBtn[j].setFont(new Font("David", 1, 18));
			actionBtn[j].setBorder(new LineBorder(new Color(0,194,255)));
			actionBtn[j].setPreferredSize(new Dimension(230, 30));
			actionBtn[j].addActionListener(new DrugRecordListener());
			westPanel1.add(actionBtn[j]);
			// setBackground(new Color(0, 194, 255));
		}
		actionBtn[0].setText("HOME");
		actionBtn[1].setText("DRUGS' RECORD");
		actionBtn[2].setText("INVENTORY");
		actionBtn[3].setText("ORDER DRUGS");
		actionBtn[4].setText("TALLY CARD");
		actionBtn[5].setText("DRUG STATUS");
		actionBtn[6].setText("EXPIRED DRUGS");
		//
		// adding image to west panel
		ImageIcon westPanelIcon = new ImageIcon(getClass().getResource("/all/images/pharmacy_icon.png"));
		JLabel westLabel = new JLabel("", westPanelIcon, JLabel.CENTER);

		westPanel2.add(westLabel);
		add(westPanel, BorderLayout.WEST);

		//
		centerPanel = new JPanel();

		cLayout = new CardLayout();
		centerPanel.setLayout(cLayout);
		centerPanel.add(new HomeImage(), "home");
		centerPanel.add(new InnerDrugStore(), "innerStore");
		centerPanel.add(new DrugTallyPanel(), "drugList");
		centerPanel.add(new DrugListStatus(), "drugStatus");
		//centerPanel.add(new ExpiredDrugPanel(), "expiry");

		add(centerPanel, BorderLayout.CENTER);

		// soutPanel
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(Color.GRAY);

		//

		ImageIcon logOutIcon = new ImageIcon(getClass().getResource("/all/images/logout.png"));

		logOutB = new JButton("", logOutIcon);

		logOutB.setPreferredSize(new Dimension(30, 30));
		logOutB.addActionListener(new LogoutListener());
		southPanel.add(logOutB);
		add(southPanel, BorderLayout.SOUTH);
		// visibility of frame
		this.setVisible(true);

	}

	// drug buttons operations class
	private class DrugRecordListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			JButton btn = (JButton) ev.getSource();
			if (btn.getActionCommand().equals("DRUGS' RECORD")) {
				cLayout.show(centerPanel, "innerStore");
				// refresh combo box for unit, category and location of drugs
				InnerDrugStore.catModel.removeAllElements();
				InnerDrugStore.unitModel.removeAllElements();
				InnerDrugStore.locationModel.removeAllElements();
				InnerDrugStore.catUnitLoaction();
			}

			if (btn.getActionCommand().equals("ORDER DRUGS")) {
				DrugOrderDialog dod = new DrugOrderDialog();
				dod.setVisible(true);

			}
			if (btn.getActionCommand().equals("TALLY CARD")) {
				cLayout.show(centerPanel, "drugList");
				DrugTallyPanel.drugModel.removeAllElements();
				DrugTallyPanel.allDrugList();

			}

			if (btn.getActionCommand().equals("HOME")) {
				cLayout.show(centerPanel, "home");
			}
			if (btn.getActionCommand().equals("INVENTORY")) {
				InventoryDrugDialog idialog = new InventoryDrugDialog(null, null, null, null, null);
				idialog.setVisible(true);

				/*
				 * remove combo box elements first before populating new one
				 */

			}
			if (btn.getActionCommand().equals("DRUG STATUS")) {
				cLayout.show(centerPanel, "drugStatus");
				DrugListStatus.drugStatusModel.setRowCount(0);
				DrugListStatus.expirationDate();
			}
			if (btn.getActionCommand().equals("EXPIRED DRUGS")) {
				cLayout.show(centerPanel, "expiry");
				// remove table data first before refreshing
				ExpiredDrugPanel edp = new ExpiredDrugPanel();
				edp.setVisible(true);
			}
		}

	}

	// logout class
	private class LogoutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			Hospital_HomePage hhp = new Hospital_HomePage();
			hhp.setVisible(true);
			dispose();
		}

	}

//about panel class
	private class AboutPanelPanel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			AboutDeveloperDialog abt = new AboutDeveloperDialog();
			abt.setVisible(true);
		}

	}
}
