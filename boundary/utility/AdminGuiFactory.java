package boundary.utility;

import entity.User;

public class AdminGuiFactory extends UserGuiFactory{
	private static User ADMIN;
	
	public AdminGuiFactory(User user) {
		ADMIN = user;
	}
	
	public Header createHeader() {
		return new AdminHeader();
	}
	
	public MidSection createMidSection() {
		return new AdminMidSection();
	}
	
	public Body createBody() {
		return new AdminBody(ADMIN);
	}
}