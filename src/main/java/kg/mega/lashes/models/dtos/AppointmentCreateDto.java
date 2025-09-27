package kg.mega.lashes.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentCreateDto {
    @NotBlank(message = "Имя клиента не может быть пустым")
    private String clientName;

    private String clientPhone;

    @NotNull(message = "Дата записи обязательна")
    @Future(message = "Дата записи должна быть в будущем")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @NotNull(message = "Время записи обязательно")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;

    private String notes;

    // Getters and Setters
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
