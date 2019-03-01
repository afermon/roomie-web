package com.cosmicode.roomie.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RoomTaskSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RoomTaskSearchRepositoryMockConfiguration {

    @MockBean
    private RoomTaskSearchRepository mockRoomTaskSearchRepository;

}
