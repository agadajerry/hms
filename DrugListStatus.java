package st_mary.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JLabel;
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

public class DrugListStatus extends JPanel {

	private JTable table;
	public static DefaultTableModel drugStatusModel;
	private JTextField searchField;
	private DefaultTableCellRenderer cellRenderer;
	private TableColumn tColumn;
	private TableRowSorter<DefaultTableModel> sorter;

	DrugListStatus() {

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/drug_status.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		// center panel for date and table for record display
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel cNorthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		cNorthPanel.setBackground(Color.WHITE);
		JLabel searchLabel = new JLabel("Search Drug");
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

		String tableColumn[] = { "SerialNo", "Description", "Category", "AvailableQtty", "Days_To_Expire",
				"ReorderQuantity", "VoucherNo", "BatchNo" };
		drugStatusModel = (DefaultTableModel) table.getModel();
		drugStatusModel.setColumnIdentifiers(tableColumn);
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

		add(centerPanel, BorderLayout.CENTER);
	}

	// this method handle date for a particular drug to expire
	public static void expirationDate() {

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
				+ "quantity AS RemainingQuantity, expiryDate AS "
				+ "RemainingDays, Re_Order_Qtty, category From drug_inventory order by quantity";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);

			rs = ps.executeQuery();
			while (rs.next()) {
				//
				expirD = rs.getString("RemainingDays");
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

				//
				if (noOfDateDiff >= 1) {
					drugStatusModel.addRow(new String[] { "" + idNo, drugName, drugCat, "" + qttyR, "" + noOfDateDiff,
							"" + reOrderQtty, voucherNo, batchNo });
				}
			}

			//
		} catch (SQLException exc) {
			System.out.println("Getting remaining qtty and expiry date remaining from db error" + exc);
		}

	}

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(drugStatusModel);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// search drug
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
}
