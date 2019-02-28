package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.*;
import com.cosmicode.roomie.service.dto.RoomFeatureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RoomFeature and its DTO RoomFeatureDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoomFeatureMapper extends EntityMapper<RoomFeatureDTO, RoomFeature> {



    default RoomFeature fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomFeature roomFeature = new RoomFeature();
        roomFeature.setId(id);
        return roomFeature;
    }
}
