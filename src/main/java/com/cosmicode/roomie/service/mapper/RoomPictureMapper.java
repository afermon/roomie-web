package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.RoomPicture;
import com.cosmicode.roomie.service.dto.RoomPictureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity RoomPicture and its DTO RoomPictureDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface RoomPictureMapper extends EntityMapper<RoomPictureDTO, RoomPicture> {

    @Mapping(source = "room.id", target = "roomId")
    RoomPictureDTO toDto(RoomPicture roomPicture);

    @Mapping(source = "roomId", target = "room")
    RoomPicture toEntity(RoomPictureDTO roomPictureDTO);

    default RoomPicture fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomPicture roomPicture = new RoomPicture();
        roomPicture.setId(id);
        return roomPicture;
    }
}
