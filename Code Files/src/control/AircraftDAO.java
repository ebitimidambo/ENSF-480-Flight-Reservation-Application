package control;

import java.util.List;

import entity.Aircraft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class AircraftDAO {
	private final Connection connection;
	
	public AircraftDAO() {
		this.connection = DatabaseManager.getInstance().getConnetcion();
	}
	
	public List<Aircraft> getAllAircrafts() {
		List<Aircraft> aircrafts = new ArrayList<>();
        String query = "SELECT aircraftID, model, capacity, haulType FROM Aircraft";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
               Aircraft a = new Aircraft(
                    rs.getInt("aircraftID"),
                    rs.getString("model"),
                    rs.getInt("capacity"),
                    rs.getString("haulType")
                );
                aircrafts.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircrafts;
	}
	
	public void addAircraft(String aircraftModel, int capacity, String haulType) {
        String sql = "INSERT INTO Aircraft (model, capacity, haulType) " +
                     "VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, aircraftModel);
            stmt.setInt(2, capacity);
            stmt.setString(3, haulType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public void deleteAircraft(int aircraftID) {
		String sql = "DELETE FROM Aircraft WHERE aircraftID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, aircraftID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	public Aircraft getAircraftById(int aircraftID) {
		String sql = """
	            SELECT a.aircraftID, a.model, a.capacity, a.haulType
	            FROM Aircraft a
	            WHERE a.aircraftID = ?
	        """;
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
		   stmt.setInt(1, aircraftID);
		   ResultSet rs = stmt.executeQuery();

		   if (rs.next()) {     
			   if (rs.getObject("aircraftID") != null) {
				   return new Aircraft(
						   rs.getInt("aircraftID"),
						   rs.getString("model"),
						   rs.getInt("capacity"),
						   rs.getString("haulType")
		            	);
			   }
		   	}
		}catch (SQLException e) {
	            e.printStackTrace();
		}
		return null;
	}

	public void updateAircraft(int aircraftId, String model, int capacity, String haulType) {
		String sql = "UPDATE Aircraft SET model=?, capacity=?, haulType = ? WHERE aircraftID=?";
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, model);
			stmt.setInt(2, capacity);
			stmt.setString(3, haulType);
			stmt.setInt(4, aircraftId);
			stmt.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}