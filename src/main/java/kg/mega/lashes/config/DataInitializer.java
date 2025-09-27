package kg.mega.lashes.config;

import kg.mega.lashes.models.User;
import kg.mega.lashes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔍 Проверяем существование админа...");
        
        // Создаем админа, если его еще нет
        if (!userRepository.existsByEmail("iskenpubg@gmail.com")) {
            System.out.println("📝 Создаем нового администратора...");
            
            User admin = new User();
            admin.setName("Администратор");
            admin.setEmail("iskenpubg@gmail.com");
            admin.setPassword(passwordEncoder.encode("isken1234-"));
            admin.setPhoneNumber("+996555123456");
            admin.setRole(User.Role.ADMIN);
            admin.setRememberMe(false);
            
            User savedAdmin = userRepository.save(admin);
            System.out.println("✅ Администратор создан: iskenpubg@gmail.com");
            System.out.println("🆔 ID админа: " + savedAdmin.getId());
            System.out.println("🔐 Роль: " + savedAdmin.getRole());
        } else {
            System.out.println("ℹ️ Администратор уже существует: iskenpubg@gmail.com");
            
            // Проверим данные существующего админа
            User existingAdmin = userRepository.findByEmail("iskenpubg@gmail.com").orElse(null);
            if (existingAdmin != null) {
                System.out.println("🆔 ID: " + existingAdmin.getId());
                System.out.println("🔐 Роль: " + existingAdmin.getRole());
                System.out.println("📅 Создан: " + existingAdmin.getCreatedAt());
            }
        }
    }
}
