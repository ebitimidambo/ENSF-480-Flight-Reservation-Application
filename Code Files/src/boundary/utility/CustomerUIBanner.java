package boundary.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.User;

public class CustomerUIBanner {
	private static final String FONT = "Segoe UI Light";
	
	public static JPanel createBanner(User user) {
		JPanel bannerPanel = new JPanel(new BorderLayout());
		
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		leftPanel.setOpaque(false);
		
		ImageIcon logoIcon =  new ImageIcon("images/AEAELogo.png");
    	Image logoImage = logoIcon.getImage();
    	Image scaledLogoImage = logoImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);  // Width: 100px, Height: 100px
    	ImageIcon scaledLogoIcon = new ImageIcon(scaledLogoImage);
    	JLabel logoLabel = new JLabel(scaledLogoIcon);
    	logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    	
    	JLabel companyName = new JLabel("Atlantic European Airways Express");
    	companyName.setForeground(new Color(45,59,73));
    	companyName.setFont(new Font(FONT, Font.BOLD, 20));
    	
    	leftPanel.add(Box.createHorizontalStrut(20));
    	leftPanel.add(logoLabel);
    	leftPanel.add(Box.createHorizontalStrut(20));
    	leftPanel.add(companyName);
    	
    	String name = (user != null) ? user.getFirstName() : "PlaceHolder";
    	JLabel welcomeMessage = new JLabel("Welcome " + name);
    	welcomeMessage.setForeground(new Color(45,59,73));
    	welcomeMessage.setFont(new Font(FONT, Font.BOLD, 16));
    	
    	ImageIcon countryFlag =  new ImageIcon("images/CanadaFlag.jpg");
    	Image countryImage = countryFlag.getImage();
    	Image scaledCountryImage = countryImage.getScaledInstance(30, 20, Image.SCALE_SMOOTH);  // Width: 100px, Height: 100px
    	ImageIcon scaledCountryIcon = new ImageIcon(scaledCountryImage);
    	JLabel countryLabel = new JLabel(scaledCountryIcon);
    	
    	JLabel countryInformation = new JLabel("Canada - EN");
    	countryInformation.setForeground(new Color(45,59,73));
    	countryInformation.setFont(new Font(FONT, Font.BOLD, 16));
    	
    	JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 30));
        rightPanel.setOpaque(false);
        rightPanel.add(countryLabel);
        rightPanel.add(countryInformation);
        rightPanel.add(welcomeMessage);
        rightPanel.add(Box.createHorizontalStrut(20));
   
        bannerPanel.add(leftPanel, BorderLayout.WEST);
        bannerPanel.add(rightPanel, BorderLayout.EAST);
    	
    	return bannerPanel;
	}
	
}
