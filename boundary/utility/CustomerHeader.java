package boundary.utility;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import boundary.CustomerReservationsUI;
import boundary.LoginUI;
import boundary.MainFrame;
import boundary.PromotionNewsUI;
import entity.User;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class CustomerHeader implements Header{
	private User user;
	
	public CustomerHeader(User user) {
		this.user = user;
	}
	
	public JPanel createHeader() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setBackground(new Color(255,255,255));
    	
		JButton viewReservations = createHeaderButton("View Reservations");
		
		viewReservations.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			Timer goBack = new Timer(10, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						 MainFrame mainFrame = MainFrame.getInstance();
					     mainFrame.showPanel(new CustomerReservationsUI(user));
					}
				});
    			
    			goBack.setRepeats(false);
    			goBack.start();
    		}
		});
		
		JButton promotionNews = createHeaderButton("Promotion News");
		
		promotionNews.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			Timer goBack = new Timer(10, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						 MainFrame mainFrame = MainFrame.getInstance();
					     mainFrame.showPanel(new PromotionNewsUI(user));
					}
				});
    			
    			goBack.setRepeats(false);
    			goBack.start();
    		}
		});
		
		JButton logoutButton = createHeaderButton("Logout");
		
		logoutButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			Timer goBack = new Timer(10, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						int result = JOptionPane.showConfirmDialog(
								null,
								"Are you sure you want to log out?",
								"Logout Confirmation",
								JOptionPane.YES_NO_OPTION
							);
						
						if (result == JOptionPane.YES_OPTION){
							MainFrame main = MainFrame.getInstance();
							main.showPanel(new LoginUI());
						} else {
							return;
						}
					}
				});
    			
    			goBack.setRepeats(false);
    			goBack.start();
    		}
		});
		
		headerPanel.add(Box.createHorizontalStrut(20));
		headerPanel.add(viewReservations);
		headerPanel.add(Box.createHorizontalStrut(20));
		headerPanel.add(promotionNews);
		headerPanel.add(Box.createHorizontalStrut(20));
		headerPanel.add(logoutButton);
		
    	return headerPanel;
	}
	
	private JButton createHeaderButton(String text) {
        RoundedButton button = new RoundedButton(text);
        button.setDrawBorder(false);
        button.setPreferredSize(new Dimension(150, 30));  // Adjust size if needed
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Segoe UI Light", Font.BOLD, 14));
        
        button.addMouseListener(new MouseAdapter() {
        	public void mouseEntered(MouseEvent e) {
        		button.setBackground(new Color(250,250,250));
    		}
    		
    		public void mouseExited(MouseEvent e) {
    			button.setBackground(new Color(255,255,255));
    		}
    	});
        return button;
    }
}
