package kg.mega.lashes.controllers;

import kg.mega.lashes.models.Client;
import kg.mega.lashes.models.dtos.ClientCreateDto;
import kg.mega.lashes.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
//    @PostMapping("/create")
//    public  Client create(@RequestParam String name,@RequestParam  String phoneNumber,@RequestParam  String data) {
//        return clientService.createClient(name,phoneNumber,data);

//    }
    @PostMapping("/create")

    public Client createClient(@RequestBody ClientCreateDto clientCreateDto) {
        return clientService.create(clientCreateDto);
    }
    @GetMapping("/getAll")
    public List<Client> getAllClients() {
        return clientService.getAll();
    }
}
