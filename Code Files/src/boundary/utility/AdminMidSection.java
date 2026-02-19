package boundary.utility;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;


public class AdminMidSection implements MidSection{
	private static final String FONT = "Segoe UI Light";
	
	@Override
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
        overlayPanel.setLayout(new GridBagLayout());
        layeredPane.add(overlayPanel, Integer.valueOf(1));
		
        
		JLabel infoLabel = new JLabel("<html><div style='text-align:center;'>"
				+ "<b>WELCOME ADMINISTRATOR</b></div></html>");
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(new Font(FONT, Font.BOLD, 18));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		overlayPanel.add(infoLabel, gbc);
		
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

		        int rectWidth = (int)(width * 0.35);
		        int rectHeight = (int)(height * 0.40);

		        rectWidth = Math.max(rectWidth, 250);
		        rectHeight = Math.max(rectHeight, 120);

		        int x = (width - rectWidth) / 2;
		        int y = (height - rectHeight) / 2;

		        overlayPanel.setBounds(x, y, rectWidth, rectHeight);

		        int newFontSize = Math.min(28, Math.max(14, rectWidth / 22));
		        infoLabel.setFont(new Font(FONT, Font.BOLD, newFontSize));
		    }
		});

		JPanel midPanel = new JPanel(new BorderLayout());
		midPanel.add(layeredPane, BorderLayout.CENTER);
		midPanel.setBackground(new Color(255, 255, 255));
		return midPanel;
	}
}