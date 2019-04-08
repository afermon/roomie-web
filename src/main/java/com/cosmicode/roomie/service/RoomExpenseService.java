package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomEvent;
import com.cosmicode.roomie.domain.RoomExpense;
import com.cosmicode.roomie.domain.enumeration.NotificationState;
import com.cosmicode.roomie.domain.enumeration.NotificationType;
import com.cosmicode.roomie.repository.RoomExpenseRepository;
import com.cosmicode.roomie.service.dto.NotificationDTO;
import com.cosmicode.roomie.service.dto.RoomExpenseDTO;
import com.cosmicode.roomie.service.mapper.RoomExpenseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

    public RoomExpenseService(RoomExpenseRepository roomExpenseRepository, RoomExpenseMapper roomExpenseMapper, NotificationService notificationService) {
        this.roomExpenseRepository = roomExpenseRepository;
        this.roomExpenseMapper = roomExpenseMapper;
        this.notificationService = notificationService;
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
        roomExpense = roomExpenseRepository.save(roomExpense);
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


    private void sendNotification(RoomEvent event, Long recipientId, boolean isNew){
        NotificationDTO notification = new NotificationDTO();
        notification.setCreated(Instant.now());
        notification.setState(NotificationState.NEW);
        notification.setType(NotificationType.EXPENSE);
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
