package kg.mega.lashes.services;

import jakarta.validation.Valid;
import kg.mega.lashes.models.Client;
import kg.mega.lashes.models.dtos.ClientCreateDto;
import kg.mega.lashes.models.dtos.ClientUpdateDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ClientService {
    Client create(ClientCreateDto clientCreateDto);


    List<Client> getAll();

    Client update(ClientUpdateDto clientUpdateDto, Long id);

    List<Client> getByName(String name);

    List<Client> getByPhoneNumber(String phoneNumber);

    Client delete(Long id);

    List<LocalTime> getTakenTimesByDate(LocalDate date);
}
