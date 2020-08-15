package st_mary_hospital.jerry;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class ColumnColorRenderer extends DefaultTableCellRenderer {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	Color backColor, foreGroundColor;

	public ColumnColorRenderer(Color backC, Color foreColor) {
		super();
		this.backColor = backC;
		this.foreGroundColor = foreColor;
	}

	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4,
			int arg5) {
		Component cell = super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
		cell.setBackground(backColor);
		cell.setForeground(foreGroundColor);
		return cell;
	}

}
