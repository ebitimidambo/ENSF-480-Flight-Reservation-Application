package control;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import entity.Flight;
import entity.Airline;
import control.FlightDAO;

@SuppressWarnings("unused")
public class FlightController {
	private FlightDAO flight;
	
	public FlightController() {
        this.flight = new FlightDAO();
    }
	
	public void loadAllFlights() {
        List<Flight> flights = flight.getAllFlights();
        Airline.getInstance().setFlights(flights);
    }
	
	public List<Flight> getAllFlightsForAdmin() {
		List<Flight> flights = flight.getAllFlightsForAdmin();
		return flights;
	}
	
	public void deleteFlight(int flightId) {
		flight.deleteFlight(flightId);
	}
	
	public void cancelFlightsByRoute(int routeId) {
		flight.cancelFlightsByRoute(routeId);
	}
	
	public void addFlight(String flightName, int routeId, int aircraftId, String departure, String arrival) {
		flight.addFlight(flightName, routeId, aircraftId, departure, arrival);
	}
	
	public void updateFlight(int flightId, String name, int routeId, int aircraftId, LocalDateTime dep, LocalDateTime arr, String status) {
		flight.updateFlight(flightId, name, routeId, aircraftId, dep, arr, status);
	}
	
	public Flight getFlightById(int id) {
		Flight obtainedFlight;
		obtainedFlight = flight.getFlightById(id);
		
		return obtainedFlight;
	}
}
