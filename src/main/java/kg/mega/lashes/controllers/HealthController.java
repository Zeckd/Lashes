package kg.mega.lashes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/database")
    public ResponseEntity<?> checkDatabase() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            try (Connection connection = dataSource.getConnection()) {
                response.put("status", "success");
                response.put("message", "База данных подключена");
                response.put("database", connection.getMetaData().getDatabaseProductName());
                response.put("url", connection.getMetaData().getURL());
                response.put("username", connection.getMetaData().getUserName());
                
                try {
                    var statement = connection.createStatement();
                    var resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
                    if (resultSet.next()) {
                        response.put("users_count", resultSet.getInt(1));
                    }
                } catch (SQLException e) {
                    response.put("users_table_error", e.getMessage());
                }
                
                return ResponseEntity.ok(response);
            }
        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Ошибка подключения к базе данных");
            response.put("error", e.getMessage());
            response.put("sql_state", e.getSQLState());
            response.put("error_code", e.getErrorCode());
            
            return ResponseEntity.status(500).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Неожиданная ошибка");
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/app")
    public ResponseEntity<?> checkApp() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Приложение работает");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
