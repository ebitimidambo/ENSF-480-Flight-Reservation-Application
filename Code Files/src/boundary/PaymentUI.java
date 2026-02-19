package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Map;

import javax.swing.JPanel;

import boundary.utility.Body;
import boundary.utility.CustomerUIBanner;
import boundary.utility.CustomerUIFooter;
import boundary.utility.Header;
import boundary.utility.PaymentGuiFactory;
import entity.Reservation;
import entity.User;

@SuppressWarnings("serial")
public class PaymentUI extends JPanel{
	private User user;
	private PaymentGuiFactory factory;
	private Body body;
	private Header header;
	
	public PaymentUI(User user, Map<Integer, Reservation> reservations, Map<String, Object> prompts, boolean isReturnStep) {
		this.user = user;
		this.factory = new PaymentGuiFactory(user, reservations, prompts, isReturnStep);
		this.body = factory.createBody();
		this.header = factory.createHeader();
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
