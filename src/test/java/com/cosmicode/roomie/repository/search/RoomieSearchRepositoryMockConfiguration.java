package com.cosmicode.roomie.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RoomieSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RoomieSearchRepositoryMockConfiguration {

    @MockBean
    private RoomieSearchRepository mockRoomieSearchRepository;

}
