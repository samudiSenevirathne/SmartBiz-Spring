package lk.acpt.smartbizspring.service;

import lk.acpt.smartbizspring.dto.LoadAllResponseDto;
import lk.acpt.smartbizspring.dto.LoginResponseDto;
import lk.acpt.smartbizspring.dto.RegisterDto;
import lk.acpt.smartbizspring.dto.UserRegisterDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {
    boolean register(UserRegisterDto userRegisterDto, MultipartFile profilePic);
    LoginResponseDto login(RegisterDto registerDto);
    boolean updateUser(UserRegisterDto userRegisterDto, MultipartFile profilePic);
    boolean deleteUser (Integer id);
    List<LoadAllResponseDto> getAllUsersAccordingRoles(String role);
}
