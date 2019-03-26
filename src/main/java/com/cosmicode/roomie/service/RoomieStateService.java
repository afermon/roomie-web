package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomieState;
import com.cosmicode.roomie.repository.RoomieStateRepository;
import com.cosmicode.roomie.service.dto.RoomieStateDTO;
import com.cosmicode.roomie.service.mapper.RoomieStateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing RoomieState.
 */
@Service
@Transactional
public class RoomieStateService {

    private final Logger log = LoggerFactory.getLogger(RoomieStateService.class);

    private final RoomieStateRepository roomieStateRepository;

    private final RoomieStateMapper roomieStateMapper;

    public RoomieStateService(RoomieStateRepository roomieStateRepository, RoomieStateMapper roomieStateMapper) {
        this.roomieStateRepository = roomieStateRepository;
        this.roomieStateMapper = roomieStateMapper;
    }

    /**
     * Save a roomieState.
     *
     * @param roomieStateDTO the entity to save
     * @return the persisted entity
     */
    public RoomieStateDTO save(RoomieStateDTO roomieStateDTO) {
        log.debug("Request to save RoomieState : {}", roomieStateDTO);
        RoomieState roomieState = roomieStateMapper.toEntity(roomieStateDTO);
        roomieState = roomieStateRepository.save(roomieState);
        RoomieStateDTO result = roomieStateMapper.toDto(roomieState);
        return result;
    }

    /**
     * Get all the roomieStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomieStateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomieStates");
        return roomieStateRepository.findAll(pageable)
            .map(roomieStateMapper::toDto);
    }


    /**
     * Get one roomieState by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomieStateDTO> findOne(Long id) {
        log.debug("Request to get RoomieState : {}", id);
        return roomieStateRepository.findById(id)
            .map(roomieStateMapper::toDto);
    }

    /**
     * Delete the roomieState by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomieState : {}", id);
        roomieStateRepository.deleteById(id);
    }

}
