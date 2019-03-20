package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.Roomie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Roomie entity.
 */
public interface RoomieSearchRepository extends ElasticsearchRepository<Roomie, Long> {
}
