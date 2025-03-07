package DAO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserDAO {
	private String email;
    private String password;
    private String nickname;

}
