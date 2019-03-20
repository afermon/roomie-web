package com.cosmicode.roomie.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cosmicode.roomie.service.RoomieService;
import com.cosmicode.roomie.web.rest.errors.BadRequestAlertException;
import com.cosmicode.roomie.web.rest.util.HeaderUtil;
import com.cosmicode.roomie.web.rest.util.PaginationUtil;
import com.cosmicode.roomie.service.dto.RoomieDTO;
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
 * REST controller for managing Roomie.
 */
@RestController
@RequestMapping("/api")
public class RoomieResource {

    private final Logger log = LoggerFactory.getLogger(RoomieResource.class);

    private static final String ENTITY_NAME = "roomie";

    private final RoomieService roomieService;

    public RoomieResource(RoomieService roomieService) {
        this.roomieService = roomieService;
    }

    /**
     * POST  /roomies : Create a new roomie.
     *
     * @param roomieDTO the roomieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomieDTO, or with status 400 (Bad Request) if the roomie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roomies")
    @Timed
    public ResponseEntity<RoomieDTO> createRoomie(@Valid @RequestBody RoomieDTO roomieDTO) throws URISyntaxException {
        log.debug("REST request to save Roomie : {}", roomieDTO);
        if (roomieDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomieDTO result = roomieService.save(roomieDTO);
        return ResponseEntity.created(new URI("/api/roomies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roomies : Updates an existing roomie.
     *
     * @param roomieDTO the roomieDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomieDTO,
     * or with status 400 (Bad Request) if the roomieDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomieDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roomies")
    @Timed
    public ResponseEntity<RoomieDTO> updateRoomie(@Valid @RequestBody RoomieDTO roomieDTO) throws URISyntaxException {
        log.debug("REST request to update Roomie : {}", roomieDTO);
        if (roomieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomieDTO result = roomieService.save(roomieDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomieDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roomies : get all the roomies.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of roomies in body
     */
    @GetMapping("/roomies")
    @Timed
    public ResponseEntity<List<RoomieDTO>> getAllRoomies(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Roomies");
        Page<RoomieDTO> page;
        if (eagerload) {
            page = roomieService.findAllWithEagerRelationships(pageable);
        } else {
            page = roomieService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/roomies?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /roomies/:id : get the "id" roomie.
     *
     * @param id the id of the roomieDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomieDTO, or with status 404 (Not Found)
     */
    @GetMapping("/roomies/{id}")
    @Timed
    public ResponseEntity<RoomieDTO> getRoomie(@PathVariable Long id) {
        log.debug("REST request to get Roomie : {}", id);
        Optional<RoomieDTO> roomieDTO = roomieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomieDTO);
    }

    /**
     * DELETE  /roomies/:id : delete the "id" roomie.
     *
     * @param id the id of the roomieDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roomies/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoomie(@PathVariable Long id) {
        log.debug("REST request to delete Roomie : {}", id);
        roomieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/roomies?query=:query : search for the roomie corresponding
     * to the query.
     *
     * @param query the query of the roomie search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/roomies")
    @Timed
    public ResponseEntity<List<RoomieDTO>> searchRoomies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Roomies for query {}", query);
        Page<RoomieDTO> page = roomieService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/roomies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
