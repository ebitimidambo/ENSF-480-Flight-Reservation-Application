package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.Aircraft;
import entity.Flight;
import entity.Reservation;
import entity.Route;
import entity.Seat;
import entity.User;

public class ReservationDAO {
	private final Connection connection;
	@SuppressWarnings("unused")
	private final UserDAO userDAO;
    private final FlightDAO flightDAO;
    private final SeatDAO seatDAO;

	public ReservationDAO() {
		this.connection = DatabaseManager.getInstance().getConnetcion();
		this.userDAO = new UserDAO();
        this.flightDAO = new FlightDAO();
        this.seatDAO = new SeatDAO(); //something to add in the future 
	}

	public void addReservations(Map<Integer, Reservation> reservations) {
		String sql = "INSERT INTO Reservation "
				+ "(userID, flightID, departureSeatID, passengerFirstName, passengerLastName, "
				+ "passengerDateOfBirth, returnSeatID, checkedIn, status, isReturn) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			for (Reservation res : reservations.values()) {
				// --- Departure reservation ---
				stmt.setInt(1, res.getUser().getUserID());
				stmt.setInt(2, res.getDepartureFlight().getFlightId());
				stmt.setInt(3, res.getDepartureSeat() != null ? res.getDepartureSeat().getSeatId() : null);
				stmt.setString(4, res.getFirstName());
				stmt.setString(5, res.getLastName());
				stmt.setString(6, res.getDateOfBirth());
				stmt.setObject(7, null); // no return seat for departure leg
				stmt.setBoolean(8, res.getCheckedIn());
				stmt.setString(9, res.getStatus());
				stmt.setBoolean(10, false); // not a return reservation
				stmt.addBatch();

				// --- Return reservation (if applicable) ---
				if (res.getReturnFlight() != null && res.getReturnSeat() != null) {
					stmt.setInt(1, res.getUser().getUserID());
					stmt.setInt(2, res.getReturnFlight().getFlightId());
					stmt.setObject(3, null); // no departure seat for return leg
					stmt.setString(4, res.getFirstName());
					stmt.setString(5, res.getLastName());
					stmt.setString(6, res.getDateOfBirth());
					stmt.setInt(7, res.getReturnSeat().getSeatId());
					stmt.setBoolean(8, res.getCheckedIn());
					stmt.setString(9, res.getStatus());
					stmt.setBoolean(10, true); // return leg
					stmt.addBatch();
				}
			}

			stmt.executeBatch();

			SeatsController seatManager = new SeatsController();
			seatManager.updateSeats(reservations);

