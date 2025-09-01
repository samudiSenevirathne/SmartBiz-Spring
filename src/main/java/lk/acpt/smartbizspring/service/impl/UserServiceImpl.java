package lk.acpt.smartbizspring.service.impl;

import lk.acpt.smartbizspring.dto.RegisterDto;
import lk.acpt.smartbizspring.dto.UserRegisterDto;
import lk.acpt.smartbizspring.entity.Register;
import lk.acpt.smartbizspring.entity.User;
import lk.acpt.smartbizspring.repo.RegisterRepo;
import lk.acpt.smartbizspring.repo.UserRepo;
import lk.acpt.smartbizspring.service.UserService;

import lk.acpt.smartbizspring.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final RegisterRepo registerRepo;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo,RegisterRepo registerRepo, JwtUtil jwtUtil,PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.registerRepo = registerRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public boolean register(UserRegisterDto ur) {
        User user = new User(ur.getName(), ur.getRole(),ur.getAddress(),ur.getEmail());
        User savedUser = userRepo.save(user);
        System.out.println(ur);
        String encodedPassword = passwordEncoder.encode(ur.getPassword());
        ur.setPassword(encodedPassword);
        Register registerr = new Register(
                ur.getUsername(),
                encodedPassword,
                savedUser
        );
        Register savedRegister = registerRepo.save(registerr);
        return savedRegister != null;
    }


    @Override
    public String login(RegisterDto registerDto) {
        Optional<Register> optionalUser = registerRepo.findByUsername(registerDto.getUsername());

        if (optionalUser.isPresent()) {
            Register register = optionalUser.get();

            // Simple password check (in real app, hash password)
            if (passwordEncoder.matches(registerDto.getPassword(),register.getPassword())) {
                // Generate JWT token
                return "Bearer " + jwtUtil.generateToken(register.getUsername());
            } else {
                throw new RuntimeException("Invalid password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
