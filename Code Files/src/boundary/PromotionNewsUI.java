package boundary;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import boundary.utility.Body;
import boundary.utility.CustomerUIBanner;
import boundary.utility.CustomerUIFooter;
import boundary.utility.Header;
import boundary.utility.MidSection;
import boundary.utility.NewsGuiFactory;
import boundary.utility.UserGuiFactory;
import entity.User;

@SuppressWarnings("serial")
public class PromotionNewsUI extends JPanel{
	private User user;
	private UserGuiFactory factory;
	private Header header;
	private MidSection mid;
	private Body body;
	
	public PromotionNewsUI(User user) {
		this.user = user;
		this.factory = new NewsGuiFactory(user);
		this.header = factory.createHeader();
		this.mid = factory.createMidSection();
		this.body = factory.createBody();
		setupUI();
	}
	
	private void setupUI() {
		setLayout(new BorderLayout());
		setBackground(Color.white);
		
		JPanel topPanel = new JPanel();
	    topPanel.setLayout(new BorderLayout());
	    topPanel.add(CustomerUIBanner.createBanner(user), BorderLayout.NORTH);
	    topPanel.add(header.createHeader(), BorderLayout.SOUTH);
	    topPanel.setBackground(new Color(45,59,73));
	    
	    add(topPanel, BorderLayout.NORTH);
	    add(mid.createMidSection(), BorderLayout.CENTER);
	    
	    JPanel bottomPanel = new JPanel();
	    bottomPanel.setLayout(new BorderLayout());
	    bottomPanel.add(body.createBody(), BorderLayout.NORTH);
	    bottomPanel.add(CustomerUIFooter.createFooter(), BorderLayout.SOUTH);
	    bottomPanel.setBackground(new Color(45,59,73));
	    add(bottomPanel, BorderLayout.SOUTH);
	    
	}
	
}
