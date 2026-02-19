package boundary.utility;

import entity.User;


import javax.swing.*;

import boundary.AgentReservationsUI;
import boundary.MainFrame;

import boundary.AgentCustomersUI;
import boundary.AgentFlightScheduleUI;

import java.awt.*;
import java.awt.event.ActionListener;

public class FlightAgentBody implements Body {

    private static final String FONT = "Segoe UI Light";
    private final User agent;

    public FlightAgentBody(User agent) {
        this.agent = agent;
    }

    @SuppressWarnings("unused")
	@Override
    public JPanel createBody() {
        JPanel outer = new JPanel();
        outer.setLayout(new BorderLayout());
        outer.setBackground(new Color(245, 245, 240));
        outer.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JPanel grid = new JPanel(new GridLayout(1, 3, 25, 0));
        grid.setOpaque(false);

    // 1) Manage Customer Data
        grid.add(createCard(
            "Manage Customer Data",
            "Update passenger details, contact info, and profiles.",
            e -> {
                MainFrame mainFrame = MainFrame.getInstance();
                mainFrame.showPanel(new AgentCustomersUI(agent));
            }
        ));

    // 2) Manage Reservations
        grid.add(createCard(
            "Manage Reservations",
            "View, modify, or cancel customer reservations.",
            e -> {
                MainFrame mainFrame = MainFrame.getInstance();
                mainFrame.showPanel(new AgentReservationsUI(agent));
            }
        ));

    // 3) View Flight Schedule
        grid.add(createCard(
            "View Flight Schedule",
            "Browse and verify current flight schedules.",
            e -> {
                MainFrame mainFrame = MainFrame.getInstance();
                mainFrame.showPanel(new AgentFlightScheduleUI(agent));
            }
        ));

    outer.add(grid, BorderLayout.CENTER);
    return outer;
}


    private JPanel createCard(String title, String description, ActionListener action) {
        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(new Color(255, 255, 255));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(FONT, Font.BOLD, 16));
        titleLabel.setForeground(new Color(45, 59, 73));

        JLabel descLabel = new JLabel(
                "<html><div style='width:180px;'>" + description + "</div></html>"
        );
        descLabel.setFont(new Font(FONT, Font.PLAIN, 13));
        descLabel.setForeground(new Color(80, 80, 80));

        JButton actionButton = new RoundedButton("Open");
        actionButton.setFont(new Font(FONT, Font.BOLD, 13));
        actionButton.setForeground(Color.WHITE);
        actionButton.setBackground(new Color(45, 59, 73));
        actionButton.setFocusPainted(false);

        actionButton.addActionListener(action);

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(titleLabel);
        top.add(Box.createVerticalStrut(10));
        top.add(descLabel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(actionButton);

        card.add(top, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }
    
    /*
    private void showNotImplemented(String featureName) {
        JOptionPane.showMessageDialog(
                null,
                featureName + " is not implemented yet.",
                "Flight Agent Action",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    */
}
