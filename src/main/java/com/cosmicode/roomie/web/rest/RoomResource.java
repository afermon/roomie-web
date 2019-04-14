package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.service.RoomService;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.service.dto.SearchFilterDTO;
import com.cosmicode.roomie.web.rest.errors.BadRequestAlertException;
import com.cosmicode.roomie.web.rest.util.HeaderUtil;
import com.cosmicode.roomie.web.rest.util.PaginationUtil;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * SEARCH  /_search/rooms/advanced : search for the room corresponding
     * to the query.
     *
     * @param searchFilterDTO the query of the room search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @PostMapping("/_search/rooms/advanced")
    public ResponseEntity<List<RoomDTO>> searchRoomsAdvanced(@RequestBody SearchFilterDTO searchFilterDTO, Pageable pageable) {
        log.debug("REST request to search for a page of Rooms for: {}", searchFilterDTO.toString());
        Page<RoomDTO> page = roomService.search(searchFilterDTO, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(searchFilterDTO.toString(), page, "/api/_search/rooms/advanced");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/owned-rooms/{id}")
    public List<RoomDTO> getRoomsByOwner(@PathVariable Long id){
        return roomService.findAllByOwner(id);
    }

    @PostMapping("/pay-room/{token}")
    public ResponseEntity<RoomDTO> payPremium(@RequestBody RoomDTO roomDTO, @PathVariable String token) throws StripeException, URISyntaxException{
        Stripe.apiKey = "pk_test_tvOqreoDBMCR33zFGuIpqwHM00njthUCtW";
        Map<String, Object> params = new HashMap<>();
        params.put("amount", 1.99);
        params.put("currency", "usd");
        params.put("description", "Premium room");
        params.put("source", token);
        try {
            Charge charge = Charge.create(params);
            RoomDTO result = roomService.save(roomDTO);
            return ResponseEntity.created(new URI("/pay-room/{token}/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        }catch (StripeException e){
            return ResponseEntity.badRequest()
                .header(e.toString())
                .body(null);
        }

    }

}
