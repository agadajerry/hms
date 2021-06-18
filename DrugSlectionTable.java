package st_mary.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class DrugSlectionTable extends JDialog {

	private JTextField searchField;
	private DefaultTableCellRenderer cellRenderer;
	private TableRowSorter<DefaultTableModel> sorter;
	private JTable table;
	private DefaultTableModel model;

	public DrugSlectionTable() {
		setSize(new Dimension(1200, 600));
		setTitle("Hospital management system");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setModal(true);
		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dispose();
		setBackground(Color.WHITE);
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.setBackground(Color.WHITE);
		ImageIcon northBgIcon = new ImageIcon(getClass().getResource("/all/images/drug.png"));
		JLabel northLabel = new JLabel("", northBgIcon, JLabel.CENTER);

		northPanel.add(northLabel);

		add(northPanel, BorderLayout.NORTH);

		//

		JPanel southPanel2 = new JPanel();
		southPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));

		southPanel2.setBackground(Color.WHITE);

		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(250, 30));
		searchField.setFont(new Font("David", 1, 16));
		searchField.setBorder(new LineBorder(Color.GRAY));
		searchField.addKeyListener(new ItemSearchListener());
		JLabel searchLabel = new JLabel("Search:");
		searchLabel.setFont(new Font("David", 1, 16));
		southPanel2.add(searchLabel);
		southPanel2.add(searchField);
		// adding components to panel31

		//

		String[] columnName = { "DrugId", "Description", "Class", "Unit", "Category", "Drug Location", "Date" };
		table = new JTable();
		table.getTableHeader().setFont(new Font("David", Font.BOLD, 18));
		table.getTableHeader().setBackground(new Color(0, 194, 255));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(false);
		table.setRowHeight(25);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("David", Font.BOLD, 12));
		table.addMouseListener(new RowClickedListener());
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(columnName);

		JScrollPane tableScroll = new JScrollPane(table);
		JPanel tableScrollPanel = new JPanel(new GridLayout());

		table.getColumnModel().getColumn(0).setPreferredWidth(4);
		table.getColumnModel().getColumn(1).setPreferredWidth(320);
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		// tableScrollPanel.setBorder(new EmptyBorder(5,5,5,5));
		tableScrollPanel.add(tableScroll);
		//
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setBorder(new LineBorder(Color.GRAY, 2));
		southPanel.setBackground(Color.LIGHT_GRAY);
		southPanel.add(tableScrollPanel, BorderLayout.CENTER);
		southPanel.add(southPanel2, BorderLayout.NORTH);
		add(southPanel, BorderLayout.CENTER);
		getAllDrugs();	// select all drugs from db table and set on app table

	}

	// select all drugs from db table and set on app table

	private void getAllDrugs() {
		String qry = "Select * From drug_details";
		String dDescribe = "", dLocation = "", dCat = "", dUnit = null, dDate = "", dClass = "";
		int dId = 0;
		try {
			PreparedStatement ps = St_MaryConnection.getConnection().prepareStatement(qry);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				dDescribe = rs.getString("description");
				dLocation = rs.getString("drug_location");
				dCat = rs.getString("category");
				dClass = rs.getString("class");
				dUnit = rs.getString("Unit");
				dDate = rs.getString("Daily_Date");
				dId = rs.getInt("drugId");
				model.addRow(new String[] { "" + dId, dDescribe, dClass, dUnit, dCat, dLocation, dDate });
			}

		} catch (SQLException ex) {
			System.out.println("Getting all drug details errror\n" + ex);
		}
	}

	// sorter focus listener class
	private void sorterProduct(String qry) {
		sorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(sorter);
		sorter.setRowFilter(RowFilter.regexFilter(qry));
	}

	// key listener class
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
	//Table row click, collecting the selected row data
	private class RowClickedListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent ev) {
			
		
			if (ev.getClickCount() == 1) {
				JTable targetCell = (JTable) ev.getSource();
				int row = targetCell.getSelectedRow();
				int[] col = targetCell.getSelectedColumns();
				//String []text = (String[]) table.getValueAt(row, col);
				
				
				
				String drugId = (String) targetCell.getValueAt(row, 0);
				String dDescription = (String) targetCell.getValueAt(row, 1);
				String dClass = (String) targetCell.getValueAt(row, 2);
				String dCat = (String) targetCell.getValueAt(row, 4);
				String dDate = (String) targetCell.getValueAt(row, 6);
				
				
				
				
				 InventoryDrugDialog ids = new InventoryDrugDialog(drugId,dDescription,dClass,dCat,dDate);
				ids.setVisible(true);
				dispose();
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
