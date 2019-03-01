package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.RoomieState;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoomieState entity.
 */
public interface RoomieStateSearchRepository extends ElasticsearchRepository<RoomieState, Long> {
}
