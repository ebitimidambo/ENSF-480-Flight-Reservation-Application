package boundary.utility;

import java.util.Map;

import entity.Reservation;
import entity.User;

public class PaymentGuiFactory extends UserGuiFactory{
	private User user;
	private Map<Integer, Reservation> reservations;
	private Map<String, Object> prompts; 
	boolean isReturnStep;
	
	public PaymentGuiFactory(User user, Map<Integer, Reservation> reservations, Map<String, Object> prompts, 
			boolean isReturnStep) {
		this.user = user;
		this.reservations = reservations;
		this.prompts = prompts;
		this.isReturnStep = isReturnStep;
	}
	
	public Header createHeader() {
		return new PaymentHeader(user);
	}
	
	public MidSection createMidSection() {
		return null;
	}
	
	public Body createBody() {
		return new PaymentBody(user, reservations, prompts, isReturnStep);
	}
}
