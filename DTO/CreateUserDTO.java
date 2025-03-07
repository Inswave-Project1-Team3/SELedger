package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserDTO {
	private String email;
    private String password;
    private String nickname;

}
