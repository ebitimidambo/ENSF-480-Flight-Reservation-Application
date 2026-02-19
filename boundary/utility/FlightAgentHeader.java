package boundary.utility;

import boundary.AgentCustomersUI;
import boundary.AgentReservationsUI;
import boundary.FlightAgentUI;
import boundary.LoginUI;
import boundary.AgentFlightScheduleUI;
import boundary.MainFrame;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FlightAgentHeader implements Header {

    private static final String FONT = "Segoe UI Light";

    private final User agent;

    public FlightAgentHeader(User agent) {
        this.agent = agent;
    }

    @SuppressWarnings("unused")
	@Override
    public JPanel createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(255, 255, 255));
        
        
        headerPanel.add(Box.createHorizontalStrut(30));

        headerPanel.add(createNavButton(
                "Back",
                e -> {
                    MainFrame mainFrame = MainFrame.getInstance();
                    mainFrame.showPanel(new FlightAgentUI(agent));
                }
        ));

        headerPanel.add(Box.createHorizontalStrut(30));

        headerPanel.add(createNavButton(
                "Manage Customer Data",
                e -> {
                    MainFrame mainFrame = MainFrame.getInstance();
                    mainFrame.showPanel(new AgentCustomersUI(agent));
                }
        ));

        headerPanel.add(Box.createHorizontalStrut(30));

        headerPanel.add(createNavButton(
                "Manage Reservations",
                e -> {
                    MainFrame mainFrame = MainFrame.getInstance();
                    mainFrame.showPanel(new AgentReservationsUI(agent));
                }
        ));

        headerPanel.add(Box.createHorizontalStrut(30));

        headerPanel.add(createNavButton(
                "View Flight Schedule",
                e -> {
                    MainFrame mainFrame = MainFrame.getInstance();
                    mainFrame.showPanel(new AgentFlightScheduleUI(agent));
                }
        ));
        
        headerPanel.add(Box.createHorizontalStrut(30));

        headerPanel.add(createNavButton(
                "Log Out",
                e -> openLogoutPopup()
        ));

        return headerPanel;
    }

    private JButton createNavButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(new Color(255, 255, 255));
        button.setForeground(Color.DARK_GRAY);
        button.setFont(new Font(FONT, Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(action);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(250, 250, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 255, 255));
            }
        });

        return button;
    }
    
    private void openLogoutPopup() {
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

        new javax.swing.Timer(1200, e -> {
            logoutPopup.dispose();

            MainFrame frame = MainFrame.getInstance();
            frame.showPanel(new LoginUI());

            ((javax.swing.Timer) e.getSource()).stop();

        }).start();
   	 
   }
}
