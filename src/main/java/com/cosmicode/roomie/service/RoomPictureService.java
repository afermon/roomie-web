package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomPicture;
import com.cosmicode.roomie.repository.RoomPictureRepository;
import com.cosmicode.roomie.repository.search.RoomPictureSearchRepository;
import com.cosmicode.roomie.service.dto.RoomPictureDTO;
import com.cosmicode.roomie.service.mapper.RoomPictureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing RoomPicture.
 */
@Service
@Transactional
public class RoomPictureService {

    private final Logger log = LoggerFactory.getLogger(RoomPictureService.class);

    private final RoomPictureRepository roomPictureRepository;

    private final RoomPictureMapper roomPictureMapper;

    private final RoomPictureSearchRepository roomPictureSearchRepository;

    public RoomPictureService(RoomPictureRepository roomPictureRepository, RoomPictureMapper roomPictureMapper, RoomPictureSearchRepository roomPictureSearchRepository) {
        this.roomPictureRepository = roomPictureRepository;
        this.roomPictureMapper = roomPictureMapper;
        this.roomPictureSearchRepository = roomPictureSearchRepository;
    }

    /**
     * Save a roomPicture.
     *
     * @param roomPictureDTO the entity to save
     * @return the persisted entity
     */
    public RoomPictureDTO save(RoomPictureDTO roomPictureDTO) {
        log.debug("Request to save RoomPicture : {}", roomPictureDTO);
        RoomPicture roomPicture = roomPictureMapper.toEntity(roomPictureDTO);
        roomPicture = roomPictureRepository.save(roomPicture);
        RoomPictureDTO result = roomPictureMapper.toDto(roomPicture);
        roomPictureSearchRepository.save(roomPicture);
        return result;
    }

    /**
     * Get all the roomPictures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomPictureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomPictures");
        return roomPictureRepository.findAll(pageable)
            .map(roomPictureMapper::toDto);
    }


    /**
     * Get one roomPicture by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomPictureDTO> findOne(Long id) {
        log.debug("Request to get RoomPicture : {}", id);
        return roomPictureRepository.findById(id)
            .map(roomPictureMapper::toDto);
    }

    /**
     * Delete the roomPicture by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomPicture : {}", id);
        roomPictureRepository.deleteById(id);
        roomPictureSearchRepository.deleteById(id);
    }

    /**
     * Search for the roomPicture corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomPictureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RoomPictures for query {}", query);
        return roomPictureSearchRepository.search(queryStringQuery(query), pageable)
            .map(roomPictureMapper::toDto);
    }
}
