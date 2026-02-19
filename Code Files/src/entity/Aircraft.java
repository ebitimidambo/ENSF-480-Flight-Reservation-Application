package entity;

public class Aircraft {
	private int aircraftId;
	private String aircraftModel;
	private int capacity;
	private String haulType;
	
	public Aircraft(int id, String model, int capacity, String haul) {
		this.aircraftId = id;
		this.aircraftModel = model;
		this.capacity = capacity;
		this.haulType = haul;
	}
	
	public int getAircraftId() {return aircraftId;}
	public String getAircraftModel() {return aircraftModel;}
	public int getCapacity() {return capacity;}
	public String getHaulType() {return haulType;}
}
