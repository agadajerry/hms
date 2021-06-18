package st_mary.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDeveloperDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutDeveloperDialog() {

		setSize(new Dimension(390, 660));
		setTitle("About Dev");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(204, 204, 204)));

		// this.setTitle();
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);
		
		ImageIcon meIcon = new ImageIcon(getClass().getResource("/all/images/aboutme.PNG"));

		JLabel imageLabel = new JLabel("", meIcon, JLabel.CENTER);

		JPanel centerPanel = new JPanel();
		centerPanel.add(imageLabel);
		centerPanel.setBounds(0, 0, 382, 600);
		
		add(centerPanel,BorderLayout.CENTER);
		
	
		
	}

}
