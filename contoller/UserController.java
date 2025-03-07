package contoller;

import DTO.CreateUserDTO;
import DTO.LoginUserDTO;
import service.UserService;

public class UserController {
	UserService userService = new UserService();
	
	public void createUser(CreateUserDTO dto) {
		userService.createUser(dto);
		
	}
	
	public void login(LoginUserDTO dao) {
		userService.login(dao);
		
	}
}


