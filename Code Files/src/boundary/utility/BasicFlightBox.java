package boundary.utility;

import java.awt.*;


import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class BasicFlightBox extends FlightBox{
	public JPanel createBox() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(150, 150));
		panel.setMaximumSize(new Dimension(150, 150));
		panel.setBackground(new Color(230, 230, 230));
		panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255)));
		return panel;
	}
}
