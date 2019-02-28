package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.Room;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Room entity.
 */
public interface RoomSearchRepository extends ElasticsearchRepository<Room, Long> {
}
