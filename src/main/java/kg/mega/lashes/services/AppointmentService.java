package kg.mega.lashes.services;

import kg.mega.lashes.models.Appointment;
import kg.mega.lashes.models.User;
import kg.mega.lashes.models.dtos.AppointmentCreateDto;
import kg.mega.lashes.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment createAppointment(AppointmentCreateDto createDto, User user) {
        if (appointmentRepository.existsByAppointmentDateAndAppointmentTime(
                createDto.getAppointmentDate(), createDto.getAppointmentTime())) {
            throw new RuntimeException("Выбранное время уже занято");
        }

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setClientName(createDto.getClientName());
        appointment.setClientPhone(createDto.getClientPhone());
        appointment.setAppointmentDate(createDto.getAppointmentDate());
        appointment.setAppointmentTime(createDto.getAppointmentTime());
        appointment.setNotes(createDto.getNotes());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getUserAppointments(User user) {
        return appointmentRepository.findByUser(user);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository.findUpcomingAppointments(LocalDate.now());
    }
    @Transactional // !!! Важно, чтобы этот метод был транзакционным !!!
    public List<Appointment> getMyAppointments(User currentUser) {
        // Используем новый метод, который загружает все необходимые данные
        return appointmentRepository.findMyActiveAppointmentsWithUser(currentUser.getId());
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    public List<LocalTime> getTakenTimesByDate(LocalDate date) {
        return appointmentRepository.findTakenTimesByDate(date);
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public boolean isTimeSlotAvailable(LocalDate date, LocalTime time) {
        return !appointmentRepository.existsByAppointmentDateAndAppointmentTime(date, time);
    }
}
