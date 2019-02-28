package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.RoomFeature;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoomFeature entity.
 */
public interface RoomFeatureSearchRepository extends ElasticsearchRepository<RoomFeature, Long> {
}
