package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.RoomExpenseSplit;
import com.cosmicode.roomie.service.dto.RoomExpenseSplitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity RoomExpenseSplit and its DTO RoomExpenseSplitDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomExpenseMapper.class, RoomieMapper.class})
public interface RoomExpenseSplitMapper extends EntityMapper<RoomExpenseSplitDTO, RoomExpenseSplit> {

    @Mapping(source = "expense.id", target = "expenseId")
    @Mapping(source = "roomie.id", target = "roomieId")
    RoomExpenseSplitDTO toDto(RoomExpenseSplit roomExpenseSplit);

    @Mapping(target = "records", ignore = true)
    @Mapping(source = "expenseId", target = "expense")
    @Mapping(source = "roomieId", target = "roomie")
    RoomExpenseSplit toEntity(RoomExpenseSplitDTO roomExpenseSplitDTO);

    default RoomExpenseSplit fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomExpenseSplit roomExpenseSplit = new RoomExpenseSplit();
        roomExpenseSplit.setId(id);
        return roomExpenseSplit;
    }
}
