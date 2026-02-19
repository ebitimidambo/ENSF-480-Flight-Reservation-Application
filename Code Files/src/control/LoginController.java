package control;

import entity.User;

public class LoginController {
	private UserDAO userDAO;
	
	public LoginController() {
		userDAO = new UserDAO();
	}
	
	public User login(String username, String password) {
		if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return null;
        }
		
		User user = userDAO.getUser(username, password);
		
		if (user == null) {
			System.out.println("Invalid Credentials");
		}
		
		return user;
	}
}
