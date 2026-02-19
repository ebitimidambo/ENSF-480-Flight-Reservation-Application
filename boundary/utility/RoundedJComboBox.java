
package boundary.utility;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboBoxUI;

@SuppressWarnings("serial")
public class RoundedJComboBox extends JComboBox<String> {
	private static final int ARC = 15; // corner radius

	public RoundedJComboBox(String[] options) {
		super(options);
		setOpaque(false); // Make the JTextField transparent
		
		setUI(new BasicComboBoxUI() {
			public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setBackground(getBackground());
				g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, ARC, ARC);
				g2.dispose();
			}
			
			protected JButton createArrowButton() {
				 JButton arrow = new JButton("▼");
				 arrow.setBorderPainted(false);
				 arrow.setFocusPainted(false);
				 arrow.setContentAreaFilled(false);
				 arrow.setForeground(Color.GRAY);
				 return arrow;
			}
		});
	}


	@Override
	protected void paintBorder(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getForeground());
		g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC));
		g2.dispose();
	}

}