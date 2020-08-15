package surgery.co;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.toedter.calendar.JDateChooser;

public class SugeryInfo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel[] inputLabel = new JLabel[2];
	private JTextField[] inputField = new JTextField[2];
	private JDateChooser regDate;
	private JComboBox<String> sexBox;
	private JTextField patientField;
	private JTextArea addressField;
	

	/**
	 * Create the dialog.
	 */
	public SugeryInfo() {
		this.setSize(new Dimension(1000, 600));
		this.setLocationRelativeTo(null);
		this.setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{

			//
			{
				//
				patientField = new JTextField();
				JLabel patientLabel = new JLabel("Patient Name:");
				patientLabel.setFont(new Font("David", 1, 16));
				patientField.setFont(new Font("David", 1, 16));
				patientLabel.setBounds(20,40,150,30);
				patientField.setBounds(175,40,200,30);
				contentPanel.add(patientLabel);
				contentPanel.add(patientField);
				//
				String[] genda = { "Gender", "Male", "Female" };
				sexBox = new JComboBox<String>(genda);
				JLabel sexLabel = new JLabel("Gender:");
				sexLabel.setBounds(20, 80, 150, 30);
				sexLabel.setForeground(new Color(0, 0, 0));
				sexLabel.setFont(new Font("David", 1, 18));
				//
				sexBox.setPreferredSize(new Dimension(200, 30));
				sexBox.setForeground(new Color(0, 0, 0));
				sexBox.setFont(new Font("David", 1, 18));
				sexBox.setBounds(175, 80, 200, 30);
				contentPanel.add(sexLabel);
				contentPanel.add(sexBox);
				//
				
				for (int i = 0; i < inputField.length; i++) {
					inputField[i] = new JTextField();
					inputLabel[i] = new JLabel();
					inputLabel[i].setFont(new Font("David", 1, 18));
					inputField[i].setFont(new Font("David", 1, 18));

					inputLabel[i].setBounds(20, 120 + (35 * i), 150, 30);
					inputField[i].setBounds(175, 120 + (35 * i), 200, 30);
					contentPanel.add(inputLabel[i]);
					contentPanel.add(inputField[i]);

				}
				inputLabel[0].setText("Age:");
				inputLabel[1].setText("Weight:");
				
				//
				regDate = new JDateChooser();
				regDate.setLocale(Locale.US);
				JLabel dateLabel = new JLabel("Surgery Date:");
				dateLabel.setFont(new Font("David", 1, 18));
				dateLabel.setBounds(20, 195, 150, 30);
				regDate.setBounds(175, 195, 200, 30);
				contentPanel.add(dateLabel);
				contentPanel.add(regDate);
				//
				JLabel addrLabel = new JLabel("Address:");
				addressField = new JTextArea();
				addrLabel.setBounds(20,235,150,30);
				addressField.setBounds(175,235,200,80);
				addressField.setLineWrap(true);
				addressField.setFont(new Font("David", 1, 18));
				addrLabel.setFont(new Font("David", 1, 18));
				contentPanel.add(addrLabel);
				contentPanel.add(addressField);
			}

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
