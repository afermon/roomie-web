package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.Room;
import com.cosmicode.roomie.repository.RoomRepository;
import com.cosmicode.roomie.repository.search.RoomSearchRepository;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.service.mapper.RoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Room.
 */
@Service
@Transactional
public class RoomService {

    private final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    private final RoomSearchRepository roomSearchRepository;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper, RoomSearchRepository roomSearchRepository) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomSearchRepository = roomSearchRepository;
    }

    /**
     * Save a room.
     *
     * @param roomDTO the entity to save
     * @return the persisted entity
     */
    public RoomDTO save(RoomDTO roomDTO) {
        log.debug("Request to save Room : {}", roomDTO);

        Room room = roomMapper.toEntity(roomDTO);
        room = roomRepository.save(room);
        RoomDTO result = roomMapper.toDto(room);
        roomSearchRepository.save(room);
        return result;
    }

    /**
     * Get all the rooms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rooms");
        return roomRepository.findAll(pageable)
            .map(roomMapper::toDto);
    }

    /**
     * Get all the Room with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<RoomDTO> findAllWithEagerRelationships(Pageable pageable) {
        return roomRepository.findAllWithEagerRelationships(pageable).map(roomMapper::toDto);
    }
    

    /**
     * Get one room by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomDTO> findOne(Long id) {
        log.debug("Request to get Room : {}", id);
        return roomRepository.findOneWithEagerRelationships(id)
            .map(roomMapper::toDto);
    }

    /**
     * Delete the room by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Room : {}", id);
        roomRepository.deleteById(id);
        roomSearchRepository.deleteById(id);
    }

    /**
     * Search for the room corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Rooms for query {}", query);
        return roomSearchRepository.search(queryStringQuery(query), pageable)
            .map(roomMapper::toDto);
    }
}