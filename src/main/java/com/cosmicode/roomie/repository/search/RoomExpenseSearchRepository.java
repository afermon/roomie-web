package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.RoomExpense;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoomExpense entity.
 */
public interface RoomExpenseSearchRepository extends ElasticsearchRepository<RoomExpense, Long> {
}
