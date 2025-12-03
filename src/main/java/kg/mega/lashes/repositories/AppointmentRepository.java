package kg.mega.lashes.repositories;

import kg.mega.lashes.models.Appointment;
import kg.mega.lashes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUser(User user);
    List<Appointment> findByAppointmentDate(LocalDate date);
    List<Appointment> findByAppointmentDateAndAppointmentTime(LocalDate date, LocalTime time);
    boolean existsByAppointmentDateAndAppointmentTime(LocalDate date, LocalTime time);
    
    @Query("SELECT a.appointmentTime FROM Appointment a WHERE a.appointmentDate = :date AND a.status != 'CANCELLED'")
    List<LocalTime> findTakenTimesByDate(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate >= :startDate ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findUpcomingAppointments(@Param("startDate") LocalDate startDate);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.user u WHERE u.id = :userId ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findMyActiveAppointmentsWithUser(@Param("userId") Long userId);
    @Query("SELECT a FROM Appointment a JOIN FETCH a.user")
    List<Appointment> findAllWithUser();
    List<Appointment> findByUserEmail(String email);
}
