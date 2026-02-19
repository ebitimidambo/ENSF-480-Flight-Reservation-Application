package boundary.utility;

import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class AdminHeader implements Header{
	
	private JButton manageFlights;
    private JButton manageRoutes;
    private JButton manageAircraft;
    private JButton back;
    private JButton logOut;
    public enum AdminPage {
        FLIGHTS,
        ROUTES,
        AIRCRAFT,
        MAIN
    }
    private AdminPage activePage = AdminPage.MAIN;
    
    private ActionListener flightsListener;
    private ActionListener routesListener;
    private ActionListener aircraftListener;
    private ActionListener backListener;
    private ActionListener logOutListener;
    
    public void setActivePage(AdminPage page) {
        this.activePage = page;
    }
    
    public void setFlightsListener(ActionListener l) {
        this.flightsListener = l;
    }

    public void setRoutesListener(ActionListener l) {
        this.routesListener = l;
    }
    
    public void setAircraftListener(ActionListener l) {
    	this.aircraftListener = l;
    }
    
    public void setBackListener(ActionListener l) {
        this.backListener = l;
    }
    
    public void setLogOutListener(ActionListener l) {
        this.logOutListener = l;
    }
    
    @Override
	public JPanel createHeader() {
		JPanel headerPanel = new JPanel(new GridLayout(1, 5));
        headerPanel.setBackground(new Color(255,255,255));

        JPanel centerLeft = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerLeft.setOpaque(false);

        JPanel centerRight = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerRight.setOpaque(false);
        
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        center.setOpaque(false);
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.CENTER));
        left.setOpaque(false);
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.CENTER));
        right.setOpaque(false);

        manageRoutes  = createHeaderButton("Manage Routes");
        manageFlights = createHeaderButton("Manage Flights");
        manageAircraft = createHeaderButton("Manage Aircraft");
        back = createHeaderButton("Back");
        logOut = createHeaderButton("Log Out");
        
        if (activePage == AdminPage.FLIGHTS)   disableButton(manageFlights);
        if (activePage == AdminPage.ROUTES)    disableButton(manageRoutes);
        if (activePage == AdminPage.AIRCRAFT)  disableButton(manageAircraft);
        if (activePage == AdminPage.MAIN)	disableButton(back);
          
        manageFlights.addActionListener(e -> {
            if (flightsListener != null) flightsListener.actionPerformed(e);
        });

        manageRoutes.addActionListener(e -> {
            if (routesListener != null) routesListener.actionPerformed(e);
        });
        
        manageAircraft.addActionListener(e -> {
        	if (aircraftListener != null) aircraftListener.actionPerformed(e);
        });
        
        back.addActionListener(e -> {
            if (backListener != null) backListener.actionPerformed(e);
        });
        
        logOut.addActionListener(e -> {
            if (logOutListener != null) logOutListener.actionPerformed(e);
        });

        centerLeft.add(manageRoutes);
        center.add(manageFlights);
        centerRight.add(manageAircraft);
        left.add(back);
        right.add(logOut);

        headerPanel.add(left);
        headerPanel.add(centerLeft);
        headerPanel.add(center);
        headerPanel.add(centerRight);
        headerPanel.add(right);

        return headerPanel;
	}
	
	private JButton createHeaderButton(String text) {
        RoundedButton button = new RoundedButton(text);
        button.setDrawBorder(false);
        button.setPreferredSize(new Dimension(160, 35));  // Adjust size if needed
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Segoe UI Light", Font.BOLD, 14));
        
        button.addMouseListener(new MouseAdapter() {
        	public void mouseEntered(MouseEvent e) {
        		button.setBackground(new Color(245,245,245));
    		}
    		
    		public void mouseExited(MouseEvent e) {
    			button.setBackground(new Color(255,255,255));
    		}
    	});
        return button;
    }
	
	private void disableButton(JButton btn) {
	    btn.setEnabled(false);
	    btn.setForeground(new Color(160, 160, 160));
	    btn.setBackground(new Color(240, 240, 240));
	}

}