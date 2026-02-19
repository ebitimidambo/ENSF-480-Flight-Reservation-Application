package boundary.utility;

import entity.User;

public class NewsGuiFactory extends UserGuiFactory{
	private User user;
	
	public NewsGuiFactory(User user) {
		this.user = user;
	}
	
	public Header createHeader() {
		return new NewsHeader(user);
	}
	
	public MidSection createMidSection() {
		return new NewsMidSection();
	}
	
	public Body createBody() {
		return new NewsBody(user);
	}
}
