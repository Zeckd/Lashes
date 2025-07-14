package kg.mega.lashes.mappers;

import kg.mega.lashes.models.Client;
import kg.mega.lashes.models.dtos.ClientCreateDto;
import kg.mega.lashes.models.dtos.ClientUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);
    @Mapping(source = "comment", target = "data")
    Client clientCreateDtoToClient(ClientCreateDto clientCreateDto);
    Client clientUpdateDtoToClient(ClientUpdateDto clientUpdateDto);

}
