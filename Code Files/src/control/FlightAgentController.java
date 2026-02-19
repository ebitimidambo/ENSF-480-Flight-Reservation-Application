package control;

import entity.Reservation;
import entity.Seat;
import entity.User;
import entity.Flight;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlightAgentController {

    private final ReservationDAO reservationDAO;
    private final UserDAO userDAO;
    private final FlightDAO flightDAO;
    private final SeatDAO seatDAO;

    public FlightAgentController() {
        this.reservationDAO = new ReservationDAO();
        this.userDAO = new UserDAO();
        this.flightDAO = new FlightDAO();
        this.seatDAO = new SeatDAO();
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    // --- new methods for other use cases ---

    public List<User> getAllCustomers() {
        return userDAO.getAllCustomers();
    }

    public void updateCustomerProfile(User user) {
        userDAO.updateCustomerProfile(user);
    }

    public List<Flight> getAllFlights() {
        return flightDAO.getAllFlights();
    }
    
    public void updateReservationFull(int reservationId, String first, String last, String dob, String status) {
        reservationDAO.updateReservationFull(reservationId, first, last, dob, status);
    }

    public void updateSeatForReservation(Reservation r, Seat newSeat) throws SQLException {
        reservationDAO.updateSeatForReservation(r, newSeat);
    }

	public ArrayList<Seat> getSeatsForFlight(int flightId){
		try {
			return seatDAO.getSeatsForFlight(flightId);
		} catch (SQLException e) {
			throw new RuntimeException("DB error loading seats for flight " + flightId, e);
		}
	}

	public Set<Integer> getReservedSeatIdsForFlight(int flightId) {
		return seatDAO.getReservedSeatIdsForFlight(flightId);
	}
	
	public	boolean cancelReservation(Reservation res) {
		return reservationDAO.cancelReservation(res);
	}

	public void addCustomer(String first, String last, String username, String password, String email, String address, LocalDate dob) {
		userDAO.addCustomer(first, last, username, password, email, address, dob);
	}
	
	public void deleteUser(int userID) {

        try {
            reservationDAO.cancelAllReservationsForUser(userID);
            userDAO.deleteUser(userID);
        } catch (Exception e) {
            throw new RuntimeException("User deletion aborted", e);
        }
    }
}
