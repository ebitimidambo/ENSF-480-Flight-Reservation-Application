package boundary.utility;

import entity.User;

public class CustomerReservationsGuiFactory extends UserGuiFactory{
	private User user;
	
	public CustomerReservationsGuiFactory(User user) {
		this.user = user;
	}
	
	public Body createBody() {
		return new CustomerReservationsBody(user);
	}

	@Override
	public Header createHeader() {
		return new CustomerReservationsHeader(user);
	}

	@Override
	public MidSection createMidSection() {
		// TODO Auto-generated method stub
		return null;
	}
}
