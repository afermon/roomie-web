package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.*;
import com.cosmicode.roomie.service.dto.RoomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Room and its DTO RoomDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, RoomieMapper.class, RoomFeatureMapper.class})
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "owner.id", target = "ownerId")
    RoomDTO toDto(Room room);

    @Mapping(target = "address")
    @Mapping(target = "roomExpenses", ignore = true)
    @Mapping(target = "picutres", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "roomTasks", ignore = true)
    @Mapping(target = "roomEvents", ignore = true)
    @Mapping(source = "ownerId", target = "owner")
    Room toEntity(RoomDTO roomDTO);

    default Room fromId(Long id) {
        if (id == null) {
            return null;
        }
        Room room = new Room();
        room.setId(id);
        return room;
    }
}
