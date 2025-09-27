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
import java.util.List;
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
            // Проверяем совпадение паролей
            if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Пароли не совпадают");
                return ResponseEntity.badRequest().body(error);
            }
            
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

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            // Проверяем, есть ли уже админы в системе
            if (userService.hasAnyAdmin()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Администратор уже существует"));
            }
            
            User user = userService.registerUser(registrationDto);
            // Устанавливаем роль админа
            user.setRole(User.Role.ADMIN);
            user = userService.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("user", createUserResponse(user));
            response.put("message", "Администратор успешно создан");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/reset-admin-password")
    public ResponseEntity<?> resetAdminPassword() {
        try {
            User admin = userService.findByEmail("iskenpubg@gmail.com")
                    .orElseThrow(() -> new RuntimeException("Админ не найден"));
            
            // Сбрасываем пароль на дефолтный
            admin.setPassword(userService.encodePassword("isken1234-"));
            userService.save(admin);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Пароль админа сброшен на: isken1234-");
            response.put("email", "iskenpubg@gmail.com");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/check-admin")
    public ResponseEntity<?> checkAdmin() {
        try {
            User admin = userService.findByEmail("iskenpubg@gmail.com").orElse(null);
            
            Map<String, Object> response = new HashMap<>();
            if (admin != null) {
                response.put("exists", true);
                response.put("user", createUserResponse(admin));
                response.put("message", "Админ найден");
            } else {
                response.put("exists", false);
                response.put("message", "Админ не найден");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Не авторизован"));
        }
        
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Доступ запрещен"));
        }
        
        List<Map<String, Object>> users = userService.getAllUsers().stream()
                .map(this::createUserResponse)
                .collect(java.util.stream.Collectors.toList());
        
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long userId, 
                                          @RequestBody Map<String, String> roleRequest,
                                          Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Не авторизован"));
        }
        
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Доступ запрещен"));
        }
        
        try {
            String newRole = roleRequest.get("role");
            if (newRole == null || (!newRole.equals("USER") && !newRole.equals("ADMIN"))) {
                return ResponseEntity.badRequest().body(Map.of("error", "Некорректная роль"));
            }
            
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            
            user.setRole(User.Role.valueOf(newRole));
            user = userService.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("user", createUserResponse(user));
            response.put("message", "Роль пользователя успешно изменена");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Не авторизован"));
        }
        
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Доступ запрещен"));
        }
        
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            
            // Нельзя удалить самого себя
            if (user.getId().equals(currentUser.getId())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Нельзя удалить самого себя"));
            }
            
            userService.deleteUser(userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Пользователь успешно удален");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
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
