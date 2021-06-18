package st_mary.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class ExpiredDrugPanel extends JDialog {

	private JTable table;
	public static DefaultTableModel drugexpireModel;
	private JTextField searchField;
	private DefaultTableCellRenderer cellRenderer;
	private TableColumn tColumn;
	private TableRowSorter<DefaultTableModel> sorter;
	private JButton deleteExpiredB;

	public ExpiredDrugPanel() {

		setSize(new Dimension(1200, 700));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setModal(true);
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/expire_bana.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		// center panel for date and table for record display
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel cNorthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		cNorthPanel.setBackground(Color.WHITE);
		JLabel searchLabel = new JLabel("Search expired Drug");
		searchLabel.setFont(new Font("David", 1, 18));
		searchLabel.setForeground(Color.BLACK);

		searchField = new JTextField();
		searchField.setBorder(new LineBorder(Color.GRAY, 2));
		searchField.setFont(new Font("David", 1, 18));
		searchField.setPreferredSize(new Dimension(350, 30));
		searchField.addKeyListener(new SearchListener());
		cNorthPanel.add(searchLabel);
		cNorthPanel.add(searchField);
		// ----------------------------------------------------------------

		centerPanel.add(cNorthPanel, BorderLayout.NORTH);
		// table for inserted data from input field
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 14));
		// table.setShowVerticalLines(true);
		// table.setGridColor(Color.GREEN);

		// table.addMouseListener(new TextFieldGetTextListener());

		String tableColumn[] = { "SerialNo", "Description", "Category", "AvailableQtty", "Status", "ReorderQuantity",
				"VoucherNo", "BatchNo" };
		drugexpireModel = (DefaultTableModel) table.getModel();
		drugexpireModel.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar = new JScrollPane();
		scrollBar.setViewportView(table);

		table.getColumnModel().getColumn(0).setPreferredWidth(24);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		tColumn = table.getColumnModel().getColumn(4);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.YELLOW, Color.BLACK));

		//
		tColumn = table.getColumnModel().getColumn(1);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.PINK, Color.BLACK));

		//
		tColumn = table.getColumnModel().getColumn(3);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.RED, Color.BLACK));
		//
		JPanel tablePanel = new JPanel(new GridLayout());
		// tablePanel.add(operationBtnPanel,BorderLayout.NORTH);
		tablePanel.add(scrollBar);
		centerPanel.add(tablePanel, BorderLayout.CENTER);

		// south panel containing all the information the transaction made
		JPanel southPanel = new JPanel(new GridLayout(1, 2));

		JPanel qttyPanel = new JPanel(new GridLayout(3, 1));
		qttyPanel.setBackground(Color.GRAY);

		JPanel profitPanel = new JPanel(new GridLayout(3, 1));
		profitPanel.setBackground(Color.GRAY);

		southPanel.add(qttyPanel);
		southPanel.add(profitPanel);
		//
		centerPanel.add(southPanel, BorderLayout.SOUTH);

		//
		// this is southPanel of the panel
		JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		deleteExpiredB = new JButton("Delete");
		//deleteExpiredB.setBackground(Color.GRAY);
		deleteExpiredB.setForeground(Color.BLACK);
		deleteExpiredB.setPreferredSize(new Dimension(200, 30));
		deleteExpiredB.setFont(new Font("Daivid", 1, 16));
		deleteExpiredB.addActionListener(new DeleteExpiredListener());
		deletePanel.add(deleteExpiredB);
		add(deletePanel, BorderLayout.SOUTH);

		add(centerPanel, BorderLayout.CENTER);

		// expired drugs method call
		expiredDrugs();
	}

	//
	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(drugexpireModel);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// search expired drug listening class
	private class SearchListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			sorterProduct(searchField.getText().toString().toUpperCase());

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	// selecting expired drug ffrom the inventory table
	// this method handle date for a particular drug to expire
	public static void expiredDrugs() {

		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-01");
		Date dNow = new Date();
		String todayDate = dFormat.format(dNow.getTime());

		PreparedStatement ps = null;
		ResultSet rs = null;
		String drugName = "";
		int qttyR = 0, idNo = 0;
		String batchNo = "", voucherNo = "";
		String expirD = "";
		long noOfDateDiff = 0;
		int reOrderQtty = 0;
		String drugCat = "";

		String qry = "Select inventory_id as SerialNo, Description,VoucherNo,batchNo,"
				+ "quantity AS RemainingQuantity, expiryDate, Re_Order_Qtty, "
				+ "category From drug_inventory where expiryDate <= ?";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ps.setString(1, todayDate);

			rs = ps.executeQuery();
			while (rs.next()) {
				//
				expirD = rs.getString("expiryDate");
				drugName = rs.getString("Description");
				batchNo = rs.getString("batchNo");
				voucherNo = rs.getString("VoucherNo");
				qttyR = rs.getInt("RemainingQuantity");
				idNo = rs.getInt("SerialNo");
				reOrderQtty = rs.getInt("Re_Order_Qtty");
				drugCat = rs.getString("category");

				LocalDate expireDate = LocalDate.parse(expirD);
				LocalDate dateNow = LocalDate.parse(todayDate);
				noOfDateDiff = ChronoUnit.DAYS.between(dateNow, expireDate);
				if (noOfDateDiff <= 0) {

					drugexpireModel.addRow(new String[] { "" + idNo, drugName, drugCat, "" + qttyR, "expired",
							"" + reOrderQtty, voucherNo, batchNo });
				}
			}

			//
		} catch (SQLException exc) {
			System.out.println("Getting expiry drug and it quantity remaining from db error" + exc);
		}

	}

	// delete the expire drugs from the table
	private class DeleteExpiredListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (table.getRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "The table has no expired drugs to delete..");
			}
			if (table.getRowCount() >= 1) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Ensure that you empty the expired drugs from the catalogue"
								+ " before carry out this operation.\n\n"
								+ "Are you sure you want to remove the drugs ?");
				if (choice == JOptionPane.YES_OPTION) {
					deleteExpiredDrugs();
				}
			}
		}

	}

	// expired drug deletion method
	private void deleteExpiredDrugs() {

		try {
			for (int i = 0; i < table.getRowCount(); i++) {

				PreparedStatement ps = St_MaryConnection.getConnection()
						.prepareStatement("Delete  from drug_inventory " + "where inventory_id= ?");
				ps.setInt(1, Integer.parseInt((String) table.getValueAt(i, 0)));
				ps.executeUpdate();

			}

			JOptionPane.showMessageDialog(null, "Expired drugs are removed successfully...");
			drugexpireModel.setRowCount(0);
			// look for available expired drug immediately
			expiredDrugs();
			//
		} catch (SQLException ex) {
			System.out.println("Delete expired drug from th table error\n" + ex);
		}
	}
}
