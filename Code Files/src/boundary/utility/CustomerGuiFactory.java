package boundary.utility;

import entity.User;

public class CustomerGuiFactory extends UserGuiFactory{
	private static User CUSTOMER;
	
	public CustomerGuiFactory(User user) {
		CUSTOMER = user;
	}
	
	public Header createHeader() {
		return new CustomerHeader(CUSTOMER);
	}
	
	public MidSection createMidSection() {
		return new CustomerMidSection();
	}
	
	public Body createBody() {
		return new CustomerBody(CUSTOMER);
	}
}
