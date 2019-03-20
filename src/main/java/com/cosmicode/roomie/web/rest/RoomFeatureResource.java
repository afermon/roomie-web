package com.cosmicode.roomie.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cosmicode.roomie.service.RoomFeatureService;
import com.cosmicode.roomie.web.rest.errors.BadRequestAlertException;
import com.cosmicode.roomie.web.rest.util.HeaderUtil;
import com.cosmicode.roomie.web.rest.util.PaginationUtil;
import com.cosmicode.roomie.service.dto.RoomFeatureDTO;
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
 * REST controller for managing RoomFeature.
 */
@RestController
@RequestMapping("/api")
public class RoomFeatureResource {

    private final Logger log = LoggerFactory.getLogger(RoomFeatureResource.class);

    private static final String ENTITY_NAME = "roomFeature";

    private final RoomFeatureService roomFeatureService;

    public RoomFeatureResource(RoomFeatureService roomFeatureService) {
        this.roomFeatureService = roomFeatureService;
    }

    /**
     * POST  /room-features : Create a new roomFeature.
     *
     * @param roomFeatureDTO the roomFeatureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomFeatureDTO, or with status 400 (Bad Request) if the roomFeature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/room-features")
    @Timed
    public ResponseEntity<RoomFeatureDTO> createRoomFeature(@Valid @RequestBody RoomFeatureDTO roomFeatureDTO) throws URISyntaxException {
        log.debug("REST request to save RoomFeature : {}", roomFeatureDTO);
        if (roomFeatureDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomFeature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomFeatureDTO result = roomFeatureService.save(roomFeatureDTO);
        return ResponseEntity.created(new URI("/api/room-features/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /room-features : Updates an existing roomFeature.
     *
     * @param roomFeatureDTO the roomFeatureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomFeatureDTO,
     * or with status 400 (Bad Request) if the roomFeatureDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomFeatureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/room-features")
    @Timed
    public ResponseEntity<RoomFeatureDTO> updateRoomFeature(@Valid @RequestBody RoomFeatureDTO roomFeatureDTO) throws URISyntaxException {
        log.debug("REST request to update RoomFeature : {}", roomFeatureDTO);
        if (roomFeatureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomFeatureDTO result = roomFeatureService.save(roomFeatureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomFeatureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /room-features : get all the roomFeatures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roomFeatures in body
     */
    @GetMapping("/room-features")
    @Timed
    public ResponseEntity<List<RoomFeatureDTO>> getAllRoomFeatures(Pageable pageable) {
        log.debug("REST request to get a page of RoomFeatures");
        Page<RoomFeatureDTO> page = roomFeatureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/room-features");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /room-features/:id : get the "id" roomFeature.
     *
     * @param id the id of the roomFeatureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomFeatureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/room-features/{id}")
    @Timed
    public ResponseEntity<RoomFeatureDTO> getRoomFeature(@PathVariable Long id) {
        log.debug("REST request to get RoomFeature : {}", id);
        Optional<RoomFeatureDTO> roomFeatureDTO = roomFeatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomFeatureDTO);
    }

    /**
     * DELETE  /room-features/:id : delete the "id" roomFeature.
     *
     * @param id the id of the roomFeatureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/room-features/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoomFeature(@PathVariable Long id) {
        log.debug("REST request to delete RoomFeature : {}", id);
        roomFeatureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/room-features?query=:query : search for the roomFeature corresponding
     * to the query.
     *
     * @param query the query of the roomFeature search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/room-features")
    @Timed
    public ResponseEntity<List<RoomFeatureDTO>> searchRoomFeatures(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoomFeatures for query {}", query);
        Page<RoomFeatureDTO> page = roomFeatureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/room-features");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
