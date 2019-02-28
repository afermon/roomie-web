package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.*;
import com.cosmicode.roomie.service.dto.RoomExpenseSplitRecordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RoomExpenseSplitRecord and its DTO RoomExpenseSplitRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomExpenseSplitMapper.class})
public interface RoomExpenseSplitRecordMapper extends EntityMapper<RoomExpenseSplitRecordDTO, RoomExpenseSplitRecord> {

    @Mapping(source = "split.id", target = "splitId")
    RoomExpenseSplitRecordDTO toDto(RoomExpenseSplitRecord roomExpenseSplitRecord);

    @Mapping(source = "splitId", target = "split")
    RoomExpenseSplitRecord toEntity(RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO);

    default RoomExpenseSplitRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomExpenseSplitRecord roomExpenseSplitRecord = new RoomExpenseSplitRecord();
        roomExpenseSplitRecord.setId(id);
        return roomExpenseSplitRecord;
    }
}
