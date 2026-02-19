package boundary;

import java.awt.BorderLayout;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

import boundary.utility.Body;
import boundary.utility.BookingGuiFactory;
import boundary.utility.CustomerUIBanner;
import boundary.utility.CustomerUIFooter;
import boundary.utility.Header;
import boundary.utility.UserGuiFactory;
import entity.Flight;
import entity.Reservation;
import entity.User;

@SuppressWarnings("serial")
public class BookingUI extends JPanel{
	private User user;
	private UserGuiFactory factory;
	private Header header;
	private Body body;
	
	public BookingUI(User user, Map<String, Object> prompts, List<Flight> departureFlights, 
			List<Flight> arrivalFlights, Flight selectedDeparture, Flight selectedReturn, boolean isReturn,
			Map<Integer, Reservation> existingReservations) {
		this.user = user;
		this.factory = new BookingGuiFactory(user, prompts, departureFlights, arrivalFlights, 
				selectedDeparture, selectedReturn, isReturn, existingReservations);
		this.header = factory.createHeader();
		this.body = factory.createBody();
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
	    topPanel.setLayout(new BorderLayout());
	    topPanel.add(CustomerUIBanner.createBanner(user), BorderLayout.NORTH);
	    topPanel.add(header.createHeader(), BorderLayout.SOUTH);
	    topPanel.setBackground(new Color(45,59,73));
	    
	    add(topPanel, BorderLayout.NORTH);
	    
	    JPanel bottomPanel = new JPanel();
	    bottomPanel.setLayout(new BorderLayout());
	    bottomPanel.add(body.createBody(), BorderLayout.NORTH);
	    bottomPanel.add(CustomerUIFooter.createFooter(), BorderLayout.SOUTH);
	    bottomPanel.setBackground(new Color(45,59,73));
	    add(bottomPanel, BorderLayout.SOUTH);
	}
}
