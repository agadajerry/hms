package st_mary_hospital.jerry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class DeleteDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> patientBox;
	private static DefaultComboBoxModel<String> patientComboModel = new DefaultComboBoxModel<String>();
	private JButton okBtn;

	public DeleteDialog() {
		setSize(new Dimension(360, 100));
		setTitle("Delete Patient History");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		initUi();
	}

	private void initUi() {
		patientComboModel = new DefaultComboBoxModel<String>();
		patientBox = new JComboBox<String>(patientComboModel);
		patientBox.setEditable(true);
		AutoCompleteDecorator.decorate(patientBox);
		patientBox.setBorder(new LineBorder(new Color(63, 63, 63), 2));
		patientBox.setFont(new Font("David", 1, 20));
		patientBox.setPreferredSize(new Dimension(200, 30));
		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.add(patientBox);
		okBtn = new JButton("DELETE");
		okBtn.setPreferredSize(new Dimension(110, 30));
		okBtn.addActionListener(new DeleteProductListener());

		centerPanel.add(okBtn);
		add(centerPanel, BorderLayout.CENTER);
		add(new JPanel(), BorderLayout.NORTH);
		patientId();// select all patient id and set it on combobox,
	}

	// delete patient information
	private class DeleteProductListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (patientBox.getSelectedIndex() == -1) {
				System.out.println("No patient id is in db yet");
			} else {
				PreparedStatement ps = null;
				String qry = "Delete From patient_register where patient_id=?";
				try {
					ps = St_MaryConnection.getConnection().prepareStatement(qry);
					ps.setString(1, patientBox.getSelectedItem().toString());
					int choice = JOptionPane.showConfirmDialog(null,
							"Are you sure" + " you want to delete " + patientBox.getSelectedItem().toString() + " ?");
					if (choice == JOptionPane.YES_OPTION) {
						ps.execute();
						
						JOptionPane.showMessageDialog(null, "Patient with ID " + patientBox.getSelectedItem().toString()
								+ " is deleted successfully...");
						
						//
						// refresh the age display bar by the left
						RegistrationPage.setAgeOnButtons();// call this method for refreshing age bar---
						dispose();// exit the delete dialog

					}
				} catch (SQLException exc) {
					System.out.println("Patient data delete error\n" + exc);
				}
			}
		}
	}

	/*
	 * this method get all patient id from the data base
	 */
	private void patientId() {

		try {
			PreparedStatement ps = St_MaryConnection.getConnection()
					.prepareStatement("Select patient_id From patient_register");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String patientId = rs.getString("patient_id");
				patientComboModel.addElement(patientId);
			}
		} catch (SQLException exc) {
			System.out.println("Patient id selection error\n" + exc);
		}

	}
}
