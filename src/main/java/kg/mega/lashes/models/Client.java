package kg.mega.lashes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kg.mega.lashes.enums.Delete;

import java.time.LocalDate;

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
    private LocalDate dateOfVisit;
    @JoinColumn(name = "Данные")
    private String data;
    @JsonIgnore
    private Delete remove;

    public Delete getRemove() {
        return remove;
    }

    public void setRemove(Delete remove) {
        this.remove = remove;
    }

    public LocalDate getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(LocalDate dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
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
