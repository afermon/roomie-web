package com.cosmicode.roomie.repository.search;

import com.cosmicode.roomie.domain.UserReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserReport entity.
 */
public interface UserReportSearchRepository extends ElasticsearchRepository<UserReport, Long> {
}
