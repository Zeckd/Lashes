package kg.mega.lashes.mappers;

import kg.mega.lashes.models.Client;
import kg.mega.lashes.models.dtos.ClientCreateDto;
import kg.mega.lashes.models.dtos.ClientUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);
    Client clientCreateDtoToClient(ClientCreateDto clientCreateDto);
    Client clientUpdateDtoToClient(ClientUpdateDto clientUpdateDto);

}
