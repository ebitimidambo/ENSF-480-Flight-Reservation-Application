package boundary.utility;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import entity.Seat;

@SuppressWarnings("serial")
public class AlternateRowRenderer extends DefaultTableCellRenderer {

    private static final Color STRIPE = new Color(230, 240, 250);
    private static final Color CANCELED_BG = new Color(235, 235, 235);
    private static final Color CANCELED_FG = Color.GRAY;

    private Integer statusColumnIndex = null;
    private String canceledValue = null;

    public AlternateRowRenderer() {
        // default = no status awareness (generic striped renderer)
    }

    public AlternateRowRenderer(int statusColumnIndex, String canceledValue) {
        this.statusColumnIndex = statusColumnIndex;
        this.canceledValue = canceledValue;
    }
    
    protected void setValue(Object value) {
        if (value == null) {
            setText("");          // empty cell
        }
        else if (value instanceof Seat s) {
            setText(s.getSeatNumber());   // nice display
        }
        else {
            setText(String.valueOf(value));   // ✅ safe toString()
        }
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int col) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, col);

        boolean isCanceledRow = false;

        if (statusColumnIndex != null && canceledValue != null) {
            Object status = table.getValueAt(row, statusColumnIndex);
            if (status != null && canceledValue.equalsIgnoreCase(status.toString())) {
                isCanceledRow = true;
            }
        }

        if (isCanceledRow) {
            c.setBackground(CANCELED_BG);
            c.setForeground(CANCELED_FG);
            return c;
        }

        if (!isSelected) {
            c.setBackground(row % 2 == 0 ? Color.WHITE : STRIPE);
            c.setForeground(Color.BLACK);
        }
        else {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        }

        return c;
    }
}