package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.RoomieState;
import com.cosmicode.roomie.service.dto.RoomieStateDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity RoomieState and its DTO RoomieStateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoomieStateMapper extends EntityMapper<RoomieStateDTO, RoomieState> {



    default RoomieState fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomieState roomieState = new RoomieState();
        roomieState.setId(id);
        return roomieState;
    }
}
