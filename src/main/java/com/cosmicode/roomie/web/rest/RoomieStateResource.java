package com.cosmicode.roomie.web.rest;
import com.cosmicode.roomie.service.RoomieStateService;
import com.cosmicode.roomie.web.rest.errors.BadRequestAlertException;
import com.cosmicode.roomie.web.rest.util.HeaderUtil;
import com.cosmicode.roomie.web.rest.util.PaginationUtil;
import com.cosmicode.roomie.service.dto.RoomieStateDTO;
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
 * REST controller for managing RoomieState.
 */
@RestController
@RequestMapping("/api")
public class RoomieStateResource {

    private final Logger log = LoggerFactory.getLogger(RoomieStateResource.class);

    private static final String ENTITY_NAME = "roomieState";

    private final RoomieStateService roomieStateService;

    public RoomieStateResource(RoomieStateService roomieStateService) {
        this.roomieStateService = roomieStateService;
    }

    /**
     * POST  /roomie-states : Create a new roomieState.
     *
     * @param roomieStateDTO the roomieStateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomieStateDTO, or with status 400 (Bad Request) if the roomieState has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roomie-states")
    public ResponseEntity<RoomieStateDTO> createRoomieState(@Valid @RequestBody RoomieStateDTO roomieStateDTO) throws URISyntaxException {
        log.debug("REST request to save RoomieState : {}", roomieStateDTO);
        if (roomieStateDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomieState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomieStateDTO result = roomieStateService.save(roomieStateDTO);
        return ResponseEntity.created(new URI("/api/roomie-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roomie-states : Updates an existing roomieState.
     *
     * @param roomieStateDTO the roomieStateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomieStateDTO,
     * or with status 400 (Bad Request) if the roomieStateDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomieStateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roomie-states")
    public ResponseEntity<RoomieStateDTO> updateRoomieState(@Valid @RequestBody RoomieStateDTO roomieStateDTO) throws URISyntaxException {
        log.debug("REST request to update RoomieState : {}", roomieStateDTO);
        if (roomieStateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomieStateDTO result = roomieStateService.save(roomieStateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomieStateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roomie-states : get all the roomieStates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roomieStates in body
     */
    @GetMapping("/roomie-states")
    public ResponseEntity<List<RoomieStateDTO>> getAllRoomieStates(Pageable pageable) {
        log.debug("REST request to get a page of RoomieStates");
        Page<RoomieStateDTO> page = roomieStateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roomie-states");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /roomie-states/:id : get the "id" roomieState.
     *
     * @param id the id of the roomieStateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomieStateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/roomie-states/{id}")
    public ResponseEntity<RoomieStateDTO> getRoomieState(@PathVariable Long id) {
        log.debug("REST request to get RoomieState : {}", id);
        Optional<RoomieStateDTO> roomieStateDTO = roomieStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomieStateDTO);
    }

    /**
     * DELETE  /roomie-states/:id : delete the "id" roomieState.
     *
     * @param id the id of the roomieStateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roomie-states/{id}")
    public ResponseEntity<Void> deleteRoomieState(@PathVariable Long id) {
        log.debug("REST request to delete RoomieState : {}", id);
        roomieStateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/roomie-states?query=:query : search for the roomieState corresponding
     * to the query.
     *
     * @param query the query of the roomieState search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/roomie-states")
    public ResponseEntity<List<RoomieStateDTO>> searchRoomieStates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoomieStates for query {}", query);
        Page<RoomieStateDTO> page = roomieStateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/roomie-states");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
