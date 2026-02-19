package entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User {
	private int userID;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String address;
	private Date dateOfBirth;
	private String roleName;
	private ArrayList<Reservation> reservations;

	public User(int userID, String username, String password, String firstName, String lastName, String emailAddress,
			String roleName) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.roleName = roleName;
		reservations = new ArrayList<>();
	}

	// Getters
	public int getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getRoleName() {
		return roleName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void addReservation(Reservation reservation) {
		reservations.add(reservation);
	}

	public void setReservation(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}

	@Override
	public String toString() {
		return "User{" + "userID = " + userID + ", username='" + username + '\'' + ", role='" + roleName + '\'' + '}';
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDateOfBirth(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try {
			this.dateOfBirth = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
