package control;

import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Connection;

public class PaymentDAO {
	private final Connection connection;

	public PaymentDAO() {
		this.connection = DatabaseManager.getInstance().getConnetcion();
	}

	public void recordPayment(int reservationID, double amount, String method) {
		String sql = "INSERT INTO Payment (reservationID, amount, method, status) VALUES (?, ?, ?, 'Completed')";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, reservationID);
			stmt.setDouble(2, amount);
			stmt.setString(3, method);
			stmt.executeUpdate();
			System.out.println("Payment recorded successfully.");
		} catch (SQLException e) {
			System.err.println("Error recording payment: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
