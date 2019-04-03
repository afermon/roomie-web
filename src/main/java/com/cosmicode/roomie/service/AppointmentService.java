package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.Appointment;
import com.cosmicode.roomie.domain.enumeration.AppointmentState;
import com.cosmicode.roomie.domain.enumeration.NotificationState;
import com.cosmicode.roomie.domain.enumeration.NotificationType;
import com.cosmicode.roomie.repository.AppointmentRepository;
import com.cosmicode.roomie.service.dto.AppointmentDTO;
import com.cosmicode.roomie.service.dto.NotificationDTO;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.service.dto.RoomieDTO;
import com.cosmicode.roomie.service.mapper.AppointmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing Appointment.
 */
@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    private final NotificationService notificationService;

    private final RoomService roomService;

    private final RoomieService roomieService;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, RoomService roomService, NotificationService notificationService, RoomieService roomieService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.notificationService = notificationService;
        this.roomService = roomService;
        this.roomieService = roomieService;
    }

    /**
     * Save a appointment.
     *
     * @param appointmentDTO the entity to save
     * @return the persisted entity
     */
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        log.debug("Request to save Appointment : {}", appointmentDTO);

        try {
            if(appointmentDTO.getState().equals(AppointmentState.PENDING)) {
                RoomieDTO roomieDTO = roomieService.findCurrentLoggedRoomie();
                appointmentDTO.setPetitioner(roomieDTO);
                appointmentDTO.setPetitionerId(roomieDTO.getId());
            }
        } catch (Exception e) {
            log.error("Error getting current Roomie");
        }

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);

        appointment = appointmentRepository.save(appointment);

        appointment = appointmentRepository.findById(appointment.getId()).get();

        try {
            NotificationDTO notification = new NotificationDTO();
            notification.setCreated(Instant.now());
            notification.setState(NotificationState.NEW);
            notification.setType(NotificationType.APPOINTMENT);
            notification.setEntityId(appointment.getId());

            switch (appointment.getState()) {
                case ACCEPTED:
                    notification.setRecipientId(appointment.getPetitioner().getId());
                    notification.setTitle("Your appointment request has been accepted!");
                    notification.setBody("Yay! the owner accepted your appointment request. See you there!");
                    break;
                case DECLINED:
                    notification.setRecipientId(appointment.getPetitioner().getId());
                    notification.setTitle("Sorry, the owner can't make it!");
                    notification.setBody("Unfortunately the owner declined your appointment request!");
                    break;
                case PENDING:
                    Optional<RoomDTO> room = roomService.findOne(appointment.getRoom().getId());
                    if (room.isPresent()) {
                        notification.setRecipientId(room.get().getOwnerId());
                        notification.setTitle("New appointment request!");
                        notification.setBody("You got a new appointment request for your room.");
                    }
                    break;
            }
            notificationService.save(notification);
        } catch (Exception e) {
            log.error("Error building notification {}", e.getMessage());
        }

        return appointmentMapper.toDto(appointment);
    }

    /**
     * Get all the appointments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appointments");
        return appointmentRepository.findAll(pageable)
            .map(appointmentMapper::toDto);
    }


    /**
     * Get one appointment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AppointmentDTO> findOne(Long id) {
        log.debug("Request to get Appointment : {}", id);
        return appointmentRepository.findById(id)
            .map(appointmentMapper::toDto);
    }

    /**
     * Delete the appointment by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Appointment : {}", id);
        appointmentRepository.deleteById(id);
    }

    /**
     * Get all the appointments for roomie.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findAllRoomie(Pageable pageable) {
        log.debug("Request to get all Appointments for roomie");
        return appointmentRepository.findAllRoomie(pageable)
            .map(appointmentMapper::toDto);
    }
}
