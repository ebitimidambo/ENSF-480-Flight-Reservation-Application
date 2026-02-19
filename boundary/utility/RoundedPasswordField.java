package boundary.utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

@SuppressWarnings("serial")
public class RoundedPasswordField extends JPasswordField {
    private Shape shape;
    // Define the corner radius
    private static final int ARC_WIDTH = 15;
    private static final int ARC_HEIGHT = 15;
    private String defaultText;
    boolean isPlaceHolderActive = true;

    public RoundedPasswordField(int columns, String label) {
        super(columns);
        this.defaultText = label;
        setOpaque(false); // We must be non-opaque since we won't fill all pixels
        // Add an empty border to compensate for the rounded corners' margins
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        setEchoChar((char) 0);
        setText(defaultText);
        
        addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if(isPlaceHolderActive) {
					setText("");
					setEchoChar('*');
					isPlaceHolderActive = false;
				} 
			}
			
			public void focusLost(FocusEvent e) {
				if(getPassword().length == 0) {
					setEchoChar((char) 0);
					setText(defaultText);
					isPlaceHolderActive = true;
				} 
			}
		});
		
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        // Fills the rounded rectangle
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
        super.paintComponent(g); // Paint the text content on top
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        // Draws the outline of the rounded rectangle
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
    }

    @Override
    public boolean contains(int x, int y) {
        // Ensures events (like clicks) only register within the round shape
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
        }
        return shape.contains(x, y);
    }
}