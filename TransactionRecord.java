package st_mary.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import net.proteanit.sql.DbUtils;
import st_mary_hospital.jerry.St_MaryConnection;

public class TransactionRecord extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> dateBox;
	public static DefaultComboBoxModel<String> comboModel;
	private JTable table, saleTable;
	private DefaultTableModel model, saleModel;
	private JLabel totalLabel;
	private double amountTotal = 0;
	private JButton okB;
	private DefaultTableCellRenderer cellRendererSale;

	//
	public TransactionRecord() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel(new GridLayout(1,2,2,3));
		northPanel.setBackground(Color.WHITE);
		
		
		JLabel northLabel = new JLabel("<html><body><div>"
				+ "<h2> DAILY FINANCIAL REPORT"
				+ ""
				+ "</h2><hr/></div></body></html>"
				);
		
		northLabel.setFont(new Font("Aharoni",Font.BOLD,18));
		
		//This panel hold display images
		JPanel dispPanel = new JPanel();
		dispPanel.setBackground(Color.WHITE);
		northPanel.add(dispPanel);
		
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		// center panel for date and table for record display
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(Color.WHITE);
		JPanel cNorthPanel = new JPanel(new FlowLayout());
		cNorthPanel.setBackground(Color.WHITE);
		JLabel dateComboLabel = new JLabel("Sale Date");
		dateComboLabel.setFont(new Font("David", 1, 18));
		dateComboLabel.setForeground(Color.BLACK);
		cNorthPanel.add(dateComboLabel);
		//
		comboModel = new DefaultComboBoxModel<String>();
		dateBox = new JComboBox<String>(comboModel);
		dateBox.setPreferredSize(new Dimension(200, 30));
		dateBox.setBorder(new LineBorder(Color.GRAY, 1));
		AutoCompleteDecorator.decorate(dateBox);
		dateBox.addItemListener(new DateSelectionListener());
		cNorthPanel.add(dateBox);
		//
		okB = new JButton("Calculate");
		okB.setPreferredSize(new Dimension(100, 30));
		// okB.setBackground(Color.GRAY);
		okB.setForeground(Color.BLACK);
		okB.addActionListener(new SumAllPurchaseListener());
		cNorthPanel.add(okB);
		//
		centerPanel.add(cNorthPanel, BorderLayout.NORTH);
		// table for inserted data from input field
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 16));

		// table.addMouseListener(new TextFieldGetTextListener());

		String tableColumn[] = { "PATIENT ID", "AMOUNT PAID" };
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar = new JScrollPane(table);
		scrollBar.setPreferredSize(new Dimension(1100, 230));
		//
		JPanel tablePanel = new JPanel(new GridLayout());
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		// tablePanel.add(operationBtnPanel,BorderLayout.NORTH);
		tablePanel.add(scrollBar);
		centerPanel.add(tablePanel, BorderLayout.CENTER);

		// south panel containing all the information the transaction made
		JPanel southPanel = new JPanel(new GridLayout(1, 2));
		southPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel totalPanel = new JPanel();
		totalLabel = new JLabel("Total");
		totalLabel.setFont(new Font("David", Font.BOLD, 23));
		totalLabel.setForeground(Color.WHITE);
		totalPanel.add(totalLabel);
		totalPanel.setBackground(Color.GRAY);

		southPanel.add(totalPanel);
		//
		centerPanel.add(southPanel, BorderLayout.SOUTH);

		//

		add(centerPanel, BorderLayout.CENTER);

		saleTable = new JTable();
		saleTable.getTableHeader().setFont(new Font("David", Font.BOLD, 16));
		saleTable.getTableHeader().setBackground(new Color(0, 194, 255));
		saleTable.getTableHeader().setForeground(Color.WHITE);
		saleTable.getTableHeader().setOpaque(false);
		saleTable.setRowHeight(22);
		saleTable.setForeground(Color.BLACK);
		saleTable.setFont(new Font("David", Font.BOLD, 16));

