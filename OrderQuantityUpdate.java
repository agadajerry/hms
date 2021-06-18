package st_mary.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class OrderQuantityUpdate extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel drugNameLabel, qttyLabel;
	private JButton cancelB, updateB;
	private JTextField drugField;
	private JTextField qttyField;
	private int drugIdNo = 0;
	private String dDescription;
	protected int serialNo = 0;
	private int orderQ;
	private double unitPrice;

	public OrderQuantityUpdate(int sNo, int drugIdNo2, String drugName1, int orderQtty,double unitPrice) {

		setSize(new Dimension(400, 300));

		setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);

		this.setLocationRelativeTo(null);

		this.serialNo = sNo;
		this.dDescription = drugName1;
		this.drugIdNo = drugIdNo2;
		this.orderQ = orderQtty;
		this.unitPrice =unitPrice;
		setBackground(Color.LIGHT_GRAY);
		this.setResizable(false);
		Image iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/all/images/add1.png"));
		setIconImage(iconImage);
		initialiseUi();

	}

	private void initialiseUi() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(null);
		centerPanel.setBounds(10, 10, 390, 290);
		centerPanel.setBackground(Color.LIGHT_GRAY);

		centerPanel.setBackground(Color.WHITE);

		// admin registration dialog
		JLabel adLabel = new JLabel("<html><h2 style= color:rgb(0,130,230);>"
				+ "<span style= color:red;>E</span><span style= color:orange;>n</span>"
				+ "<span style= color:green;>t</span><span style= color:blue;>e</span>"
				+ "<span style= color:pink;>r</span> Returned <span style= color:red;>"
				+ "Q</span>uantity</h2></html>");
		adLabel.setBounds(70, 8, 300, 30);
		centerPanel.add(adLabel);
		///

		drugNameLabel = new JLabel("Drug Name:");
		drugNameLabel.setFont(new Font("David", 1, 16));
		drugField = new JTextField(dDescription);
		drugField.setEditable(false);
		drugNameLabel.setBounds(20, 45, 100, 30);
		drugField.setBounds(105, 40, 200, 40);
		drugField.setFont(new Font("David", 1, 16));
		drugField.setBorder(new LineBorder(Color.BLACK, 2));
		centerPanel.add(drugNameLabel);
		centerPanel.add(drugField);
		//
		qttyLabel = new JLabel("Quantity:");
		qttyLabel.setFont(new Font("David", 1, 16));
		qttyField = new JTextField();
		qttyLabel.setLabelFor(qttyField);
		qttyLabel.setBounds(20, 110, 100, 16);
		qttyField.setBounds(105, 100, 200, 40);
		qttyField.setBorder(new LineBorder(Color.BLACK, 2));
		qttyField.setFont(new Font("David", 1, 30));
		centerPanel.add(qttyLabel);
		centerPanel.add(qttyField);
		//

		//
		cancelB = new JButton("Exit");
		cancelB.setBounds(105, 210, 200, 40);

		updateB = new JButton("Update");
		updateB.setBounds(105, 149, 200, 40);
		updateB.addActionListener(new UpdateListener());
		updateB.setToolTipText("Enter the amount of unit you want to returned.");

		cancelB.setForeground(Color.BLACK);
		// loginB.setBackground(new Color(0, 194, 255));
		cancelB.setFont(new Font("David", 1, 18));
		// updateB.addActionListener(new RegisterListener());

		updateB.setForeground(Color.BLACK);
		cancelB.addActionListener(new ExitListener());
		// loginB.setBackground(new Color(0, 194, 255));
		updateB.setFont(new Font("David", 1, 18));
		centerPanel.add(cancelB);
		centerPanel.add(updateB);
		//

		add(centerPanel, BorderLayout.CENTER);

	}

	// update class
	private class UpdateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (qttyField.getText().isEmpty() || drugField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Empty field(s). Enter require quantity you want to reverse..");
			} else {
				try {
				updateQuantity();
				dispose();
				}catch(NumberFormatException nfc) {
					JOptionPane.showMessageDialog(null, "The value enter is not a number...");
				}
			}
		}

	}

	private void updateQuantity() {

		PreparedStatement ps = null, ps1 = null, ps2 = null;
		String qryUpdateQttyLog = "CREATE TRIGGER if not exists`logTriger` AFTER UPDATE ON "
				+ "`counterdrugordertable` FOR EACH ROW INSERT INTO"
				+ " orderDrug_Update_Log VALUES(null, NEW.drugNameorder," + "NEW.quantityDemanded,now())";

		String updateOperation = "Update counterdrugordertableremove SET " + "" + " "
				+ "sold_qtty=sold_qtty-? where order_id=? ";
		// ---------------------------------------------------------------------------------------
		//
		String updatePatientDrugQtty = "Update patientdruglisttable SET " + " "
				+ "quantityDemanded=quantityDemanded-?, totalPrice = totalPrice-? where ID=?";
		
		

		try {
			ps = St_MaryConnection.getConnection().prepareStatement(updateOperation);
			ps1 = St_MaryConnection.getConnection().prepareStatement(qryUpdateQttyLog);

			// Check if reversal quantity is higher than the total quantity bought initaly
			//
			if (Integer.parseInt(qttyField.getText().toString()) > orderQ) {
				JOptionPane.showMessageDialog(null, "Quantity purchase is less than reversed amount", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {

				ps.setInt(1, Integer.parseInt(qttyField.getText().toString()));
				ps.setInt(2, drugIdNo);

				// -------------------------------------------------------------------------------
				int removeQtty = Integer.parseInt(qttyField.getText().toString());
				
				ps2 = St_MaryConnection.getConnection().prepareStatement(updatePatientDrugQtty);

				ps2.setInt(1, Integer.parseInt(qttyField.getText().toString()));
				ps2.setDouble(2, (unitPrice*removeQtty));
				ps2.setInt(3, serialNo);
				System.out.println(unitPrice);
				ps.executeUpdate();
				ps2.executeUpdate();
				ps1.execute();
				JOptionPane.showMessageDialog(null, Integer.parseInt(qttyField.getText().toString()) + ""
						+ " was reversed back. Your operation is logged...");

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	private class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			dispose();
		}

	}

}
