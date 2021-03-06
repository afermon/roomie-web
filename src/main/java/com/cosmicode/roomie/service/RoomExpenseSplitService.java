package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.RoomExpenseSplit;
import com.cosmicode.roomie.repository.RoomExpenseSplitRepository;
import com.cosmicode.roomie.service.dto.RoomExpenseSplitDTO;
import com.cosmicode.roomie.service.mapper.RoomExpenseSplitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing RoomExpenseSplit.
 */
@Service
@Transactional
public class RoomExpenseSplitService {

    private final Logger log = LoggerFactory.getLogger(RoomExpenseSplitService.class);

    private final RoomExpenseSplitRepository roomExpenseSplitRepository;

    private final RoomExpenseSplitMapper roomExpenseSplitMapper;


    public RoomExpenseSplitService(RoomExpenseSplitRepository roomExpenseSplitRepository, RoomExpenseSplitMapper roomExpenseSplitMapper) {
        this.roomExpenseSplitRepository = roomExpenseSplitRepository;
        this.roomExpenseSplitMapper = roomExpenseSplitMapper;
    }

    /**
     * Save a roomExpenseSplit.
     *
     * @param roomExpenseSplitDTO the entity to save
     * @return the persisted entity
     */
    public RoomExpenseSplitDTO save(RoomExpenseSplitDTO roomExpenseSplitDTO) {
        log.debug("Request to save RoomExpenseSplit : {}", roomExpenseSplitDTO);
        RoomExpenseSplit roomExpenseSplit = roomExpenseSplitMapper.toEntity(roomExpenseSplitDTO);
        roomExpenseSplit = roomExpenseSplitRepository.save(roomExpenseSplit);
        RoomExpenseSplitDTO result = roomExpenseSplitMapper.toDto(roomExpenseSplit);
        return result;
    }

    /**
     * Get all the roomExpenseSplits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomExpenseSplitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomExpenseSplits");
        return roomExpenseSplitRepository.findAll(pageable)
            .map(roomExpenseSplitMapper::toDto);
    }


    /**
     * Get one roomExpenseSplit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomExpenseSplitDTO> findOne(Long id) {
        log.debug("Request to get RoomExpenseSplit : {}", id);
        return roomExpenseSplitRepository.findById(id)
            .map(roomExpenseSplitMapper::toDto);
    }

    /**
     * Delete the roomExpenseSplit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomExpenseSplit : {}", id);
        roomExpenseSplitRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RoomExpenseSplitDTO> findAllByExpense (Long id) {
        return roomExpenseSplitMapper.toDto(roomExpenseSplitRepository.findAllByExpenseId(id));
    }
}
