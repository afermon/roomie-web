package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.RoomPicture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RoomPicture entity.
 */
public interface RoomPictureSearchRepository extends ElasticsearchRepository<RoomPicture, Long> {
}
