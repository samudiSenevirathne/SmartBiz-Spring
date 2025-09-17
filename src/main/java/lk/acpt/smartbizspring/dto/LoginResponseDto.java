package lk.acpt.smartbizspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String username;
    private String password;
    private String name;
    private String role;
    private String email;
    private String address;
    private String profilePic;
}