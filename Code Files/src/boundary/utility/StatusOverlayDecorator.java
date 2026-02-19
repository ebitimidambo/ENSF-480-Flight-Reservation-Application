package boundary.utility;

import java.awt.*;
import javax.swing.*;

public class StatusOverlayDecorator extends FlightBoxDecorator {
    private boolean isCancelled;
    
    public StatusOverlayDecorator(FlightBox decoratedBox, boolean isCancelled) {
        super(decoratedBox);
        this.isCancelled = isCancelled;
    }

    public JPanel createBox() {
        // Call the super method to get the fully assembled JPanel (with logo/info labels)
        JPanel panel = super.createBox();

        // Check availability status and set the background color of the existing panel
        if (isCancelled) {
            // Green color for available flights
        	panel.setBackground(new Color(255, 200, 200)); 
        } else {
            // Red/Pink color for booked/unavailable flights
        	panel.setBackground(new Color(200, 255, 200)); 
        }

        // Return the modified existing panel
        return panel;
    }
}
