package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomEvent;
import com.cosmicode.roomie.repository.RoomEventRepository;
import com.cosmicode.roomie.repository.search.RoomEventSearchRepository;
import com.cosmicode.roomie.service.dto.RoomEventDTO;
import com.cosmicode.roomie.service.mapper.RoomEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RoomEvent.
 */
@Service
@Transactional
public class RoomEventService {

    private final Logger log = LoggerFactory.getLogger(RoomEventService.class);

    private final RoomEventRepository roomEventRepository;

    private final RoomEventMapper roomEventMapper;

    private final RoomEventSearchRepository roomEventSearchRepository;

    public RoomEventService(RoomEventRepository roomEventRepository, RoomEventMapper roomEventMapper, RoomEventSearchRepository roomEventSearchRepository) {
        this.roomEventRepository = roomEventRepository;
        this.roomEventMapper = roomEventMapper;
        this.roomEventSearchRepository = roomEventSearchRepository;
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
        roomEvent = roomEventRepository.save(roomEvent);
        RoomEventDTO result = roomEventMapper.toDto(roomEvent);
        roomEventSearchRepository.save(roomEvent);
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
        roomEventSearchRepository.deleteById(id);
    }

    /**
     * Search for the roomEvent corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomEventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RoomEvents for query {}", query);
        return roomEventSearchRepository.search(queryStringQuery(query), pageable)
            .map(roomEventMapper::toDto);
    }
}
