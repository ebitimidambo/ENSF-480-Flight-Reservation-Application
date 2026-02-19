package boundary.utility;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.JPanel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import control.ReservationController;
import entity.Reservation;
import entity.User;

public class CustomerReservationsBody implements Body {
	private User user;
	private static final String FONT = "Segoe UI Light";

	public CustomerReservationsBody(User user) {
		ReservationController controller = new ReservationController();
		controller.loadUserReservations(user);
		this.user = user;
	}

	@Override
	public JPanel createBody() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setOpaque(true);

		ArrayList<Reservation> reservations = user.getReservations();
		
	    if (reservations == null || reservations.isEmpty()) {
	        JPanel emptyPanel = new JPanel(new BorderLayout());
	        emptyPanel.setOpaque(false);

	        JLabel emptyLabel = new JLabel("You have no reservations yet.");
	        emptyLabel.setFont(new Font(FONT, Font.PLAIN, 18));
	        emptyLabel.setForeground(new Color(80, 80, 80));
	        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        emptyLabel.setVerticalAlignment(SwingConstants.CENTER);

	        emptyPanel.add(emptyLabel, BorderLayout.CENTER);

	        mainPanel.add(emptyPanel, BorderLayout.CENTER);
	        mainPanel.add(Box.createVerticalStrut(300), BorderLayout.SOUTH);
	        return mainPanel;
	    }

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		listPanel.setOpaque(false);

		for (Reservation res : reservations) {
			JPanel card = createBoardingPassPanel(res);
			listPanel.add(card);
			listPanel.add(Box.createVerticalStrut(20));
		}

		listPanel.add(Box.createVerticalGlue());
		listPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		listPanel.setMaximumSize(new Dimension(750, listPanel.getPreferredSize().height));

		JScrollPane scrollPane = new JScrollPane(listPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		scrollPane.setPreferredSize(new Dimension(800, 500)); // set viewport height limit
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		mainPanel.add(scrollPane, BorderLayout.CENTER);
		return mainPanel;
	}

