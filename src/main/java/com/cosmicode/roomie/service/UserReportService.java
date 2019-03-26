package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.UserReport;
import com.cosmicode.roomie.repository.UserReportRepository;
import com.cosmicode.roomie.service.dto.UserReportDTO;
import com.cosmicode.roomie.service.mapper.UserReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing UserReport.
 */
@Service
@Transactional
public class UserReportService {

    private final Logger log = LoggerFactory.getLogger(UserReportService.class);

    private final UserReportRepository userReportRepository;

    private final UserReportMapper userReportMapper;

    public UserReportService(UserReportRepository userReportRepository, UserReportMapper userReportMapper) {
        this.userReportRepository = userReportRepository;
        this.userReportMapper = userReportMapper;
    }

    /**
     * Save a userReport.
     *
     * @param userReportDTO the entity to save
     * @return the persisted entity
     */
    public UserReportDTO save(UserReportDTO userReportDTO) {
        log.debug("Request to save UserReport : {}", userReportDTO);
        UserReport userReport = userReportMapper.toEntity(userReportDTO);
        userReport = userReportRepository.save(userReport);
        UserReportDTO result = userReportMapper.toDto(userReport);
        return result;
    }

    /**
     * Get all the userReports.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserReports");
        return userReportRepository.findAll(pageable)
            .map(userReportMapper::toDto);
    }


    /**
     * Get one userReport by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserReportDTO> findOne(Long id) {
        log.debug("Request to get UserReport : {}", id);
        return userReportRepository.findById(id)
            .map(userReportMapper::toDto);
    }

    /**
     * Delete the userReport by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserReport : {}", id);
        userReportRepository.deleteById(id);
    }

}
