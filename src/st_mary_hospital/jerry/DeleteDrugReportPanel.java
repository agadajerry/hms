package st_mary_hospital.jerry;

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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class DeleteDrugReportPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField searchField;
	private JTable table;
	public static DefaultTableModel deleteTableModel;
	private TableRowSorter<DefaultTableModel> sorter;
	private DefaultTableCellRenderer cellRenderer;
	private TableColumn tColumn;
	private JButton deleteB;

	//

	public DeleteDrugReportPanel() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/delete_drug_report.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);

		// center panel for date and table for record display
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Color.WHITE);
		JPanel cNorthPanel = new JPanel(new FlowLayout());
		cNorthPanel.setBackground(Color.WHITE);
		JLabel searchLabel = new JLabel("Search");
		searchLabel.setFont(new Font("David", 1, 18));
		searchLabel.setForeground(Color.BLACK);
		cNorthPanel.add(searchLabel);
		//
		searchField = new JTextField();
		searchField.addKeyListener(new SearchDeleteDrugListener());
		searchField.setPreferredSize(new Dimension(200, 30));
		searchField.setBorder(new LineBorder(Color.GRAY, 1));
		searchField.setFont(new Font("David", 1, 16));
		cNorthPanel.add(searchField);
		//
		deleteB = new JButton("Clear Table");
		deleteB.setPreferredSize(new Dimension(200, 30));
		deleteB.setBorder(new LineBorder(new Color(0, 194, 255), 1));
		deleteB.setFont(new Font("David", 1, 18));
		deleteB.setBackground(Color.WHITE);
		deleteB.setForeground(Color.BLACK);

		deleteB.addActionListener(new DeleteAllTableDateListener());
		cNorthPanel.add(deleteB);

		centerPanel.add(cNorthPanel, BorderLayout.NORTH);
		// table for inserted data from input field
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Times New Roman", Font.BOLD, 12));

		// table.addMouseListener(new TextFieldGetTextListener());

		String tableColumn[] = { "S/No", "Description", "Class", "Category", "Quantity", "Re-Order_Lever", "UnitPrice",
				"BatchNo", "VoucherNo", "Supplier", "Expiry Date,", "Inv_Date" };
		deleteTableModel = (DefaultTableModel) table.getModel();
		deleteTableModel.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar = new JScrollPane(table);
		scrollBar.setPreferredSize(new Dimension(1100, 230));
		//
		//
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(320);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		//

		tColumn = table.getColumnModel().getColumn(4);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.YELLOW, Color.BLACK));
		//
		JPanel tablePanel = new JPanel(new GridLayout());
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		// tablePanel.add(operationBtnPanel,BorderLayout.NORTH);
		tablePanel.add(scrollBar);
		centerPanel.add(tablePanel, BorderLayout.CENTER);

		add(centerPanel, BorderLayout.CENTER);
		//
		// call this method when constructor is called
		fetchDeletedDrugs();

	}
	//

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(deleteTableModel);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// search drugs listener class
	private class SearchDeleteDrugListener implements KeyListener {

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

	// this method select deleted drug from it table
	public static void fetchDeletedDrugs() {

		String dDescribe1 = "", dCat1 = "", dClass1 = "", batchNo1 = null, voucherNo1 = null, supplier1 = null,
				expireDate1 = null, invDate1 = null;
		int orderQtty1 = 0, unitPrice1 = 0, reOrderQtty1 = 0;
		int dId1 = 0;
		String qry = "Select * From inventory_deleteHistory  order by invDate desc";
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				dDescribe1 = rs.getString("description");
				dCat1 = rs.getString("category");
				dClass1 = rs.getString("class");
				batchNo1 = rs.getString("batchNo");
				voucherNo1 = rs.getString("voucherNo");
				supplier1 = rs.getString("supplier");
				expireDate1 = rs.getString("expiryDate");
				invDate1 = rs.getString("invDate");
				reOrderQtty1 = rs.getInt("Re_Order_Qtty");
				orderQtty1 = rs.getInt("quantity");
				dId1 = rs.getInt("s_No");
				unitPrice1 = rs.getInt("sellingPrice");
				deleteTableModel.addRow(new String[] { "" + dId1, dDescribe1, dClass1, dCat1, "" + orderQtty1,
						"" + reOrderQtty1, "" + unitPrice1, batchNo1, voucherNo1, supplier1, expireDate1, invDate1 });
			}
		} catch (SQLException ex) {
			System.out.println("Error in fetching deleted drug from it table\n" + ex);
		}
	}

	// delete all table data
	private class DeleteAllTableDateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (table.getRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "The table is empty. No drug is deleted from the inventory.");
			} else {
				try {
					for (int i = 0; i < table.getRowCount(); i++) {

						PreparedStatement ps = St_MaryConnection.getConnection()
								.prepareStatement("Delete  from inventory_deletehistory " + "where s_No= ?");
						ps.setInt(1, Integer.parseInt((String) table.getValueAt(i, 0)));
						ps.executeUpdate();
					}
					JOptionPane.showMessageDialog(null, "Deleted successfully...");
					// call this method when constructor is called
					fetchDeletedDrugs();
				} catch (SQLException ex) {
					System.out.println("Error for deleting deleted drugs from inventory\n" + ex);
				}
			}

		}
	}
}
