package kg.mega.lashes.controllers;

import jakarta.validation.Valid;
import kg.mega.lashes.models.Appointment;
import kg.mega.lashes.models.User;
import kg.mega.lashes.models.dtos.AppointmentCreateDto;
import kg.mega.lashes.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentCreateDto createDto, 
                                               Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Appointment appointment = appointmentService.createAppointment(createDto, user);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<Appointment>> getMyAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getUserAppointments(user);
        return ResponseEntity.ok(appointments);
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
