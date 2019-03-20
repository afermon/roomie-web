package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.Notification;
import com.cosmicode.roomie.service.dto.NotificationDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Notification and its DTO NotificationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {



    default Notification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(id);
        return notification;
    }
}