//

		String pattableColumn1[] = { "JAN", "FEB", "MARCH", "APRIL", "MAY", "JUN", "JUL", "AUG", "SEPT", "OCT", "NOV",
				"DEC" };
		saleModel = (DefaultTableModel) saleTable.getModel();
		saleModel.setColumnIdentifiers(pattableColumn1);
		JScrollPane patientScrollBar = new JScrollPane(saleTable);
		patientScrollBar.setPreferredSize(new Dimension(1100, 80));
		//

		cellRendererSale = new DefaultTableCellRenderer();
		cellRendererSale.setHorizontalAlignment(JLabel.LEFT);
		// saleTable.getColumnModel().getColumn(0).setPreferredWidth(320);
		saleTable.getColumnModel().getColumn(0).setCellRenderer(cellRendererSale);
		//
		JPanel saleTablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		saleTablePanel.setPreferredSize(new Dimension(1010, 110));

		JLabel patientTitleLabel = new JLabel("<html><body><div>"
				+ "</h2><hr/><span style=color:red> MONTHLY SALES TOTAL</span><hr/></div></body></html>");

		patientTitleLabel.setFont(new Font("David", Font.BOLD, 18));
		saleTablePanel.add(patientTitleLabel);
		saleTablePanel.add(patientScrollBar);

		saleTablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		saleTablePanel.setBackground(Color.WHITE);
		add(saleTablePanel, BorderLayout.SOUTH);

		// visit date
		patientDateVisted();
	}// end of constructor

	// show patient daily amount paid
	private void patientAmountPaid(String dateVis) {
		String dateV = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		double amount = 0.0;
		String qry = "Select patientId, totalPrice from patientdruglisttable where"
				+ " orderDate=?  order by orderDate desc";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ps.setString(1, dateVis);
			rs = ps.executeQuery();

			while (rs.next()) {
				String pId = rs.getString("patientId");
				amount = rs.getDouble("totalPrice");
				model.addRow(new String[] { pId, "" + amount});

			}
			//

		} catch (SQLException ex) {
			System.out.println("Error in getting customer pay"+ex.getMessage());
		}
	}

	//
	// date selection class
	private class DateSelectionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg0) {

			if (dateBox.getSelectedIndex() != -1) {
				amountTotal = 0;// reset
				model.setRowCount(0);// reset table first
				patientAmountPaid(dateBox.getSelectedItem().toString());
				totalLabel.setText("Total amount: " + amountTotal);

			}
		}

	}

	// show patient daily amount paid
	public static void patientDateVisted() {
		// String dateV="";
		PreparedStatement ps = null;
		ResultSet rs = null;
		String qry = "Select Distinct orderDate from patientdruglisttable "
				+ "order by orderDate desc";
		try {
			ps = St_MaryConnection.getConnection().prepareStatement(qry);

			rs = ps.executeQuery();
			// table.setModel(DbUtils.resultSetToTableModel(rs));

			while (rs.next()) {

				String dateV = rs.getString("orderDate");

				comboModel.addElement(dateV);

			}
		} catch (SQLException ex) {
			System.out.println("Error in getting customer date visit");
		}
	}

	// sum all item purchase on a particular date
	private class SumAllPurchaseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			double totalsum = 0.0;
			if (table.getRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "The table is empty. No transaction is made yet.");
			} else {

				for (int i = 0; i < table.getRowCount(); i++) {

					totalsum += Double.parseDouble((String) table.getValueAt(i, 1));
					System.out.println(totalsum);
					//
					//
					NumberFormat nf = NumberFormat.getInstance();
					// amountTotal += amount;
					totalLabel.setText("Total amount: " + nf.format(totalsum) + " [Naira]");
				}
				
				
				saleModel.setRowCount(0);
				monthySale();//monthly sale total
				

			}
		}

	}

	private void monthySale() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("" + " SELECT SUM(CASE WHEN MONTH(orderDate)=1 THEN totalPrice END) AS jan,\r\n"
							+ "   SUM(CASE WHEN MONTH(orderDate)=2 THEN totalPrice END) AS feb,\r\n"
							+ "   SUM(CASE WHEN MONTH(orderDate)=3 THEN totalPrice END) AS march,\r\n"
							+ "   SUM(CASE WHEN MONTH(orderDate)=4 THEN totalPrice END) AS april,\r\n"
							+ "  SUM(CASE WHEN MONTH(orderDate)=5 THEN totalPrice END) AS may,\r\n"
							+ "   SUM(CASE WHEN MONTH(orderDate)=6 THEN totalPrice END) AS jun,\r\n"
							+ "   SUM(CASE WHEN MONTH(orderDate)=7 THEN totalPrice END) AS jul,\r\n"
							+ "  SUM(CASE WHEN MONTH(orderDate)=8 THEN totalPrice END) AS aug,\r\n"
							+ "   SUM(CASE WHEN MONTH(orderDate)=9 THEN totalPrice END) AS sept,\r\n"
							+ "  SUM(CASE WHEN MONTH(orderDate)=10 THEN totalPrice END) AS oct,\r\n"
							+ "  SUM(CASE WHEN MONTH(orderDate)=11 THEN totalPrice END) AS nov,\r\n"
							+ "   SUM(CASE WHEN MONTH(orderDate)=12 THEN totalPrice END) AS december\r\n"
							+ "   FROM patientdruglisttable where YEAR(orderDate) =?");

			ps.setString(1, dateBox.getSelectedItem().toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String jan = rs.getString("jan");
				String feb = rs.getString("feb");
				String marchy = rs.getString("march");
				String april = rs.getString("april");
				String may = rs.getString("may");
				String jun = rs.getString("jun");
				String jul = rs.getString("jul");
				String aug = rs.getString("aug");
				String sept = rs.getString("sept");
				String oct = rs.getString("oct");
				String nov = rs.getString("nov");
				String dec = rs.getString("december");

				saleModel.addRow(new String[] { jan, feb, marchy, april,
						may, jun, jul, aug, sept, oct, nov, dec });
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
