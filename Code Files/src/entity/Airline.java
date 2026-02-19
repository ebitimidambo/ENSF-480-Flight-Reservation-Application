package entity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Airline {
	private static Airline airline;
	private String airlineCode;
	private String name;
	private List<Flight> flights;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private Airline(String code, String name) {
		this.airlineCode = code;
		this.name = name;
		this.flights = new ArrayList<>();
	}

	public void addFlight(Flight flight) {
		flights.add(flight);
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public String getAirlineName() {
		return name;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public static Airline getInstance() {
		if (airline == null) {
			airline = new Airline("AE", "Atlantic European Airways Express");
		}

		return airline;
	}

	public List<Flight> searchFlights(String origin, String destination) {
		return flights;
	}

	public List<Flight> searchFlights(String origin, String destination, String date) {
		return flights.stream()
				.filter(f -> f.getRoute().getOrigin().equalsIgnoreCase(origin))
				.filter(f -> f.getRoute().getDestination().equalsIgnoreCase(destination))
				.filter(f -> f.getDepartureTime().toLocalDate().format(formatter).equals(date))
				.collect(Collectors.toList());
	}
}
