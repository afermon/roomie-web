package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.UserPreferences;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserPreferences entity.
 */
public interface UserPreferencesSearchRepository extends ElasticsearchRepository<UserPreferences, Long> {
}
