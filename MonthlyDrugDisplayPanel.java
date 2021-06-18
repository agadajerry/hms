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
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class MonthlyDrugDisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table,patientTable;
	private DefaultTableModel model,patientModel;
	private DefaultComboBoxModel<String> yrModel,yrModel2;
	private JComboBox<String> yrBox, yrBox2;
	private JButton okBtn, printB;
	// private TableColumn tColumn;
	private DefaultTableCellRenderer cellRenderer1,cellPatRender;
	private JTextField searchField;
	private TableRowSorter<DefaultTableModel> sorter;
	private JLabel totalDrugLabel;

	public MonthlyDrugDisplayPanel() {
		setLayout(new BorderLayout(8,8));
		setBackground(Color.WHITE);

		

		JPanel northPanel = new JPanel(new GridLayout(1,2,2,3));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/med_drug.png"));
		
		
		JLabel northLabel = new JLabel("<html><body><div>"
				+ "<h2> SUMMARY OF MONTHLY CONSUMPTION "
				+ ""
				+ "</h2><hr/><span style=color:red>MONTHLY DRUGS ORDER RATE</span></div></body></html>"
				);
		
		northLabel.setFont(new Font("Aharoni",Font.BOLD,18));
		JLabel northIconLabel = new JLabel("REQUEST Rate|",northBgIcon,JLabel.CENTER);
		
		//This panel hold display images
		JPanel dispPanel = new JPanel();
		dispPanel.setBackground(Color.WHITE);
		dispPanel.add(northIconLabel);
		northPanel.add(dispPanel);
		
		northPanel.add(northLabel);
		add(northPanel, BorderLayout.NORTH);
		//

		//

		JPanel centerPanel = new JPanel(new FlowLayout());
		// centerPanel.setBorder(new EmptyBorder(10,10,10,10));
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
		yrModel = new DefaultComboBoxModel<String>();
		yrBox = new JComboBox<String>(yrModel);
		yrBox.setPreferredSize(new Dimension(230, 30));

		AutoCompleteDecorator.decorate(yrBox);

		//

		//
		JLabel dateLabel = new JLabel("Start Year:");
		centerPanel.add(dateLabel);
		centerPanel.add(yrBox);
		
		///
		
		yrModel2 = new DefaultComboBoxModel<String>();
		yrBox2 = new JComboBox<String>(yrModel2);
		yrBox2.setPreferredSize(new Dimension(230, 30));

		AutoCompleteDecorator.decorate(yrBox2);

		//

		//
		JLabel dateLabel2 = new JLabel("End Year:");
		centerPanel.add(dateLabel2);
		centerPanel.add(yrBox2);

		//
		okBtn = new JButton("Go/Calculate");
		okBtn.setPreferredSize(new Dimension(100, 30));
		okBtn.setBackground(Color.WHITE);
		okBtn.setForeground(Color.BLACK);
		okBtn.addActionListener(new SearchDrugListener());
		centerPanel.add(okBtn);

		//
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 16));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(22);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 16));
	

//

		String tableColumn[] = { "DrugName", "JAN", "FEB", "MARCH", "APRIL", "MAY", "JUN", "JUL", "AUG", "SEPT", "OCT",
				"NOV", "DEC" };
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(tableColumn);
		JScrollPane scrollBar = new JScrollPane(table);
		scrollBar.setPreferredSize(new Dimension(1000, 300));
		//

		cellRenderer1 = new DefaultTableCellRenderer();
		cellRenderer1.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(0).setPreferredWidth(320);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer1);
		//
		JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		tablePanel.setPreferredSize(new Dimension(1010, 355));

		tablePanel.add(scrollBar);

		centerPanel.add(tablePanel);
		add(centerPanel, BorderLayout.CENTER);
		// table.addMouseListener(new TextFieldGetTextListener());
		
		/*
		 * patient table for monthly visit
		 */
		
		//
		patientTable = new JTable();
		patientTable.getTableHeader().setFont(new Font("David", Font.BOLD, 16));
		patientTable.getTableHeader().setBackground(new Color(0, 194, 255));
		patientTable.getTableHeader().setForeground(Color.WHITE);
		patientTable.getTableHeader().setOpaque(false);
		patientTable.setRowHeight(22);
		patientTable.setForeground(Color.BLACK);
		patientTable.setFont(new Font("David", Font.BOLD, 16));
	

