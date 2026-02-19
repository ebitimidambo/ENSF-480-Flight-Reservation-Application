package boundary.utility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class NewsMidSection implements MidSection{
	
	public JPanel createMidSection() {
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(0, 350)); // adjust to fit your layout

		JLabel backgroundLabel = new JLabel();
		backgroundLabel.setBounds(0, 0, 1920, 350);
		layeredPane.add(backgroundLabel, Integer.valueOf(0));
        
		
		ImageIcon backgroundImage = new ImageIcon("images/Promotions.png");

		// --- Keep background full width when resized ---
		layeredPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = layeredPane.getWidth();
				int height = 250;
				
				Image scaled = backgroundImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
				backgroundLabel.setIcon(new ImageIcon(scaled));
				backgroundLabel.setBounds(0, 0, width, height);
			}
		});

		JPanel midPanel = new JPanel(new BorderLayout());
		midPanel.add(layeredPane, BorderLayout.CENTER);

		return midPanel;
	}
}
