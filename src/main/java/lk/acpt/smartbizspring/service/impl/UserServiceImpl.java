package lk.acpt.smartbizspring.service.impl;

import lk.acpt.smartbizspring.controller.UserController;
import lk.acpt.smartbizspring.dto.LoginResponseDto;
import lk.acpt.smartbizspring.dto.RegisterDto;
import lk.acpt.smartbizspring.dto.UserRegisterDto;
import lk.acpt.smartbizspring.entity.Register;
import lk.acpt.smartbizspring.entity.User;
import lk.acpt.smartbizspring.repo.RegisterRepo;
import lk.acpt.smartbizspring.repo.UserRepo;
import lk.acpt.smartbizspring.service.StorageService;
import lk.acpt.smartbizspring.service.UserService;

import lk.acpt.smartbizspring.util.JwtUtil;
import lk.acpt.smartbizspring.util.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final RegisterRepo registerRepo;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final SecurityConfig securityConfig;
    private final StorageService storageService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RegisterRepo registerRepo, JwtUtil jwtUtil, SecurityConfig securityConfig,StorageService storageService) {
        this.userRepo = userRepo;
        this.registerRepo = registerRepo;
        this.jwtUtil = jwtUtil;
        this.securityConfig = securityConfig;
        this.storageService = storageService;
    }

    @Override
    @Transactional
    public boolean register(UserRegisterDto ur, MultipartFile profilePic) {
        try {
            String fileName = null;
            if (profilePic != null && !profilePic.isEmpty()) {
                fileName = storageService.store(profilePic);
            }
            User user = new User(
                    ur.getName(),
                    ur.getRole(),
                    ur.getAddress(),
                    ur.getEmail(),
                    fileName
            );
            User savedUser=userRepo.save(user);
            System.out.println(ur);
            String encodedPassword = securityConfig.passwordEncoder().encode(ur.getPassword());
            ur.setPassword(encodedPassword);
            Register registerr = new Register(
                    ur.getUsername(),
                    encodedPassword,
                    savedUser
            );
            Register savedRegister = registerRepo.save(registerr);
            return savedRegister != null;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public LoginResponseDto login(RegisterDto registerDto) {
        Optional<Register> optionalUser = registerRepo.findByUsername(registerDto.getUsername());

        if (optionalUser.isPresent()) {
            Register register = optionalUser.get();

            // Simple password check
            if (securityConfig.passwordEncoder().matches(registerDto.getPassword(), register.getPassword())) {
                // Get associated user entity
                User user = register.getUser();

                // Generate JWT token
                String token = "Bearer " + jwtUtil.generateToken(register.getUsername());

                // Build full profile picture URL
                String profilePicUrl = null;
                if (user.getProfilePic() != null) {
                    profilePicUrl = MvcUriComponentsBuilder
                            .fromMethodName(UserController.class, "serveFile", user.getProfilePic())
                            .build()
                            .toUri()
                            .toString();
                }

                // Return all user info + token
                return new LoginResponseDto(
                        token,
                        register.getUsername(),
                        user.getName(),
                        user.getRole(),
                        user.getEmail(),
                        user.getAddress(),
                        profilePicUrl
                );
            } else {
                throw new RuntimeException("Invalid password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

}
