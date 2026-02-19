package boundary.utility;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;

import control.PromotionController;

import java.awt.*;

import entity.Promotion;
import entity.User;

public class NewsBody implements Body{
	private User user;
	private static final String FONT = "Segoe UI Light";
	
	public NewsBody(User user) {
		this.user = user;
	}

	@Override
	public JPanel createBody() {
		JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Monthly Promotions & Announcements");
        title.setFont(new Font(FONT, Font.BOLD, 22));
        title.setForeground(new Color(45, 59, 73));
        title.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 0));

        bodyPanel.add(title, BorderLayout.NORTH);

        PromotionController controller = new PromotionController();
        
        if (controller.isPromotionReleaseDay()) {
            JOptionPane.showMessageDialog(null,
                "🎉 New monthly promotions have just been released!\n\nCheck them out below.",
                "Monthly Promotion Alert",
                JOptionPane.INFORMATION_MESSAGE);
            
            controller.setShownForSession();
        }
        
        ArrayList<Promotion> promotions = controller.getMonthlyPromotion();

        // --- Table-like scroll area ---
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        for (Promotion promo : promotions) {
            JPanel row = createPromotionRow(promo);
            listPanel.add(row);
            listPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(800, 280)); 

        bodyPanel.add(scrollPane, BorderLayout.CENTER);
        return bodyPanel;
    }

    @SuppressWarnings("unused")
	private JPanel createPromotionRow(Promotion promo) {
        JPanel row = new RoundedPanel(20);
        row.setBackground(new Color(245, 247, 249));
        row.setLayout(new BorderLayout());
        row.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Left: promo info
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel title = new JLabel(promo.getTitle());
        title.setFont(new Font(FONT, Font.BOLD, 16));
        title.setForeground(new Color(45, 59, 73));

        JLabel desc = new JLabel("<html><p style='width:500px;'>" + promo.getDescription() + "</p></html>");
        desc.setFont(new Font(FONT, Font.PLAIN, 14));
        desc.setForeground(new Color(60, 60, 60));

        left.add(title);
        left.add(Box.createVerticalStrut(6));
        left.add(desc);

        // Right: date + button
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));

        JLabel date = new JLabel("Posted: " + promo.getDatePosted().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        date.setFont(new Font(FONT, Font.ITALIC, 13));
        date.setForeground(Color.GRAY);

        JButton claimButton = new JButton("Claim Offer");
        claimButton.setFont(new Font(FONT, Font.PLAIN, 13));
        claimButton.setBackground(new Color(45, 59, 73));
        claimButton.setForeground(Color.WHITE);
        claimButton.setFocusPainted(false);
        claimButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        claimButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(null, 
                "Promotion claimed successfully!\nHappy travels, " + user.getFirstName() + "!",
                "Offer Claimed", JOptionPane.INFORMATION_MESSAGE)
        );

        right.add(date);
        right.add(Box.createVerticalStrut(10));
        right.add(claimButton);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        return row;
	}
}
