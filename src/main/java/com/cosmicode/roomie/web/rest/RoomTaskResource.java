package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.service.RoomTaskService;
import com.cosmicode.roomie.service.dto.RoomTaskDTO;
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
 * REST controller for managing RoomTask.
 */
@RestController
@RequestMapping("/api")
public class RoomTaskResource {

    private final Logger log = LoggerFactory.getLogger(RoomTaskResource.class);

    private static final String ENTITY_NAME = "roomTask";

    private final RoomTaskService roomTaskService;

    public RoomTaskResource(RoomTaskService roomTaskService) {
        this.roomTaskService = roomTaskService;
    }

    /**
     * POST  /room-tasks : Create a new roomTask.
     *
     * @param roomTaskDTO the roomTaskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomTaskDTO, or with status 400 (Bad Request) if the roomTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/room-tasks")
    public ResponseEntity<RoomTaskDTO> createRoomTask(@Valid @RequestBody RoomTaskDTO roomTaskDTO) throws URISyntaxException {
        log.debug("REST request to save RoomTask : {}", roomTaskDTO);
        if (roomTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomTaskDTO result = roomTaskService.save(roomTaskDTO);
        return ResponseEntity.created(new URI("/api/room-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /room-tasks : Updates an existing roomTask.
     *
     * @param roomTaskDTO the roomTaskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomTaskDTO,
     * or with status 400 (Bad Request) if the roomTaskDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomTaskDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/room-tasks")
    public ResponseEntity<RoomTaskDTO> updateRoomTask(@Valid @RequestBody RoomTaskDTO roomTaskDTO) throws URISyntaxException {
        log.debug("REST request to update RoomTask : {}", roomTaskDTO);
        if (roomTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomTaskDTO result = roomTaskService.save(roomTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roomTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /room-tasks : get all the roomTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roomTasks in body
     */
    @GetMapping("/room-tasks")
    public ResponseEntity<List<RoomTaskDTO>> getAllRoomTasks(Pageable pageable) {
        log.debug("REST request to get a page of RoomTasks");
        Page<RoomTaskDTO> page = roomTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/room-tasks");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /room-tasks/:id : get the "id" roomTask.
     *
     * @param id the id of the roomTaskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomTaskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/room-tasks/{id}")
    public ResponseEntity<RoomTaskDTO> getRoomTask(@PathVariable Long id) {
        log.debug("REST request to get RoomTask : {}", id);
        Optional<RoomTaskDTO> roomTaskDTO = roomTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomTaskDTO);
    }

    /**
     * DELETE  /room-tasks/:id : delete the "id" roomTask.
     *
     * @param id the id of the roomTaskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/room-tasks/{id}")
    public ResponseEntity<Void> deleteRoomTask(@PathVariable Long id) {
        log.debug("REST request to delete RoomTask : {}", id);
        roomTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/room-tasks?query=:query : search for the roomTask corresponding
     * to the query.
     *
     * @param query the query of the roomTask search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/room-tasks")
    public ResponseEntity<List<RoomTaskDTO>> searchRoomTasks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RoomTasks for query {}", query);
        Page<RoomTaskDTO> page = roomTaskService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/room-tasks");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/tasksByRoom/{id}")
    public List<RoomTaskDTO> getRoomTasksByRoomId(@PathVariable Long id){
        return roomTaskService.findAllByRoom(id);
    }

}
