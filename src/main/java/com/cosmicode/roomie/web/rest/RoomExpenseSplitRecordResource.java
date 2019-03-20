package com.cosmicode.roomie.web.rest;
import com.cosmicode.roomie.service.RoomExpenseSplitRecordService;
import com.cosmicode.roomie.web.rest.errors.BadRequestAlertException;
import com.cosmicode.roomie.web.rest.util.HeaderUtil;
import com.cosmicode.roomie.web.rest.util.PaginationUtil;
import com.cosmicode.roomie.service.dto.RoomExpenseSplitRecordDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing RoomExpenseSplitRecord.
 */
@RestController
@RequestMapping("/api")
public class RoomExpenseSplitRecordResource {

    private final Logger log = LoggerFactory.getLogger(RoomExpenseSplitRecordResource.class);

    private static final String ENTITY_NAME = "roomExpenseSplitRecord";

    private final RoomExpenseSplitRecordService roomExpenseSplitRecordService;

    public RoomExpenseSplitRecordResource(RoomExpenseSplitRecordService roomExpenseSplitRecordService) {
        this.roomExpenseSplitRecordService = roomExpenseSplitRecordService;
    }

    /**
     * POST  /room-expense-split-records : Create a new roomExpenseSplitRecord.
     *
     * @param roomExpenseSplitRecordDTO the roomExpenseSplitRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomExpenseSplitRecordDTO, or with status 400 (Bad Request) if the roomExpenseSplitRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/room-expense-split-records")
    public ResponseEntity<RoomExpenseSplitRecordDTO> createRoomExpenseSplitRecord(@Valid @RequestBody RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO) throws URISyntaxException {
        log.debug("REST request to save RoomExpenseSplitRecord : {}", roomExpenseSplitRecordDTO);
        if (roomExpenseSplitRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomExpenseSplitRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomExpenseSplitRecordDTO result = roomExpenseSplitRecordService.save(roomExpenseSplitRecordDTO);
        return ResponseEntity.created(new URI("/api/room-expense-split-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /room-expense-split-records : Updates an existing roomExpenseSplitRecord.
     *
     * @param roomExpenseSplitRecordDTO the roomExpenseSplitRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomExpenseSplitRecordDTO,
     * or with status 400 (Bad Request) if the roomExpenseSplitRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomExpenseSplitRecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/room-expense-split-records")
    public ResponseEntity<RoomExpenseSplitRecordDTO> updateRoomExpenseSplitRecord(@Valid @RequestBody RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO) throws URISyntaxException {
        log.debug("REST request to update RoomExpenseSplitRecord : {}", roomExpenseSplitRecordDTO);
        if (roomExpenseSplitRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomExpenseSplitRecordDTO result = roomExpenseSplitRecordService.save(roomExpenseSplitRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomExpenseSplitRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /room-expense-split-records : get all the roomExpenseSplitRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roomExpenseSplitRecords in body
     */
    @GetMapping("/room-expense-split-records")
    public ResponseEntity<List<RoomExpenseSplitRecordDTO>> getAllRoomExpenseSplitRecords(Pageable pageable) {
        log.debug("REST request to get a page of RoomExpenseSplitRecords");
        Page<RoomExpenseSplitRecordDTO> page = roomExpenseSplitRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/room-expense-split-records");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /room-expense-split-records/:id : get the "id" roomExpenseSplitRecord.
     *
     * @param id the id of the roomExpenseSplitRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomExpenseSplitRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/room-expense-split-records/{id}")
    public ResponseEntity<RoomExpenseSplitRecordDTO> getRoomExpenseSplitRecord(@PathVariable Long id) {
        log.debug("REST request to get RoomExpenseSplitRecord : {}", id);
        Optional<RoomExpenseSplitRecordDTO> roomExpenseSplitRecordDTO = roomExpenseSplitRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomExpenseSplitRecordDTO);
    }

    /**
     * DELETE  /room-expense-split-records/:id : delete the "id" roomExpenseSplitRecord.
     *
     * @param id the id of the roomExpenseSplitRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/room-expense-split-records/{id}")
    public ResponseEntity<Void> deleteRoomExpenseSplitRecord(@PathVariable Long id) {
        log.debug("REST request to delete RoomExpenseSplitRecord : {}", id);
        roomExpenseSplitRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/room-expense-split-records?query=:query : search for the roomExpenseSplitRecord corresponding
     * to the query.
     *
     * @param query the query of the roomExpenseSplitRecord search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/room-expense-split-records")
    public ResponseEntity<List<RoomExpenseSplitRecordDTO>> searchRoomExpenseSplitRecords(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoomExpenseSplitRecords for query {}", query);
        Page<RoomExpenseSplitRecordDTO> page = roomExpenseSplitRecordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/room-expense-split-records");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
