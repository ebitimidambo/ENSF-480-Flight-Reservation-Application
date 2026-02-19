package control;

import java.util.Map;

import entity.Reservation;
import entity.User;

public class ReservationController {
	private ReservationDAO reservationDAO;

	public ReservationController() {
		reservationDAO = new ReservationDAO();
	}
	
	public void addReservations(Map<Integer, Reservation> reservations) {
		reservationDAO.addReservations(reservations);
	}
	
	public void loadUserReservations(User user) {
		reservationDAO.loadUserReservations(user);
	}
	
	public boolean cancelReservation(Reservation res) {
	    return reservationDAO.cancelReservation(res);
	}
}
