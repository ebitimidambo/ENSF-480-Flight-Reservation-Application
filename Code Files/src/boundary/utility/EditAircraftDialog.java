package boundary.utility;

import javax.swing.*;
import java.awt.*;

import control.AircraftDAO;
import entity.Aircraft;

@SuppressWarnings("serial")
public class EditAircraftDialog extends JDialog {
	private Aircraft aircraft;
    private boolean saved = false;

    private JTextField modelField;
    private JTextField capacityField;
    private JComboBox<String> haulTypeBox;

    public EditAircraftDialog(Window parent, Aircraft aircraft) {
        super(parent, "Edit Aircraft", ModalityType.APPLICATION_MODAL);
        this.aircraft = aircraft;

        setSize(420, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        buildUI();
    }

    @SuppressWarnings("unused")
	private void buildUI() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        modelField = new JTextField(aircraft.getAircraftModel());
        capacityField = new JTextField(String.valueOf(aircraft.getCapacity()));
        
        haulTypeBox = new JComboBox<>(new String[]{"Short-haul", "Medium-haul", "Long-haul",
        		"Ultra-long-haul", "Regional"});
        haulTypeBox.setSelectedItem(aircraft.getHaulType());

        panel.add(new JLabel("Model:"));
        panel.add(modelField);

        panel.add(new JLabel("Capacity:"));
        panel.add(capacityField);

        panel.add(new JLabel("Haul Type:"));
        panel.add(haulTypeBox);

        add(panel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> applyChanges());
        cancel.addActionListener(e -> dispose());

        bottom.add(save);
        bottom.add(cancel);

        add(bottom, BorderLayout.SOUTH);
    }

    private void applyChanges() {
    	try {
    		String model = modelField.getText().trim();
    		String capacityStr = capacityField.getText().trim();
    		String newHaulType = (String) haulTypeBox.getSelectedItem();

            // ==== VALIDATION ====

            if (model.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Model and Haul Type must not be empty.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacityStr);
                if (capacity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Capacity must be a positive number.",
                    "Invalid Distance", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ==== UPDATE DB ====

            AircraftDAO dao = new AircraftDAO();
            dao.updateAircraft(aircraft.getAircraftId(), model, capacity, newHaulType);

            saved = true;
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error updating aircraft:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}