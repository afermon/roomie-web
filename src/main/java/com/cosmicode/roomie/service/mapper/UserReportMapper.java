package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.UserReport;
import com.cosmicode.roomie.service.dto.UserReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity UserReport and its DTO UserReportDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomieMapper.class, RoomMapper.class})
public interface UserReportMapper extends EntityMapper<UserReportDTO, UserReport> {

    @Mapping(source = "roomie.id", target = "roomieId")
    @Mapping(source = "room.id", target = "roomId")
    UserReportDTO toDto(UserReport userReport);

    @Mapping(source = "roomieId", target = "roomie")
    @Mapping(source = "roomId", target = "room")
    UserReport toEntity(UserReportDTO userReportDTO);

    default UserReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserReport userReport = new UserReport();
        userReport.setId(id);
        return userReport;
    }
}
