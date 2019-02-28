package com.cosmicode.roomie.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cosmicode.roomie.service.RoomEventService;
import com.cosmicode.roomie.web.rest.errors.BadRequestAlertException;
import com.cosmicode.roomie.web.rest.util.HeaderUtil;
import com.cosmicode.roomie.web.rest.util.PaginationUtil;
import com.cosmicode.roomie.service.dto.RoomEventDTO;
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
 * REST controller for managing RoomEvent.
 */
@RestController
@RequestMapping("/api")
public class RoomEventResource {

    private final Logger log = LoggerFactory.getLogger(RoomEventResource.class);

    private static final String ENTITY_NAME = "roomEvent";

    private final RoomEventService roomEventService;

    public RoomEventResource(RoomEventService roomEventService) {
        this.roomEventService = roomEventService;
    }

    /**
     * POST  /room-events : Create a new roomEvent.
     *
     * @param roomEventDTO the roomEventDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomEventDTO, or with status 400 (Bad Request) if the roomEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/room-events")
    @Timed
    public ResponseEntity<RoomEventDTO> createRoomEvent(@Valid @RequestBody RoomEventDTO roomEventDTO) throws URISyntaxException {
        log.debug("REST request to save RoomEvent : {}", roomEventDTO);
        if (roomEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomEventDTO result = roomEventService.save(roomEventDTO);
        return ResponseEntity.created(new URI("/api/room-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /room-events : Updates an existing roomEvent.
     *
     * @param roomEventDTO the roomEventDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomEventDTO,
     * or with status 400 (Bad Request) if the roomEventDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomEventDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/room-events")
    @Timed
    public ResponseEntity<RoomEventDTO> updateRoomEvent(@Valid @RequestBody RoomEventDTO roomEventDTO) throws URISyntaxException {
        log.debug("REST request to update RoomEvent : {}", roomEventDTO);
        if (roomEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomEventDTO result = roomEventService.save(roomEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /room-events : get all the roomEvents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roomEvents in body
     */
    @GetMapping("/room-events")
    @Timed
    public ResponseEntity<List<RoomEventDTO>> getAllRoomEvents(Pageable pageable) {
        log.debug("REST request to get a page of RoomEvents");
        Page<RoomEventDTO> page = roomEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/room-events");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /room-events/:id : get the "id" roomEvent.
     *
     * @param id the id of the roomEventDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomEventDTO, or with status 404 (Not Found)
     */
    @GetMapping("/room-events/{id}")
    @Timed
    public ResponseEntity<RoomEventDTO> getRoomEvent(@PathVariable Long id) {
        log.debug("REST request to get RoomEvent : {}", id);
        Optional<RoomEventDTO> roomEventDTO = roomEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomEventDTO);
    }

    /**
     * DELETE  /room-events/:id : delete the "id" roomEvent.
     *
     * @param id the id of the roomEventDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/room-events/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoomEvent(@PathVariable Long id) {
        log.debug("REST request to delete RoomEvent : {}", id);
        roomEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/room-events?query=:query : search for the roomEvent corresponding
     * to the query.
     *
     * @param query the query of the roomEvent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/room-events")
    @Timed
    public ResponseEntity<List<RoomEventDTO>> searchRoomEvents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoomEvents for query {}", query);
        Page<RoomEventDTO> page = roomEventService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/room-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
