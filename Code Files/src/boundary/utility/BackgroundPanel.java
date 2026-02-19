package boundary.utility;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BackgroundPanel extends JPanel{
	private Image backgroundImage;
	
	public BackgroundPanel(String imagePath) {
		backgroundImage = new ImageIcon("images/" + imagePath).getImage();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}
}
