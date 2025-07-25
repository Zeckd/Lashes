package kg.mega.lashes.controllers;

import jakarta.validation.Valid;
import kg.mega.lashes.models.Client;
import kg.mega.lashes.models.dtos.ClientCreateDto;
import kg.mega.lashes.models.dtos.ClientUpdateDto;
import kg.mega.lashes.services.ClientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/create")

    public Client createClient(@Valid @RequestBody(required = false) ClientCreateDto clientCreateDto) {
        return clientService.create(clientCreateDto);
    }
    @PutMapping("/update/{id}")
    public Client updateClient(@Valid @RequestBody ClientUpdateDto clientUpdateDto, @PathVariable Long id) {
        return clientService.update(clientUpdateDto, id);
    }
    @GetMapping("/getAll")
    public List<Client> getAllClients() {
        return clientService.getAll();
    }
    @GetMapping("/getByName")
    public List<Client> getClientByName(@RequestParam String name) {
        return clientService.getByName(name);
    }
    @GetMapping("/getByPhoneNumber")
    public List<Client> getClientByPhoneNumber(@RequestParam String phoneNumber) {
        return clientService.getByPhoneNumber(phoneNumber);
    }
    @DeleteMapping("/delete")
    public Client deleteClient(@RequestParam Long id) {
        return clientService.delete(id);
    }
    @GetMapping("/taken-times")
    public ResponseEntity<List<LocalTime>> getTakenTimes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(clientService.getTakenTimesByDate(date));
    }

}
