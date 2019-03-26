package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomTask;
import com.cosmicode.roomie.repository.RoomTaskRepository;
import com.cosmicode.roomie.service.dto.RoomTaskDTO;
import com.cosmicode.roomie.service.mapper.RoomTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public RoomTaskService(RoomTaskRepository roomTaskRepository, RoomTaskMapper roomTaskMapper) {
        this.roomTaskRepository = roomTaskRepository;
        this.roomTaskMapper = roomTaskMapper;
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
        roomTask = roomTaskRepository.save(roomTask);
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
}
