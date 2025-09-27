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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dayOfVisit", ignore = true)
    @Mapping(target = "remove", ignore = true)
    @Mapping(target = "dateOfRegister", ignore = true)
    Client clientCreateDtoToClient(ClientCreateDto clientCreateDto);

    @Mapping(source = "comment", target = "data")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dayOfVisit", ignore = true)
    @Mapping(target = "remove", ignore = true)
    @Mapping(target = "dateOfRegister", ignore = true)
    Client clientUpdateDtoToClient(ClientUpdateDto clientUpdateDto);

}
