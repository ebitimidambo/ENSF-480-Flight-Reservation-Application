package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	private static DatabaseManager instance;
	private Connection connection;
	
	private static final String URL = "jdbc:mysql://localhost:3306/flight_reservation";
	private static final String USER = "root";
	private static final String PASSWORD = "[Your password here]";
	
	private DatabaseManager() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Database connected successfully!");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Failed to connect to database: " + e.getMessage());
		}
	}
	
	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			instance =  new DatabaseManager();
		}
		return instance;
	}
	
	public Connection getConnetcion() {
		return connection;
	}
}

