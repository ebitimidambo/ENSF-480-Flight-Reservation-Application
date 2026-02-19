package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.User;

public class RegistrationDAO {
	private final Connection connection;

	public RegistrationDAO() {
		this.connection = DatabaseManager.getInstance().getConnetcion();
	}

	public int registerUser(User user) {
		try {
			// Step 1. Check if user already registered
			if (checkUserRegistered(user)) {
				return 0; // User already exists
			}

			// Step 2. Get role ID for CUSTOMER
			int roleId = getRoleId("CUSTOMER");
			if (roleId == -1) {
				System.err.println("Error: CUSTOMER role not found.");
				return -1;
			}

			// Step 3. Insert user
			String insertQuery = """
					    INSERT INTO `User`
					    (username, password, firstName, lastName, emailAddress, dateOfBirth, address, roleID)
					    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
					""";

			try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
				stmt.setString(1, user.getUsername());
				stmt.setString(2, user.getPassword());
				stmt.setString(3, user.getFirstName());
				stmt.setString(4, user.getLastName());
				stmt.setString(5, user.getEmailAddress());
				if (user.getDateOfBirth() != null) {
					stmt.setDate(6, new java.sql.Date(user.getDateOfBirth().getTime()));
				} else {
					stmt.setNull(6, java.sql.Types.DATE);
				}
				stmt.setString(7, user.getAddress());
				stmt.setInt(8, roleId);

				int rows = stmt.executeUpdate();
				return rows > 0 ? 1 : -1;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean checkUserRegistered(User user) {
		String checkQuery = "SELECT userID FROM `User` WHERE username = ? OR emailAddress = ?";
		try (PreparedStatement stmt = connection.prepareStatement(checkQuery)) {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getEmailAddress());
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next(); // true if user already exists
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private int getRoleId(String roleName) {
		String query = "SELECT roleID FROM `Role` WHERE roleName = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, roleName);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("roleID");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