	@SuppressWarnings("unused")
	private JPanel createBoardingPassPanel(Reservation res) {
		ImageIcon backgroundIcon = new ImageIcon("images/boarding_pass.png");
		Image backgroundImage = backgroundIcon.getImage();

		JPanel card = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(700, 200);
			}
		};

		card.setLayout(new GridBagLayout());
		card.setOpaque(false);
		card.setMaximumSize(new Dimension(700, 200)); // prevent stretching
		card.setAlignmentX(Component.CENTER_ALIGNMENT);

		// === Text & button overlay (right side) ===
		JPanel overlay = new JPanel();
		overlay.setOpaque(false);
		overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));
		overlay.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 30)); // move text right

		JLabel flight;
		JLabel seat;

		JLabel name = makeLabel("Passenger: " + res.getFirstName() + " " + res.getLastName());
		if (res.getIsReturn() == false) {
			flight = makeLabel("Flight: " + res.getDepartureFlight().getFlightName());
			seat = makeLabel("Seat: " + res.getDepartureSeat().getSeatNumber());
		} else {
			flight = makeLabel("Flight: " + res.getReturnFlight().getFlightName());
			seat = makeLabel("Seat: " + res.getReturnSeat().getSeatNumber());
		}
		JLabel status = makeLabel("Status: " + res.getStatus());

		overlay.add(name);
		overlay.add(Box.createVerticalStrut(6));
		overlay.add(flight);
		overlay.add(Box.createVerticalStrut(6));
		overlay.add(seat);
		overlay.add(Box.createVerticalStrut(6));
		overlay.add(status);
		overlay.add(Box.createVerticalStrut(15));

		// === Cancel button ===
		JButton cancelButton = new JButton("Cancel Booking");
		cancelButton.setFont(new Font(FONT, Font.PLAIN, 14));
		cancelButton.setFocusPainted(false);
		cancelButton.setBackground(new Color(200, 40, 55));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBorder(BorderFactory.createEmptyBorder(2, 12, 6, 12));
		cancelButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		cancelButton.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this booking?",
					"Confirm Cancellation", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				ReservationController controller = new ReservationController();
				boolean success = controller.cancelReservation(res);
				if (success) {
					res.setStatus("Canceled");
					status.setText("Status: Canceled");
					status.setForeground(Color.RED);
					cancelButton.setVisible(false);
					JOptionPane.showMessageDialog(null, "Booking canceled successfully!");

					Container parent = card.getParent();
					if (parent != null) {
						parent.remove(card);
						parent.revalidate();
						parent.repaint();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Error canceling booking.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		overlay.add(cancelButton);

		JButton generatorButton = new JButton("Booking Confirmation");
		generatorButton.setFont(new Font(FONT, Font.PLAIN, 12));
		generatorButton.setFocusPainted(false);
		generatorButton.setBackground(new Color(45, 59, 73));
		generatorButton.setForeground(Color.WHITE);
		generatorButton.setBorder(BorderFactory.createEmptyBorder(2, 12, 6, 12));
		generatorButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		generatorButton.addActionListener(e -> {
			int notification = JOptionPane.showConfirmDialog(null, "Booking confirmation document has been generated",
					"Document Generated", JOptionPane.OK_OPTION);
			if (notification == JOptionPane.OK_OPTION) {
				try {
					String fileName = "BookingConfirmation_" + res.getFirstName() + "_" + res.getReservationId() + ".txt";
					File file = new File(fileName);
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));

					writer.write("Booking Confirmation\n");
					writer.write("====================\n\n");

					writer.write("Passenger: " + res.getFirstName() + " " + res.getLastName() + "\n");
					if (res.getIsReturn() == false) {
						writer.write("Flight: " + res.getDepartureFlight().getFlightName() + "\n");
						writer.write("Seat: " + res.getDepartureSeat().getSeatNumber() + "\n");
						writer.write("Status: " + res.getStatus() + "\n");
						writer.write("Departure: "
								+ res.getDepartureFlight().getDepartureTime().toLocalDate().toString() + "\n\n");
						writer.write("Route: " + res.getDepartureFlight().getRoute().getOrigin() + " → "
								+ res.getDepartureFlight().getRoute().getDestination() + "\n");
						writer.write("Aircraft: " + res.getDepartureFlight().getModel().getAircraftModel() + "\n");
					} else {
						writer.write("Flight: " + res.getReturnFlight().getFlightName() + "\n");
						writer.write("Seat: " + res.getReturnSeat().getSeatNumber() + "\n");
						writer.write("Status: " + res.getStatus() + "\n");
						writer.write("Return: " + res.getReturnFlight().getDepartureTime().toLocalDate().toString()
								+ "\n\n");
						writer.write("Route: " + res.getReturnFlight().getRoute().getOrigin() + " → "
								+ res.getReturnFlight().getRoute().getDestination() + "\n");
						writer.write("Aircraft: " + res.getReturnFlight().getModel().getAircraftModel() + "\n");
					}

					// Close the writer
					writer.close();

					JOptionPane.showMessageDialog(null,
							"Booking confirmation text file has been saved to: " + file.getAbsolutePath());
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Error generating text file: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JPanel rightPanel = new JPanel();
		rightPanel.setOpaque(false);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 40));
		JLabel origin;
		JLabel destination;
		JLabel model;
		JLabel departure;

		if (res.getIsReturn() == false) {
			origin = makeLabel("Origin: " + res.getDepartureFlight().getRoute().getOrigin());
			origin.setFont(new Font(FONT, Font.PLAIN, 14));
			destination = makeLabel("Destination: " + res.getDepartureFlight().getRoute().getDestination());
			destination.setFont(new Font(FONT, Font.PLAIN, 14));
			model = makeLabel("Aircraft: " + res.getDepartureFlight().getModel().getAircraftModel());
			model.setFont(new Font(FONT, Font.PLAIN, 14));
			departure = makeLabel("Departure: " + res.getDepartureFlight().getDepartureTime().toLocalDate().toString());
			departure.setFont(new Font(FONT, Font.PLAIN, 14));
		} else {
			origin = makeLabel("Origin: " + res.getReturnFlight().getRoute().getOrigin());
			origin.setFont(new Font(FONT, Font.PLAIN, 14));
			destination = makeLabel("Destiantion: " + res.getReturnFlight().getRoute().getDestination());
			destination.setFont(new Font(FONT, Font.PLAIN, 14));
			model = makeLabel("Aircraft: " + res.getReturnFlight().getModel().getAircraftModel());
			model.setFont(new Font(FONT, Font.PLAIN, 14));
			departure = makeLabel("Departure: " + res.getReturnFlight().getDepartureTime().toLocalDate().toString());
			departure.setFont(new Font(FONT, Font.PLAIN, 14));
		}

		rightPanel.add(origin);
		rightPanel.add(Box.createVerticalStrut(8));
		rightPanel.add(destination);
		rightPanel.add(Box.createVerticalStrut(8));
		rightPanel.add(model);
		rightPanel.add(Box.createVerticalStrut(8));
		rightPanel.add(departure);
		rightPanel.add(Box.createVerticalStrut(10));
		rightPanel.add(generatorButton);

		rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		// === Place overlay using GridBag ===
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		gbc.gridx = 0;
		gbc.weightx = 0.5;
		card.add(overlay, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		card.add(rightPanel, gbc);

		return card;
	}

	private JLabel makeLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(FONT, Font.PLAIN, 16));
		label.setForeground(Color.BLACK);
		return label;
	}
}
