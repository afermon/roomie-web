package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.Room;
import com.cosmicode.roomie.service.dto.RoomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Room and its DTO RoomDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, RoomExpenseMapper.class, RoomieMapper.class, RoomFeatureMapper.class})
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "price.id", target = "priceId")
    @Mapping(source = "owner.id", target = "ownerId")
    RoomDTO toDto(Room room);

    @Mapping(source = "addressId", target = "address")
    @Mapping(source = "priceId", target = "price")
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "roomTasks", ignore = true)
    @Mapping(target = "roomEvents", ignore = true)
    @Mapping(source = "ownerId", target = "owner")
    @Mapping(target = "expenses", ignore = true)
    @Mapping(target = "pictures")
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
