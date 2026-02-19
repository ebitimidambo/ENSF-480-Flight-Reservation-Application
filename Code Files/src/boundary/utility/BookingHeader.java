package boundary.utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import boundary.FlightSearchUI;
import boundary.MainFrame;
import entity.Flight;
import entity.User;

public class BookingHeader implements Header{
	private static User user;
	private Map<String, Object> searchPrompts;
	private List<Flight> departureFlights;
	private List<Flight> arrivalFlights;
	
	public BookingHeader(User userV, Map<String, Object> prompts, List<Flight> departureFlights, List<Flight> arrivalFlights) {
		user = userV;
		this.departureFlights = departureFlights;
		this.arrivalFlights = arrivalFlights;
		this.searchPrompts = prompts;
	}
	
	public JPanel createHeader() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setBackground(new Color(255,255,255));
    	
		JButton backButton = createHeaderButton("<< Back");
		
		backButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			Timer goBack = new Timer(10, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						BookingBody.clearSeats();
						MainFrame mainFrame = MainFrame.getInstance();
					    mainFrame.showPanel(new FlightSearchUI(user, searchPrompts, departureFlights, arrivalFlights));
					}
				});
    			
    			goBack.setRepeats(false);
    			goBack.start();
    		}
		});
		
		headerPanel.add(Box.createHorizontalStrut(20));
		headerPanel.add(backButton);
		
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
