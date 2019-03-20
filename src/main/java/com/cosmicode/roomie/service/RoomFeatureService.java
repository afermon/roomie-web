package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomFeature;
import com.cosmicode.roomie.repository.RoomFeatureRepository;
import com.cosmicode.roomie.repository.search.RoomFeatureSearchRepository;
import com.cosmicode.roomie.service.dto.RoomFeatureDTO;
import com.cosmicode.roomie.service.mapper.RoomFeatureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RoomFeature.
 */
@Service
@Transactional
public class RoomFeatureService {

    private final Logger log = LoggerFactory.getLogger(RoomFeatureService.class);

    private final RoomFeatureRepository roomFeatureRepository;

    private final RoomFeatureMapper roomFeatureMapper;

    private final RoomFeatureSearchRepository roomFeatureSearchRepository;

    public RoomFeatureService(RoomFeatureRepository roomFeatureRepository, RoomFeatureMapper roomFeatureMapper, RoomFeatureSearchRepository roomFeatureSearchRepository) {
        this.roomFeatureRepository = roomFeatureRepository;
        this.roomFeatureMapper = roomFeatureMapper;
        this.roomFeatureSearchRepository = roomFeatureSearchRepository;
    }

    /**
     * Save a roomFeature.
     *
     * @param roomFeatureDTO the entity to save
     * @return the persisted entity
     */
    public RoomFeatureDTO save(RoomFeatureDTO roomFeatureDTO) {
        log.debug("Request to save RoomFeature : {}", roomFeatureDTO);
        RoomFeature roomFeature = roomFeatureMapper.toEntity(roomFeatureDTO);
        roomFeature = roomFeatureRepository.save(roomFeature);
        RoomFeatureDTO result = roomFeatureMapper.toDto(roomFeature);
        roomFeatureSearchRepository.save(roomFeature);
        return result;
    }

    /**
     * Get all the roomFeatures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomFeatureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomFeatures");
        return roomFeatureRepository.findAll(pageable)
            .map(roomFeatureMapper::toDto);
    }


    /**
     * Get one roomFeature by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomFeatureDTO> findOne(Long id) {
        log.debug("Request to get RoomFeature : {}", id);
        return roomFeatureRepository.findById(id)
            .map(roomFeatureMapper::toDto);
    }

    /**
     * Delete the roomFeature by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomFeature : {}", id);
        roomFeatureRepository.deleteById(id);
        roomFeatureSearchRepository.deleteById(id);
    }

    /**
     * Search for the roomFeature corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomFeatureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RoomFeatures for query {}", query);
        return roomFeatureSearchRepository.search(queryStringQuery(query), pageable)
            .map(roomFeatureMapper::toDto);
    }
}
