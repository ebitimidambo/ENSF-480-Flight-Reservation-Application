package boundary.utility;

import entity.Flight;

public interface FlightSelectionListener {
    void onFlightSelected(Flight flight, boolean isDeparture);
    void onFlightDeselected(Flight flight, boolean isDeparture);
}