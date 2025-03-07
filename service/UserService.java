package service;

import DTO.CreateUserDTO;
import DTO.LoginUserDTO;
import app.App;

public class UserService {
	
	public void createUser(CreateUserDTO dto) {
		
	}
	
	public void login(LoginUserDTO dao) {
		
		App.userEmail = dao.getEmail();
		App.loginCheck = true;
	}

}
