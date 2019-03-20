package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.RoomTask;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoomTask entity.
 */
public interface RoomTaskSearchRepository extends ElasticsearchRepository<RoomTask, Long> {
}
