package boundary;

import entity.User;
import boundary.utility.*;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class FlightAgentUI extends JPanel {
    private User user;
    private FlightAgentGuiFactory factory;
    private Header header;
    private MidSection mid;
    private Body body;
    private static final String FONT = "Segoe UI Light";

    public FlightAgentUI(User user) {
        this.user = user;
        this.factory = new FlightAgentGuiFactory(user);
        this.header = factory.createHeader();
        this.mid = factory.createMidSection();
        this.body = factory.createBody();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(createBanner(), BorderLayout.NORTH);
        topPanel.add(header.createHeader(), BorderLayout.SOUTH);
        topPanel.setBackground(new Color(45, 59, 73));

        add(topPanel, BorderLayout.NORTH);
        add(mid.createMidSection(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(body.createBody(), BorderLayout.NORTH);
        bottomPanel.add(createFooter(), BorderLayout.SOUTH);
        bottomPanel.setBackground(new Color(45, 59, 73));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createBanner() {
        JPanel bannerPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        ImageIcon logoIcon = new ImageIcon("images/AEAELogo.png");
        Image logoImage = logoIcon.getImage();
        Image scaledLogoImage = logoImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledLogoImage);
        JLabel logoLabel = new JLabel(scaledLogoIcon);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel companyName = new JLabel("Atlantic European Airways Express – Agent Portal");
        companyName.setForeground(new Color(45, 59, 73));
        companyName.setFont(new Font(FONT, Font.BOLD, 20));

        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(companyName);

        String name = (user != null) ? user.getFirstName() : "Agent";
        JLabel welcomeMessage = new JLabel("Welcome " + name);
        welcomeMessage.setForeground(new Color(45, 59, 73));
        welcomeMessage.setFont(new Font(FONT, Font.BOLD, 16));

        ImageIcon countryFlag = new ImageIcon("images/CanadaFlag.jpg");
        Image countryImage = countryFlag.getImage();
        Image scaledCountryImage = countryImage.getScaledInstance(30, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledCountryIcon = new ImageIcon(scaledCountryImage);
        JLabel countryLabel = new JLabel(scaledCountryIcon);

        JLabel countryInformation = new JLabel("Canada - EN");
        countryInformation.setForeground(new Color(45, 59, 73));
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

        JLabel agentTools = new JLabel("Agent tools");
        agentTools.setForeground(Color.BLACK);
        agentTools.setFont(new Font(FONT, Font.BOLD, 16));

        JLabel serviceEntry1 = new JLabel("Manage customer data");
        serviceEntry1.setForeground(Color.BLACK);
        serviceEntry1.setFont(new Font(FONT, Font.BOLD, 12));

        JLabel serviceEntry2 = new JLabel("Manage reservations");
        serviceEntry2.setForeground(Color.BLACK);
        serviceEntry2.setFont(new Font(FONT, Font.BOLD, 12));

        JLabel aboutUs = new JLabel("About AEAE");
        aboutUs.setForeground(Color.BLACK);
        aboutUs.setFont(new Font(FONT, Font.BOLD, 16));

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
        leftPanel.add(agentTools);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(serviceEntry1);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(serviceEntry2);

        middlePanel.add(Box.createVerticalStrut(10));
        middlePanel.add(aboutUs);
        middlePanel.add(Box.createVerticalStrut(10));
        middlePanel.add(aboutEntry1);
        middlePanel.add(Box.createVerticalStrut(10));
        middlePanel.add(aboutEntry2);

        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(downloadApp);

        footersPanel.add(leftPanel);
        footersPanel.add(middlePanel);
        footersPanel.add(rightPanel);

        return footersPanel;
    }
}
