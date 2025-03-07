package service;

import DAO.CreateUserDAO;
import DAO.LoginUserDAO;
import app.App;

public class UserService {
	
	public void createUser(CreateUserDAO dao) {
		
	}
	
	public void login(LoginUserDAO dao) {
		
		App.userEmail = dao.getEmail();
		App.loginCheck = true;
	}

}
