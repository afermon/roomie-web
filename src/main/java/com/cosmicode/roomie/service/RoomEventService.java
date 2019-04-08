package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomEvent;
import com.cosmicode.roomie.domain.enumeration.NotificationState;
import com.cosmicode.roomie.domain.enumeration.NotificationType;
import com.cosmicode.roomie.repository.RoomEventRepository;
import com.cosmicode.roomie.service.dto.NotificationDTO;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.service.dto.RoomEventDTO;
import com.cosmicode.roomie.service.dto.RoomieDTO;
import com.cosmicode.roomie.service.mapper.RoomEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing RoomEvent.
 */
@Service
@Transactional
public class RoomEventService {

    private final Logger log = LoggerFactory.getLogger(RoomEventService.class);

    private final RoomEventRepository roomEventRepository;

    private final RoomEventMapper roomEventMapper;

    private final RoomService roomService;

    private final NotificationService notificationService;

    public RoomEventService(RoomEventRepository roomEventRepository, RoomEventMapper roomEventMapper, NotificationService notificationService, RoomService roomService) {
        this.roomEventRepository = roomEventRepository;
        this.roomEventMapper = roomEventMapper;
        this.roomService = roomService;
        this.notificationService = notificationService;
    }

    /**
     * Save a roomEvent.
     *
     * @param roomEventDTO the entity to save
     * @return the persisted entity
     */
    public RoomEventDTO save(RoomEventDTO roomEventDTO) {
        log.debug("Request to save RoomEvent : {}", roomEventDTO);
        RoomEvent roomEvent = roomEventMapper.toEntity(roomEventDTO);

        boolean sendNotification = roomEvent.getId() == null;

        roomEvent = roomEventRepository.save(roomEvent);

        if(sendNotification)
            sendNotificationRoomies(roomEvent, true);

        RoomEventDTO result = roomEventMapper.toDto(roomEvent);
        return result;
    }

    /**
     * Get all the roomEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomEvents");
        return roomEventRepository.findAll(pageable)
            .map(roomEventMapper::toDto);
    }


    /**
     * Get one roomEvent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomEventDTO> findOne(Long id) {
        log.debug("Request to get RoomEvent : {}", id);
        return roomEventRepository.findById(id)
            .map(roomEventMapper::toDto);
    }

    /**
     * Delete the roomEvent by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomEvent : {}", id);
        roomEventRepository.deleteById(id);
    }

    /**
     * Get all the roomEvents for room.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomEventDTO> findAllByRoom(Long id, Pageable pageable) {
        log.debug("Request to get all RoomEvents for room");
        return roomEventRepository.findAllByRoom(pageable, id)
            .map(roomEventMapper::toDto);
    }

    /**
     * Task that sends events notifications every 30 minutes.
     * Scheduled task.
     */
    @Scheduled(cron = "0 0/30 * * * *")
    public void scheduledRoomEventsNotification(){
        log.info("Room Events notification execution: {}", Instant.now().toString());

        Instant startTime = Instant.now().plus(Duration.ofMinutes(59));
        Instant endTime = Instant.now().plus(Duration.ofMinutes(91));

        List<RoomEvent> roomEvents = roomEventRepository.findByStartTimeBetween(startTime, endTime);

        log.info("{} events found between {}, {}", roomEvents.size(), startTime.toString(), endTime.toString());

        for(RoomEvent roomEvent : roomEvents)
            sendNotificationRoomies(roomEvent, false);
    }

    private void sendNotificationRoomies(RoomEvent roomEvent, Boolean isNew) {
        try {
            RoomDTO room = roomService.findOne(roomEvent.getRoom().getId()).get();

            //Notify room owner
            if (!isNew || !room.getOwnerId().equals(roomEvent.getOrganizer().getId()))
                sendNotification(roomEvent, room.getOwnerId(), isNew);

            //Notify roomies
            for (RoomieDTO roomie : room.getRoomies())
                if (!isNew || !roomie.getId().equals(roomEvent.getOrganizer().getId()))
                    sendNotification(roomEvent, roomie.getId(), isNew);
        } catch (NullPointerException e){
            log.error("Error sending notification: {}", e.getMessage());
        }
    }

    private void sendNotification(RoomEvent event, Long recipientId, boolean isNew){
        NotificationDTO notification = new NotificationDTO();
        notification.setCreated(Instant.now());
        notification.setState(NotificationState.NEW);
        notification.setType(NotificationType.EVENT);
        notification.setEntityId(event.getId());
        notification.setRecipientId(recipientId);

        if(isNew)
            notification.setTitle("A new event has been created in your room!");
        else
            notification.setTitle("An event is about to start in your room!");

        notification.setBody(String.format("%s, %s UTC", event.getTitle(), event.getStartTime().toString()));

        notificationService.save(notification);
    }
}
