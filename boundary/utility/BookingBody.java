package boundary.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.Frame;

import boundary.BookingUI;
import boundary.PaymentUI;
import boundary.MainFrame;
import entity.Flight;
import entity.Reservation;
import entity.Seat;
import entity.User;

public class BookingBody implements Body {
	private static final String FONT = "Segoe UI Light";
	private User user;
	private Map<String, Object> prompts;
	private Flight departureFlight;
	private Flight arrivalFlight;
	private boolean isReturnStep;
	private List<Flight> listOfDepartureFlights;
	private List<Flight> listOfArrivalFlights;
	private static final Set<Integer> selectedSeatIds = new HashSet<>();
	private final Map<Integer, Reservation> reservations = new HashMap<>();

	public BookingBody(User user, Map<String, Object> prompts, List<Flight> departureFlights, 
			List<Flight> arrivalFlights, Flight departure, Flight arrival, boolean decision
			, Map<Integer, Reservation> existingReservations) {
		this.user = user;
		this.prompts = prompts;
		this.departureFlight = departure;
		this.arrivalFlight = arrival;
		this.isReturnStep = decision;
		this.listOfDepartureFlights = departureFlights;
		this.listOfArrivalFlights = arrivalFlights;
		if (existingReservations != null) {
			System.out.println("I put all");
			reservations.putAll(existingReservations);
			System.out.println("Now I am: " + reservations);
		}
	}

