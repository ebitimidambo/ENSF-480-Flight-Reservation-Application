package control;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import entity.Reservation;
import entity.User;

public class BookingController {
    private final ReservationDAO reservationDAO;
    private final SeatDAO seatDAO;
    private final PaymentDAO paymentDAO;
    private final Connection connection;

    public BookingController() {
        this.connection = DatabaseManager.getInstance().getConnetcion();
        this.reservationDAO = new ReservationDAO();
        this.seatDAO = new SeatDAO();
        this.paymentDAO = new PaymentDAO();
    }

    public boolean processBooking(User user, Map<Integer, Reservation> reservations, double totalPrice) {
        try {
            connection.setAutoCommit(false); // begin transaction

            reservationDAO.addReservations(reservations);
            seatDAO.updateSeatAvailability(reservations);
            paymentDAO.recordPayment(1, totalPrice, "Credit Card");

            connection.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException h) {
                h.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}