package boundary.utility;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import control.AircraftDAO;
import control.FlightController;
import control.RouteController;
import entity.Aircraft;
import entity.Flight;
import entity.Route;

@SuppressWarnings("serial")
public class ManageFlightsPage extends JPanel {
	private FlightController controller;
	private RouteController routeController;

    public ManageFlightsPage() {
        setLayout(new BorderLayout());
        setBackground(new java.awt.Color(45, 59, 73));
        this.controller = new FlightController();
        this.routeController = new RouteController();
        buildContent();
    }

    @SuppressWarnings("unused")
	private void buildContent() {
        String[] columns = {
            "ID",
            "Flight Name",
            "Origin",
            "Destination",
            "Aircraft",
            "Departure",
            "Arrival",
            "Status"
        };

        List<Flight> flights = controller.getAllFlightsForAdmin();

        Object[][] data = new Object[flights.size()][columns.length];

        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            Route r = f.getRoute();  // Flight has Route field
            Aircraft a = f.getModel();

            data[i][0] = f.getFlightId();
            data[i][1] = f.getFlightName();
            data[i][2] = (r != null ? r.getOrigin() : "N/A");
            data[i][3] = (r != null ? r.getDestination() : "N/A");
            data[i][4] = (a != null ? a.getAircraftModel() : "N/A");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            data[i][5] = (f.getDepartureTime() != null) ? dtf.format(f.getDepartureTime()) : "";
            data[i][6] = (f.getArrivalTime() != null) ? dtf.format(f.getArrivalTime()) : "";
            data[i][7] = f.getStatus();
        }

        PanelTable table = new PanelTable(columns, data);

        table.setRowActionListener(new RowActionListener() {
            @Override
            public void onEdit(int id) {
                Flight flight = controller.getFlightById(id);

                EditFlightDialog dialog = new EditFlightDialog(
                    SwingUtilities.getWindowAncestor(ManageFlightsPage.this),
                    flight
                );

                dialog.setVisible(true);

                if (dialog.isSaved()) {
                    refresh();
                }
            }

            @Override
            public void onDelete(int id) {
                int confirm = JOptionPane.showConfirmDialog(
                    ManageFlightsPage.this,
                    "Delete flight " + id + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteFlight(id);
                    refresh();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(45, 59, 73));
        scrollPane.setBackground(new Color(45, 59, 73));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = createAddButton("Add Flight");
        addButton.addActionListener(e -> handleAddFlight());
        bottomPanel.add(addButton);
        bottomPanel.setBackground(new Color(45, 59, 73));
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createAddButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setMinimumSize(new Dimension(150, 40));
        btn.setMaximumSize(new Dimension(150, 40));
        btn.setBackground(new java.awt.Color(120, 200, 120)); // green
        btn.setForeground(java.awt.Color.BLACK);
        btn.setFont(new java.awt.Font("Segoe UI Light", java.awt.Font.BOLD, 12));
        btn.setBorder(new javax.swing.border.LineBorder(java.awt.Color.DARK_GRAY, 1));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new java.awt.Color(90, 170, 90));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new java.awt.Color(120, 200, 120));
            }
        });
        return btn;
    }

    private void handleAddFlight() {
        String flightName = JOptionPane.showInputDialog(
            this,
            "Enter flight name (e.g., AC101):",
            "Add Flight",
            JOptionPane.QUESTION_MESSAGE
        );
        if (flightName == null || flightName.isBlank()) {
            return;
        }


        java.util.List<Route> routes = routeController.getAllRoutesForAdmin();
        if (routes.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No routes available. Please create a route first.",
                "No Routes",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        //ROUTE OPTIONS
        String[] routeOptions = new String[routes.size()];
        int[] routeIds = new int[routes.size()];
        List<String> names = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        for (Route r : routes) {
            if ("Active".equalsIgnoreCase(r.getStatus())) {
                ids.add(r.getRouteId());
                names.add(r.getRouteId() + " - " + r.getOrigin() + " → " + r.getDestination());
            }
        }

        routeIds = ids.stream().mapToInt(i -> i).toArray();
        routeOptions = names.toArray(new String[0]);

        Object selected = JOptionPane.showInputDialog(
            this,
            "Select route:",
            "Add Flight",
            JOptionPane.QUESTION_MESSAGE,
            null,
            routeOptions,
            routeOptions[0]
        );
        if (selected == null) {
            return;
        }

        int chosenIndex = -1;
        for (int i = 0; i < routeOptions.length; i++) {
            if (routeOptions[i].equals(selected)) {
                chosenIndex = i;
                break;
            }
        }
        
        if (chosenIndex < 0) return;
        int routeId = routeIds[chosenIndex];
        
        //AIRCRAFT OPTIONS
        AircraftDAO aDAO= new AircraftDAO();
        java.util.List<Aircraft> aircrafts = aDAO.getAllAircrafts();
        if (aircrafts.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No aircraft available. Please register an aircraft first.",
                "No Aircraft",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        
        List<String> namesAC = new ArrayList<>();
        List<Integer> idsAC = new ArrayList<>();

        for (Aircraft a: aircrafts) {
            idsAC.add(a.getAircraftId());
            namesAC.add(a.getAircraftId() + " - " + a.getAircraftModel());
        }

        int[] aircraftIds = idsAC.stream().mapToInt(i -> i).toArray();
        String[] aircraftOptions = namesAC.toArray(new String[0]);

        Object selectedAC = JOptionPane.showInputDialog(
            this,
            "Select Aircraft:",
            "Add Flight",
            JOptionPane.QUESTION_MESSAGE,
            null,
            aircraftOptions,
            aircraftOptions[0]
        );
        
        if (selectedAC == null) {
            return;
        }

        int chosenIndexAC = -1;
        for (int i = 0; i < aircraftOptions.length; i++) {
            if (aircraftOptions[i].equals(selectedAC)) {
                chosenIndexAC = i;
                break;
            }
        }
        if (chosenIndexAC < 0) return;
        int aircraftId = aircraftIds[chosenIndexAC];

        DateTimePickerDialog depPicker = new DateTimePickerDialog(
        	    SwingUtilities.getWindowAncestor(this),
        	    "Select Departure Time");
        depPicker.setVisible(true);
        	String departure = depPicker.getSelectedDateTime();
        	if (departure == null) return;
        	
        DateTimePickerDialog arrPicker = new DateTimePickerDialog(
        		SwingUtilities.getWindowAncestor(this),
        		"Select Arrival Time"
        		);
        arrPicker.setVisible(true);
        String arrival = arrPicker.getSelectedDateTime();
        if (arrival == null) return;

        try {
            
            controller.addFlight(flightName, routeId, aircraftId, departure, arrival);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error adding flight: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refresh() {
        removeAll();
        buildContent();
        revalidate();
        repaint();
    }
}