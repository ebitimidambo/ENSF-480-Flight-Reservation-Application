package boundary.utility;

import java.awt.*;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RoundedPanel extends JPanel{
	private int cornerRadius;
	
	public RoundedPanel(int radius) {
		super();
		this.cornerRadius = radius;
		setOpaque(false);
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 =  (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int shadowSize = 8;
		 
		g2.setColor(new Color(0, 0, 0, 60)); // transparent black shadow
        g2.fillRoundRect(shadowSize, shadowSize, getWidth() - shadowSize, getHeight() - shadowSize, cornerRadius, cornerRadius);
        
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth()-shadowSize + 3, getHeight()-shadowSize + 3, cornerRadius, cornerRadius);
        
		g2.setColor(new Color(200,200,200));
		g2.setStroke(new BasicStroke(2f));
		g2.drawRoundRect(1, 1, getWidth() - shadowSize + 3, getHeight() - shadowSize + 3, cornerRadius, cornerRadius);
		
		g2.dispose();
		super.paintComponent(g);
	}
}
