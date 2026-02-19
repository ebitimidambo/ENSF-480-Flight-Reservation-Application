package boundary;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import boundary.utility.Body;
import boundary.utility.CustomerReservationsGuiFactory;
import boundary.utility.CustomerUIBanner;
import boundary.utility.CustomerUIFooter;
import boundary.utility.Header;
import boundary.utility.UserGuiFactory;
import entity.User;

@SuppressWarnings("serial")
public class CustomerReservationsUI extends JPanel{
	private User user;
	private UserGuiFactory factory;
	private Header header;
	private Body body;
	
	public CustomerReservationsUI(User user) {
		this.user = user;
		this.factory =  new CustomerReservationsGuiFactory(user);
		this.header = factory.createHeader();
		this.body = factory.createBody();
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
	    topPanel.setLayout(new BorderLayout());
	    topPanel.add(CustomerUIBanner.createBanner(user), BorderLayout.NORTH);
	    topPanel.setBackground(new Color(45,59,73));
	    topPanel.add(header.createHeader(), BorderLayout.SOUTH);
	    
	    add(topPanel, BorderLayout.NORTH);
	    
	    JPanel bottomPanel = new JPanel();
	    bottomPanel.setLayout(new BorderLayout());
	    bottomPanel.add(body.createBody(), BorderLayout.NORTH);
	    bottomPanel.add(CustomerUIFooter.createFooter(), BorderLayout.SOUTH);
	    bottomPanel.setBackground(new Color(45,59,73));
	    add(bottomPanel, BorderLayout.SOUTH);
	}
}
