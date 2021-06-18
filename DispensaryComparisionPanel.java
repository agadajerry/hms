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

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import st_mary_hospital.jerry.ColumnColorRenderer;

public class DispensaryComparisionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table2;
	private DefaultTableModel model2;
	public static DefaultComboBoxModel<String> yearModel;
	private JComboBox<String> yearBox;
	private JButton okBtn;
	private DefaultTableCellRenderer cellRenderer;
	private TableColumn tColumn;
	private JTextField searchField;
	private TableRowSorter<DefaultTableModel> sorter;

	public DispensaryComparisionPanel() {

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.setBackground(Color.WHITE);
		
		
		JLabel northLabel = new JLabel("<html><body><div>"
				+ "<h2> DISPENSARY UNIT QUANTITY COMPARISON "
				+ ""
				+ "</h2><hr/></div></body></html>"
				);
		
		northLabel.setFont(new Font("Aharoni",Font.BOLD,18));
				
		
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setBackground(Color.WHITE);

		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(250, 30));
		searchField.setFont(new Font("David", 1, 16));
		searchField.setBorder(new LineBorder(Color.BLACK));
		searchField.addKeyListener(new ItemSearchListener());
		JLabel searchLabel = new JLabel("Search:");
		searchLabel.setFont(new Font("David", 1, 16));
		centerPanel.add(searchLabel);
		centerPanel.add(searchField);
		//

		yearModel = new DefaultComboBoxModel<String>();
		yearBox = new JComboBox<String>(yearModel);
		yearBox.setPreferredSize(new Dimension(200, 30));
		yearBox.setBorder(new LineBorder(Color.GRAY, 1));

		AutoCompleteDecorator.decorate(yearBox);
		JLabel yearLabel = new JLabel("Select Year");
		centerPanel.add(yearLabel);
		centerPanel.add(yearBox);

		okBtn = new JButton("Check");
		okBtn.setBackground(Color.WHITE);
		okBtn.setBorder(new LineBorder(new Color(0, 194, 255)));
		okBtn.setForeground(Color.BLACK);
		okBtn.setFont(new Font("David", 1, 16));
		okBtn.setPreferredSize(new Dimension(100, 30));
		okBtn.addActionListener(new DailySaleListenr());
		centerPanel.add(okBtn);

		// table.addMouseListener(new TextFieldGetTextListener());
//

		//
		table2 = new JTable();
		table2.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table2.getTableHeader().setBackground(new Color(0, 194, 255));
		table2.getTableHeader().setForeground(Color.WHITE);
		table2.getTableHeader().setOpaque(false);
		table2.setRowHeight(30);
		table2.setForeground(Color.BLACK);
		table2.setFont(new Font("David", Font.BOLD, 16));

//

		String tableColumn2[] = { "Drug Name", "Quantity Ordered", "Quanity Sold", "Remainder", "Expiry Date",
				"Order Date", "Whom Recieved", "To whom Issued" };

		model2 = (DefaultTableModel) table2.getModel();
		model2.setColumnIdentifiers(tableColumn2);
		JScrollPane scrollBar2 = new JScrollPane(table2);
		scrollBar2.setPreferredSize(new Dimension(1100, 800));

		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.LEFT);
		table2.getColumnModel().getColumn(0).setPreferredWidth(320);
		table2.getColumnModel().getColumn(1).setPreferredWidth(100);
		table2.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

		tColumn = table2.getColumnModel().getColumn(3);
		tColumn.setCellRenderer(new ColumnColorRenderer(Color.YELLOW, Color.RED));

		JPanel table2Panel = new JPanel(new GridLayout());
		table2Panel.setBackground(Color.WHITE);
		table2Panel.add(scrollBar2);
		centerPanel.add(table2Panel);

		add(centerPanel, BorderLayout.CENTER);
		//

		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		southPanel.setBackground(Color.WHITE);

		//

		// southPanel.add(statusLabel);
		add(southPanel, BorderLayout.SOUTH);

		yearOfTransaction();
	}

	//

	//
	// get year of the transaction
	// get all the drug name and set it on combo box
	private void yearOfTransaction() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select Distinct YEAR(orderDate) as Year" + " From" + " counterdrugordertable");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String yearlyDetail = rs.getString("Year");

				yearModel.addElement(yearlyDetail);
			}

		} catch (SQLException ex) {
			System.out.println("Error in getting all year list and set on drug box\n" + ex);
		}
	}

	// getting the drug order and sold information
	private class DailySaleListenr implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			//
			if (yearBox.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(null, "Please select year you want to compared.");
			} else {

				model2.setRowCount(0);

				//
				getSaleHistory(yearBox.getSelectedItem().toString());

				// totalQuantityInventory(drugBox.getSelectedItem().toString(),
				// expireBox.getSelectedItem().toString());

			}
		}

		// get the history of sale and compare to order qtty

		private void getSaleHistory(String year) {

			String qry2 = "SELECT drugNameorder, SUM( CASE WHEN drugNameorder= drugNameorder"
					+ " THEN quantityDemanded END) as qttyOrdered, `sold_qtty`,  "
					+ " `expiryDate` AS expiryDateYEAR_Month,`to_whom_issued`,"
					+ "`whom_Recieved`,`orderDate` FROM `counterdrugordertableremove`"
					+ " WHERE quantityDemanded>0 GROUP BY `order_id`";
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			try {
				ps1 = St_MaryConnection.getConnection().prepareStatement(qry2);

				//ps1.setString(1, year);
				rs1 = ps1.executeQuery();

				while (rs1.next()) {

					String drugName = rs1.getString("drugNameorder");
					int qttyOrder = rs1.getInt("qttyOrdered");
					int soldQ = rs1.getInt("sold_qtty");
					String expriDate = rs1.getString("expiryDateYEAR_Month");
					String whomIssue = rs1.getString("to_whom_issued");
					String whom_Recieved = rs1.getString("whom_Recieved");
					String orderDate = rs1.getString("orderDate");
					/*
					 * double unitp = rs1.getDouble("unitPrice"); double totalP =
					 * rs1.getDouble("total_per_item"); String invoiceDate =
					 * rs1.getString("invoiceDate");
					 */

					//

					model2.addRow(new String[] { drugName, "" + qttyOrder, "" + soldQ, "" + (qttyOrder - soldQ),
							expriDate, orderDate, whomIssue, whom_Recieved });

				}

			} catch (SQLException ex) {
				System.out.println("Getting drug details when expire, year and drug name is selected"
						+ "from saleinoice_table error\n" + ex);
			}
		}
	}

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(model2);
		table2.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// search listener
	private class ItemSearchListener implements KeyListener {

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
