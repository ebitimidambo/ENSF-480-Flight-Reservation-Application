package boundary.utility;

import java.awt.*;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import boundary.FlightSearchUI;
import boundary.MainFrame;
import control.FlightController;
import control.RouteController;
import entity.Airline;
import entity.Flight;
import entity.User;

public class CustomerBody implements Body {
	private static final String FONT = "Segoe UI Light";
	private RouteController route;
	private FlightController flight;
	private User user;
	private Map<String, Object> searchPrompts = new HashMap<>();

	public CustomerBody(User user) {
		route = new RouteController();
		flight = new FlightController();
		flight.loadAllFlights();
		this.user = user;
	}

	@SuppressWarnings("unused")
	@Override
	public JPanel createBody() {
		JPanel wrapperPanel = new JPanel();
		wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
		wrapperPanel.setBackground(new Color(255, 255, 255));

		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
		bodyPanel.setBackground(new Color(255, 255, 255));

		JPanel searchPanel = new RoundedPanel(25);
		searchPanel.setLayout(new GridBagLayout());
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setPreferredSize(new Dimension(800, 240));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JComboBox<String> tripType = new RoundedJComboBox(new String[] { "One-way", "Round Trip" });
		JComboBox<String> from = new RoundedJComboBox(route.getOrigins());
		JComboBox<String> to = new RoundedJComboBox(route.getDestinations());

		JTextField dateField = new RoundedJTextField(1, "Departure Date (e.g 24-05-1992)");
		dateField.setFont(new Font(FONT, Font.PLAIN, 12));
		JTextField dateField2 = new RoundedJTextField(1, "Return Date (e.g 24-05-1992)");
		dateField2.setFont(new Font(FONT, Font.PLAIN, 12));
		JTextField passengerCount = new RoundedJTextField(1, "No. of Passengers");
		JComboBox<String> seatClass = new RoundedJComboBox(new String[] { "Economy", "Premium Economy", "Business" });
		JButton searchButton = new RoundedButton("Search Flights");
		JLabel statusLabel = new JLabel("");

		styleTextField(dateField);
		styleTextField(passengerCount);
		styleComboField(seatClass);
		styleComboField(tripType);
		styleComboField(from);
		styleComboField(to);
		styleButton(searchButton);

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Timer resetNotification = new Timer(1500, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						statusLabel.setText("");
						statusLabel.setForeground(new Color(45,59,73));
					}
				});
				
				Timer loadSearchUI = new Timer(500, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (!dateField.getText().equals("") && tripType.getSelectedItem().equals("Round Trip")) {
							if (DateValidator.validateDate(dateField.getText())
									&& DateValidator.validateDate(dateField2.getText())
									&& IntegerValidator.validateInteger(passengerCount.getText())) {
								searchPrompts.put("Trip Type", tripType.getSelectedItem().toString());
								searchPrompts.put("Origin", from.getSelectedItem().toString());
								searchPrompts.put("Destination", to.getSelectedItem().toString());
								searchPrompts.put("Departure Date", dateField.getText());
								searchPrompts.put("Return Date", dateField2.getText());
								searchPrompts.put("Passengers", Integer.parseInt(passengerCount.getText()));
								searchPrompts.put("Class", seatClass.getSelectedItem());
								
								List<Flight> departureflights = Airline.getInstance().searchFlights(
									    from.getSelectedItem().toString(),
									    to.getSelectedItem().toString(),
									    dateField.getText()
									);
								
								List<Flight> arrivalflights = Airline.getInstance().searchFlights(
									    to.getSelectedItem().toString(),
									    from.getSelectedItem().toString(),
									    dateField2.getText()
									);

								MainFrame mainFrame = MainFrame.getInstance();
								mainFrame.showPanel(new FlightSearchUI(user, searchPrompts, departureflights, arrivalflights));
								System.out.println(user.toString());
							} else {
								statusLabel.setText("Please enter valid information");
								statusLabel.setForeground(new Color(92, 1, 32));
								statusLabel.setVisible(true);
								resetNotification.setRepeats(false);
								resetNotification.start();
							}
						} else if (tripType.getSelectedItem().equals("One-way")) {
							if (!dateField.getText().equals("") && DateValidator.validateDate(dateField.getText())
									&& IntegerValidator.validateInteger(passengerCount.getText())) {
								searchPrompts.put("Trip Type", tripType.getSelectedItem().toString());
								searchPrompts.put("Origin", from.getSelectedItem().toString());
								searchPrompts.put("Destination", to.getSelectedItem().toString());
								searchPrompts.put("Departure Date", dateField.getText());
								searchPrompts.put("Passengers", Integer.parseInt(passengerCount.getText()));
								searchPrompts.put("Class", seatClass.getSelectedItem());
								
								List<Flight> flights = Airline.getInstance().searchFlights(
									    from.getSelectedItem().toString(),
									    to.getSelectedItem().toString(),
									    dateField.getText()
									);
		
								MainFrame mainFrame = MainFrame.getInstance();
								mainFrame.showPanel(new FlightSearchUI(user, searchPrompts, flights, null));
							} else {
								statusLabel.setText("Please enter valid information");
								statusLabel.setForeground(new Color(92, 1, 32));
								statusLabel.setVisible(true);
								resetNotification.setRepeats(false);
								resetNotification.start();
								
								if (DateValidator.validateDate(dateField.getText()) == false) {
									JOptionPane.showMessageDialog(null, 
											"The data you entered is invalid. Format: DD-MM-YYYY", 
											"Invalid Date", 
											JOptionPane.INFORMATION_MESSAGE);
								}
							}
						}
					}
				});

				loadSearchUI.setRepeats(false);
				loadSearchUI.start();
			}
		});

		seatClass.setPreferredSize(new Dimension(250, 40));
		seatClass.setMaximumSize(new Dimension(250, 40));
		seatClass.setMinimumSize(new Dimension(150, 40));

		gbc.gridy = 0;
		gbc.gridx = 0;
		searchPanel.add(tripType, gbc);
		gbc.gridx = 1;
		searchPanel.add(from, gbc);
		gbc.gridx = 2;
		searchPanel.add(to, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		searchPanel.add(dateField, gbc);
		JPanel returnFieldWrapper = new JPanel(new BorderLayout());
		returnFieldWrapper.setOpaque(false);
		returnFieldWrapper.setPreferredSize(new Dimension(250, 40)); // same size as your text fields
		returnFieldWrapper.add(dateField2, BorderLayout.CENTER);
		gbc.gridx = 1;
		searchPanel.add(returnFieldWrapper, gbc);
		gbc.gridx = 2;
		searchPanel.add(passengerCount, gbc);

		dateField2.setEnabled(false);

		tripType.addActionListener(e -> {
			boolean roundTrip = "Round Trip".equals(tripType.getSelectedItem());
			dateField2.setEnabled(roundTrip); // enable typing only for round trip

			// Optional: adjust background color for clarity
			if (roundTrip) {
				dateField2.setBackground(new Color(235, 235, 235)); // normal look
			} else {
				dateField2.setBackground(new Color(220, 220, 220)); // slight gray tint when disabled
			}

		});

		gbc.gridy = 2;
		gbc.gridx = 0;
		searchPanel.add(seatClass, gbc);
		gbc.gridx = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.add(searchButton);
		searchPanel.add(buttonPanel, gbc);

		bodyPanel.add(searchPanel);
		wrapperPanel.add(Box.createVerticalStrut(60)); // space from top
		wrapperPanel.add(bodyPanel);
		wrapperPanel.add(Box.createVerticalStrut(0));
		wrapperPanel.add(statusLabel); // added below
		wrapperPanel.add(Box.createVerticalStrut(20));

		return wrapperPanel;
	}

	private void styleTextField(JTextField field) {
		field.setPreferredSize(new Dimension(250, 40));
		field.setBackground(new Color(235, 235, 235));
		field.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
		field.setFont(new Font(FONT, Font.PLAIN, 14));
	}

	private void styleComboField(JComboBox<String> field) {
		field.setPreferredSize(new Dimension(150, 40));
		field.setBackground(new Color(235, 235, 235));
		field.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
		field.setFont(new Font(FONT, Font.PLAIN, 14));
	}

	private void styleButton(JButton btn) {
		btn.setPreferredSize(new Dimension(95, 40));
		btn.setBackground(new Color(45, 59, 73));
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font(FONT, Font.PLAIN, 14));
		btn.setFocusPainted(false);
		btn.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
	}

}
