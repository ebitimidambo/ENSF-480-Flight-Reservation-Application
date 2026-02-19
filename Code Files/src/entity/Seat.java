package entity;

public class Seat {
	private int seatId;
	private String seatNumber;
	private String seatClass;
	private double seatPrice;
	private boolean isAvailable;

	public Seat(int seatId, String seatNumber, String seatClass, double seatPrice, boolean isAvailable) {
		this.seatId = seatId;
		this.seatNumber = seatNumber;
		this.seatClass = seatClass;
		this.seatPrice = seatPrice;
		this.isAvailable = isAvailable;
	}
	
	public int getSeatId() {return seatId;}
	public String getSeatNumber() {return seatNumber;}
	public String getSeatClass() {return seatClass;}
	public double getSeatPrice() {return seatPrice;}
	public boolean getAvailability() {return isAvailable;}
	
	public void setAvailability(boolean value) {this.isAvailable = value;}
}
