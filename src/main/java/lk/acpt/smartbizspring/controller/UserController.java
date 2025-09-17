package lk.acpt.smartbizspring.controller;

import lk.acpt.smartbizspring.dto.LoadAllResponseDto;
import lk.acpt.smartbizspring.dto.LoginResponseDto;
import lk.acpt.smartbizspring.dto.RegisterDto;
import lk.acpt.smartbizspring.dto.UserRegisterDto;
import lk.acpt.smartbizspring.exception.PasswordAlreadyExistsException;
import lk.acpt.smartbizspring.exception.UsernameAlreadyExistsException;
import lk.acpt.smartbizspring.service.StorageService;
import lk.acpt.smartbizspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;
    private final StorageService storageService;

    @Autowired
    public UserController(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<String> registerUser(@ModelAttribute UserRegisterDto userRegisterDto) {
        try {
            userService.register(userRegisterDto, userRegisterDto.getProfilePic());
            return ResponseEntity.ok("User registered successfully");

        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        } catch (PasswordAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody RegisterDto registerDto) {
        try {
            LoginResponseDto response = userService.login(registerDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> listUploadedFiles() throws IOException {
        List<String> files = storageService.loadAll()
                .map(path -> MvcUriComponentsBuilder
                        .fromMethodName(UserController.class,
                                "serveFile", path.getFileName().toString())
                        .build()
                        .toUri()
                        .toString())
                .collect(Collectors.toList());

        return ResponseEntity.ok(files);
    }


    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        if (file == null) return ResponseEntity.notFound().build();

        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException ignored) {}

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)  // allows inline display
                .body(file);
    }


    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> registerUser(@PathVariable Integer id,@ModelAttribute UserRegisterDto userRegisterDto) {
        userRegisterDto.setId(id);
        boolean updatedUser = userService.updateUser(userRegisterDto, userRegisterDto.getProfilePic());
        if (updatedUser) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.badRequest().body("User updated failed");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteUser(@PathVariable Integer id) {
        boolean deletedUser = userService.deleteUser(id);
        if (deletedUser) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("User deleted failed");
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<?>getAllUserAccordingRoles(@PathVariable String role) {
        try {
            List<LoadAllResponseDto> allUsersAccordingRoles = userService.getAllUsersAccordingRoles(role);
            return ResponseEntity.ok(allUsersAccordingRoles);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
