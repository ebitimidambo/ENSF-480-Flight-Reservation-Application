package boundary;

import entity.User;
import boundary.utility.*;
import control.PromotionController;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class CustomerUI extends JPanel{
	private User user;
	private CustomerGuiFactory factory;
	private Header header;
	private MidSection mid;
	private Body body;
	
	public CustomerUI(User user) {
		this.user = user;
		this.factory = new CustomerGuiFactory(user);
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
	    
	    SwingUtilities.invokeLater(() -> {
	        PromotionController controller = new PromotionController();
	        if (controller.isPromotionReleaseDay()) {
	            JOptionPane.showMessageDialog(
	                CustomerUI.this,
	                "🎉 New monthly promotions have just been released!\n\nCheck them out in the promotion news tab!",
	                "Monthly Promotion Alert",
	                JOptionPane.INFORMATION_MESSAGE
	            );
	            controller.setShownForSession();
	        }
	    });
	    
	}
	
}
