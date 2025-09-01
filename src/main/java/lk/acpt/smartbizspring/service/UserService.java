package lk.acpt.smartbizspring.service;

import lk.acpt.smartbizspring.dto.LoginResponseDto;
import lk.acpt.smartbizspring.dto.RegisterDto;
import lk.acpt.smartbizspring.dto.UserRegisterDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
    boolean register(UserRegisterDto userRegisterDto, MultipartFile profilePic);
    LoginResponseDto login(RegisterDto registerDto);
}
