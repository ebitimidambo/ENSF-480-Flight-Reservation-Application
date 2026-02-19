package boundary.utility;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class PanelTable extends JPanel implements Scrollable{
	
	private Object[][] tableData;
	private String[] columns;
	private boolean[] ascModes;

    private static final String FONT = "Segoe UI Light";

    // Optional callback for Edit/Delete
    private RowActionListener listener;

    public PanelTable(String[] columns, Object[][] data) {
    	this.columns = columns;
        this.tableData = data;
        this.ascModes = new boolean[columns.length];  // false = DESC, true = ASC

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(TOP_ALIGNMENT);
        setBackground(new Color(45, 59, 73));
        setOpaque(true);
        setAutoscrolls(true);

        rebuildTable();
    }

    public void setRowActionListener(RowActionListener listener) {
        this.listener = listener;
    }

    // ================= HEADER =================
    private JPanel createHeader(String[] cols) {
        // +1 for the actions column (Edit/Delete)
    	JPanel header = new JPanel(new GridLayout(1, cols.length + 1));
        header.setBackground(new Color(240, 240, 240));
        header.setBorder(new EmptyBorder(5, 10, 5, 10));
        header.setMaximumSize(null);

        for (int i = 0; i < cols.length; i++) {
            final int colIndex = i;

            JLabel lbl = new JLabel(cols[i]);
            lbl.setFont(new Font(FONT, Font.BOLD, 14));
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    sortByColumn(colIndex);
                }
            });

            header.add(lbl);
        }

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        header.add(filler);

        return header;
    }

    // ================= ROW =================
    @SuppressWarnings("unused")
	private JPanel createRow(String[] cols, Object[] rowData, int rowNb) {
        JPanel row = new JPanel(new GridLayout(1, cols.length + 1));
        row.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        if (rowNb % 2 == 0) {
        	row.setBackground(Color.WHITE);
        } else {
        	row.setBackground(new Color(230, 242, 255));
        }
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        row.setPreferredSize(null);

        for (int c = 0; c < cols.length; c++) {
            JLabel lbl = new JLabel(rowData[c] != null ? rowData[c].toString() : "");
            lbl.setFont(new Font(FONT, Font.PLAIN, 13));
            lbl.setBorder(new EmptyBorder(5, 10, 5, 10));
            row.add(lbl);
        }

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
        actionPanel.setOpaque(false);

        actionPanel.setPreferredSize(new Dimension(160, 38));

        // Restore original style: Edit = gray, Delete = red, darker on hover
        JButton editBtn = createActionButton("Edit", new Color(200, 200, 200));
        JButton deleteBtn = createActionButton("Delete", new Color(45, 59, 73));
        deleteBtn.setForeground(Color.WHITE);

        editBtn.addActionListener(e -> {
            if (listener != null) listener.onEdit((int) rowData[0]);
        });

        deleteBtn.addActionListener(e -> {
            if (listener != null) listener.onDelete((int) rowData[0]);
        });

        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);

        row.add(actionPanel);

        return row;
    }

    // ============ UTIL: BUTTON WITH HOVER ============
    private JButton createActionButton(String text, Color baseColor) {
        JButton btn = new JButton(text);

        btn.setPreferredSize(new Dimension(70, 28));
        btn.setMinimumSize(new Dimension(70, 28));
        btn.setMaximumSize(new Dimension(70, 28));

        btn.setFocusPainted(false);
        btn.setBackground(baseColor);
        btn.setForeground(Color.BLACK);

        btn.setFont(new Font(FONT, Font.BOLD, 12));      
        btn.setBorder(new LineBorder(Color.DARK_GRAY, 1));

        btn.setOpaque(true);
        btn.setContentAreaFilled(true);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });

        return btn;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            super.getPreferredSize().width,
            getComponentCount() * 40
        );
    }
    
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 40;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    private void rebuildTable() {
        removeAll();

        add(createHeader(columns));

        for (int i = 0; i < tableData.length; i++) {
            add(createRow(columns, tableData[i], i));
        }

        revalidate();
        repaint();
    }
    
    private void sortByColumn(int col) {

        ascModes[col] = !ascModes[col]; // toggle sort direction

        java.util.Arrays.sort(tableData, (a, b) -> {
            Object o1 = a[col];
            Object o2 = b[col];

            if (o1 == null) return 1;
            if (o2 == null) return -1;

            // Numeric sort
            if (o1 instanceof Number && o2 instanceof Number) {
                return ascModes[col]
                    ? Double.compare(((Number)o1).doubleValue(), ((Number)o2).doubleValue())
                    : Double.compare(((Number)o2).doubleValue(), ((Number)o1).doubleValue());
            }

            // Date (yyyy-mm-dd or timestamp)
            try {
                java.time.LocalDate d1 = java.time.LocalDate.parse(o1.toString());
                java.time.LocalDate d2 = java.time.LocalDate.parse(o2.toString());
                return ascModes[col]
                    ? d1.compareTo(d2)
                    : d2.compareTo(d1);
            } catch (Exception ignored) {}

            // Fallback to string sort
            return ascModes[col]
                ? o1.toString().compareToIgnoreCase(o2.toString())
                : o2.toString().compareToIgnoreCase(o1.toString());
        });

        rebuildTable();
    }
}