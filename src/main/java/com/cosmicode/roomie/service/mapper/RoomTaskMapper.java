package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.RoomTask;
import com.cosmicode.roomie.service.dto.RoomTaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity RoomTask and its DTO RoomTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface RoomTaskMapper extends EntityMapper<RoomTaskDTO, RoomTask> {

    @Mapping(source = "room.id", target = "roomId")
    RoomTaskDTO toDto(RoomTask roomTask);

    @Mapping(source = "roomId", target = "room")
    RoomTask toEntity(RoomTaskDTO roomTaskDTO);

    default RoomTask fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomTask roomTask = new RoomTask();
        roomTask.setId(id);
        return roomTask;
    }
}
