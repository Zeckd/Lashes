package kg.mega.lashes.controllers;

import jakarta.validation.Valid;
import kg.mega.lashes.models.User;
import kg.mega.lashes.models.dtos.UserLoginDto;
import kg.mega.lashes.models.dtos.UserRegistrationDto;
import kg.mega.lashes.security.JwtUtils;
import kg.mega.lashes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            
            // Автоматически аутентифицируем пользователя после регистрации
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrationDto.getEmail(), registrationDto.getPassword())
            );
            
            String jwt = jwtUtils.generateJwtToken(authentication, registrationDto.getRememberMe());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("user", createUserResponse(user));
            response.put("message", "Пользователь успешно зарегистрирован");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto loginDto) {
        try {
            User user = userService.authenticateUser(loginDto);
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            
            String jwt = jwtUtils.generateJwtToken(authentication, loginDto.getRememberMe());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("user", createUserResponse(user));
            response.put("message", "Успешный вход в систему");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Успешный выход из системы");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Не авторизован"));
        }
        
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(createUserResponse(user));
    }

    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("name", user.getName());
        userResponse.put("email", user.getEmail());
        userResponse.put("phoneNumber", user.getPhoneNumber());
        userResponse.put("role", user.getRole().name());
        userResponse.put("createdAt", user.getCreatedAt());
        return userResponse;
    }
}