			System.out.println("All reservations successfully inserted into the Reservation table.");

		} catch (SQLException e) {
			System.err.println("Error adding reservations: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void loadUserReservations(User user) {
		ArrayList<Reservation> reservations = new ArrayList<>();

		String sql = """
				    SELECT
				        r.reservationID, r.status, r.isReturn,
				        r.passengerFirstName, r.passengerLastName, r.passengerDateOfBirth,

				        f.flightID, f.flightName, f.departureTime, f.arrivalTime, f.status AS flightStatus,

				        rt.routeID, rt.origin, rt.destination, rt.distance,
				        a.aircraftId, a.model AS aircraftModel, a.capacity AS aircraftCapacity, a.haulType,

				        s1.seatID AS departureSeatID, s1.seatNumber AS departureSeatNumber,
				        s1.class AS departureSeatClass, s1.price AS departureSeatPrice,

				        s2.seatID AS returnSeatID, s2.seatNumber AS returnSeatNumber,
				        s2.class AS returnSeatClass, s2.price AS returnSeatPrice

				    FROM Reservation r
				    JOIN Flight f ON r.flightID = f.flightID
				    JOIN Route rt ON f.routeID = rt.routeID
				    JOIN Aircraft a ON f.aircraftID = a.aircraftID
				    LEFT JOIN Seat s1 ON r.departureSeatID = s1.seatID
				    LEFT JOIN Seat s2 ON r.returnSeatID = s2.seatID
				    WHERE r.userID = ? AND r.status = 'Approved'
				    ORDER BY r.dateBooked DESC;
				""";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, user.getUserID());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					// --- Build Route ---
					Route route = new Route(rs.getInt("routeID"), rs.getString("origin"), rs.getString("destination"),
							rs.getDouble("distance"), "Active");

					Aircraft aircraftModel = new Aircraft(rs.getInt("aircraftId"), rs.getString("aircraftModel"),
							rs.getInt("aircraftCapacity"), rs.getString("haulType"));

					// --- Build Flight ---
					Flight flight = new Flight(rs.getInt("flightID"), rs.getString("flightName"), route, aircraftModel,
							rs.getTimestamp("departureTime").toLocalDateTime(),
							rs.getTimestamp("arrivalTime").toLocalDateTime(), rs.getString("flightStatus"));

					Reservation res;

					if (rs.getBoolean("isReturn") == false) {
						// --- Build Reservation ---
						res = new Reservation(user, flight, rs.getString("passengerFirstName"),
								rs.getString("passengerLastName"), rs.getString("passengerDateOfBirth"));
					} else {
						res = new Reservation(user, null, rs.getString("passengerFirstName"),
								rs.getString("passengerLastName"), rs.getString("passengerDateOfBirth"));
						;
						res.setReturnFlight(flight);
					}

					res.setStatus(rs.getString("status"));
					res.setReservationId(rs.getInt("reservationID"));

					if (rs.getBoolean("isReturn")) {
						res.setIsReturn(); // only mark true if the DB says it's true
					}

					// --- Departure seat ---
					if (rs.getObject("departureSeatID") != null) {
						Seat seat = new Seat(rs.getInt("departureSeatID"), rs.getString("departureSeatNumber"),
								rs.getString("departureSeatClass"), rs.getDouble("departureSeatPrice"), false);
						res.setDepartureSeat(seat);
					}

					// --- Return seat ---
					if (rs.getObject("returnSeatID") != null) {
						Seat seat = new Seat(rs.getInt("returnSeatID"), rs.getString("returnSeatNumber"),
								rs.getString("returnSeatClass"), rs.getDouble("returnSeatPrice"), false);
						res.setReturnSeat(seat);
					}

					reservations.add(res);
				}
			}
			user.setReservation(reservations);
			System.out.println("Loaded " + reservations.size() + " reservations for user " + user.getUsername());

		} catch (SQLException e) {
			System.err.println("Error loading user reservations: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public boolean cancelReservation(Reservation res) {
		String sql = "UPDATE Reservation SET status = 'Canceled' WHERE reservationID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, res.getReservationId());
			if (res.getIsReturn() == true){
				seatDAO.freeSeat(res.getReturnSeat().getSeatId());
				res.getReturnSeat().setAvailability(true);
			} else {
				seatDAO.freeSeat(res.getDepartureSeat().getSeatId());
				res.getDepartureSeat().setAvailability(true);
			}
			//flightDAO.getAllFlights();
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Reservation> getAllReservations() {
		List<Reservation> reservations = new ArrayList<>();

		String sql = """
				    SELECT
				        r.reservationID,
				        r.isReturn,
				        r.checkedIn,
				        r.status,
				        r.dateBooked,
				        r.flightID,
				        r.passengerFirstName,
				        r.passengerLastName,
				        r.passengerDateOfBirth,

				        u.userID, u.firstName AS bookerFirstName, u.lastName AS bookerLastName, u.username,

				        f.flightID, f.flightName, f.departureTime, f.arrivalTime,

				        s1.seatID AS departureSeatID, s1.seatNumber AS departureSeatNumber,
				  s1.class AS departureSeatClass, s1.price AS departureSeatPrice,

				  s2.seatID AS returnSeatID, s2.seatNumber AS returnSeatNumber,
				  s2.class AS returnSeatClass, s2.price AS returnSeatPrice

				    FROM Reservation r
				    JOIN User u ON r.userID = u.userID
				    JOIN Flight f ON r.flightID = f.flightID
				    LEFT JOIN Seat s1 ON r.departureSeatID = s1.seatID
				    LEFT JOIN Seat s2 ON r.returnSeatID = s2.seatID
				    ORDER BY r.dateBooked DESC
				""";

		try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {

				// Booker
				User booker = new User(rs.getInt("userID"), rs.getString("username"), null,
						rs.getString("bookerFirstName"), rs.getString("bookerLastName"), null, "CUSTOMER");

				// Flight
				Flight flight = flightDAO.getFlightById(rs.getInt("flightID"));

				// Reservation object
				Reservation reservation = new Reservation(booker, flight, rs.getString("passengerFirstName"),
						rs.getString("passengerLastName"), rs.getString("passengerDateOfBirth"));

				// Seats
				// --- Departure seat ---
				if (rs.getObject("departureSeatID") != null) {
					Seat seat = new Seat(rs.getInt("departureSeatID"), rs.getString("departureSeatNumber"),
							rs.getString("departureSeatClass"), rs.getDouble("departureSeatPrice"), false);
					reservation.setDepartureSeat(seat);
				}

				// --- Return seat ---
				if (rs.getObject("returnSeatID") != null) {
					Seat seat = new Seat(rs.getInt("returnSeatID"), rs.getString("returnSeatNumber"),
							rs.getString("returnSeatClass"), rs.getDouble("returnSeatPrice"), false);
					reservation.setReturnSeat(seat);
				}

				// Metadata
				reservation.setReservationId(rs.getInt("reservationID"));
				reservation.setStatus(rs.getString("status"));

				reservation.setDateBooked(rs.getTimestamp("dateBooked").toLocalDateTime());

				if (rs.getBoolean("isReturn")) {
					reservation.setIsReturn();
					reservation.setReturnFlight(flight);
					reservation.setDepartureFlight(null);
				}

				if (rs.getBoolean("checkedIn")) {
					reservation.setCheckedIn();
				}

				reservations.add(reservation);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reservations;
	}

	public void updateReservationFull(int id, String first, String last, String dob, String status) {

		String sql = """
				    UPDATE Reservation
				    SET passengerFirstName = ?,
				        passengerLastName = ?,
				        passengerDateOfBirth = ?,
				        status = ?
				    WHERE reservationID = ?
				""";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, first);
			stmt.setString(2, last);
			stmt.setString(3, dob);
			stmt.setString(4, status);
			stmt.setInt(5, id);

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new SQLException("Reservation update failed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateSeatForReservation(Reservation r, Seat newSeat) throws SQLException {

		connection.setAutoCommit(false);
		try {

			int oldSeatId = -1;
			if (r.getIsReturn() && r.getReturnSeat() != null) {
				oldSeatId = r.getReturnSeat().getSeatId();
			} else if (!r.getIsReturn() && r.getDepartureSeat() != null) {
				oldSeatId = r.getDepartureSeat().getSeatId();
			}

			// 1) Release old seat
			if (oldSeatId != -1) {
				seatDAO.updateSeatAvailability(oldSeatId, true, connection);
			}

			// 2) Lock new seat
			seatDAO.updateSeatAvailability(newSeat.getSeatId(), false, connection);

			// 3) Update reservation row
			String sql = r.getIsReturn() ? "UPDATE Reservation SET returnSeatID = ? WHERE reservationID = ?"
					: "UPDATE Reservation SET departureSeatID = ? WHERE reservationID = ?";

			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, newSeat.getSeatId());
				ps.setInt(2, r.getReservationId());
				ps.executeUpdate();
			}

			connection.commit();

		} catch (SQLException ex) {
			connection.rollback(); // atomic repair
			throw ex;
		}
	}
	
	public void cancelAllReservationsForUser(int userId) throws SQLException {

        String findSql = """
            SELECT reservationID, isReturn, departureSeatID, returnSeatID
            FROM Reservation
            WHERE userID = ? AND status <> 'Canceled'
        """;

        String cancelSql = "UPDATE Reservation SET status = 'Canceled' WHERE reservationID = ?";

        connection.setAutoCommit(false);

        try (
            PreparedStatement findStmt = connection.prepareStatement(findSql);
            PreparedStatement cancelStmt = connection.prepareStatement(cancelSql)
        ) {
            findStmt.setInt(1, userId);

            ResultSet rs = findStmt.executeQuery();

            while (rs.next()) {

                int resId = rs.getInt("reservationID");
                boolean isReturn = rs.getBoolean("isReturn");
                Integer depSeat = (Integer) rs.getObject("departureSeatID");
                Integer retSeat = (Integer) rs.getObject("returnSeatID");

                cancelStmt.setInt(1, resId);
                cancelStmt.executeUpdate();

                if (!isReturn && depSeat != null) {
                    seatDAO.freeSeat(depSeat);
                }

                if (isReturn && retSeat != null) {
                    seatDAO.freeSeat(retSeat);
                }
            }

            connection.commit();
            System.out.println("All reservations canceled and seats freed for userID=" + userId);

        } catch (SQLException ex) {
            connection.rollback();
            System.err.println("Rollback: could not cancel reservations for userID=" + userId);
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    } 

}
