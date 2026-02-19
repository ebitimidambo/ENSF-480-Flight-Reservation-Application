package entity;

public class Route {
	private int routeId;
	private String origin;
	private String destination;
	private double distance;
	private String status;
	
	public Route(int routeId, String origin, String destination, double distance) {
		this.routeId = routeId;
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
	}
	
	public Route(int routeId, String origin, String destination, double distance, String status) {
	    this.routeId = routeId;
	    this.origin = origin;
	    this.destination = destination;
	    this.distance = distance;
	    this.status = status;
	}
	
	public int getRouteId() {return routeId;}
	public String getOrigin() {return origin;}
	public String getDestination() {return destination;}
	public double getDistance() {return distance;}
	public String getStatus() { return status; }
}
