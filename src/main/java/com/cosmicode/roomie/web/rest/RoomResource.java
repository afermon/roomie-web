package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.service.RoomService;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.web.rest.errors.BadRequestAlertException;
import com.cosmicode.roomie.web.rest.util.HeaderUtil;
import com.cosmicode.roomie.web.rest.util.PaginationUtil;
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

/**
 * REST controller for managing Room.
 */
@RestController
@RequestMapping("/api")
public class RoomResource {

    private final Logger log = LoggerFactory.getLogger(RoomResource.class);

    private static final String ENTITY_NAME = "room";

    private final RoomService roomService;

    public RoomResource(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * POST  /rooms : Create a new room.
     *
     * @param roomDTO the roomDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomDTO, or with status 400 (Bad Request) if the room has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rooms")
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDTO) throws URISyntaxException {
        log.debug("REST request to save Room : {}", roomDTO);
        if (roomDTO.getId() != null) {
            throw new BadRequestAlertException("A new room cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomDTO result = roomService.save(roomDTO);
        return ResponseEntity.created(new URI("/api/rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rooms : Updates an existing room.
     *
     * @param roomDTO the roomDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomDTO,
     * or with status 400 (Bad Request) if the roomDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rooms")
    public ResponseEntity<RoomDTO> updateRoom(@Valid @RequestBody RoomDTO roomDTO) throws URISyntaxException {
        log.debug("REST request to update Room : {}", roomDTO);
        if (roomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomDTO result = roomService.save(roomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rooms : get all the rooms.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of rooms in body
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDTO>> getAllRooms(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Rooms");
        Page<RoomDTO> page;
        if (eagerload) {
            page = roomService.findAllWithEagerRelationships(pageable);
        } else {
            page = roomService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/rooms?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /rooms/:id : get the "id" room.
     *
     * @param id the id of the roomDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long id) {
        log.debug("REST request to get Room : {}", id);
        Optional<RoomDTO> roomDTO = roomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomDTO);
    }

    /**
     * DELETE  /rooms/:id : delete the "id" room.
     *
     * @param id the id of the roomDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.debug("REST request to delete Room : {}", id);
        roomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rooms?query=:query : search for the room corresponding
     * to the query.
     *
     * @param query the query of the room search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/rooms")
    public ResponseEntity<List<RoomDTO>> searchRooms(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Rooms for query {}", query);
        Page<RoomDTO> page = roomService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rooms");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * SEARCH  /_search/rooms?query=:query : search for the room corresponding
     * to the query.
     *
     * @param latitude the query of the room search
     * @param longitude the query of the room search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/rooms/geo")
    public ResponseEntity<List<RoomDTO>> searchRoomsLocation(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam int distance, Pageable pageable) {
        log.debug("REST request to search for a page of Rooms for query {} {} {} km", latitude, longitude, distance);
        Page<RoomDTO> page = roomService.search(latitude, longitude, distance, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(String.format("%s,%s", latitude, longitude), page, "/api/_search/rooms/geo");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
