package entity;

import java.time.LocalDateTime;

public class Reservation {
	private int reservationId;
	private User passenger;
	private boolean isReturn = false;
	private Flight departureFlight;
	private Flight returnFlight;
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private Seat departureSeat;
	private Seat arrivalSeat;
	private boolean checkedIn = false;
	private LocalDateTime dateBooked;
	private String status = "Pending";
	
	public Reservation(User user, Flight departureFlight, String firstName, String lastName, String dateOfBirth
			) {
		this.passenger = user;
		this.departureFlight = departureFlight;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}
	
	public User getUser() {return passenger;}
	public Flight getDepartureFlight() {return departureFlight;}
	public Seat getDepartureSeat() { return departureSeat;}
	public Seat getReturnSeat() { return arrivalSeat;}
	public Flight getReturnFlight() {return returnFlight;}
	public String getFirstName() { return firstName;}
	public String getLastName() { return lastName;}
	public String getDateOfBirth() { return dateOfBirth;}
	public boolean getCheckedIn() { return checkedIn;}
	public String getStatus() { return status;}
	public boolean getIsReturn() { return isReturn;}
	public int getReservationId() { return reservationId;}
	public LocalDateTime getDateBooked() {return dateBooked;}
	
	public void setReturnFlight(Flight flight) {
		this.returnFlight = flight;
	}
	
	public void setReservationId(int id) {
		this.reservationId = id;
	}
	
	public void setDepartureFlight(Flight flight) {this.departureFlight = flight;}
	public void setDepartureSeat(Seat seat) { this.departureSeat = seat; }
	public void setReturnSeat(Seat seat) { this.arrivalSeat = seat;}
	public void setFirstName(String name) { this.firstName = name;}
	public void setLastName(String name) {this.lastName = name;}
	public void setDateOfBirth(String date) {this.dateOfBirth = date;}
	public void setIsReturn() {this.isReturn = true;}
	public void setCheckedIn() {this.checkedIn = true;}
	public void setStatus(String status) {this.status = status;}
	public void setDateBooked (LocalDateTime date) {this.dateBooked = date;}
}
