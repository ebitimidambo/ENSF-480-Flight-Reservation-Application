package boundary.utility;

import java.awt.*;
import javax.swing.*;

public class InfoDecorator extends FlightBoxDecorator {
    private String infoText;
    
    public InfoDecorator(FlightBox decoratedBox, String infoText) {
    	super(decoratedBox);
    	this.infoText = infoText;
    }
    
    public JPanel createBox() {
    	JPanel panel = super.createBox();
    	if (!(panel.getLayout() instanceof BorderLayout)) {
            panel.setLayout(new BorderLayout());
        }

    	JLabel infoLabel = new JLabel("<html><div style='text-align:center;'>" + infoText + "</div></html>");
        infoLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, BorderLayout.CENTER);
        return panel;
    }
}