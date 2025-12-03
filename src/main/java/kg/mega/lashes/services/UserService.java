package kg.mega.lashes.services;

import kg.mega.lashes.models.User;
import kg.mega.lashes.models.dtos.UserLoginDto;
import kg.mega.lashes.models.dtos.UserRegistrationDto;
import kg.mega.lashes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + email));
        return user;
    }


    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setRememberMe(registrationDto.getRememberMe());
        user.setRole(User.Role.USER);

        return userRepository.save(user);
    }

    public User authenticateUser(UserLoginDto loginDto) {
        System.out.println("🔍 Попытка входа для email: " + loginDto.getEmail());
        
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> {
                    System.out.println("❌ Пользователь не найден: " + loginDto.getEmail());
                    return new RuntimeException("Неверный email или пароль");
                });

        System.out.println("✅ Пользователь найден: " + user.getName() + " (ID: " + user.getId() + ")");
        System.out.println("🔐 Роль пользователя: " + user.getRole());
        
        boolean passwordMatches = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
        System.out.println("🔑 Проверка пароля: " + (passwordMatches ? "✅ Совпадает" : "❌ Не совпадает"));
        
        if (!passwordMatches) {
            System.out.println("❌ Неверный пароль для пользователя: " + loginDto.getEmail());
            throw new RuntimeException("Неверный email или пароль");
        }

        user.setLastLogin(LocalDateTime.now());
        user.setRememberMe(loginDto.getRememberMe());
        User savedUser = userRepository.save(user);
        System.out.println("✅ Пользователь успешно аутентифицирован: " + savedUser.getEmail());
        
        return savedUser;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean hasAnyAdmin() {
        return userRepository.existsByRole(User.Role.ADMIN);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
