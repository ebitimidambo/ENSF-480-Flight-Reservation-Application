package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Flight {
	private int flightId;
	private String flightName;
	private Route route;
	private Aircraft aircraftModel;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	private String status;
	private double basePrice;
	private ArrayList<Seat> seats;
	private ArrayList<Reservation> reservations;
	
	public Flight(int flightId, String flightName, Route route, Aircraft aircraftModel, LocalDateTime departureTime,
			LocalDateTime arrivalTime, String status) {
		this.flightId = flightId;
		this.flightName = flightName;
		this.route = route;
		this.aircraftModel = aircraftModel;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.status = status;
		seats = new ArrayList<>();
		reservations = new ArrayList<>();
		setBasePrice();
	}
	
	public int getFlightId() {return flightId;}
	public String getFlightName() {return flightName;}
	public Route getRoute() {return route;}
	public Aircraft getModel() {return aircraftModel;}
	public LocalDateTime getDepartureTime() {return departureTime;}
	public LocalDateTime getArrivalTime() {return arrivalTime;}
	public String getStatus() {return status;}
	public double getBasePrice() {return basePrice;}
	public ArrayList<Seat> getSeats() {return seats;}
	public void addReservation(Reservation reservation) {reservations.add(reservation);}
	
	private void setBasePrice() {
		double distanceFactor = 0;

	    if (route != null) {
	        distanceFactor = route.getDistance() / 20.0;
	    }

	    double modelFactor = 0;
	    if (aircraftModel != null) {
	        modelFactor = aircraftModel.getAircraftModel().length();
	    }
	    
		basePrice = 300 + (distanceFactor) + (getFlightId() * modelFactor);
	}
	
	public void setSeats(ArrayList<Seat> seats) {
		this.seats = seats;
	}
}
