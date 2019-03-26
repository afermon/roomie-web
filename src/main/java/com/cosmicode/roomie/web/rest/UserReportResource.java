package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.service.UserReportService;
import com.cosmicode.roomie.service.dto.UserReportDTO;
import com.cosmicode.roomie.web.rest.errors.BadRequestAlertException;
import com.cosmicode.roomie.web.rest.util.HeaderUtil;
import com.cosmicode.roomie.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserReport.
 */
@RestController
@RequestMapping("/api")
public class UserReportResource {

    private final Logger log = LoggerFactory.getLogger(UserReportResource.class);

    private static final String ENTITY_NAME = "userReport";

    private final UserReportService userReportService;

    public UserReportResource(UserReportService userReportService) {
        this.userReportService = userReportService;
    }

    /**
     * POST  /user-reports : Create a new userReport.
     *
     * @param userReportDTO the userReportDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userReportDTO, or with status 400 (Bad Request) if the userReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-reports")
    public ResponseEntity<UserReportDTO> createUserReport(@Valid @RequestBody UserReportDTO userReportDTO) throws URISyntaxException {
        log.debug("REST request to save UserReport : {}", userReportDTO);
        if (userReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new userReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserReportDTO result = userReportService.save(userReportDTO);
        return ResponseEntity.created(new URI("/api/user-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-reports : Updates an existing userReport.
     *
     * @param userReportDTO the userReportDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userReportDTO,
     * or with status 400 (Bad Request) if the userReportDTO is not valid,
     * or with status 500 (Internal Server Error) if the userReportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-reports")
    public ResponseEntity<UserReportDTO> updateUserReport(@Valid @RequestBody UserReportDTO userReportDTO) throws URISyntaxException {
        log.debug("REST request to update UserReport : {}", userReportDTO);
        if (userReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserReportDTO result = userReportService.save(userReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-reports : get all the userReports.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userReports in body
     */
    @GetMapping("/user-reports")
    public ResponseEntity<List<UserReportDTO>> getAllUserReports(Pageable pageable) {
        log.debug("REST request to get a page of UserReports");
        Page<UserReportDTO> page = userReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-reports");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /user-reports/:id : get the "id" userReport.
     *
     * @param id the id of the userReportDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userReportDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-reports/{id}")
    public ResponseEntity<UserReportDTO> getUserReport(@PathVariable Long id) {
        log.debug("REST request to get UserReport : {}", id);
        Optional<UserReportDTO> userReportDTO = userReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userReportDTO);
    }

    /**
     * DELETE  /user-reports/:id : delete the "id" userReport.
     *
     * @param id the id of the userReportDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-reports/{id}")
    public ResponseEntity<Void> deleteUserReport(@PathVariable Long id) {
        log.debug("REST request to delete UserReport : {}", id);
        userReportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
