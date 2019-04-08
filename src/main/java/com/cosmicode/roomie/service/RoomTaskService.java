package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomTask;
import com.cosmicode.roomie.domain.enumeration.NotificationState;
import com.cosmicode.roomie.domain.enumeration.NotificationType;
import com.cosmicode.roomie.repository.RoomTaskRepository;
import com.cosmicode.roomie.service.dto.NotificationDTO;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.service.dto.RoomTaskDTO;
import com.cosmicode.roomie.service.dto.RoomieDTO;
import com.cosmicode.roomie.service.mapper.RoomTaskMapper;
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
 * Service Implementation for managing RoomTask.
 */
@Service
@Transactional
public class RoomTaskService {

    private final Logger log = LoggerFactory.getLogger(RoomTaskService.class);

    private final RoomTaskRepository roomTaskRepository;

    private final RoomTaskMapper roomTaskMapper;

    private final RoomService roomService;

    private final NotificationService notificationService;

    public RoomTaskService(RoomTaskRepository roomTaskRepository, RoomTaskMapper roomTaskMapper, NotificationService notificationService, RoomService roomService) {
        this.roomTaskRepository = roomTaskRepository;
        this.roomTaskMapper = roomTaskMapper;
        this.notificationService = notificationService;
        this.roomService = roomService;
    }

    /**
     * Save a roomTask.
     *
     * @param roomTaskDTO the entity to save
     * @return the persisted entity
     */
    public RoomTaskDTO save(RoomTaskDTO roomTaskDTO) {
        log.debug("Request to save RoomTask : {}", roomTaskDTO);
        RoomTask roomTask = roomTaskMapper.toEntity(roomTaskDTO);

        boolean sendNotification = roomTask.getId() == null;

        roomTask = roomTaskRepository.save(roomTask);

        if(sendNotification)
            sendNotificationRoomies(roomTask, true);

        RoomTaskDTO result = roomTaskMapper.toDto(roomTask);
        return result;
    }

    /**
     * Get all the roomTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomTasks");
        return roomTaskRepository.findAll(pageable)
            .map(roomTaskMapper::toDto);
    }


    /**
     * Get one roomTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomTaskDTO> findOne(Long id) {
        log.debug("Request to get RoomTask : {}", id);
        return roomTaskRepository.findById(id)
            .map(roomTaskMapper::toDto);
    }

    /**
     * Delete the roomTask by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomTask : {}", id);
        roomTaskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RoomTaskDTO> findAllByRoom(Long id) {
        log.debug("Request to get all RoomTasks by room id");
        return roomTaskMapper.toDto(roomTaskRepository.findByRoomId(id));
    }


    /**
     * Task that sends task notifications every 30 minutes.
     * Scheduled task.
     */
    @Scheduled(cron = "0 */30 * * * *")
    public void scheduledRoomEventsNotification(){
        log.info("Room Tasks notification execution: {}", Instant.now().toString());

        Instant startTime = Instant.now().plus(Duration.ofMinutes(59));
        Instant endTime = Instant.now().plus(Duration.ofMinutes(91));

        List<RoomTask> roomTasks = roomTaskRepository.findByDeadlineBetween(startTime, endTime);

        log.info("{} tasks found between {}, {}", roomTasks.size(), startTime.toString(), endTime.toString());

        for(RoomTask roomTask : roomTasks)
            sendNotificationRoomies(roomTask, false);
    }

    private void sendNotificationRoomies(RoomTask roomTask, Boolean isNew) {
        try {
            RoomDTO room = roomService.findOne(roomTask.getRoom().getId()).get();

            //Notify room owner
            sendNotification(roomTask, room.getOwnerId(), isNew);

            //Notify roomies
            for (RoomieDTO roomie : room.getRoomies())
                    sendNotification(roomTask, roomie.getId(), isNew);
        } catch (NullPointerException e){
            log.error("Error sending notification: {}", e.getMessage());
        }
    }

    private void sendNotification(RoomTask roomTask, Long recipientId, boolean isNew){
        NotificationDTO notification = new NotificationDTO();
        notification.setCreated(Instant.now());
        notification.setState(NotificationState.NEW);
        notification.setType(NotificationType.TODO);
        notification.setEntityId(roomTask.getId());
        notification.setRecipientId(recipientId);

        if(isNew)
            notification.setTitle("A new task has been added to the room!");
        else
            notification.setTitle("One of your room tasks needs to be completed soon!");

        notification.setBody(String.format("%s, deadline: %s UTC", roomTask.getTitle(), roomTask.getDeadline().toString()));

        notificationService.save(notification);
    }
}
