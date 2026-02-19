package boundary.utility;

import javax.swing.*;
import java.awt.*;

public class FlightAgentMidSection implements MidSection {

    private static final String FONT = "Segoe UI Light";

    @Override
    public JPanel createMidSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 59, 73));

        JLabel title = new JLabel("Flight Agent Dashboard", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font(FONT, Font.BOLD, 28));

        JLabel subtitle = new JLabel(
            "<html><div style='text-align:center;'>Access tools to manage customer data, " +
            "reservations, and flight schedules.</div></html>",
            SwingConstants.CENTER
        );
        subtitle.setForeground(new Color(230, 230, 230));
        subtitle.setFont(new Font(FONT, Font.PLAIN, 16));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(Box.createVerticalStrut(40));
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(15));
        textPanel.add(subtitle);

        JPanel centeredWrapper = new JPanel(new GridBagLayout());
        centeredWrapper.setOpaque(false);
        centeredWrapper.add(textPanel);

        panel.add(centeredWrapper, BorderLayout.CENTER);

        return panel;
    }
}
