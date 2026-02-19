package control;

import java.sql.Connection;
import java.sql.PreparedStatement;

import entity.Route;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.List;


public class RouteDAO {
	private final Connection connection;
	
	public RouteDAO() {
		this.connection = DatabaseManager.getInstance().getConnetcion();
	}
	
	public String[] getOrigins() {
		ArrayList<String> origins = new ArrayList<>();
		
		try {
			String query = "SELECT DISTINCT origin FROM route WHERE status = 'Active'";
		
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				origins.add(rs.getString("origin"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
		return origins.toArray(new String[0]);
	}
	
	public String[] getDestinations() {
		ArrayList<String> origins = new ArrayList<>();
		
		try {
			String query = "SELECT DISTINCT destination FROM route WHERE status = 'Active'";
		
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				origins.add(rs.getString("destination"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
		return origins.toArray(new String[0]);
	}
	
	public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String query = "SELECT routeID, origin, destination, distance FROM Route";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Route r = new Route(
                    rs.getInt("routeID"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getDouble("distance")
                );
                routes.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }
	
	public List<Route> getAllRoutesForAdmin() {
        List<Route> routes = new ArrayList<>();

        String sql = """
            SELECT routeID, origin, destination, distance, status
            FROM Route
            ORDER BY routeID;
        """;

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                routes.add(new Route(
                    rs.getInt("routeID"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getDouble("distance"),
                    rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }

    public Route getRouteById(int id) {
        String sql = """
            SELECT routeID, origin, destination, distance, status
            FROM Route
            WHERE routeID = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Route(
                    rs.getInt("routeID"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getDouble("distance"),
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateRoute(int routeId, String origin, String destination,
                            double distance, String status)
    {
        String sql = """
            UPDATE Route
            SET origin = ?, destination = ?, distance = ?, status = ?
            WHERE routeID = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, origin);
            stmt.setString(2, destination);
            stmt.setDouble(3, distance);
            stmt.setString(4, status);
            stmt.setInt(5, routeId);
            stmt.executeUpdate();
            
            if (status.equalsIgnoreCase("Inactive")) {
            	FlightDAO flightdao = new FlightDAO();
                flightdao.cancelFlightsByRoute(routeId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public void addRoute(String origin, String destination, double distance) {
        String sql = "INSERT INTO Route (origin, destination, distance, status) " +
                     "VALUES (?, ?, ?, 'Active')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, origin);
            stmt.setString(2, destination);
            stmt.setDouble(3, distance);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoute(int routeId) {
    	FlightDAO flightDAO = new FlightDAO();
        flightDAO.cancelFlightsByRoute(routeId);
    	
        String sql = "DELETE FROM Route WHERE routeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, routeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
