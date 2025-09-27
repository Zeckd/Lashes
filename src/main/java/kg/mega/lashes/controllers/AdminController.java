package kg.mega.lashes.controllers;

import kg.mega.lashes.models.Appointment;
import kg.mega.lashes.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointments/date/{date}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusRequest) {
        try {
            String statusStr = statusRequest.get("status");
            Appointment.AppointmentStatus status = Appointment.AppointmentStatus.valueOf(statusStr.toUpperCase());
            Appointment appointment = appointmentService.updateAppointmentStatus(id, status);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok(Map.of("message", "Запись успешно удалена"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/appointments/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        
        long totalAppointments = allAppointments.size();
        long scheduledAppointments = allAppointments.stream()
                .filter(a -> a.getStatus() == Appointment.AppointmentStatus.SCHEDULED)
                .count();
        long completedAppointments = allAppointments.stream()
                .filter(a -> a.getStatus() == Appointment.AppointmentStatus.COMPLETED)
                .count();
        long cancelledAppointments = allAppointments.stream()
                .filter(a -> a.getStatus() == Appointment.AppointmentStatus.CANCELLED)
                .count();
        
        Map<String, Object> statistics = Map.of(
            "total", totalAppointments,
            "scheduled", scheduledAppointments,
            "completed", completedAppointments,
            "cancelled", cancelledAppointments
        );
        
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testAdminAccess() {
        Map<String, String> response = Map.of(
            "message", "Админ доступ работает!",
            "timestamp", java.time.LocalDateTime.now().toString()
        );
        return ResponseEntity.ok(response);
    }
}
