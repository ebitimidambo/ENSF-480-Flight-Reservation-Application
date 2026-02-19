package boundary.utility;

import javax.swing.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import control.RouteDAO;
import control.FlightDAO;
import control.AircraftDAO;
import control.FlightController;
import entity.Aircraft;
import entity.Flight;
import entity.Route;

@SuppressWarnings("serial")
public class EditFlightDialog extends JDialog {
    private Flight flight;
    private boolean saved = false;

    private JTextField nameField;
    private JComboBox<String> routeBox;
    private int[] routeIds;
    private java.util.List<Route> routeObjects;
    
    private JComboBox<String> aircraftBox;
    private int[] aircraftIds;
    private java.util.List<Aircraft> aircraftObjects;

    private JTextField departureField;
    private JTextField arrivalField;

    private JComboBox<String> statusBox;
    @SuppressWarnings("unused")
	private FlightController flightController;

    public EditFlightDialog(Window parent, Flight flight) {
        super(parent, "Edit Flight", ModalityType.APPLICATION_MODAL);
        this.flight = flight;

        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        buildUI();
    }

    @SuppressWarnings("unused")
	private void buildUI() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // === FLIGHT NAME ===
        nameField = new JTextField(flight.getFlightName());

        // === ROUTE SELECTOR (no editing route itself) ===
        RouteDAO routeDAO = new RouteDAO();
        routeObjects = routeDAO.getAllRoutesForAdmin();
        String[] options = new String[routeObjects.size()];
        routeIds = new int[routeObjects.size()];

        for (int i = 0; i < routeObjects.size(); i++) {
            Route r = routeObjects.get(i);
            routeIds[i] = r.getRouteId();
            options[i] = r.getRouteId() + " - " + r.getOrigin() + " → " + r.getDestination();
        }  
        
        routeBox = new JComboBox<>(options);

        // pre-select current route
        if (flight.getRoute() != null) {
            int currentRouteId = flight.getRoute().getRouteId();
            for (int i = 0; i < routeIds.length; i++) {
                if (routeIds[i] == currentRouteId) {
                    routeBox.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            // Flight has no route — select nothing by default
            routeBox.setSelectedIndex(-1);
        }
        
        // === AIRCRAFT SELECTOR (no editing aircraft itself) ===
        AircraftDAO aircraftDAO = new AircraftDAO();
        aircraftObjects = aircraftDAO.getAllAircrafts();
        String[] options2 = new String[aircraftObjects.size()];
        aircraftIds = new int[aircraftObjects.size()];

        for (int i = 0; i < aircraftObjects.size(); i++) {
            Aircraft a = aircraftObjects.get(i);
            aircraftIds[i] = a.getAircraftId();
            options2[i] = a.getAircraftId() + " - " + a.getAircraftModel();
        }
        
        aircraftBox = new JComboBox<>(options2);
        
        // pre-select current aircraft
        if (flight.getModel() != null) {
            int currentAircraftId = flight.getModel().getAircraftId();
            for (int i = 0; i < aircraftIds.length; i++) {
                if (aircraftIds[i] == currentAircraftId) {
                    aircraftBox.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            // Flight has no aircraft — select nothing by default
            aircraftBox.setSelectedIndex(-1);
        }

        // === DATE/TIME FIELDS ===
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        departureField = new JTextField(flight.getDepartureTime().format(fmt));
        arrivalField = new JTextField(flight.getArrivalTime().format(fmt));

        // === STATUS ===
        statusBox = new JComboBox<>(new String[]{"Scheduled", "Cancelled"});
        statusBox.setSelectedItem(flight.getStatus());

        panel.add(new JLabel("Flight Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Route:"));
        panel.add(routeBox);
        
        panel.add(new JLabel("Aircraft:"));
        panel.add(aircraftBox);

        panel.add(new JLabel("Departure Time:"));
        panel.add(departureField);

        panel.add(new JLabel("Arrival Time:"));
        panel.add(arrivalField);

        panel.add(new JLabel("Status:"));
        panel.add(statusBox);

        add(panel, BorderLayout.CENTER);

        // === BUTTONS ===
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
        	
        	if (routeBox.getItemCount() == 0) {
        	    JOptionPane.showMessageDialog(
        	        this,
        	        "No available routes exist. Cannot edit flight.",
        	        "No Routes",
        	        JOptionPane.WARNING_MESSAGE
        	    );
        	    return;
        	}
        	
            String newName = nameField.getText().trim();
            
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Flight name cannot be empty.",
                    "Invalid Name", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String departureStr = departureField.getText().trim();
            String arrivalStr = arrivalField.getText().trim();

            String newStatus = (String) statusBox.getSelectedItem();

            int routeIndex = routeBox.getSelectedIndex();
            int newRouteID = routeIds[routeIndex];
            Route selectedRoute = routeObjects.get(routeIndex);
            
            if (routeBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select a route.",
                    "No Route Selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int aircraftIndex = aircraftBox.getSelectedIndex();
            int newAircraftID = aircraftIds[aircraftIndex];
            
            if (aircraftBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select an aircraft.",
                    "No Aircraft Selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newStatus.equals("Scheduled") &&
                selectedRoute.getStatus().equalsIgnoreCase("Inactive")) {

                JOptionPane.showMessageDialog(
                    this,
                    "Cannot schedule a flight on an INACTIVE route.",
                    "Invalid Status",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // convert to LocalDateTime
            LocalDateTime dep = LocalDateTime.parse(departureStr.replace("T", " "), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime arr = LocalDateTime.parse(arrivalStr.replace("T", " "), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            if (arr.isBefore(dep)) {
                JOptionPane.showMessageDialog(this,
                    "Arrival time must be AFTER departure time.",
                    "Invalid Times", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update database
            FlightDAO dao = new FlightDAO();
            dao.updateFlight(
                flight.getFlightId(),
                newName,
                newRouteID,
                newAircraftID,
                dep,
                arr,
                newStatus
            );

            saved = true;
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Invalid data:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isSaved() {
        return saved;
    }
}