	public JPanel createBody() {
		JPanel bodyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 40));
		bodyPanel.setOpaque(false);

		JPanel leftContainer = createBookingForm("Passenger Information");
		leftContainer.setPreferredSize(new Dimension(600, 504));

		bodyPanel.add(leftContainer);

		return bodyPanel;
	}

	@SuppressWarnings("unused")
	private JPanel createBookingForm(String title) {
		JPanel containerPanel = new RoundedPanel(10);
		containerPanel.setLayout(new BorderLayout());
		containerPanel.setBackground(Color.WHITE);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(FONT, Font.BOLD, 16));
		titleLabel.setForeground(new Color(45, 59, 73));

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		headerPanel.setOpaque(false);
		headerPanel.add(titleLabel);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// ---------------------------------------------------------------------------
		JTextField firstName = makeTextField("Enter first Name");
		JTextField lastName = makeTextField("Enter last Name");
		JTextField phoneNumber = makeTextField("Enter phone number");
		JTextField email = makeTextField("Enter email");

		formPanel.add(firstName);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(lastName);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(phoneNumber);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(email);
		formPanel.add(Box.createVerticalStrut(20));

		int count = (int) prompts.get("Passengers");

		for (int i = 0; i < count; i++) {
			int passengerNumber = i + 1;
			Reservation reservation = reservations.get(passengerNumber);
		    if (reservation == null) {
		        reservation = new Reservation(user, departureFlight, "", "", "");
		        reservations.put(passengerNumber, reservation);
		    }
		    
			JPanel passsengerPanel = createPassengerPanel(passengerNumber, reservation);
			passsengerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			formPanel.add(passsengerPanel);
			formPanel.add(Box.createVerticalStrut(20));
		}

		// ---------------------------------------------------------------------------
		RoundedButton nextButton;
		
		if (prompts.get("Trip Type").equals("Round Trip")){
			nextButton = new RoundedButton(isReturnStep ? "Confirm Booking" : "Continue to Return Flight");
		} else {
			nextButton = new RoundedButton("Confirm Booking");
		}
        nextButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nextButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextButton.addActionListener(e -> handleNextStep(containerPanel));
        formPanel.add(Box.createVerticalStrut(30));
       

        formPanel.add(nextButton);
        
        
		JScrollPane scrollPane = new JScrollPane(formPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		scrollPane.setViewportBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);

		containerPanel.add(headerPanel, BorderLayout.NORTH);
		containerPanel.add(scrollPane, BorderLayout.CENTER);

		return containerPanel;
	}

	@SuppressWarnings("unused")
	private JPanel createPassengerPanel(int number, Reservation reservation) {
		JPanel bodyPanel = new RoundedPanel(10);
		bodyPanel.setLayout(new BorderLayout(20, 10));
		bodyPanel.setBackground(new Color(250, 250, 250));
		bodyPanel.setBorder(BorderFactory.createTitledBorder("Passenger " + number));
		bodyPanel.setMaximumSize(new Dimension(600, 160));

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTextField firstName = makeTextField("Enter first Name");
		firstName.getDocument().addDocumentListener((SimpleDocumentListener) () -> 
	    	reservation.setFirstName(firstName.getText())
		);
		JTextField lastName = makeTextField("Enter last Name");
		lastName.getDocument().addDocumentListener((SimpleDocumentListener) () -> 
    		reservation.setLastName(lastName.getText())
		);
		JTextField dateOfBirth = makeTextField("Enter date of birth");
		dateOfBirth.getDocument().addDocumentListener((SimpleDocumentListener) () -> 
			reservation.setDateOfBirth(dateOfBirth.getText())
		);
		
		if (isReturnStep) {
		    firstName.setText(reservation.getFirstName());
		    lastName.setText(reservation.getLastName());
		    dateOfBirth.setText(reservation.getDateOfBirth());

		    firstName.setEditable(false);
		    lastName.setEditable(false);
		    dateOfBirth.setEditable(false);

		    Color disabledColor = new Color(220, 220, 220);
		    firstName.setBackground(disabledColor);
		    lastName.setBackground(disabledColor);
		    dateOfBirth.setBackground(disabledColor);
		}

		formPanel.add(firstName);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(lastName);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(dateOfBirth);
		
		JPanel seatPanel = new JPanel();
		seatPanel.setLayout(new BoxLayout(seatPanel, BoxLayout.Y_AXIS));
		seatPanel.setOpaque(false);
		seatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel seatLabel = new JLabel("Seat: None");
        seatLabel.setFont(new Font(FONT, Font.BOLD, 13));
        seatLabel.setForeground(new Color(45, 59, 73));
        seatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton selectSeat = new RoundedButton("Select Seat");
        selectSeat.setPreferredSize(new Dimension(120, 30));
        selectSeat.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        selectSeat.addActionListener(e -> {
            JDialog seatDialog = new JDialog((Frame) null, "Select Seat", true);
            seatDialog.setSize(600, 400);
            seatDialog.setLocationRelativeTo(null);
            
            Seat preselected = isReturnStep
                    ? reservation.getReturnSeat()
                    : reservation.getDepartureSeat();
            
            SeatMapPanel seatMapPanel = new SeatMapPanel(
                (isReturnStep ? arrivalFlight.getSeats() : departureFlight.getSeats()), // or arrivalFlight depending on isReturnStep
                seatLabel,
                () -> seatDialog.dispose(),
                selectedSeatIds,
                preselected
            );

            seatDialog.add(seatMapPanel);
            seatDialog.setVisible(true);
            if (isReturnStep) {
            	reservation.setReturnSeat(seatMapPanel.getSelectedSeat());
            } else {
            	reservation.setDepartureSeat(seatMapPanel.getSelectedSeat());
            }
        });
        
        seatPanel.add(seatLabel);
        seatPanel.add(Box.createVerticalStrut(10));
        seatPanel.add(selectSeat);

		bodyPanel.add(formPanel, BorderLayout.CENTER);
		bodyPanel.add(seatPanel, BorderLayout.EAST);
		
		return bodyPanel;
	}
	
	private void handleNextStep(JPanel containerPanel) {
		for (Reservation res : reservations.values()) {
			String seatNumber;
			
			if (isReturnStep) {
				seatNumber = (res.getReturnSeat() != null) ? res.getReturnSeat().getSeatNumber() : "None";
			} else {
				seatNumber = (res.getDepartureSeat() != null) ? res.getDepartureSeat().getSeatNumber() : "None";
			}
			
			System.out.println(seatNumber);
			if (!validatePassengerForm(res.getFirstName(), res.getLastName(), res.getDateOfBirth(), seatNumber)) {
			    return;
			}
            res.setReturnFlight(arrivalFlight);
        }
		
        if (!isReturnStep && arrivalFlight != null) {
            MainFrame mainFrame = MainFrame.getInstance();
            for (Reservation res : reservations.values()) {
            	res.setIsReturn();
            }
            clearSeats();
            mainFrame.showPanel(new BookingUI(user, prompts,
                    listOfDepartureFlights, listOfArrivalFlights, null, arrivalFlight, true, reservations));
        } else {
        	System.out.println("Booking confirmed!");
            for (Map.Entry<Integer, Reservation> entry : reservations.entrySet()) {
                Reservation res = entry.getValue();
                System.out.println("Passenger " + entry.getKey() + ": " +
                    res.getFirstName() + " " + res.getLastName() +
                    " | Departure Seat: " + (res.getDepartureSeat() != null ? res.getDepartureSeat().getSeatNumber() : "None") +
                    " | Return Seat: " + (res.getReturnSeat() != null ? res.getReturnSeat().getSeatNumber() : "None"));
            }
            clearSeats();
            JOptionPane.showMessageDialog(null, "Booking confirmed!");
            MainFrame mainFrame = MainFrame.getInstance();
            mainFrame.showPanel(new PaymentUI(user, reservations, prompts, isReturnStep));
        }
    }

	private JTextField makeTextField(String placeholder) {
		JTextField field = new RoundedJTextField(1, placeholder);
		field.setFont(new Font(FONT, Font.PLAIN, 13));
		field.setForeground(new Color(45, 59, 73));
		field.setBackground(new Color(245, 245, 245));
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		field.setAlignmentX(Component.LEFT_ALIGNMENT);
		return field;
	}
	
	private boolean validatePassengerForm(String firstName, String lastName, String dateOfBirth, String seat) {
	    if (firstName.isBlank() || lastName.isBlank() || dateOfBirth.isBlank()) {
	        JOptionPane.showMessageDialog(null, "Please fill out all passenger fields.", "Missing Information", JOptionPane.WARNING_MESSAGE);
	        return false;
	    }
	    if (seat.equals("None")) {
	        JOptionPane.showMessageDialog(null, "Please select a seat before proceeding.", "Seat Required", JOptionPane.WARNING_MESSAGE);
	        return false;
	    }
	    return true;
	}
	
	public static void clearSeats() {
		selectedSeatIds.clear();
	}
}
