package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.RoomExpenseSplitRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoomExpenseSplitRecord entity.
 */
public interface RoomExpenseSplitRecordSearchRepository extends ElasticsearchRepository<RoomExpenseSplitRecord, Long> {
}
