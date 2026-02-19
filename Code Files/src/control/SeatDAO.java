package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import entity.Reservation;
import entity.Seat;

public class SeatDAO {
	private Connection connection;

	public SeatDAO() {
		this.connection = DatabaseManager.getInstance().getConnetcion();
	}

	public ArrayList<Seat> getSeatsForFlight(int flightId) throws SQLException {
		ArrayList<Seat> seats = new ArrayList<>();

		String query = """
	            SELECT s.seatID, s.seatNumber, s.class, s.price, s.isAvailable
	            FROM Seat s
	            JOIN FlightSeat fs ON s.seatID = fs.seatID
	            WHERE fs.flightID = ?
	        """;
		
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, flightId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int seatId = rs.getInt("seatID");
					String seatNumber = rs.getString("seatNumber");
					String seatClass = rs.getString("class");
					double price = rs.getDouble("price");
					boolean isAvailable = rs.getBoolean("isAvailable");

					Seat seat = new Seat(seatId, seatNumber, seatClass, price, isAvailable);
					seats.add(seat);
				}
			}
		}

		return seats;
	}
	
	public void updateSeatAvailability(Map<Integer, Reservation> reservations) throws SQLException {
		String updateSeat = "UPDATE Seat SET isAvailable = FALSE WHERE seatID = ?";
		try (PreparedStatement seatStmt = connection.prepareStatement(updateSeat)) {
		    for (Reservation res : reservations.values()) {
		        if (res.getDepartureSeat() != null) {
		            seatStmt.setInt(1, res.getDepartureSeat().getSeatId());
		            seatStmt.addBatch();
		        }
		        if (res.getReturnSeat() != null) {
		            seatStmt.setInt(1, res.getReturnSeat().getSeatId());
		            seatStmt.addBatch();
		        }
		    }
		    seatStmt.executeBatch();
		}
    }
	
	public void updateSeatAvailability(int seatId, boolean available, Connection conn) throws SQLException {
	    String sql = "UPDATE Seat SET isAvailable = ? WHERE seatID = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setBoolean(1, available);
	        stmt.setInt(2, seatId);
	        stmt.executeUpdate();
	    }
	}
	
	public void freeSeat(int seatId) throws SQLException {
		String updateSeat = "UPDATE Seat SET isAvailable = TRUE WHERE seatID = ?";
		try (PreparedStatement seatStmt = connection.prepareStatement(updateSeat)) {
			seatStmt.setInt(1, seatId);
			seatStmt.executeUpdate();
		}
	}
	
	public Seat getSeatById(int seatId){
	    String sql = "SELECT seatID, seatNumber, class, price, isAvailable FROM Seat WHERE seatID = ?";
	    
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, seatId);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            return new Seat(
	                rs.getInt("seatID"),
	                rs.getString("seatNumber"),
	                rs.getString("class"),
	                rs.getDouble("price"),
	                rs.getBoolean("isAvailable")
	            );
	        }
	    }catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}
	
	public Set<Integer> getReservedSeatIdsForFlight(int flightId) {

        Set<Integer> ids = new HashSet<>();

        String sql = """
            SELECT fs.seatID
            FROM FlightSeat fs
            JOIN Seat s ON fs.seatID = s.seatID
            WHERE fs.flightID = ?
              AND s.isAvailable = FALSE
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ids.add(rs.getInt("seatID"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }
}
