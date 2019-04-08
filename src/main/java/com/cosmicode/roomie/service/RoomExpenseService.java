package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomEvent;
import com.cosmicode.roomie.domain.RoomExpense;
import com.cosmicode.roomie.domain.enumeration.NotificationState;
import com.cosmicode.roomie.domain.enumeration.NotificationType;
import com.cosmicode.roomie.repository.RoomExpenseRepository;
import com.cosmicode.roomie.service.dto.NotificationDTO;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.service.dto.RoomExpenseDTO;
import com.cosmicode.roomie.service.dto.RoomieDTO;
import com.cosmicode.roomie.service.mapper.RoomExpenseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing RoomExpense.
 */
@Service
@Transactional
public class RoomExpenseService {

    private final Logger log = LoggerFactory.getLogger(RoomExpenseService.class);

    private final RoomExpenseRepository roomExpenseRepository;

    private final RoomExpenseMapper roomExpenseMapper;

    private final NotificationService notificationService;

    private final RoomService roomService;

    public RoomExpenseService(RoomExpenseRepository roomExpenseRepository, RoomExpenseMapper roomExpenseMapper, NotificationService notificationService, RoomService roomService) {
        this.roomExpenseRepository = roomExpenseRepository;
        this.roomExpenseMapper = roomExpenseMapper;
        this.notificationService = notificationService;
        this.roomService = roomService;
    }

    /**
     * Save a roomExpense.
     *
     * @param roomExpenseDTO the entity to save
     * @return the persisted entity
     */
    public RoomExpenseDTO save(RoomExpenseDTO roomExpenseDTO) {
        log.debug("Request to save RoomExpense : {}", roomExpenseDTO);
        RoomExpense roomExpense = roomExpenseMapper.toEntity(roomExpenseDTO);

        boolean sendNotification = roomExpense.getId() == null;

        roomExpense = roomExpenseRepository.save(roomExpense);

        if(sendNotification)
            sendNotificationRoomies(roomExpense, true);

        RoomExpenseDTO result = roomExpenseMapper.toDto(roomExpense);
        return result;
    }

    /**
     * Get all the roomExpenses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomExpenseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomExpenses");
        return roomExpenseRepository.findAll(pageable)
            .map(roomExpenseMapper::toDto);
    }


    /**
     * Get one roomExpense by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomExpenseDTO> findOne(Long id) {
        log.debug("Request to get RoomExpense : {}", id);
        return roomExpenseRepository.findById(id)
            .map(roomExpenseMapper::toDto);
    }

    /**
     * Delete the roomExpense by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomExpense : {}", id);
        roomExpenseRepository.deleteById(id);
    }

    /**
     * Task that sends expenses notifications every day minutes.
     * Scheduled task.
     */
    @Scheduled(cron = "0 0 */12 * * *")
    public void scheduledRoomExpensesNotification(){
        log.info("Room expenses notification execution: {}", Instant.now().toString());

        LocalDate now = LocalDate.now();
        LocalDate startTime = LocalDate.now().plus(Duration.ofDays(1)).minus(Duration.ofMinutes(1));
        LocalDate endTime = LocalDate.now().plus(Duration.ofDays(2)).plus(Duration.ofMinutes(1));

        //Get active expenses
        List<RoomExpense> roomExpenses =  roomExpenseRepository.findByStartDateBeforeAndFinishDateAfter(startTime, endTime);

        log.info("{} expenses found due between {}, {}", roomExpenses.size(), startTime.toString(), endTime.toString());

        for(RoomExpense roomExpense : roomExpenses) {
            //Amount of weeks between start date and start date
            int weeks = (int) ChronoUnit.WEEKS.between(roomExpense.getStartDate(), startTime);
            if((weeks % roomExpense.getPeriodicity() == 0) && roomExpense.getStartDate().getDayOfWeek() == startTime.getDayOfWeek()) //Means is due next week.
                sendNotificationRoomies(roomExpense, false);
        }
    }

    private void sendNotificationRoomies(RoomExpense roomExpense, Boolean isNew) {
        try {
            RoomDTO room = roomService.findOne(roomExpense.getRoom().getId()).get();
            //Notify room owner
            sendNotification(roomExpense, room.getOwnerId(), isNew);
            //Notify roomies
            for (RoomieDTO roomie : room.getRoomies())
                    sendNotification(roomExpense, roomie.getId(), isNew);

        } catch (NullPointerException e){
            log.error("Error sending notification: {}", e.getMessage());
        }
    }

    private void sendNotification(RoomExpense roomExpense, Long recipientId, boolean isNew){
        NotificationDTO notification = new NotificationDTO();
        notification.setCreated(Instant.now());
        notification.setState(NotificationState.NEW);
        notification.setType(NotificationType.EXPENSE);
        notification.setEntityId(roomExpense.getId());
        notification.setRecipientId(recipientId);

        if(isNew)
            notification.setTitle("A new expense has been added to the room!");
        else
            notification.setTitle("roomExpense.getName() is due tomorrow!");

        notification.setBody(String.format("%s, %s %s", roomExpense.getName(), roomExpense.getAmount(), roomExpense.getCurrency()));

        notificationService.save(notification);
    }
}
