package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.RoomExpenseSplit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoomExpenseSplit entity.
 */
public interface RoomExpenseSplitSearchRepository extends ElasticsearchRepository<RoomExpenseSplit, Long> {
}
