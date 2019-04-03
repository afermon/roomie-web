package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.Appointment;
import com.cosmicode.roomie.service.dto.AppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Appointment and its DTO AppointmentDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomieMapper.class, RoomMapper.class})
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {

    @Mapping(source = "petitioner.id", target = "petitionerId")
    @Mapping(target = "room")
    @Mapping(source = "room.id", target = "roomId")
    AppointmentDTO toDto(Appointment appointment);

    @Mapping(source = "petitionerId", target = "petitioner")
    @Mapping(source = "roomId", target = "room")
    Appointment toEntity(AppointmentDTO appointmentDTO);

    default Appointment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(id);
        return appointment;
    }
}
