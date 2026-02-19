package boundary.utility;

import javax.swing.*;
import java.awt.*;

import control.RouteDAO;
import control.FlightDAO;
import entity.Route;

@SuppressWarnings("serial")
public class EditRouteDialog extends JDialog {

    private Route route;
    private boolean saved = false;

    private JTextField originField;
    private JTextField destinationField;
    private JTextField distanceField;
    private JComboBox<String> statusBox;

    public EditRouteDialog(Window parent, Route route) {
        super(parent, "Edit Route", ModalityType.APPLICATION_MODAL);
        this.route = route;

        setSize(420, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        buildUI();
    }

    @SuppressWarnings("unused")
	private void buildUI() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        originField = new JTextField(route.getOrigin());
        destinationField = new JTextField(route.getDestination());
        distanceField = new JTextField(String.valueOf(route.getDistance()));

        statusBox = new JComboBox<>(new String[]{"Active", "Inactive"});
        statusBox.setSelectedItem(route.getStatus());

        panel.add(new JLabel("Origin:"));
        panel.add(originField);

        panel.add(new JLabel("Destination:"));
        panel.add(destinationField);

        panel.add(new JLabel("Distance (km):"));
        panel.add(distanceField);

        panel.add(new JLabel("Status:"));
        panel.add(statusBox);

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
            String origin = originField.getText().trim();
            String destination = destinationField.getText().trim();
            String distanceStr = distanceField.getText().trim();
            String newStatus = (String) statusBox.getSelectedItem();

            // ==== VALIDATION ====

            if (origin.isEmpty() || destination.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Origin and Destination must not be empty.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (origin.equalsIgnoreCase(destination)) {
                JOptionPane.showMessageDialog(this,
                        "Origin and destination cannot be the same.",
                        "Invalid Route", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double distance;
            try {
                distance = Double.parseDouble(distanceStr);
                if (distance <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Distance must be a positive number.",
                    "Invalid Distance", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ==== UPDATE DB ====

            RouteDAO dao = new RouteDAO();
            dao.updateRoute(route.getRouteId(), origin, destination, distance, newStatus);

            // If inactive → cancel associated flights (business rule)
            if (newStatus.equals("Inactive")) {
                FlightDAO flightDAO = new FlightDAO();
                flightDAO.cancelFlightsByRoute(route.getRouteId());
            }

            saved = true;
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error updating route:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}