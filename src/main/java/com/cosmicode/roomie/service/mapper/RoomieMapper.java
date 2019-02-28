package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.*;
import com.cosmicode.roomie.service.dto.RoomieDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Roomie and its DTO RoomieDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, RoomieStateMapper.class, AddressMapper.class, UserPreferencesMapper.class, RoomFeatureMapper.class})
public interface RoomieMapper extends EntityMapper<RoomieDTO, Roomie> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "configuration.id", target = "configurationId")
    RoomieDTO toDto(Roomie roomie);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "stateId", target = "state")
    @Mapping(source = "addressId", target = "address")
    @Mapping(source = "configurationId", target = "configuration")
    @Mapping(target = "roomExpenseSplits", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "roomEvents", ignore = true)
    Roomie toEntity(RoomieDTO roomieDTO);

    default Roomie fromId(Long id) {
        if (id == null) {
            return null;
        }
        Roomie roomie = new Roomie();
        roomie.setId(id);
        return roomie;
    }
}
