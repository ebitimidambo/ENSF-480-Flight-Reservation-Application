package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.User;

public class UserDAO {
	private final Connection connection;
	
	public UserDAO() {
		this.connection = DatabaseManager.getInstance().getConnetcion();
	}
	
	public User getUser(String username, String password) {
		String query = "SELECT u.userID, u.username, u.password, u.firstName, u.lastName, " +
                	   "u.emailAddress, r.roleName " +
                	   "FROM User u " +
                	   "JOIN Role r ON u.roleID = r.roleID " +
                	   "WHERE u.username = ? AND u.password = ?";
		try (PreparedStatement stmt =  connection.prepareStatement(query))
		{
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return new User(
						rs.getInt("userID"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("emailAddress"),
						rs.getString("roleName")
					);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public User getUserById(int userId) {
        String query = """
                SELECT u.userID, u.username, u.password, u.firstName, u.lastName,
                       u.emailAddress, u.address, r.roleName, u.dateOfBirth
                FROM User u
                JOIN Role r ON u.roleID = r.roleID
                WHERE u.userID = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("emailAddress"),
                        rs.getString("roleName")
                );
                user.setAddress(rs.getString("address"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();

        String query = """
                SELECT u.userID, u.username, u.password,
                       u.firstName, u.lastName, u.emailAddress, u.address,
                       r.roleName
                FROM User u
                JOIN Role r ON u.roleID = r.roleID
                WHERE r.roleName = 'CUSTOMER'
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("emailAddress"),
                        rs.getString("roleName")
                );
                user.setAddress(rs.getString("address"));
                customers.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    
    public void updateCustomerProfile(User u) {

        String sql = """
            UPDATE User
            SET firstName = ?,
                lastName = ?,
                username = ?,
                password = ?,
                emailAddress = ?,
                address = ?
            WHERE userID = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getEmailAddress());
            ps.setString(6, u.getAddress());
            ps.setInt(7, u.getUserID());

            int count = ps.executeUpdate();
            if (count == 0)
                throw new RuntimeException("No rows updated");
        } catch (SQLException e) {
            throw new RuntimeException("Customer update failed: " + e.getMessage(), e);
        }
    }

	public void addCustomer(String first, String last, String username, String password, String email, String address, LocalDate dob) {
		String sql = "INSERT INTO `User` (username, password, firstName, lastName, emailAddress, address, dateOfBirth, roleID)\r\n"
				+ "	VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
	   try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	       stmt.setString(1, username);
	       stmt.setString(2, password);
	       stmt.setString(3, first);
	       stmt.setString(4, last);
	       stmt.setString(5, email);
	       stmt.setString(6, address);
	       stmt.setDate(7, java.sql.Date.valueOf(dob));
	       stmt.executeUpdate();
	   } catch (SQLException e) {
		   throw new RuntimeException("Add customer failed: " + e.getMessage(), e);
	   }
	}
	
	public void deleteUser(int userID) {
        String sql = "DELETE FROM User WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
