package boundary.utility;

import javax.swing.JPanel;

import entity.Flight;

public abstract class FlightBox {
	protected Flight associatedFlight;
	abstract JPanel createBox();
	
	public void assignFlight(Flight flight) {
		this.associatedFlight = flight;
		System.out.println(associatedFlight);
	}
	
	public Flight getFlight() {
		return associatedFlight;
	}
}
