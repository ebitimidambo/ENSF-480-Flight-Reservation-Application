package boundary;

import entity.User;
import boundary.AdminAircraftsUI.AdminNavigation;
import boundary.utility.*;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class AdminUI extends JPanel {

    private User user;
    private AdminGuiFactory factory;
    private Header header;
    private MidSection mid;
    //private Body body;

    private static final String FONT = "Segoe UI Light";

    @SuppressWarnings("unused")
	public AdminUI(User user) {
        this.user = user;
        this.factory = new AdminGuiFactory(user);

        this.header = factory.createHeader();
        if (header instanceof AdminHeader adminHeader) {
            adminHeader.setFlightsListener(e -> openFlightsUI());
            adminHeader.setRoutesListener(e -> openRoutesUI());
            adminHeader.setAircraftListener(e -> openNewUI(AdminNavigation.AIRCRAFT));
            adminHeader.setLogOutListener(e -> openLoginUI());
        }
        
        this.mid = factory.createMidSection();
        //this.body = factory.createBody();

        setupUI();
    }

    private void setupUI() {

        setLayout(new BorderLayout());

        // TOP PANEL = Banner + Admin Header
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(createBanner(), BorderLayout.NORTH);
        topPanel.add(header.createHeader(), BorderLayout.SOUTH); 
        topPanel.setBackground(new Color(45,59,73)); 
        add(topPanel, BorderLayout.NORTH);

        // MID-SECTION = Admin Banner Image
        add(mid.createMidSection(), BorderLayout.CENTER);

        // BOTTOM = Admin Body + Footer
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(45, 59, 73));

        //bottomPanel.add(body.createBody(), BorderLayout.NORTH);
        bottomPanel.add(createFooter(), BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ADMIN BANNER
    private JPanel createBanner() {

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setPreferredSize(new Dimension(0, 90));
        		
        // LOGO + COMPANY NAME
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
		leftPanel.setOpaque(false);
		
		ImageIcon logoIcon =  new ImageIcon("images/AEAELogo.png");
    	Image scaledLogoImage = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);  
    	JLabel logoLabel = new JLabel(new ImageIcon(scaledLogoImage));
    	
    	JLabel companyName = new JLabel("Atlantic European Airways Express");
    	companyName.setForeground(new Color(45,59,73));
    	companyName.setFont(new Font(FONT, Font.BOLD, 20));
    	
    	leftPanel.add(logoLabel);
    	leftPanel.add(companyName);
    	
    	//EMPTY RESIZABLE MIDDLE SPACE
    	JPanel centerPanel = new JPanel();
    	centerPanel.setOpaque(false);

    	//FLAG + WELCOME
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        rightPanel.setOpaque(false);
        
    	String name = (user != null) ? user.getFirstName() : "Administrator";
    	JLabel welcomeMessage = new JLabel("Welcome " + name);
    	welcomeMessage.setForeground(new Color(45,59,73));
    	welcomeMessage.setFont(new Font(FONT, Font.BOLD, 16));
    	
    	ImageIcon countryFlag =  new ImageIcon("images/CanadaFlag.jpg");
    	Image scaledCountryImage = countryFlag.getImage().getScaledInstance(32, 20, Image.SCALE_SMOOTH); 
    	JLabel countryLabel = new JLabel(new ImageIcon(scaledCountryImage));
    	
    	JLabel countryInformation = new JLabel("Canada - EN");
    	countryInformation.setForeground(new Color(45,59,73));
    	countryInformation.setFont(new Font(FONT, Font.BOLD, 16));

        rightPanel.add(countryLabel);
        rightPanel.add(countryInformation);
        rightPanel.add(welcomeMessage);
        
        bannerPanel.add(leftPanel, BorderLayout.WEST);
        bannerPanel.add(centerPanel, BorderLayout.CENTER);
        bannerPanel.add(rightPanel, BorderLayout.EAST);
    	
    	return bannerPanel;
    }

    // ==========================================================
    // FOOTER (reuse or simplify for admin)
    // ==========================================================
    private JPanel createFooter() {

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(245, 245, 240));
        footer.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        JLabel copyright =
            new JLabel("© 2025 Atlantic European Airways Express – Admin Panel", SwingConstants.CENTER);
        copyright.setFont(new Font(FONT, Font.PLAIN, 14));

        footer.add(copyright, BorderLayout.CENTER);

        return footer;
    }
    
    private void openFlightsUI() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new AdminFlightsUI(user));
            frame.revalidate();
            frame.repaint();
    }

    private void openRoutesUI() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new AdminRoutesUI(user));
            frame.revalidate();
            frame.repaint();
    }
    
    private void openLoginUI() {
    	//confirm popup
    	int confirm = JOptionPane.showConfirmDialog(
    	        this,
    	        "Are you sure you want to log out?",
    	        "Confirm Logout",
    	        JOptionPane.YES_NO_OPTION,
    	        JOptionPane.QUESTION_MESSAGE
    	    );

    	// User canceled → do nothing
    	if (confirm != JOptionPane.YES_OPTION) {
    	    return;
    	}
    	
    	//shadow stall
    	JWindow logoutPopup = new JWindow();
        logoutPopup.setBackground(new Color(0, 0, 0, 0)); // full transparency

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Glass background
                g2.setColor(new Color(30, 30, 30, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Soft glow border
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 25, 25);

                g2.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 60, 25, 60));

        JLabel label = new JLabel("Logging out…", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        label.setForeground(Color.WHITE);

        panel.add(label, BorderLayout.CENTER);
        logoutPopup.add(panel);

        // Size + center
        logoutPopup.pack();
        logoutPopup.setLocationRelativeTo(null);
        logoutPopup.setAlwaysOnTop(true);
        logoutPopup.setVisible(true);

        new Timer(1200, e -> {
        	logoutPopup.dispose();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new LoginUI());
            frame.revalidate();
            frame.repaint();
            ((Timer) e.getSource()).stop();

        }).start();
   	 
   }
    
    private void openNewUI(AdminNavigation page) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().setVisible(false);
        switch (page) {
        case MAIN -> frame.setContentPane(new AdminUI(user));
        case ROUTES    -> frame.setContentPane(new AdminRoutesUI(user));
        case FLIGHTS   -> frame.setContentPane(new AdminFlightsUI(user));
        case AIRCRAFT  -> frame.setContentPane(new AdminAircraftsUI(user));
    }
        frame.invalidate();
        frame.validate();
        frame.getContentPane().setVisible(true);
    }
}