package kg.mega.lashes.services.impls;

import kg.mega.lashes.mappers.ClientMapper;
import kg.mega.lashes.models.Client;
import kg.mega.lashes.models.dtos.ClientCreateDto;
import kg.mega.lashes.models.dtos.ClientUpdateDto;
import kg.mega.lashes.repositories.ClientRepo;
import kg.mega.lashes.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepo;

    public ClientServiceImpl(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Client create(ClientCreateDto clientCreateDto) {
        LocalDate visitDate = clientCreateDto.visitDate();
        LocalTime visitTime = clientCreateDto.visitTime();

        if (clientRepo.existsByVisitDateAndVisitTime(visitDate, visitTime)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Это время уже занято на выбранную дату");
        }
        Client client = ClientMapper.INSTANCE.clientCreateDtoToClient(clientCreateDto);
        client.setVisitDate(visitDate);
        client.setVisitTime(visitTime);
        client.setDayOfVisit(convertToRussian(visitDate.getDayOfWeek()));
        client.setDateOfRegister(LocalDate.now());
        return clientRepo.save(client);
    }


    @Override
    public List<Client> getAll() {
        return clientRepo.findAll();
    }

    @Override
    public Client update(ClientUpdateDto clientUpdateDto, Long id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (clientUpdateDto.name() != null && !clientUpdateDto.name().isEmpty()) {
            client.setName(clientUpdateDto.name());
        }
        if (clientUpdateDto.phoneNumber() != null && !clientUpdateDto.phoneNumber().isEmpty()) {
            client.setPhoneNumber(clientUpdateDto.phoneNumber());
        }
        if (clientUpdateDto.comment() != null && !clientUpdateDto.comment().isEmpty()) {
            client.setData(clientUpdateDto.comment());
        }
        LocalDate visitDate = clientUpdateDto.visitDate();
        LocalTime visitTime = clientUpdateDto.visitTime();

        if (clientRepo.existsByVisitDateAndVisitTime(visitDate, visitTime)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Это время уже занято на выбранную дату");
        }
        if (clientUpdateDto.visitDate() != null) {
            client.setVisitDate(visitDate);
            client.setDayOfVisit(convertToRussian(visitDate.getDayOfWeek()));
        }

        if (clientUpdateDto.visitTime() != null) {
            client.setVisitTime(visitTime);
        }


        return clientRepo.save(client);
    }

    @Override
    public List<Client> getByName(String name) {
        List<Client> client = getAll().stream().filter(c ->
                c.getName().equals(name)).collect(Collectors.toList());

        return client;
    }

    @Override
    public List<Client> getByPhoneNumber(String phoneNumber) {
        List<Client> clients = getAll().stream().filter(c ->
                c.getPhoneNumber().equals(phoneNumber) ).collect(Collectors.toList());
        return clients;

    }

    @Override
    public Client delete(Long id) {
        Client clientId = clientRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientRepo.delete(clientId);
        return clientId;
    }
    private String convertToRussian(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "Понедельник";
            case TUESDAY -> "Вторник";
            case WEDNESDAY -> "Среда";
            case THURSDAY -> "Четверг";
            case FRIDAY -> "Пятница";
            case SATURDAY -> "Суббота";
            case SUNDAY -> "Воскресенье";
        };
    }
    public List<LocalTime> getTakenTimesByDate(LocalDate date) {
        return clientRepo.findAllByVisitDate(date).stream()
                .map(Client::getVisitTime)
                .toList();
    }



}
