package kg.mega.lashes.controllers;

import jakarta.validation.Valid;
import kg.mega.lashes.models.Appointment;
import kg.mega.lashes.models.User;
import kg.mega.lashes.models.dtos.AppointmentCreateDto;
import kg.mega.lashes.services.AppointmentService;
import kg.mega.lashes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;

    // AppointmentController.java
    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentCreateDto createDto,
                                               Authentication authentication) {
        try {
            // 1. Получаем имя пользователя (email) из Principal
            String username = authentication.getName();

            // 2. Находим полный объект User
            User user = userService.findByEmail(username) // Должен быть метод findByEmail в UserService
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            Appointment appointment = appointmentService.createAppointment(createDto, user);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            // Валидационные ошибки (RuntimeException) будут возвращены как 400 Bad Request
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<List<Appointment>> getUserAppointments(Principal principal) {
        // principal.getName() вернет email (или username) текущего залогиненного пользователя
        String email = principal.getName();

        // Теперь этот метод существует и вернет список
        return ResponseEntity.ok(appointmentService.findAppointmentsByUserEmail(email));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Appointment>> getMyAppointments(Authentication authentication) {
        try {
            String email = authentication.getName(); // Получаем email
            User user = userService.findByEmail(email) // Находим объект User через сервис
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            // Предполагая, что appointmentService.getMyAppointments принимает User
            List<Appointment> appointments = appointmentService.getMyAppointments(user);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/taken-times")
    public ResponseEntity<List<LocalTime>> getTakenTimes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LocalTime> takenTimes = appointmentService.getTakenTimesByDate(date);
        return ResponseEntity.ok(takenTimes);
    }

    @GetMapping("/available")
    public ResponseEntity<Map<String, Boolean>> checkTimeAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        boolean available = appointmentService.isTimeSlotAvailable(date, time);
        return ResponseEntity.ok(Map.of("available", available));
    }
}
