package boundary.utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


import boundary.CustomerUI;
import boundary.MainFrame;
import control.BookingController;
import entity.Reservation;
import entity.User;

public class PaymentBody implements Body{
	private User user;
	private Map<Integer, Reservation> reservations;
	@SuppressWarnings("unused")
	private Map<String, Object> prompts; 
	private boolean isReturnStep;
	private double total_price;
	private static final String FONT = "Segoe UI Light";
	
	public PaymentBody(User user, Map<Integer, Reservation> reservations, Map<String, Object> prompts, 
			boolean isReturnStep) {
		this.user = user;
		this.reservations = reservations;
		this.prompts = prompts;
		this.isReturnStep = isReturnStep;
		this.total_price = 0;
	}
	
	public JPanel createBody() {
		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 80));
		bodyPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE,600));
		bodyPanel.setBackground(new Color(255, 255, 255));
		
		JPanel searchPanel = new RoundedPanel(25);
		// Allow dynamic resizing — not GridLayout
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setPreferredSize(new Dimension(900, 400));
		
		JPanel paymentInfoPanel = new RoundedPanel(30);
		paymentInfoPanel.setLayout(new BoxLayout(paymentInfoPanel, BoxLayout.PAGE_AXIS));
		paymentInfoPanel.setBackground(new Color(45,59,73));
		paymentInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		paymentInfoPanel.setPreferredSize(new Dimension(450, 400));
		
		JPanel paymentFormPanel = new RoundedPanel(30);
		paymentFormPanel.setLayout(new BoxLayout(paymentFormPanel, BoxLayout.PAGE_AXIS));
		paymentFormPanel.setBackground(Color.gray);
		paymentFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		paymentFormPanel.setPreferredSize(new Dimension(450, 400));
		
		JScrollPane scrollPane = new JScrollPane(paymentInfoPanel);
		scrollPane.setPreferredSize(new Dimension(450, 400));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		// -----------------------------------------------------------------------------------------------
		JLabel heading = makeLabelField("Payment Info");
		JTextField email = makeTextField("Email");
		JTextField cardDetails = makeTextField("Card Number");
		JTextField expiryDate = makeTextField("Expiry Date");
		expiryDate.setMaximumSize(new Dimension(150, 60));
		JTextField cvv = makeTextField("CVV");
		cvv.setMaximumSize(new Dimension(150, 60));
		JButton paymentButton = makeButton("Pay");
		
		JPanel shortPanel = new JPanel();
		shortPanel.setOpaque(false);
		shortPanel.add(expiryDate);
		shortPanel.add(cvv);
		shortPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		shortPanel.setLayout(new BoxLayout(shortPanel, BoxLayout.LINE_AXIS));
		
		JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	    buttonWrapper.setOpaque(false);
	    buttonWrapper.add(paymentButton);
	    buttonWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		cvv.setMaximumSize(new Dimension(150, 40));
		
		paymentFormPanel.add(Box.createVerticalStrut(50));
		paymentFormPanel.add(heading);
		paymentFormPanel.add(Box.createVerticalStrut(20));
		paymentFormPanel.add(email);
		paymentFormPanel.add(Box.createVerticalStrut(20));
		paymentFormPanel.add(cardDetails);
		paymentFormPanel.add(Box.createVerticalStrut(20));
		paymentFormPanel.add(shortPanel);
		paymentFormPanel.add(Box.createVerticalStrut(30));
		paymentFormPanel.add(buttonWrapper);
		// -----------------------------------------------------------------------------------------------
		JLabel title = makeLabelField("Payment Summary");
		JLabel underlineStart = makeLabelField("-".repeat(80));
		paymentInfoPanel.add(title);
		paymentInfoPanel.add(underlineStart);
		
		for(Reservation res: reservations.values()) {
			JPanel panel = makePaymentSummary(res);
			paymentInfoPanel.add(panel);
		}
		
		JLabel total = makeLabelField("Total Price: $" + this.total_price);
		JLabel underlineEnd = makeLabelField("-".repeat(80));
		paymentInfoPanel.add(underlineEnd);
		paymentInfoPanel.add(total);
		
		searchPanel.add(scrollPane);
		searchPanel.add(paymentFormPanel);
		bodyPanel.add(searchPanel);
		
		paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailText = email.getText().trim();
                String cardText = cardDetails.getText().trim();
                String expiryText = expiryDate.getText().trim();
                String cvvText = cvv.getText().trim();

                if (emailText.isEmpty() || emailText.equals("Email") ||cardText.isEmpty() || expiryText.isEmpty() 
                		|| expiryText.equals("Expiry Date") || cvvText.isEmpty() || cvvText.equals("CVV")) {
                    JOptionPane.showMessageDialog(null, "Please fill out all payment fields.",
                            "Missing Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!cardText.matches("\\d{16,}")) {
                    JOptionPane.showMessageDialog(null, "Card number must be at least 16 digits.",
                            "Invalid Card", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!EmailValidator.validateEmail(emailText)) {
    				JOptionPane.showMessageDialog(null, "Invalid email addrress", "Error", JOptionPane.ERROR_MESSAGE);
    				return;
    			}
                
                if (!cvvText.matches("\\d{3}")) {
                    JOptionPane.showMessageDialog(null, "CVV Must be 3 digits",
                            "Invalid Card", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!DateValidator.validateFormDate(expiryText)) {
    				JOptionPane.showMessageDialog(null, "Invalid expiry date", "Error", JOptionPane.ERROR_MESSAGE);
    				return;
    			}
                
                for (Reservation res : reservations.values()) {
                    res.setStatus("Approved");
                }

                BookingController bookingController = new BookingController();

                boolean success = bookingController.processBooking(user, reservations, total_price);

                if (success) {
                    JOptionPane.showMessageDialog(null,
                            "Payment successful! Your booking has been confirmed.",
                            "Payment Confirmation: $" + total_price + " for " + user.getFirstName()
                            , JOptionPane.INFORMATION_MESSAGE);

                    for (Reservation res : reservations.values()) {
                        user.addReservation(res);
                        if (!res.getIsReturn()) {
                            res.getDepartureFlight().addReservation(res);
                        } else {
                            res.getReturnFlight().addReservation(res);
                        }
                    }

                    MainFrame mainFrame = MainFrame.getInstance();
                    mainFrame.showPanel(new CustomerUI(user));

                } else {
                    JOptionPane.showMessageDialog(null,
                            "An error occurred while saving your booking. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

		return bodyPanel;
	}
	
	private JTextField makeTextField(String placeholder) {
		JTextField field = new RoundedJTextField(1, placeholder);
		field.setFont(new Font(FONT, Font.PLAIN, 13));
		field.setForeground(new Color(45, 59, 73));
		field.setBackground(new Color(245, 245, 245));
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		field.setAlignmentX(Component.LEFT_ALIGNMENT);
		return field;
	}
	
	private JLabel makeLabelField(String placeholder) {
		JLabel field = new JLabel(placeholder);
		field.setFont(new Font(FONT, Font.PLAIN, 13));
		field.setForeground(Color.WHITE);
		field.setOpaque(false);
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		field.setAlignmentX(Component.LEFT_ALIGNMENT);
		return field;
	}
	
	private JButton makeButton(String placeholder) {
		JButton field = new RoundedButton(placeholder);
		field.setPreferredSize(new Dimension(150, 40));
		field.setBackground(new Color(45, 59, 73));
		field.setForeground(Color.WHITE);
		field.setFont(new Font("Arial", Font.PLAIN, 14));
		field.setFocusPainted(false);
		field.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
		return field;
	}
	
	private JPanel makePaymentSummary(Reservation passenger) {
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		JLabel returnSeatField = null;
		JLabel returnFlightField = null;
		
		JLabel field = makeLabelField("Passenger: " + passenger.getFirstName() + " " + passenger.getLastName());
		JLabel departureFlightField = makeLabelField("Departure Flight: $" + passenger.getDepartureFlight().getBasePrice());
		total_price += passenger.getDepartureFlight().getBasePrice();
		JLabel departureSeatField = makeLabelField("Departure Seat: $" + passenger.getDepartureSeat().getSeatPrice());
		total_price += passenger.getDepartureSeat().getSeatPrice();
		container.add(field);
		container.add(departureFlightField);
		container.add(departureSeatField);
		
		if (isReturnStep) {
			returnFlightField = makeLabelField("Return Flight: $" + passenger.getReturnFlight().getBasePrice());
			total_price += passenger.getReturnFlight().getBasePrice();
			returnSeatField = makeLabelField("Return Seat: $" + passenger.getReturnSeat().getSeatPrice());
			total_price += passenger.getReturnSeat().getSeatPrice();
			container.add(returnFlightField);
			container.add(Box.createVerticalStrut(20));
			container.add(returnSeatField);
			container.add(Box.createVerticalStrut(20));
		}
		
		return container;
	}
}
