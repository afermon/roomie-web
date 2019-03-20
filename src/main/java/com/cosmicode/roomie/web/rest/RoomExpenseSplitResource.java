package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.service.RoomExpenseSplitService;
import com.cosmicode.roomie.service.dto.RoomExpenseSplitDTO;
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
 * REST controller for managing RoomExpenseSplit.
 */
@RestController
@RequestMapping("/api")
public class RoomExpenseSplitResource {

    private final Logger log = LoggerFactory.getLogger(RoomExpenseSplitResource.class);

    private static final String ENTITY_NAME = "roomExpenseSplit";

    private final RoomExpenseSplitService roomExpenseSplitService;

    public RoomExpenseSplitResource(RoomExpenseSplitService roomExpenseSplitService) {
        this.roomExpenseSplitService = roomExpenseSplitService;
    }

    /**
     * POST  /room-expense-splits : Create a new roomExpenseSplit.
     *
     * @param roomExpenseSplitDTO the roomExpenseSplitDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomExpenseSplitDTO, or with status 400 (Bad Request) if the roomExpenseSplit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/room-expense-splits")
    public ResponseEntity<RoomExpenseSplitDTO> createRoomExpenseSplit(@Valid @RequestBody RoomExpenseSplitDTO roomExpenseSplitDTO) throws URISyntaxException {
        log.debug("REST request to save RoomExpenseSplit : {}", roomExpenseSplitDTO);
        if (roomExpenseSplitDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomExpenseSplit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomExpenseSplitDTO result = roomExpenseSplitService.save(roomExpenseSplitDTO);
        return ResponseEntity.created(new URI("/api/room-expense-splits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /room-expense-splits : Updates an existing roomExpenseSplit.
     *
     * @param roomExpenseSplitDTO the roomExpenseSplitDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomExpenseSplitDTO,
     * or with status 400 (Bad Request) if the roomExpenseSplitDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomExpenseSplitDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/room-expense-splits")
    public ResponseEntity<RoomExpenseSplitDTO> updateRoomExpenseSplit(@Valid @RequestBody RoomExpenseSplitDTO roomExpenseSplitDTO) throws URISyntaxException {
        log.debug("REST request to update RoomExpenseSplit : {}", roomExpenseSplitDTO);
        if (roomExpenseSplitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomExpenseSplitDTO result = roomExpenseSplitService.save(roomExpenseSplitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomExpenseSplitDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /room-expense-splits : get all the roomExpenseSplits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roomExpenseSplits in body
     */
    @GetMapping("/room-expense-splits")
    public ResponseEntity<List<RoomExpenseSplitDTO>> getAllRoomExpenseSplits(Pageable pageable) {
        log.debug("REST request to get a page of RoomExpenseSplits");
        Page<RoomExpenseSplitDTO> page = roomExpenseSplitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/room-expense-splits");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /room-expense-splits/:id : get the "id" roomExpenseSplit.
     *
     * @param id the id of the roomExpenseSplitDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomExpenseSplitDTO, or with status 404 (Not Found)
     */
    @GetMapping("/room-expense-splits/{id}")
    public ResponseEntity<RoomExpenseSplitDTO> getRoomExpenseSplit(@PathVariable Long id) {
        log.debug("REST request to get RoomExpenseSplit : {}", id);
        Optional<RoomExpenseSplitDTO> roomExpenseSplitDTO = roomExpenseSplitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomExpenseSplitDTO);
    }

    /**
     * DELETE  /room-expense-splits/:id : delete the "id" roomExpenseSplit.
     *
     * @param id the id of the roomExpenseSplitDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/room-expense-splits/{id}")
    public ResponseEntity<Void> deleteRoomExpenseSplit(@PathVariable Long id) {
        log.debug("REST request to delete RoomExpenseSplit : {}", id);
        roomExpenseSplitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/room-expense-splits?query=:query : search for the roomExpenseSplit corresponding
     * to the query.
     *
     * @param query the query of the roomExpenseSplit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/room-expense-splits")
    public ResponseEntity<List<RoomExpenseSplitDTO>> searchRoomExpenseSplits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoomExpenseSplits for query {}", query);
        Page<RoomExpenseSplitDTO> page = roomExpenseSplitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/room-expense-splits");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
