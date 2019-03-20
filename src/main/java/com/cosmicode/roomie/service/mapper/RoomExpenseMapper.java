package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.*;
import com.cosmicode.roomie.service.dto.RoomExpenseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RoomExpense and its DTO RoomExpenseDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface RoomExpenseMapper extends EntityMapper<RoomExpenseDTO, RoomExpense> {

    @Mapping(source = "room.id", target = "roomId")
    RoomExpenseDTO toDto(RoomExpense roomExpense);

    @Mapping(target = "splits", ignore = true)
    @Mapping(source = "roomId", target = "room")
    RoomExpense toEntity(RoomExpenseDTO roomExpenseDTO);

    default RoomExpense fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomExpense roomExpense = new RoomExpense();
        roomExpense.setId(id);
        return roomExpense;
    }
}
