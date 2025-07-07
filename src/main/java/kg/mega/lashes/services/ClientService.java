package kg.mega.lashes.services;

import kg.mega.lashes.models.Client;
import kg.mega.lashes.models.dtos.ClientCreateDto;

import java.util.List;

public interface ClientService {
    Client create(ClientCreateDto clientCreateDto);

    Client createClient(String name, String phoneNumber, String data);

    List<Client> getAll();
}
