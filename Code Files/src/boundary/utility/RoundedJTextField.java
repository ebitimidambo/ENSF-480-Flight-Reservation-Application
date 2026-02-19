
package boundary.utility;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class RoundedJTextField extends JTextField {
	private Shape shape;
	private String defaultText;

	public RoundedJTextField(int columns, String label) {
		super(columns);
		this.defaultText = label;
		setOpaque(false); // Make the JTextField transparent
		setText(defaultText);
		
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if(getText().equals(defaultText)) {
					setText("");
				} 
			}
			
			public void focusLost(FocusEvent e) {
				if(getText().isEmpty()) {
					setText(defaultText);
				} 
			}
		});
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); // Adjust corner radius (15, 15)
		super.paintComponent(g2);
		g2.dispose();
	}

	@Override
	protected void paintBorder(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getForeground());
		g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); // Adjust corner radius
		g2.dispose();
	}

	@Override
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
		}
		return shape.contains(x, y);
	}
}