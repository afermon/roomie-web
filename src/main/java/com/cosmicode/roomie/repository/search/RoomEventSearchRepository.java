package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.RoomEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoomEvent entity.
 */
public interface RoomEventSearchRepository extends ElasticsearchRepository<RoomEvent, Long> {
}
