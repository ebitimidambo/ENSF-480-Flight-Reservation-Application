package boundary.utility;

import java.util.List;
import java.util.Map;

import entity.Flight;
import entity.User;

public class FlightSearchGuiFactory extends UserGuiFactory{
	private User user;
	private Map<String, Object> searchPrompts;
	private List<Flight> departureFlights;
	private List<Flight> arrivalFlights;
	
	public FlightSearchGuiFactory(User user, Map<String, Object> prompts, List<Flight> departureFlights, List<Flight> arrivalFlights) {
		this.searchPrompts = prompts;
		this.departureFlights = departureFlights;
		this.arrivalFlights = arrivalFlights;
		this.user = user;
	}
	
	public Body createBody() {
		return new FlightSearchBody(user, searchPrompts, departureFlights, arrivalFlights);
	}

	@Override
	public Header createHeader() {
		return new FlightSearchHeader(user);
	}

	@Override
	public MidSection createMidSection() {
		// TODO Auto-generated method stub
		return null;
	}
}
