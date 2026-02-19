package boundary.utility;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import boundary.MainFrame;
import control.SeatsController;
import boundary.BookingUI;
import entity.Flight;
import entity.User;

public class FlightSearchBody implements Body, FlightSelectionListener {
	private static final String FONT = "Segoe UI Light";
	private User user;
	private Map<String, Object> searchPrompts;
	private List<Flight> departureFlights;
	private List<Flight> arrivalFlights;
	private Flight selectedDepartureFlight = null;
	private Flight selectedReturnFlight = null;
	private final Map<Flight, BookDecorator> decoratorMap = new HashMap<>();
	private SeatsController seatController;
	private static final int Time = 500;

	public FlightSearchBody(User user, Map<String, Object> prompts, List<Flight> departureFlights,
			List<Flight> arrivalFlights) {
		this.searchPrompts = prompts;
		this.departureFlights = departureFlights;
		this.arrivalFlights = arrivalFlights;
		this.user = user;
		this.seatController = new SeatsController();
	}

	public JPanel createBody() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(new Color(245, 245, 245));

		JPanel bodyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 40));
		bodyPanel.setOpaque(false);

		JPanel leftContainer = createFlightContainer("Departure Flights");

		leftContainer.setPreferredSize(new Dimension(600, 400));

		String tripType = (String) searchPrompts.get("Trip Type");
		if (tripType != null && tripType.equals("Round Trip")) {
			JPanel rightContainer = createFlightContainer("Return Flights");
			rightContainer.setPreferredSize(new Dimension(600, 400));
			bodyPanel.add(leftContainer);
			bodyPanel.add(rightContainer);

			if (departureFlights.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Warning: No Departure Flights For Information Provided",
						"Flights Not Found", JOptionPane.INFORMATION_MESSAGE);
			}

			if (arrivalFlights.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Warning: No Return Flights For Information Provided",
						"Flights Not Found", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			// One-way flight — only show departure container centered
			leftContainer.setPreferredSize(new Dimension(600, 400));
			bodyPanel.add(leftContainer);

			if (departureFlights.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Warning: No Departure Flights For Information Provided",
						"Flights Not Found", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		JPanel verticalTopSpacer = new JPanel(new BorderLayout());
		verticalTopSpacer.setPreferredSize(new Dimension(0, 80));
		verticalTopSpacer.setOpaque(false);

		JLabel titleLabel = new JLabel("Book A Flight");
		titleLabel.setFont(new Font(FONT, Font.BOLD, 16));
		titleLabel.setForeground(new Color(45, 59, 73));

		JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		titleWrapper.setOpaque(false);
		titleWrapper.add(titleLabel);

		verticalTopSpacer.add(titleWrapper, BorderLayout.SOUTH);

		JPanel verticalBottomSpacer = new JPanel();
		verticalBottomSpacer.setPreferredSize(new Dimension(0, 80));
		verticalBottomSpacer.setOpaque(false);

		wrapper.add(verticalTopSpacer, BorderLayout.NORTH);
		wrapper.add(bodyPanel, BorderLayout.CENTER);
		wrapper.add(verticalBottomSpacer, BorderLayout.SOUTH);

		return wrapper;
	}

	private JPanel createFlightContainer(String title) {
		JPanel containerPanel = new RoundedPanel(10);
		containerPanel.setLayout(new BorderLayout());
		containerPanel.setBackground(Color.WHITE);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(FONT, Font.BOLD, 16));
		titleLabel.setForeground(new Color(45, 59, 73));

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		headerPanel.setOpaque(false);
		headerPanel.add(titleLabel);

		JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
		gridPanel.setOpaque(false);
		gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		 if ((title.equals("Departure Flights") && departureFlights == null)
			        || (title.equals("Return Flights") && arrivalFlights == null)) {
			 return containerPanel;
		 }
		 
		if (title.equals("Departure Flights")) {
			ListIterator<Flight> iterator = departureFlights.listIterator();

			while (iterator.hasNext()) {
				Flight element = iterator.next();
				System.out.println(element.getStatus());
				boolean result = (element.getStatus().equalsIgnoreCase("Scheduled") ? false : true);
				String info = element.getRoute().getOrigin() + " -> " + element.getRoute().getDestination() + "<br>"
						+ "Flight: " + element.getFlightName() + "<br>" + "Base Price: $ " + element.getBasePrice()
						+ " CAD" + "<br>" + "Schedule: "
						+ element.getDepartureTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
						+ " " + element.getDepartureTime().toLocalTime();

				FlightBox box = new BasicFlightBox();
				box.assignFlight(element);
				ImageIcon logo = new ImageIcon("images/AEAELogo.png");
				box = new LogoDecorator(box, logo);
				box = new InfoDecorator(box, info);
				box = new StatusOverlayDecorator(box, result);
				box = new BookDecorator(box, element, true, this);

				gridPanel.add(box.createBox());
			}
		} else if (title.equals("Return Flights")) {
			ListIterator<Flight> iterator = arrivalFlights.listIterator();

			while (iterator.hasNext()) {
				Flight element = iterator.next();
				System.out.println(element.getStatus());
				boolean result = (element.getStatus().equalsIgnoreCase("Scheduled") ? false : true);
				String info = element.getRoute().getOrigin() + " -> " + element.getRoute().getDestination() + "<br>"
						+ "Flight: " + element.getFlightName() + "<br>" + "Base Price: $ " + element.getBasePrice()
						+ " CAD" + "<br>" + "Schedule: "
						+ element.getDepartureTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
						+ " " + element.getDepartureTime().toLocalTime();

				FlightBox box = new BasicFlightBox();
				ImageIcon logo = new ImageIcon("images/AEAELogo.png");
				box.assignFlight(element);
				box = new LogoDecorator(box, logo);
				box = new InfoDecorator(box, info);
				box = new StatusOverlayDecorator(box, result);
				box = new BookDecorator(box, element, false, this);

				gridPanel.add(box.createBox());
			}
		}

		JPanel gridWrapper = new JPanel(new BorderLayout());
		gridWrapper.setOpaque(false);

		if (gridPanel.getComponentCount() == 0) {
			JPanel emptyState = new JPanel(new GridBagLayout());
			emptyState.setOpaque(false);

			JLabel msg = new JLabel("<html><center><b>No flights found</b><br>"
					+ "Try different dates, airports, or trip type.</center></html>");
			msg.setFont(new Font(FONT, Font.ITALIC, 14));
			msg.setForeground(new Color(120, 120, 120));

			emptyState.add(msg);
			gridWrapper.add(emptyState, BorderLayout.CENTER);

			containerPanel.setBackground(new Color(235, 235, 235));
		} else {
			gridWrapper.add(gridPanel, BorderLayout.NORTH);
		}

		JScrollPane scrollPane = new JScrollPane(gridWrapper);
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

	public void setPrompts(Map<String, Object> prompts) {
		this.searchPrompts = prompts;
	}

	public void onFlightSelected(Flight flight, boolean isDeparture) {
		// 🔹 1. Block cancelled flights
		if ("Cancelled".equalsIgnoreCase(flight.getStatus())) {
			JOptionPane.showMessageDialog(null, "This flight is cancelled and cannot be booked.", "Booking Not Allowed",
					JOptionPane.WARNING_MESSAGE);
			BookDecorator cancelledDecorator = decoratorMap.get(flight);
			if (cancelledDecorator != null)
				cancelledDecorator.deselect();
			return;
		}

		// 🔹 2. Prevent multiple selections of the same category
		if (isDeparture && selectedDepartureFlight != null && !selectedDepartureFlight.equals(flight)) {
			JOptionPane.showMessageDialog(null,
					"You have already selected a departure flight.\n" + "Deselect it before choosing another.",
					"Selection Not Allowed", JOptionPane.INFORMATION_MESSAGE);
			BookDecorator decorator = decoratorMap.get(flight);
			if (decorator != null)
				decorator.deselect();
			return;
		}

		if (!isDeparture && selectedReturnFlight != null && !selectedReturnFlight.equals(flight)) {
			JOptionPane.showMessageDialog(null,
					"You have already selected a return flight.\n" + "Deselect it before choosing another.",
					"Selection Not Allowed", JOptionPane.INFORMATION_MESSAGE);
			BookDecorator decorator = decoratorMap.get(flight);
			if (decorator != null)
				decorator.deselect();
			return;
		}

		if (isDeparture) {
			selectedDepartureFlight = flight;
		} else {
			selectedReturnFlight = flight;
		}

		if (isRoundTrip() && selectedDepartureFlight != null && selectedReturnFlight != null) {
			int result = JOptionPane
					.showConfirmDialog(null,
							"Confirm booking for:\nDeparture: " + selectedDepartureFlight.getFlightName() + "\nReturn: "
									+ selectedReturnFlight.getFlightName(),
							"Confirm Booking", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				proceedToBooking();
			} else {
				if (selectedDepartureFlight != null) {
					BookDecorator depDecorator = decoratorMap.get(selectedDepartureFlight);
					if (depDecorator != null)
						depDecorator.deselect();
					selectedDepartureFlight = null;
				}
				if (selectedReturnFlight != null) {
					BookDecorator retDecorator = decoratorMap.get(selectedReturnFlight);
					if (retDecorator != null)
						retDecorator.deselect();
					selectedReturnFlight = null;
				}
			}
		} else if (!isRoundTrip()) {
			int result = JOptionPane.showConfirmDialog(null,
					"Confirm booking for flight " + flight.getFlightName() + "?", "Confirm Booking",
					JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				proceedToBooking();
			} else {
				BookDecorator decorator = decoratorMap.get(flight);
				if (decorator != null) {
					decorator.deselect();
				}
			}

		}
	}

	public void registerBookDecorator(Flight flight, BookDecorator decorator) {
		decoratorMap.put(flight, decorator);
	}

	public void onFlightDeselected(Flight flight, boolean isDeparture) {
		if (isDeparture)
			selectedDepartureFlight = null;
		else
			selectedReturnFlight = null;
	}

	private boolean isRoundTrip() {
		String tripType = (String) searchPrompts.get("Trip Type");
		return "Round Trip".equalsIgnoreCase(tripType);
	}

	private void proceedToBooking() {
		JOptionPane.showMessageDialog(null, "Booking confirmed! Redirecting...");
		Timer loadBookingUI = new Timer(Time, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (selectedDepartureFlight != null) {
						selectedDepartureFlight
								.setSeats(seatController.getSeatsForFlight(selectedDepartureFlight.getFlightId()));
						System.out.println(selectedDepartureFlight.getSeats());
					}
					if (selectedReturnFlight != null) {
						selectedReturnFlight.setSeats(seatController.getSeatsForFlight(selectedReturnFlight.getFlightId()));
						System.out.println(selectedReturnFlight.getSeats());
					}
				} catch (SQLException e) {

					e.printStackTrace();
				}
				MainFrame mainFrame = MainFrame.getInstance();
				mainFrame.showPanel(new BookingUI(user, searchPrompts, departureFlights, arrivalFlights,
						selectedDepartureFlight, selectedReturnFlight, false, null));
			}
		});

		loadBookingUI.setRepeats(false);
		loadBookingUI.start();
	}
}
