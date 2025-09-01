package lk.acpt.smartbizspring.service;

import lk.acpt.smartbizspring.dto.RegisterDto;
import lk.acpt.smartbizspring.dto.UserRegisterDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean register(UserRegisterDto userRegisterDto);
    String login(RegisterDto registerDto);
}
