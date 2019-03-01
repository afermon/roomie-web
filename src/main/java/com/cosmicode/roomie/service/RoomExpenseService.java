package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomExpense;
import com.cosmicode.roomie.repository.RoomExpenseRepository;
import com.cosmicode.roomie.repository.search.RoomExpenseSearchRepository;
import com.cosmicode.roomie.service.dto.RoomExpenseDTO;
import com.cosmicode.roomie.service.mapper.RoomExpenseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RoomExpense.
 */
@Service
@Transactional
public class RoomExpenseService {

    private final Logger log = LoggerFactory.getLogger(RoomExpenseService.class);

    private final RoomExpenseRepository roomExpenseRepository;

    private final RoomExpenseMapper roomExpenseMapper;

    private final RoomExpenseSearchRepository roomExpenseSearchRepository;

    public RoomExpenseService(RoomExpenseRepository roomExpenseRepository, RoomExpenseMapper roomExpenseMapper, RoomExpenseSearchRepository roomExpenseSearchRepository) {
        this.roomExpenseRepository = roomExpenseRepository;
        this.roomExpenseMapper = roomExpenseMapper;
        this.roomExpenseSearchRepository = roomExpenseSearchRepository;
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
        roomExpenseSearchRepository.save(roomExpense);
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
        roomExpenseSearchRepository.deleteById(id);
    }

    /**
     * Search for the roomExpense corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomExpenseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RoomExpenses for query {}", query);
        return roomExpenseSearchRepository.search(queryStringQuery(query), pageable)
            .map(roomExpenseMapper::toDto);
    }
}
