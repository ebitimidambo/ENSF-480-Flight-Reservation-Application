package boundary.utility;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class CustomerMidSection implements MidSection {
	private static final String FONT = "Segoe UI Light";
	
	public JPanel createMidSection() {
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(0, 350)); // adjust to fit your layout

		JLabel backgroundLabel = new JLabel();
		backgroundLabel.setBounds(0, 0, 1920, 350);
		layeredPane.add(backgroundLabel, Integer.valueOf(0));
		
		JPanel overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 200, 100, 70)); // semi-transparent black
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                g2.setColor(new Color(0, 0, 0, 120)); 
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 25, 25);

                // --- Optional subtle outline ---
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 25, 25);
                
                g2.dispose();
            }
        };
        
        overlayPanel.setOpaque(false);
        layeredPane.add(overlayPanel, Integer.valueOf(1));
		
        
		JLabel infoLabel = new JLabel("<html><div style='width:180px; text-align:center;'>"
				+ "<b>Enjoy a world-class experience with AEAE</b></div></html>");
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(new Font(FONT, Font.BOLD, 18));
		overlayPanel.add(infoLabel, new GridBagConstraints());
		
		JLabel infoLabel2 = new JLabel("<html><div style='width:190px; text-align:center;'> "
				+ "<i>Canada is amazing every season. What are you waiting for? "
				+ "Book your next trip and explore!</div></html></i>");
		infoLabel2.setForeground(Color.WHITE);
		infoLabel2.setFont(new Font(FONT, Font.BOLD, 18));
		overlayPanel.add(infoLabel2, new GridBagConstraints());
		
		
		ImageIcon backgroundImage = new ImageIcon("images/CalgR.jpg");

		// --- Keep background full width when resized ---
		layeredPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = layeredPane.getWidth();
				int height = 350;
				
				Image scaled = backgroundImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
				backgroundLabel.setIcon(new ImageIcon(scaled));
				backgroundLabel.setBounds(0, 0, width, height);
				
				int rectWidth = (int) (width * 0.35)-250;  
                int rectHeight = (int) (height * 0.4)-40; 
                // Control Image Size using x and y coordinates
                overlayPanel.setBounds(1000, 18, rectWidth, rectHeight); 
                
                int newFontSize = Math.max(12, rectWidth / 30);
				infoLabel.setFont(new Font(FONT, Font.BOLD, newFontSize));
				infoLabel2.setFont(new Font(FONT, Font.BOLD, newFontSize));
			}
		});

		JPanel midPanel = new JPanel(new BorderLayout());
		midPanel.add(layeredPane, BorderLayout.CENTER);

		return midPanel;
	}
}
