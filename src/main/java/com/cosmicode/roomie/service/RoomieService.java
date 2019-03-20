package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.Roomie;
import com.cosmicode.roomie.repository.RoomieRepository;
import com.cosmicode.roomie.repository.search.RoomieSearchRepository;
import com.cosmicode.roomie.service.dto.RoomieDTO;
import com.cosmicode.roomie.service.mapper.RoomieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Roomie.
 */
@Service
@Transactional
public class RoomieService {

    private final Logger log = LoggerFactory.getLogger(RoomieService.class);

    private final RoomieRepository roomieRepository;

    private final RoomieMapper roomieMapper;

    private final RoomieSearchRepository roomieSearchRepository;

    public RoomieService(RoomieRepository roomieRepository, RoomieMapper roomieMapper, RoomieSearchRepository roomieSearchRepository) {
        this.roomieRepository = roomieRepository;
        this.roomieMapper = roomieMapper;
        this.roomieSearchRepository = roomieSearchRepository;
    }

    /**
     * Save a roomie.
     *
     * @param roomieDTO the entity to save
     * @return the persisted entity
     */
    public RoomieDTO save(RoomieDTO roomieDTO) {
        log.debug("Request to save Roomie : {}", roomieDTO);
        Roomie roomie = roomieMapper.toEntity(roomieDTO);
        roomie = roomieRepository.save(roomie);
        RoomieDTO result = roomieMapper.toDto(roomie);
        roomieSearchRepository.save(roomie);
        return result;
    }

    /**
     * Get all the roomies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Roomies");
        return roomieRepository.findAll(pageable)
            .map(roomieMapper::toDto);
    }

    /**
     * Get all the Roomie with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<RoomieDTO> findAllWithEagerRelationships(Pageable pageable) {
        return roomieRepository.findAllWithEagerRelationships(pageable).map(roomieMapper::toDto);
    }


    /**
     * Get one roomie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomieDTO> findOne(Long id) {
        log.debug("Request to get Roomie : {}", id);
        return roomieRepository.findOneWithEagerRelationships(id)
            .map(roomieMapper::toDto);
    }

    /**
     * Delete the roomie by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Roomie : {}", id);
        roomieRepository.deleteById(id);
        roomieSearchRepository.deleteById(id);
    }

    /**
     * Search for the roomie corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomieDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Roomies for query {}", query);
        return roomieSearchRepository.search(queryStringQuery(query), pageable)
            .map(roomieMapper::toDto);
    }

    public RoomieDTO findCurrentLoggedRoomie(){
        log.debug("Request to get currently logged Roomie");
        return  roomieMapper.toDto(roomieRepository.findCurrentlyLoggedRoomie());
    }
}
