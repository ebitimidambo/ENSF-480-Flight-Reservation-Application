package control;

import entity.User;

public class RegistrationController {
	private RegistrationDAO registrar;
	
	public RegistrationController() {
		this.registrar = new RegistrationDAO();
	}
	
	public int registerUser(User user) {
		int result = registrar.registerUser(user);
		
		return result;
	}
}
