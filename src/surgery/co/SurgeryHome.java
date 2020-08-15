package surgery.co;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SurgeryHome extends JFrame {

	private JPanel northPanel, centerPanel, contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JButton actionBtn[] = new JButton[3];
	private DefaultTableCellRenderer cellRenderer;
	private TableRowSorter<DefaultTableModel> sorter;
	private JTextField searchField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SurgeryHome frame = new SurgeryHome();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SurgeryHome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1200, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/drug.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.setBorder(new EmptyBorder(10,10,10,10));

		northPanel.add(northLabel);

		contentPane.add(northPanel, BorderLayout.NORTH);
		
		///
		

		centerPanel = new JPanel();
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		//
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(300, 30));
		searchField.setFont(new Font("David", 1, 16));

		JLabel searchLabel = new JLabel("Search");
		searchLabel.setFont(new Font("David", 1, 16));
		btnPanel.add(searchLabel);
		btnPanel.add(searchField);

		for (int i = 0; i < actionBtn.length; i++) {
			actionBtn[i] = new JButton();
			actionBtn[i].setPreferredSize(new Dimension(200, 30));
			actionBtn[i].setFont(new Font("David", 1, 20));
			actionBtn[i].addActionListener(new BtnListeners());

			btnPanel.add(actionBtn[i]);
		}
		actionBtn[0].setText("Add New Record");
		actionBtn[1].setText("Update Record");
		actionBtn[2].setText("Delete");
		//
		table = new JTable();
		String[] columnName = { "S/No", "Date", "Patient Names","Address", "Age", "Sex", "Weight"
				+ "", "Diagnosis","Treatment","Type of Anesthesia","Surgeon Name","Assistance surgeon","anesthestiest names" + 
						"","Complications"};
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 12));
		// table.addMouseListener(new RowClickedListener());
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(columnName);

		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(1300, 500));
		JPanel tableScrollPanel = new JPanel(new BorderLayout());
		tableScrollPanel.setBackground(Color.WHITE);

		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(10).setPreferredWidth(200);
		table.getColumnModel().getColumn(11).setPreferredWidth(200);
		table.getColumnModel().getColumn(11).setCellRenderer(cellRenderer);
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		// tableScrollPanel.setBorder(new EmptyBorder(5,5,5,5));
		tableScrollPanel.add(tableScroll);
		//
		centerPanel.add(btnPanel, BorderLayout.NORTH);
		centerPanel.add(tableScrollPanel, BorderLayout.CENTER);

		//
		// add table panel to content pane
		contentPane.add(centerPanel, BorderLayout.CENTER);
	}
	
	//btn listener class
	private class BtnListeners implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent ev) {

			JButton btn =(JButton)ev.getSource();
			if(btn.getActionCommand().equals("Add New Record")) {
				SugeryInfo si = new SugeryInfo();
				si.setVisible(true);
			}
		}
		
	}

}
