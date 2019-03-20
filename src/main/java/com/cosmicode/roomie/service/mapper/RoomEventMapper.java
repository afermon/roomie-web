package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.*;
import com.cosmicode.roomie.service.dto.RoomEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RoomEvent and its DTO RoomEventDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomMapper.class, RoomieMapper.class})
public interface RoomEventMapper extends EntityMapper<RoomEventDTO, RoomEvent> {

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "organizer.id", target = "organizerId")
    RoomEventDTO toDto(RoomEvent roomEvent);

    @Mapping(source = "roomId", target = "room")
    @Mapping(source = "organizerId", target = "organizer")
    RoomEvent toEntity(RoomEventDTO roomEventDTO);

    default RoomEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setId(id);
        return roomEvent;
    }
}
