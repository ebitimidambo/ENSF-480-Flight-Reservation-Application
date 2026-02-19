package boundary.utility;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.Shape;

@SuppressWarnings("serial")
public class RoundedButton extends JButton {
    private Shape shape;
    // Define the corner radius
    private static final int ARC_WIDTH = 15;
    private static final int ARC_HEIGHT = 15;
    private boolean drawBorder =  true;

    public RoundedButton(String label) {
        super(label);
        // Important settings to prevent the default square background/border from showing
        setContentAreaFilled(false); // Do not paint the standard button background
        setFocusPainted(false); // Do not paint the focus rectangle
        setBorderPainted(false); // Do not paint the default border
        // Add an empty border to provide internal spacing for the text
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
    }
    
    public void setDrawBorder(boolean draw) {
    	drawBorder = draw;
    	repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Use Graphics2D for smoother rendering (anti-aliasing)
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint the rounded background
        if (getModel().isArmed()) {
            g2.setColor(getBackground().darker()); // Darker color when pressed
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter()); // Brighter color on hover
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);

        g2.dispose(); // Release graphic resources

        super.paintComponent(g); // Paint the label/text on top
    }

    @Override
    protected void paintBorder(Graphics g) {
    	if (!drawBorder) return;
    	
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint the rounded border
        g2.setColor(getForeground()); // Use the foreground color for the border
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
        
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        // Ensures clicks only register within the round shape boundaries
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
        }
        return shape.contains(x, y);
    }
}