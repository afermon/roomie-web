package com.cosmicode.roomie.service;

import com.cosmicode.roomie.domain.Room;
import com.cosmicode.roomie.domain.enumeration.CurrencyType;
import com.cosmicode.roomie.domain.enumeration.RoomState;
import com.cosmicode.roomie.repository.RoomRepository;
import com.cosmicode.roomie.repository.search.RoomSearchRepository;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.service.mapper.RoomMapper;
import org.elasticsearch.common.unit.DistanceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Room.
 */
@Service
@Transactional
public class RoomService {

    private final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    private final RoomSearchRepository roomSearchRepository;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper, RoomSearchRepository roomSearchRepository) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomSearchRepository = roomSearchRepository;
    }

    /**
     * Save a room.
     *
     * @param roomDTO the entity to save
     * @return the persisted entity
     */
    public RoomDTO save(RoomDTO roomDTO) {
        log.debug("Request to save Room : {}", roomDTO);
        Room room = roomMapper.toEntity(roomDTO);
        room = roomRepository.save(room);
        RoomDTO result = roomMapper.toDto(room);
        room = roomRepository.findOneWithEagerRelationships(room.getId()).get();
        if(room.getState() == RoomState.SEARCH)
            roomSearchRepository.save(room);
        else
            roomSearchRepository.deleteById(room.getId());
        return result;
    }

    /**
     * Get all the rooms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rooms");
        return roomRepository.findAll(pageable)
            .map(roomMapper::toDto);
    }

    /**
     * Get all the Room with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<RoomDTO> findAllWithEagerRelationships(Pageable pageable) {
        return roomRepository.findAllWithEagerRelationships(pageable).map(roomMapper::toDto);
    }
    

    /**
     * Get one room by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoomDTO> findOne(Long id) {
        log.debug("Request to get Room : {}", id);
        return roomRepository.findOneWithEagerRelationships(id)
            .map(roomMapper::toDto);
    }

    /**
     * Delete the room by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Room : {}", id);
        roomRepository.deleteById(id);
        roomSearchRepository.deleteById(id);
    }

    /**
     * Search for the room corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Rooms for query {}", query);
        return roomSearchRepository.search(queryStringQuery(query), pageable)
            .map(roomMapper::toDto);
    }

    /**
     * Search for the room corresponding to the query.
     *
     * @param latitude the query of the search
     * @param longitude the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     * GET room/_search
     * {
     *     "query": {
     *         "bool" : {
     *             "must" : {
     *                 "match_all" : {}
     *             },
     *             "filter" : {
     *                 "geo_distance" : {
     *                     "distance" : "10km",
     *                     "address.location": "9.932533,-84.031295"
     *                     }
     *                 }
     *             }
     *         }
     *     }
     */
    @Transactional(readOnly = true)
    public Page<RoomDTO> search(Optional<Double> latitude, Optional<Double> longitude, Optional<Integer> distance, Optional<Double> price, Optional<CurrencyType> currency, Optional<String> features, Pageable pageable) {
        log.debug("Request to search for a page of Rooms for location {} {} {} km", latitude, longitude, distance);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        if(latitude.isPresent() && longitude.isPresent() && distance.isPresent())
            queryBuilder.withQuery(geoDistanceQuery("address.location").point(latitude.get(), longitude.get()).distance(distance.get(), DistanceUnit.KILOMETERS));

        price.ifPresent(aDouble -> queryBuilder.withQuery(termQuery("price.amount", aDouble)));

        currency.ifPresent(currencyType -> queryBuilder.withQuery(matchQuery("price.currency", currencyType)));

        if(features.isPresent()) {}

        SearchQuery searchQuery = queryBuilder.build();
        log.debug(searchQuery.getQuery().toString());

        return roomSearchRepository.search(searchQuery.getQuery(), pageable)
            .map(roomMapper::toDto);
    }

    /**
     * Reindex a room.
     *
     * @param id the id of the entity to reindex
     * @return the persisted entity
     */
    public void reindex(Long id) {
        log.debug("Request to reindex Room : {}", id);
        Optional<Room> room = roomRepository.findOneWithEagerRelationships(id);
        if(room.isPresent() && room.get().getState() == RoomState.SEARCH)
            roomSearchRepository.save(room.get());
    }
}
