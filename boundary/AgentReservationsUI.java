package boundary;

import boundary.utility.AlternateRowRenderer;

import boundary.utility.CustomerUIBanner;
import boundary.utility.CustomerUIFooter;
import boundary.utility.DateTimePickerDialog;
import boundary.utility.FlightAgentGuiFactory;
import boundary.utility.Header;
import boundary.utility.SeatMapPanel;
import control.FlightAgentController;
import entity.Flight;
import entity.Reservation;
import entity.Seat;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class AgentReservationsUI extends JPanel {

	private static final String FONT = "Segoe UI Light";

    private User user;
    private FlightAgentGuiFactory factory;
    private Header header;
    private FlightAgentController controller;

    private JTable table;
    private List<Reservation> currentReservations = new ArrayList<Reservation>();
    private Map<Integer, Reservation> originalState = new HashMap<Integer, Reservation>();

    public AgentReservationsUI(User user) {
        this.user = user;
        this.factory = new FlightAgentGuiFactory(user);
        this.header = factory.createHeader();
        this.controller = new FlightAgentController();
        setupUI();
        loadReservations();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(CustomerUIBanner.createBanner(user), BorderLayout.NORTH);
        topPanel.add(header.createHeader(), BorderLayout.SOUTH);
        topPanel.setBackground(new Color(45, 59, 73));
        add(topPanel, BorderLayout.NORTH);

        add(createReservationsPanel(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(CustomerUIFooter.createFooter(), BorderLayout.SOUTH);
        bottomPanel.setBackground(new Color(45, 59, 73));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel title = new JLabel("All Reservations", SwingConstants.LEFT);
        title.setFont(new Font(FONT, Font.BOLD, 20));
        title.setForeground(new Color(45, 59, 73));

        String[] columns = {
        	    "Booker",
        	    "Passenger First Name",
        	    "Passenger Last Name",
        	    "Date of Birth",
        	    "Flight Info",
        	    "Seat Number",
        	    "Status",
        	    "Date Booked"
        	};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                String status = (String) getValueAt(row, 6);
                return (!"Canceled".equalsIgnoreCase(status) && (col == 1 || col == 2));
            }
        };

        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new AlternateRowRenderer(6, "Canceled"));
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	if (e.getClickCount() == 2) {   // double-click only
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (row < 0 || col < 0) return;
                    
                    if (!"Canceled".equalsIgnoreCase((String)table.getValueAt(row, 6))) {
                    	if (col == 3) editDOB(row);      // DOB
                    	if (col == 5) editSeat(row);     // Seat
                    	if (col == 6) editStatus(row);	// Status
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);

        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(new Font(FONT, Font.BOLD, 13));
        saveButton.setBackground(new Color(45, 59, 73));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(this::handleSaveChanges);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(saveButton);
        bottom.setOpaque(false);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void loadReservations() {

        currentReservations = controller.getAllReservations();
        originalState.clear();	
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Reservation r : currentReservations) {

            User u = r.getUser();
            Flight f = r.getIsReturn() == false? r.getDepartureFlight() : r.getReturnFlight();

            String booker = (u != null) ? u.getFirstName() + " " + u.getLastName() : "";

            String passengerFirst = r.getFirstName();
            String passengerLast = r.getLastName();
            String dob = r.getDateOfBirth();

            String departureTime = "";
            if (f.getDepartureTime() != null) {
                departureTime = f.getDepartureTime().format(fmt);
            }
            
            String flightInfo = (f != null)
                    ? f.getFlightName() + " (" + departureTime + ")"
                    : "";
            
            Seat seat = r.getIsReturn() == false ? r.getDepartureSeat() : r.getReturnSeat();
            
            String status = r.getStatus();

            String dateBooked = "—";
            if (r.getDateBooked() != null) {
                dateBooked = r.getDateBooked().format(fmt);
            };

            model.addRow(new Object[]{
                    booker,
                    passengerFirst,
                    passengerLast,
                    dob,
                    flightInfo,
                    seat,
                    status,
                    dateBooked
            });
        }
    }
    
    private void editDOB(int row) {
        Window parent = SwingUtilities.getWindowAncestor(this);

        DateTimePickerDialog picker =
            new DateTimePickerDialog(parent, "Select Date of Birth", true);

        picker.setVisible(true);
        String dob = picker.getSelectedDateTime();

        if (dob != null) {
            table.setValueAt(dob, row, 3);
        }
    }
    
    private void editSeat(int row) {

    	Reservation r = currentReservations.get(row);
        Flight f = r.getIsReturn() == false? r.getDepartureFlight() : r.getReturnFlight();
        Seat current = r.getIsReturn() == false ? r.getDepartureSeat() : r.getReturnSeat();
        
        ArrayList<Seat> seats;
        Set<Integer> used;

        if (f == null) {
            JOptionPane.showMessageDialog(this, "No flight found for this reservation.");
            return;
        }

		seats = controller.getSeatsForFlight(f.getFlightId());
		used  = controller.getReservedSeatIdsForFlight(f.getFlightId());
		if (current != null) used.remove(current.getSeatId());

        JLabel status = new JLabel("Select seat");
        if (current != null)
            status.setText("Current seat: " + current.getSeatNumber());

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Seat Selection", Dialog.ModalityType.APPLICATION_MODAL
        );

        SeatMapPanel panel = new SeatMapPanel(seats, status, dialog::dispose, used, current);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(status, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        Seat newSeat = panel.getSelectedSeat();
        if (newSeat == null) return;

        table.setValueAt(newSeat, row, 5);
    }
    
    private void editStatus(int row) {
    	Reservation r = currentReservations.get(row);

        JComboBox<String> statusBox =
            new JComboBox<>(new String[]{"Pending", "Approved", "Canceled"});

        statusBox.setSelectedItem(r.getStatus());

        int result = JOptionPane.showConfirmDialog(
                this,
                statusBox,
                "Change Reservation Status",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String newStatus = (String) statusBox.getSelectedItem();

        if (newStatus.equals(r.getStatus())) return;
        
        table.setValueAt(newStatus, row, 6);   
    }
    
    
    private boolean isValidName(String name) {
        if (name == null) {
            return false;
        }

        name = name.trim();

        boolean matches = name.matches("[A-Za-z][A-Za-z .'-]{1,39}");
        
        return matches;
    }
    
    private boolean isValidDOB(String dob) {
    	if (dob == null || dob.trim().isEmpty()) return false;

        dob = dob.trim();
    	
    	DateTimeFormatter[] formats = {
    	        DateTimeFormatter.ISO_LOCAL_DATE,           // yyyy-MM-dd (from picker)
    	        DateTimeFormatter.ofPattern("dd-MM-yyyy")   // database compatibility
    	    };

    	    LocalDate date = null;

    	    for (DateTimeFormatter format : formats) {
    	        try {
    	            date = LocalDate.parse(dob, format);
    	            break; // format matched
    	        } catch (Exception ignored) {}
    	    }

    	    if (date == null) return false;
    	    
    	    LocalDate today = LocalDate.now();
    	    LocalDate oldest = today.minusYears(120);

    	    return !date.isAfter(today) &&
    	           !date.isBefore(oldest);
    }
    
    private void handleSaveChanges(ActionEvent e) {
    	if (table.isEditing()) {
    	    table.getCellEditor().stopCellEditing();
    	}
    	
    	boolean anyChange = false;
    	
        for (int row = 0; row < table.getRowCount(); row++) {

            Reservation r = currentReservations.get(row);

            String first = (String) table.getValueAt(row, 1);
            String last  = (String) table.getValueAt(row, 2);
            String dob   = (String) table.getValueAt(row, 3);
            Seat newSeat = (Seat) table.getValueAt(row, 5);
            String status = (String) table.getValueAt(row,6);
            
            if ("Canceled".equalsIgnoreCase(r.getStatus())) continue;
            
            
            Seat oldSeat = r.getIsReturn() ? r.getReturnSeat() : r.getDepartureSeat();

            boolean seatChanged = oldSeat == null
                    ? newSeat != null
                    : oldSeat.getSeatId() != newSeat.getSeatId();       
                        
           boolean changed =
            	    !first.equals(r.getFirstName()) ||
            	    !last.equals(r.getLastName()) ||
            	    !dob.equals(r.getDateOfBirth()) ||
            	    !status.equals(r.getStatus()) ||
            	    seatChanged;

            if (!changed) continue;
            
            anyChange = true;

            // Validate before saving
            if (!isValidName(first) || !isValidName(last)) {
                JOptionPane.showMessageDialog(this,
                        "Invalid name at row " + (row + 1));
                return;
            }

            if (!isValidDOB(dob)) {
                JOptionPane.showMessageDialog(this,
                        "Invalid DOB at row " + (row + 1));
                return;
            }
            
            
            try {
            	if ("Canceled".equalsIgnoreCase(status)){
            		controller.cancelReservation(r);
            	}   	
            	controller.updateReservationFull(r.getReservationId(), first, last, dob, status);

            	if (seatChanged) {
            	    controller.updateSeatForReservation(r, newSeat);
            	}

                // Update local object
                r.setFirstName(first);
                r.setLastName(last);
                r.setDateOfBirth(dob);
                r.setStatus(status);

                if (r.getIsReturn()) r.setReturnSeat(newSeat);
                else r.setDepartureSeat(newSeat);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed saving row " + (row + 1));
                ex.printStackTrace();
                return;
            }
        }
        
        if (!anyChange) {
            JOptionPane.showMessageDialog(this,
                "No changes detected.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Reservation passenger details updated.");
        loadReservations();
    }

}