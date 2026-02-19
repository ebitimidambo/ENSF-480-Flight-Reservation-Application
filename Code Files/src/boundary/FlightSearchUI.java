package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import boundary.utility.Body;
import boundary.utility.FlightSearchGuiFactory;
import boundary.utility.Header;
import boundary.utility.UserGuiFactory;
import entity.Flight;
import entity.User;

@SuppressWarnings("serial")
public class FlightSearchUI extends JPanel{
	private User user;
	private UserGuiFactory factory;
	private Body body;
	private Header header;
	@SuppressWarnings("unused")
	private Map<String, Object> searchPrompts;
	@SuppressWarnings("unused")
	private List<Flight> departureFlights;
	@SuppressWarnings("unused")
	private List<Flight> arrivalFlights;
	private static final String FONT = "Segoe UI Light";
	
	public FlightSearchUI(User user, Map<String, Object> searchPrompts, List<Flight> departureFlights,
			List<Flight> arrivalFlights) {
		this.user = user;
		this.factory = new FlightSearchGuiFactory(user, searchPrompts, departureFlights, arrivalFlights);
		this.body = factory.createBody();
		this.header = factory.createHeader();
		this.searchPrompts = searchPrompts;
		this.departureFlights = departureFlights;
		this.arrivalFlights = arrivalFlights;
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
	    topPanel.setLayout(new BorderLayout());
	    topPanel.add(createBanner(), BorderLayout.NORTH);
	    topPanel.add(header.createHeader(), BorderLayout.SOUTH);
	    topPanel.setBackground(new Color(45,59,73));
	    add(topPanel, BorderLayout.NORTH);
	    
	    JPanel bottomPanel = new JPanel();
	    bottomPanel.setLayout(new BorderLayout());
	    bottomPanel.add(body.createBody(), BorderLayout.NORTH);
	    bottomPanel.add(createFooter(), BorderLayout.SOUTH);
	    bottomPanel.setBackground(new Color(45,59,73));
	    add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private JPanel createBanner() {
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
	
	private JPanel createFooter() {
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
