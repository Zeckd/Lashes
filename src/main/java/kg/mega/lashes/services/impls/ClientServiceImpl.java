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

import java.time.LocalDate;
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
        Client client = ClientMapper.INSTANCE.clientCreateDtoToClient(clientCreateDto);
        client.setDateOfVisit(LocalDate.now());
        return clientRepo.save(client);
    }


    @Override
    public List<Client> getAll() {
        return clientRepo.findAll();
    }

    @Override
    public Client update(ClientUpdateDto clientUpdateDto, Long id) {
        Client clientId = clientRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Client client = ClientMapper.INSTANCE.clientUpdateDtoToClient(clientUpdateDto);
        client.setDateOfVisit(LocalDate.now());
        client.setId(clientId.getId());
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


}
