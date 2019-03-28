package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.Notification;
import com.cosmicode.roomie.domain.enumeration.NotificationState;
import com.cosmicode.roomie.repository.NotificationRepository;
import com.cosmicode.roomie.service.dto.NotificationDTO;
import com.cosmicode.roomie.service.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Notification.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final PushNotificationService pushNotificationService;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper, PushNotificationService pushNotificationService) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.pushNotificationService = pushNotificationService;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save
     * @return the persisted entity
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        if(notification.getState() == NotificationState.NEW)
            pushNotificationService.send(notification);
        NotificationDTO result = notificationMapper.toDto(notification);
        return result;
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable)
            .map(notificationMapper::toDto);
    }


    /**
     * Get one notification by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id)
            .map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    /**
     * Get all the notifications for current roomie.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAllCurrentRoomie(Pageable pageable) {
        log.debug("Request to get all Notifications for current roomie");
        return notificationRepository.findCurrentlyLoggedRoomie(pageable)
            .map(notificationMapper::toDto);
    }
}
