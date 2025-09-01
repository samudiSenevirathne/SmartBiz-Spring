package lk.acpt.smartbizspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRegisterDto {
    private String name;
    private String role;
    private String address;
    private String email;
    private String username;
    private String password;
    // For profile picture
    private MultipartFile profilePic;
}
