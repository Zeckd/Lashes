package kg.mega.lashes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kg.mega.lashes.enums.Delete;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "Имя")
    private String name;
    @JoinColumn(name = "Номер телефона")
    private String phoneNumber;
    @JoinColumn(name = "Дата посещения")
    private LocalDate dateOfRegister;
    @JoinColumn(name = "Данные")
    private String data;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String dayOfVisit;
    @JsonIgnore
    private Delete remove;

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalTime getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(LocalTime visitTime) {
        this.visitTime = visitTime;
    }

    public String getDayOfVisit() {
        return dayOfVisit;
    }

    public void setDayOfVisit(String dayOfVisit) {
        this.dayOfVisit = dayOfVisit;
    }

    public Delete getRemove() {
        return remove;
    }

    public void setRemove(Delete remove) {
        this.remove = remove;
    }

    public LocalDate setDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(LocalDate dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
