package boundary.utility;

import java.util.List;
import java.util.Map;

import entity.Flight;
import entity.Reservation;
import entity.User;

public class BookingGuiFactory extends UserGuiFactory{
	private static User CUSTOMER;
	private Map<String, Object> searchPrompts;
	Map<Integer, Reservation> existingReservations;
	private List<Flight> departureFlights;
	private List<Flight> arrivalFlights;
	private Flight departure;
	private Flight arrival;
	private boolean isReturnStep;
	
	public BookingGuiFactory(User user, Map<String, Object> prompts, List<Flight> departureFlights, 
			List<Flight> arrivalFlights, Flight departure, Flight arrival, boolean isReturn, 
			Map<Integer, Reservation> existingReservations) {
		CUSTOMER = user;
		this.departureFlights = departureFlights;
		this.arrivalFlights = arrivalFlights;
		this.searchPrompts = prompts;
		this.departure = departure;
		this.arrival = arrival;
		this.isReturnStep = isReturn;
		this.existingReservations = existingReservations;
	}
	
	public Header createHeader() {
		return new BookingHeader(CUSTOMER, searchPrompts, departureFlights, arrivalFlights);
	}
	
	public MidSection createMidSection() {
		return null;
	}
	
	public Body createBody() {
		System.out.println("Existing Reservations: " + existingReservations);
		if (existingReservations == null) {
			return new BookingBody(CUSTOMER, searchPrompts, departureFlights, arrivalFlights, departure, arrival, isReturnStep, null);
		} else {
			return new BookingBody(CUSTOMER, searchPrompts, departureFlights, arrivalFlights, departure, arrival, isReturnStep, existingReservations);
		}
	}
}
