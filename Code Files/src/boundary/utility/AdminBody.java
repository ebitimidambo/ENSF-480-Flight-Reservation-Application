package boundary.utility;

import java.awt.*;

import javax.swing.*;

import entity.User;

public class AdminBody implements Body{
	public static final int FLIGHTS_MODE = 1;
    public static final int ROUTES_MODE = 2;
    public static final int AIRCRAFT_MODE = 3;
    public static final int NONE = 4;
    
    // Shared mode flag
    private static int mode = FLIGHTS_MODE;

    public static void setMode(int m) {
        mode = m;
    }
	
	@SuppressWarnings("unused")
	private User user;
	
	public AdminBody(User user) {
	    this.user = user;
	}
	
	@Override
    public JPanel createBody() {

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setOpaque(true);
        bodyPanel.setBackground(new Color(255, 255, 255));

        switch (mode) {

            case FLIGHTS_MODE:
                ManageFlightsPage flights = new ManageFlightsPage();
                bodyPanel.add(flights, BorderLayout.CENTER);
                break;

            case ROUTES_MODE:
                ManageRoutesPage routes = new ManageRoutesPage();
                bodyPanel.add(routes, BorderLayout.CENTER);
                break;
             
            case AIRCRAFT_MODE:
            	ManageAircraftsPage aircrafts = new ManageAircraftsPage();
                bodyPanel.add(aircrafts, BorderLayout.CENTER);
                break;

            case NONE:
                JPanel empty = new JPanel();
                empty.setOpaque(true);
                bodyPanel.add(empty);
                break;
        }

        return bodyPanel;
    }
}