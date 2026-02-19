package boundary.utility;

import java.awt.*;
import javax.swing.*;
import java.time.LocalDate;
import java.util.Calendar;

@SuppressWarnings("serial")
public class DateTimePickerDialog extends JDialog {
	private JComboBox<Integer> yearBox;
    private JComboBox<Integer> monthBox;
    private JComboBox<Integer> dayBox;
    private JComboBox<Integer> hourBox;
    private JComboBox<Integer> minuteBox;

    private String result = null; // final formatted datetime
    private final boolean dateOnly;

    public DateTimePickerDialog(Window parent, String title, boolean dateOnly) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        this.dateOnly = dateOnly;
        initUI(parent);
    }
    
    public DateTimePickerDialog(Window parent, String title) {
        this(parent, title, false); // false = full date+time (current behavior)
    }
    @SuppressWarnings("unused")
	private void initUI(Window parent) {
    	if (dateOnly) {
    	    setTitle("Select Date of Birth");
    	}
    	
        setLayout(new GridLayout(3, 1, 10, 10));

        // PANEL 1 → Date dropdowns
        JPanel datePanel = new JPanel(new FlowLayout());

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        yearBox = new JComboBox<>();
        if (dateOnly) {
            // Date of birth: go far into the past
            for (int y = currentYear; y >= currentYear - 120; y--) {
                yearBox.addItem(y);
            }
        } else {
            // Admin/scheduling: near future only
            for (int y = currentYear; y <= currentYear + 5; y++) {
                yearBox.addItem(y);
            }
        }

        monthBox = new JComboBox<>();
        for (int m = 1; m <= 12; m++) monthBox.addItem(m);

        dayBox = new JComboBox<>();
        for (int d = 1; d <= 31; d++) dayBox.addItem(d);

        datePanel.add(new JLabel("Date:"));
        datePanel.add(yearBox);
        datePanel.add(monthBox);
        datePanel.add(dayBox);

        // PANEL 2 → Time dropdowns
        JPanel timePanel = new JPanel(new FlowLayout());

        hourBox = new JComboBox<>();
        for (int h = 0; h < 24; h++) hourBox.addItem(h);

        minuteBox = new JComboBox<>();
        for (int m = 0; m < 60; m += 5) minuteBox.addItem(m);

        timePanel.add(new JLabel("Time:"));
        timePanel.add(hourBox);
        timePanel.add(minuteBox);

        // If date-only → hide the time controls
        if (dateOnly) {
            timePanel.setVisible(false);
        }

        // PANEL 3 → OK and Cancel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(e -> {
            int y = (int) yearBox.getSelectedItem();
            int mo = (int) monthBox.getSelectedItem();
            int d = (int) dayBox.getSelectedItem();

            int h = (int) hourBox.getSelectedItem();
            int min = (int) minuteBox.getSelectedItem();

            try {
                LocalDate date = LocalDate.of(y, mo, d); // validates date

                if (dateOnly) {
                    // For DOB
                    result = String.format("%04d-%02d-%02d", y, mo, d);
                } else {
                    // Original behavior for admin: full datetime
                    result = String.format("%04d-%02d-%02d %02d:%02d:00", y, mo, d, h, min);
                }

                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date selected.");
            }
        });

        cancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        buttonPanel.add(ok);
        buttonPanel.add(cancel);

        add(datePanel);
        add(timePanel);
        add(buttonPanel);

        pack();
        setLocationRelativeTo(parent);
    }

    public String getSelectedDateTime() {
        return result;
    }
}