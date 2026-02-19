package control;

import java.util.List;

import entity.Aircraft;
import entity.Flight;
import entity.Route;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FlightDAO {
private final Connection connection;
	
	public FlightDAO() {
		this.connection = DatabaseManager.getInstance().getConnetcion();
	}
	
	public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        
        String query = """
                SELECT f.flightID, f.flightName, f.departureTime, f.arrivalTime,
                   f.status, r.routeID, r.origin, r.destination, r.distance, a.aircraftId,
                   a.model, a.capacity, a.haulType
            FROM Flight f
            JOIN Route r ON f.routeID = r.routeID
            JOIN aircraft a ON f.aircraftID = a.aircraftID
            """;
        
        try {
        	Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Route route = new Route (rs.getInt("routeID"), rs.getString("origin"), rs.getString("destination"),
						rs.getDouble("distance"));
				Aircraft aircraftModel = new Aircraft(rs.getInt("aircraftId"), rs.getString("model"), 
						rs.getInt("capacity"), rs.getString("haulType"));
				Flight flight = new Flight(
	                    rs.getInt("flightID"),
	                    rs.getString("flightName"),
	                    route,
	                    aircraftModel,
	                    rs.getTimestamp("departureTime").toLocalDateTime(),
	                    rs.getTimestamp("arrivalTime").toLocalDateTime(),
	                    rs.getString("status")
	                );
				
				flights.add(flight);
			}
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return flights;
	}
	
	
	public List<Flight> getAllFlightsForAdmin() {
	    List<Flight> flights = new ArrayList<>();

	    String query = """
	        SELECT 
	            f.flightID, f.flightName,
	            f.departureTime, f.arrivalTime,
	            f.status,
	            r.routeID, r.origin, r.destination, r.distance,
	            a.aircraftID, a.model, a.capacity, a.haulType
	        FROM Flight f
	        JOIN Route r ON f.routeID = r.routeID
	        JOIN aircraft a ON f.aircraftID = a.aircraftID
	        ORDER BY f.flightID;
	    """;

	    try (Statement stmt = connection.createStatement()) {

	        ResultSet rs = stmt.executeQuery(query);

	        while (rs.next()) {

	            Route route = null;

	            if (rs.getObject("routeID") != null) {
	                route = new Route(
	                    rs.getInt("routeID"),
	                    rs.getString("origin"),
	                    rs.getString("destination"),
	                    rs.getDouble("distance")
	                );
	            }
	            
	            Aircraft ac = null;
	            if (rs.getObject("aircraftID") != null) {
	            	ac = new Aircraft(
	            			rs.getInt("aircraftID"),
	            			rs.getString("model"),
	            			rs.getInt("capacity"),
	            			rs.getString("haulType")
	            		);
	            }
	            
	            Flight flight = new Flight(
	                rs.getInt("flightID"),
	                rs.getString("flightName"),
	                route,
	                ac,
	                rs.getTimestamp("departureTime").toLocalDateTime(),
	                rs.getTimestamp("arrivalTime").toLocalDateTime(),
	                rs.getString("status")
	            );

	            flights.add(flight);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return flights;
	}
	
	public void deleteFlight(int flightId) {
        String sql = "DELETE FROM Flight WHERE flightID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, flightId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelFlightsByRoute(int routeId) {
    	String sql = "UPDATE Flight SET status='Cancelled' WHERE routeID=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, routeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFlight(String flightName, int routeId, int aircraftId, String departure, String arrival) {
        String flightSQL = """
                INSERT INTO Flight (flightName, routeID, aircraftID, departureTime, arrivalTime, status)
                VALUES (?, ?, ?, ?, ?, 'Scheduled')
            """;

        String seatSQL = """
                INSERT INTO Seat (seatNumber, class, price, isAvailable)
                VALUES (?, ?, ?, TRUE)
            """;

        String mapSQL = """
                INSERT INTO FlightSeat (flightID, seatID)
                VALUES (?, ?)
            """;
        try {
            connection.setAutoCommit(false); // start

            // ==== 1) INSERT FLIGHT ====
            int flightID;

            try (PreparedStatement stmt =
                         connection.prepareStatement(flightSQL, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, flightName);
                stmt.setInt(2, routeId);
                stmt.setInt(3, aircraftId);
                stmt.setTimestamp(4, Timestamp.valueOf(departure));
                stmt.setTimestamp(5, Timestamp.valueOf(arrival));

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();

                if (!rs.next())
                    throw new SQLException("Failed to retrieve flightID.");

                flightID = rs.getInt(1);
            }
            
            // ==== 2) CREATE SEATS ====
            Object[][] layout = {
                {"1A", "Business", 450},
                {"1B", "Business", 450},
                {"2A", "Premium Economy", 300},
                {"2B", "Premium Economy", 300},
                {"3A", "Economy", 200},
                {"3B", "Economy", 200}
            };

            List<Integer> seatIDs = new ArrayList<>();

            for (Object[] seat : layout) {
                try (PreparedStatement stmt =
                             connection.prepareStatement(seatSQL, Statement.RETURN_GENERATED_KEYS)) {

                    stmt.setString(1, (String) seat[0]);
                    stmt.setString(2, (String) seat[1]);
                    stmt.setDouble(3, (int) seat[2]);
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    if (!rs.next())
                        throw new SQLException("Seat insert failed.");

                    seatIDs.add(rs.getInt(1));
                }
            }
            
            // ==== 3) MAP SEATS TO FLIGHT ====
            try (PreparedStatement stmt = connection.prepareStatement(mapSQL)) {
                for (int seatID : seatIDs) {
                    stmt.setInt(1, flightID);
                    stmt.setInt(2, seatID);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            // ==== COMMIT ====
            connection.commit();
        
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Flight creation failed: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void updateFlight(int flightId, String name, int routeId, int aircraftId,
            LocalDateTime dep, LocalDateTime arr, String status) {

		String sql = "UPDATE Flight SET flightName=?, routeID=?, aircraftID = ?, departureTime=?, arrivalTime=?, status=? WHERE flightID=?";
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, name);
			stmt.setInt(2, routeId);
			stmt.setInt(3, aircraftId);
			stmt.setTimestamp(4, Timestamp.valueOf(dep));
			stmt.setTimestamp(5, Timestamp.valueOf(arr));
			stmt.setString(6, status);
			stmt.setInt(7, flightId);
			stmt.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public Flight getFlightById(int id) {
        String sql = """
            SELECT f.flightID, f.flightName, f.departureTime, f.arrivalTime,
                   f.status, r.routeID, r.origin, r.destination, r.distance,
                   a.aircraftID, a.model, a.capacity, a.haulType
            FROM Flight f
	        LEFT JOIN Route r ON f.routeID = r.routeID
	        LEFT JOIN aircraft a ON f.aircraftID = a.aircraftID
            WHERE f.flightID = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Route route = null;

                if (rs.getObject("routeID") != null) {
                    route = new Route(
                        rs.getInt("routeID"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getDouble("distance")
                    );
                }
                
                Aircraft ac = null;
	            if (rs.getObject("aircraftID") != null) {
	            	ac = new Aircraft(
	            			rs.getInt("aircraftID"),
	            			rs.getString("model"),
	            			rs.getInt("capacity"),
	            			rs.getString("haulType")
	            		);
	            }

                return new Flight(
                    rs.getInt("flightID"),
                    rs.getString("flightName"),
                    route,
                    ac,
                    rs.getTimestamp("departureTime").toLocalDateTime(),
                    rs.getTimestamp("arrivalTime").toLocalDateTime(),
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
