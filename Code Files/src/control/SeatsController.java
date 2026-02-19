package control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import entity.Reservation;
import entity.Seat;

public class SeatsController {
	private SeatDAO seatDAO;
	
	public SeatsController() {
		seatDAO = new SeatDAO();
	}
	
	public void updateSeats(Map<Integer, Reservation> reservations) throws SQLException {
		seatDAO.updateSeatAvailability(reservations);
	}
	
	public ArrayList<Seat> getSeatsForFlight(int flightId) throws SQLException{
		ArrayList<Seat> seats = seatDAO.getSeatsForFlight(flightId);
		return seats;
	}
}
