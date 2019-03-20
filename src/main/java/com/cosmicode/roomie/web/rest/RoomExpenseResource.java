package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.service.RoomExpenseService;
import com.cosmicode.roomie.service.dto.RoomExpenseDTO;
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
 * REST controller for managing RoomExpense.
 */
@RestController
@RequestMapping("/api")
public class RoomExpenseResource {

    private final Logger log = LoggerFactory.getLogger(RoomExpenseResource.class);

    private static final String ENTITY_NAME = "roomExpense";

    private final RoomExpenseService roomExpenseService;

    public RoomExpenseResource(RoomExpenseService roomExpenseService) {
        this.roomExpenseService = roomExpenseService;
    }

    /**
     * POST  /room-expenses : Create a new roomExpense.
     *
     * @param roomExpenseDTO the roomExpenseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomExpenseDTO, or with status 400 (Bad Request) if the roomExpense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/room-expenses")
    public ResponseEntity<RoomExpenseDTO> createRoomExpense(@Valid @RequestBody RoomExpenseDTO roomExpenseDTO) throws URISyntaxException {
        log.debug("REST request to save RoomExpense : {}", roomExpenseDTO);
        if (roomExpenseDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomExpense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomExpenseDTO result = roomExpenseService.save(roomExpenseDTO);
        return ResponseEntity.created(new URI("/api/room-expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /room-expenses : Updates an existing roomExpense.
     *
     * @param roomExpenseDTO the roomExpenseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomExpenseDTO,
     * or with status 400 (Bad Request) if the roomExpenseDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomExpenseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/room-expenses")
    public ResponseEntity<RoomExpenseDTO> updateRoomExpense(@Valid @RequestBody RoomExpenseDTO roomExpenseDTO) throws URISyntaxException {
        log.debug("REST request to update RoomExpense : {}", roomExpenseDTO);
        if (roomExpenseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomExpenseDTO result = roomExpenseService.save(roomExpenseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomExpenseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /room-expenses : get all the roomExpenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roomExpenses in body
     */
    @GetMapping("/room-expenses")
    public ResponseEntity<List<RoomExpenseDTO>> getAllRoomExpenses(Pageable pageable) {
        log.debug("REST request to get a page of RoomExpenses");
        Page<RoomExpenseDTO> page = roomExpenseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/room-expenses");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /room-expenses/:id : get the "id" roomExpense.
     *
     * @param id the id of the roomExpenseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomExpenseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/room-expenses/{id}")
    public ResponseEntity<RoomExpenseDTO> getRoomExpense(@PathVariable Long id) {
        log.debug("REST request to get RoomExpense : {}", id);
        Optional<RoomExpenseDTO> roomExpenseDTO = roomExpenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomExpenseDTO);
    }

    /**
     * DELETE  /room-expenses/:id : delete the "id" roomExpense.
     *
     * @param id the id of the roomExpenseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/room-expenses/{id}")
    public ResponseEntity<Void> deleteRoomExpense(@PathVariable Long id) {
        log.debug("REST request to delete RoomExpense : {}", id);
        roomExpenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/room-expenses?query=:query : search for the roomExpense corresponding
     * to the query.
     *
     * @param query the query of the roomExpense search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/room-expenses")
    public ResponseEntity<List<RoomExpenseDTO>> searchRoomExpenses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoomExpenses for query {}", query);
        Page<RoomExpenseDTO> page = roomExpenseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/room-expenses");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
