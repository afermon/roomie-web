package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.Notification;
import com.cosmicode.roomie.service.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Notification and its DTO NotificationDTO.
 */
@Mapper(componentModel = "spring", uses = {RoomieMapper.class})
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {

    @Mapping(source = "recipient.id", target = "recipientId")
    NotificationDTO toDto(Notification notification);

    @Mapping(source = "recipientId", target = "recipient")
    Notification toEntity(NotificationDTO notificationDTO);

    default Notification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(id);
        return notification;
    }
}
