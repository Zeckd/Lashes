package kg.mega.lashes.repositories;

import kg.mega.lashes.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    boolean existsByVisitDateAndVisitTime(LocalDate visitDate, LocalTime visitTime);

    List<Client> findAllByVisitDate(LocalDate visitDate);
}
