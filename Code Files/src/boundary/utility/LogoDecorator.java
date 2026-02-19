package boundary.utility;

import java.awt.*;
import javax.swing.*;

public class LogoDecorator extends FlightBoxDecorator {
	private ImageIcon logo;
	
	public LogoDecorator(FlightBox decoratedBox, ImageIcon logo) {
        super(decoratedBox);
        this.logo = logo;
    }
	
	public JPanel createBox() {
		JPanel panel = super.createBox();
		
		int width = 50; 
        int height = 50;
        Image scaledImage = logo.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        
		JLabel logoLabel = new JLabel(scaledIcon);
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(Box.createVerticalStrut(20));
        panel.add(logoLabel, BorderLayout.NORTH);
		return panel;
	}
}