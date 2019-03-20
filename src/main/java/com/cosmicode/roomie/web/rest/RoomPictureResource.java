package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.service.RoomPictureService;
import com.cosmicode.roomie.service.dto.RoomPictureDTO;
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
 * REST controller for managing RoomPicture.
 */
@RestController
@RequestMapping("/api")
public class RoomPictureResource {

    private final Logger log = LoggerFactory.getLogger(RoomPictureResource.class);

    private static final String ENTITY_NAME = "roomPicture";

    private final RoomPictureService roomPictureService;

    public RoomPictureResource(RoomPictureService roomPictureService) {
        this.roomPictureService = roomPictureService;
    }

    /**
     * POST  /room-pictures : Create a new roomPicture.
     *
     * @param roomPictureDTO the roomPictureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomPictureDTO, or with status 400 (Bad Request) if the roomPicture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/room-pictures")
    public ResponseEntity<RoomPictureDTO> createRoomPicture(@Valid @RequestBody RoomPictureDTO roomPictureDTO) throws URISyntaxException {
        log.debug("REST request to save RoomPicture : {}", roomPictureDTO);
        if (roomPictureDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomPicture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomPictureDTO result = roomPictureService.save(roomPictureDTO);
        return ResponseEntity.created(new URI("/api/room-pictures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /room-pictures : Updates an existing roomPicture.
     *
     * @param roomPictureDTO the roomPictureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomPictureDTO,
     * or with status 400 (Bad Request) if the roomPictureDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomPictureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/room-pictures")
    public ResponseEntity<RoomPictureDTO> updateRoomPicture(@Valid @RequestBody RoomPictureDTO roomPictureDTO) throws URISyntaxException {
        log.debug("REST request to update RoomPicture : {}", roomPictureDTO);
        if (roomPictureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomPictureDTO result = roomPictureService.save(roomPictureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomPictureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /room-pictures : get all the roomPictures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roomPictures in body
     */
    @GetMapping("/room-pictures")
    public ResponseEntity<List<RoomPictureDTO>> getAllRoomPictures(Pageable pageable) {
        log.debug("REST request to get a page of RoomPictures");
        Page<RoomPictureDTO> page = roomPictureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/room-pictures");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /room-pictures/:id : get the "id" roomPicture.
     *
     * @param id the id of the roomPictureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomPictureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/room-pictures/{id}")
    public ResponseEntity<RoomPictureDTO> getRoomPicture(@PathVariable Long id) {
        log.debug("REST request to get RoomPicture : {}", id);
        Optional<RoomPictureDTO> roomPictureDTO = roomPictureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomPictureDTO);
    }

    /**
     * DELETE  /room-pictures/:id : delete the "id" roomPicture.
     *
     * @param id the id of the roomPictureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/room-pictures/{id}")
    public ResponseEntity<Void> deleteRoomPicture(@PathVariable Long id) {
        log.debug("REST request to delete RoomPicture : {}", id);
        roomPictureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/room-pictures?query=:query : search for the roomPicture corresponding
     * to the query.
     *
     * @param query the query of the roomPicture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/room-pictures")
    public ResponseEntity<List<RoomPictureDTO>> searchRoomPictures(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoomPictures for query {}", query);
        Page<RoomPictureDTO> page = roomPictureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/room-pictures");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