//

		String pattableColumn1[] = {  "JAN", "FEB", "MARCH", "APRIL", "MAY", "JUN", "JUL", "AUG", "SEPT", "OCT",
				"NOV", "DEC" };
		patientModel = (DefaultTableModel) patientTable.getModel();
		patientModel.setColumnIdentifiers(pattableColumn1);
		JScrollPane patientScrollBar = new JScrollPane(patientTable);
		patientScrollBar.setPreferredSize(new Dimension(1000, 80));
		//

		cellPatRender = new DefaultTableCellRenderer();
		cellPatRender.setHorizontalAlignment(JLabel.LEFT);
		//patientTable.getColumnModel().getColumn(0).setPreferredWidth(320);
		patientTable.getColumnModel().getColumn(0).setCellRenderer(cellPatRender);
		//
		JPanel patientTablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		patientTablePanel.setPreferredSize(new Dimension(1010, 110));

		JLabel patientTitleLabel = new JLabel("<html><body><div>"
				+ "</h2><hr/><span style=color:red>PATIENT MONTHLY VISIT</span><hr/></div></body></html>");
		
		totalDrugLabel = new JLabel("Total Selected Drugs:");
		patientTitleLabel.setFont(new Font("David",Font.BOLD,18));
		totalDrugLabel.setFont(new Font("David",Font.BOLD,30));
		totalDrugLabel.setForeground(Color.BLACK);
		patientTablePanel.add(totalDrugLabel);
		patientTablePanel.add(new JLabel());
		patientTablePanel.add(new JLabel());
		patientTablePanel.add(patientTitleLabel);
		patientTablePanel.add(patientScrollBar);

		patientTablePanel.setBorder(new EmptyBorder(10,10,10,10));
		patientTablePanel.setBackground(Color.WHITE);
		/*
		 * adding table to south panel
		 */
		
		
		add(patientTablePanel, BorderLayout.SOUTH);

		allDateList();
		//getDrugmonthlyConsumption();// all monthly consumption

	}

	private void allDateList() {
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(
					"Select Distinct " + "YEAR(orderDate) AS year From" + "" + " patientdruglisttable");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String yearsIn = rs.getString("year");

				// drugModel.removeAllElements();
				yrModel.addElement(yearsIn);
				yrModel2.addElement(yearsIn);
			}

		} catch (SQLException ex) {
			System.out.println("Error in getting all expiry date list and set on drug box\n" + ex);
		}
	}

	// fetch record from database base on monthly consumption

	private void getDrugmonthlyConsumption() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("SELECT drugNameorder, SUM(	IF(MONTH(`orderDate`)= 1,quantityDemanded,0))"
							+ " AS JANUARY,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 2,quantityDemanded,0)) AS FEBRUARY,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 3,quantityDemanded,0)) AS MARCH,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 4,quantityDemanded,0)) AS APRIL,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 5,quantityDemanded,0)) AS MAY,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 6,quantityDemanded,0)) AS JUNE,\r\n" + "\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 7,quantityDemanded,0)) AS JULY,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 8,quantityDemanded,0)) AS AUGUST,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 9,quantityDemanded,0)) AS SEPTEMBER,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 10,quantityDemanded,0)) AS OCTOBER,\r\n"
							+ "SUM(	IF(MONTH(`orderDate`)= 11,quantityDemanded,0)) AS NOVEMBER,"
							+ "SUM(	IF(MONTH(`orderDate`)= 12,quantityDemanded,0)) AS DECEMBER\r\n"
							+ "  FROM  patientdruglisttable WHERE YEAR(orderDate) BETWEEN"
							+ " ? AND ?  GROUP BY ID");
			
			ps.setString(1, yrBox.getSelectedItem().toString());
			ps.setString(2, yrBox2.getSelectedItem().toString());

			ResultSet rs = ps.executeQuery();

			// table.setModel(DbUtils.resultSetToTableModel(rs));
			while (rs.next()) {
				String drugNames = rs.getString("drugNameorder");
				String jan = rs.getString("JANUARY");
				String feb = rs.getString("FEBRUARY");
				String marchy = rs.getString("MARCH");
				String april = rs.getString("APRIL");
				String may = rs.getString("MAY");
				String jun = rs.getString("JUNE");
				String jul = rs.getString("JULY");
				String aug = rs.getString("AUGUST");
				String sept = rs.getString("SEPTEMBER");
				String oct = rs.getString("OCTOBER");
				String nov = rs.getString("NOVEMBER");
				String dec = rs.getString("DECEMBER");

				model.addRow(
						new String[] { drugNames, jan, feb, marchy, april, may, jun, jul, aug, sept, oct, nov, dec });
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// searching for a particular drug
	private class ItemSearchListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			sorterProduct(searchField.getText().toString().toUpperCase());

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

	}
	
	//search drug with a selected year
	private class SearchDrugListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if(yrBox.getSelectedIndex()!= -1) {
				
				model.setRowCount(0);
				patientModel.setRowCount(0);
				getDrugmonthlyConsumption();
				patientMonthyVisit();
				totalSpecificDrug();
			}else {
				JOptionPane.showMessageDialog(null, "No year is selected...");
			}
		}
		
	}
	
	/*
	 * total visit in the month
	 */
	
	private void patientMonthyVisit() {
		
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(""
					+ " SELECT COUNT(CASE WHEN MONTH(orderDate)=1 THEN patientId END) AS jan,\r\n" + 
					"  COUNT(CASE WHEN MONTH(orderDate)=2 THEN patientId END) AS feb,\r\n" + 
					"   COUNT(CASE WHEN MONTH(orderDate)=3 THEN patientId END) AS march,\r\n" + 
					"   COUNT(CASE WHEN MONTH(orderDate)=4 THEN patientId END) AS april,\r\n" + 
					"  COUNT(CASE WHEN MONTH(orderDate)=5 THEN patientId END) AS may,\r\n" + 
					"   COUNT(CASE WHEN MONTH(orderDate)=6 THEN patientId END) AS jun,\r\n" + 
					"   COUNT(CASE WHEN MONTH(orderDate)=7 THEN patientId END) AS jul,\r\n" + 
					"  COUNT(CASE WHEN MONTH(orderDate)=8 THEN patientId END) AS aug,\r\n" + 
					"   COUNT(CASE WHEN MONTH(orderDate)=9 THEN patientId END) AS sept,\r\n" + 
					"  COUNT(CASE WHEN MONTH(orderDate)=10 THEN patientId END) AS oct,\r\n" + 
					"  COUNT(CASE WHEN MONTH(orderDate)=11 THEN patientId END) AS nov,\r\n" + 
					"   COUNT(CASE WHEN MONTH(orderDate)=12 THEN patientId END) AS december\r\n" + 
					"   FROM patientMonthlyVisit1 where YEAR(orderDate) =? ");
			
			ps.setString(1, yrBox.getSelectedItem().toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
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

				patientModel.addRow(
						new String[] {  jan, feb, marchy, april, may, jun, jul, aug, sept, oct, nov, dec });
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	//This method calculate total of specific drug
	
	private void totalSpecificDrug() {
	int a1=0,a2=0,a3=0,a4=0,a5=0,a6=0,a7=0,a8=0, a9=0, a10=0,a11=0,a12=0;
	int sumTotal=0;
	
		for (int i = 0; i < table.getRowCount(); i++) {

			a1 += Integer.parseInt((String) table.getValueAt(i, 1));
			a2+= Integer.parseInt((String) table.getValueAt(i, 2));
			a3+= Integer.parseInt((String) table.getValueAt(i, 3));
			a4+= Integer.parseInt((String) table.getValueAt(i, 4));
			a5+= Integer.parseInt((String) table.getValueAt(i, 5));
			a6+= Integer.parseInt((String) table.getValueAt(i, 6));
			a7+= Integer.parseInt((String) table.getValueAt(i, 7));
			a8+= Integer.parseInt((String) table.getValueAt(i, 8));
			a9+= Integer.parseInt((String) table.getValueAt(i, 9));
			a10+= Integer.parseInt((String) table.getValueAt(i, 10));
			a11+= Integer.parseInt((String) table.getValueAt(i, 11));
			a12+= Integer.parseInt((String) table.getValueAt(i, 12));
			
			sumTotal = a1+a2+a3+a4+a5+a6+a7+a8+a9+a10+a11+a12;
			
			
			totalDrugLabel.setText("Total Selected Drug: " + sumTotal);
		}
	}
}
