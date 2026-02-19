package boundary.utility;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CustomerUIFooter {
	private static final String FONT = "Segoe UI Light";
	
	public static JPanel createFooter() {
		JPanel footersPanel = new JPanel(new GridLayout(1, 3, 50, 0));
		footersPanel.setBackground(new Color(245, 245, 240));
		footersPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setOpaque(false);
		
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.setOpaque(false);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setOpaque(false);
		
		JLabel CustomerService = new JLabel("Customer service");
		CustomerService.setForeground(Color.BLACK);
		CustomerService.setFont(new Font(FONT, Font.BOLD, 16));
		
		JLabel serviceEntry1 = new JLabel("Refund");
		serviceEntry1.setForeground(Color.BLACK);
		serviceEntry1.setFont(new Font(FONT, Font.BOLD, 12));
		
		JLabel serviceEntry2 = new JLabel("Claims");
		serviceEntry2.setForeground(Color.BLACK);
		serviceEntry2.setFont(new Font(FONT, Font.BOLD, 12));
		
		JLabel AboutUs = new JLabel("About AEAE");
		AboutUs.setForeground(Color.BLACK);
		AboutUs.setFont(new Font(FONT, Font.BOLD, 16));
		
		JLabel aboutEntry1 = new JLabel("Staff");
		aboutEntry1.setForeground(Color.BLACK);
		aboutEntry1.setFont(new Font(FONT, Font.BOLD, 12));
		
		JLabel aboutEntry2 = new JLabel("Vision");
		aboutEntry2.setForeground(Color.BLACK);
		aboutEntry2.setFont(new Font(FONT, Font.BOLD, 12));
		
		JLabel downloadApp = new JLabel("Download The App");
		downloadApp.setForeground(Color.BLACK);
		downloadApp.setFont(new Font(FONT, Font.BOLD, 16));
		
		leftPanel.add(Box.createVerticalStrut(10));
		leftPanel.add(CustomerService);
		leftPanel.add(Box.createVerticalStrut(10));
		leftPanel.add(serviceEntry1);
		leftPanel.add(Box.createVerticalStrut(10));
		leftPanel.add(serviceEntry2);
		
		middlePanel.add(Box.createVerticalStrut(10));
		middlePanel.add(AboutUs);
		middlePanel.add(Box.createVerticalStrut(10));
		middlePanel.add(aboutEntry1);
		middlePanel.add(Box.createVerticalStrut(10));
		middlePanel.add(aboutEntry2);
		
		rightPanel.add(Box.createVerticalStrut(10));
		rightPanel.add(downloadApp);
		
		footersPanel.add(leftPanel, BorderLayout.WEST);
		footersPanel.add(middlePanel, BorderLayout.CENTER);
		footersPanel.add(rightPanel, BorderLayout.EAST);
		
		return footersPanel;
	}
}
