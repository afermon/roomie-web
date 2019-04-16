package com.cosmicode.roomie.service;

import com.cosmicode.roomie.config.ApplicationProperties;
import com.cosmicode.roomie.domain.Room;
import com.cosmicode.roomie.domain.RoomFeature;
import com.cosmicode.roomie.domain.enumeration.CurrencyType;
import com.cosmicode.roomie.domain.enumeration.RoomState;
import com.cosmicode.roomie.repository.RoomRepository;
import com.cosmicode.roomie.repository.search.RoomSearchRepository;
import com.cosmicode.roomie.service.dto.RoomDTO;
import com.cosmicode.roomie.service.dto.SearchFilterDTO;
import com.cosmicode.roomie.service.mapper.RoomMapper;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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

    private final ApplicationProperties applicationProperties;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper, RoomSearchRepository roomSearchRepository, ApplicationProperties applicationProperties) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomSearchRepository = roomSearchRepository;
        this.applicationProperties = applicationProperties;
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

    public List<RoomDTO> findAllByOwner(Long id){
        return roomMapper.toDto(roomRepository.findRoomsByOwnerIdAndIsPremiumIsFalse(id));
    }

    public List<RoomDTO> findAllPremiumByOwner(Long id){
        return roomMapper.toDto(roomRepository.findPremiumRooms(id));
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
     * @param searchFilterDTO the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoomDTO> search(SearchFilterDTO searchFilterDTO, Pageable pageable) {
        log.debug("Request to search for a page of Rooms for location {}", searchFilterDTO.toString());

        int priceMinUSD, priceMaxUSD, priceMinCRC, priceMaxCRC;
        int exchangeRateAproxCRC2USD = applicationProperties.getAverageExchangeRateCrcToUsd();

        List<String> features = new ArrayList<>();
        if(searchFilterDTO.getFeatures() != null)
            for (RoomFeature feature: searchFilterDTO.getFeatures())
                features.add(feature.getName());

        if(searchFilterDTO.getCurrency() == CurrencyType.DOLLAR){
            priceMinUSD = searchFilterDTO.getPriceMin();
            priceMaxUSD = searchFilterDTO.getPriceMax();
            if(priceMaxUSD == 1000) priceMaxUSD = priceMaxUSD * 10;
            priceMinCRC = priceMinUSD * exchangeRateAproxCRC2USD;
            priceMaxCRC = priceMaxUSD * exchangeRateAproxCRC2USD;
        } else {
            priceMinCRC = searchFilterDTO.getPriceMin();
            priceMaxCRC = searchFilterDTO.getPriceMax();
            if(priceMaxCRC == 1000) priceMaxCRC = priceMaxCRC * 10;
            priceMinUSD = priceMinCRC / exchangeRateAproxCRC2USD;
            priceMaxUSD = priceMaxCRC / exchangeRateAproxCRC2USD;
        }

        QueryBuilder searchQueryBuilder = QueryBuilders.boolQuery()
            .should(QueryBuilders.queryStringQuery(searchFilterDTO.getQuery()))
            .should(QueryBuilders.rangeQuery("price.amount").lte(priceMaxCRC).gte(priceMinCRC))
            .should(QueryBuilders.rangeQuery("price.amount").lte(priceMaxUSD).gte(priceMinUSD))
            .should(QueryBuilders.termsQuery("features.name", features))
            .minimumShouldMatch(1)
            .filter(QueryBuilders.geoDistanceQuery("address.location").point(searchFilterDTO.getLatitude(), searchFilterDTO.getLongitude()).distance(searchFilterDTO.getDistance(), DistanceUnit.KILOMETERS));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withPageable(pageable)
            .withQuery(searchQueryBuilder)
            .build();

        return roomSearchRepository.search(searchQuery)
            .map(roomMapper::toDto);
    }

    /**
     * Reindex a room.
     *
     * @param id the id of the entity to reindex
     */
    public void reindex(Long id) {
        log.debug("Request to reindex Room : {}", id);
        Optional<Room> room = roomRepository.findOneWithEagerRelationships(id);
        if(room.isPresent() && room.get().getState() == RoomState.SEARCH)
            roomSearchRepository.save(room.get());
    }
}
