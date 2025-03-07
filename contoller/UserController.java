package contoller;

import DAO.CreateUserDAO;
import DAO.LoginUserDAO;
import service.UserService;

public class UserController {
	UserService userService = new UserService();
	
	public void createUser(CreateUserDAO dao) {
		userService.createUser(dao);
		
	}
	
	public void login(LoginUserDAO dao) {
		userService.login(dao);
		
	}
}


