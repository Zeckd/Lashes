package kg.mega.lashes.services.impls;

import kg.mega.lashes.mappers.ClientMapper;
import kg.mega.lashes.models.Client;
import kg.mega.lashes.models.dtos.ClientCreateDto;
import kg.mega.lashes.repositories.ClientRepo;
import kg.mega.lashes.services.ClientService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    public Client createClient(String name, String phoneNumber, String data) {
//        Client client = new Client();
//        client.setName(name);
//        client.setPhoneNumber(phoneNumber);
//        client.setData(data);
//        client.setDateOfVisit(LocalDate.now());
//        return clientRepo.save(client);
        return null;
    }

    @Override
    public List<Client> getAll() {
        return clientRepo.findAll();
    }
}
