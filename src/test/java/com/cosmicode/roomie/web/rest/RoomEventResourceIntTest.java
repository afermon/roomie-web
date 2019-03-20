package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.RoomEvent;
import com.cosmicode.roomie.repository.RoomEventRepository;
import com.cosmicode.roomie.repository.search.RoomEventSearchRepository;
import com.cosmicode.roomie.service.RoomEventService;
import com.cosmicode.roomie.service.dto.RoomEventDTO;
import com.cosmicode.roomie.service.mapper.RoomEventMapper;
import com.cosmicode.roomie.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.cosmicode.roomie.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoomEventResource REST controller.
 *
 * @see RoomEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomEventResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIVATE = false;
    private static final Boolean UPDATED_IS_PRIVATE = true;

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private RoomEventRepository roomEventRepository;

    @Autowired
    private RoomEventMapper roomEventMapper;

    @Autowired
    private RoomEventService roomEventService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.RoomEventSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoomEventSearchRepository mockRoomEventSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRoomEventMockMvc;

    private RoomEvent roomEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomEventResource roomEventResource = new RoomEventResource(roomEventService);
        this.restRoomEventMockMvc = MockMvcBuilders.standaloneSetup(roomEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomEvent createEntity(EntityManager em) {
        RoomEvent roomEvent = new RoomEvent()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .isPrivate(DEFAULT_IS_PRIVATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        return roomEvent;
    }

    @Before
    public void initTest() {
        roomEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomEvent() throws Exception {
        int databaseSizeBeforeCreate = roomEventRepository.findAll().size();

        // Create the RoomEvent
        RoomEventDTO roomEventDTO = roomEventMapper.toDto(roomEvent);
        restRoomEventMockMvc.perform(post("/api/room-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomEventDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomEvent in the database
        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeCreate + 1);
        RoomEvent testRoomEvent = roomEventList.get(roomEventList.size() - 1);
        assertThat(testRoomEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRoomEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoomEvent.isIsPrivate()).isEqualTo(DEFAULT_IS_PRIVATE);
        assertThat(testRoomEvent.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testRoomEvent.getEndTime()).isEqualTo(DEFAULT_END_TIME);

        // Validate the RoomEvent in Elasticsearch
        verify(mockRoomEventSearchRepository, times(1)).save(testRoomEvent);
    }

    @Test
    @Transactional
    public void createRoomEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomEventRepository.findAll().size();

        // Create the RoomEvent with an existing ID
        roomEvent.setId(1L);
        RoomEventDTO roomEventDTO = roomEventMapper.toDto(roomEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomEventMockMvc.perform(post("/api/room-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomEvent in the database
        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeCreate);

        // Validate the RoomEvent in Elasticsearch
        verify(mockRoomEventSearchRepository, times(0)).save(roomEvent);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomEventRepository.findAll().size();
        // set the field null
        roomEvent.setTitle(null);

        // Create the RoomEvent, which fails.
        RoomEventDTO roomEventDTO = roomEventMapper.toDto(roomEvent);

        restRoomEventMockMvc.perform(post("/api/room-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomEventDTO)))
            .andExpect(status().isBadRequest());

        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsPrivateIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomEventRepository.findAll().size();
        // set the field null
        roomEvent.setIsPrivate(null);

        // Create the RoomEvent, which fails.
        RoomEventDTO roomEventDTO = roomEventMapper.toDto(roomEvent);

        restRoomEventMockMvc.perform(post("/api/room-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomEventDTO)))
            .andExpect(status().isBadRequest());

        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomEventRepository.findAll().size();
        // set the field null
        roomEvent.setStartTime(null);

        // Create the RoomEvent, which fails.
        RoomEventDTO roomEventDTO = roomEventMapper.toDto(roomEvent);

        restRoomEventMockMvc.perform(post("/api/room-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomEventDTO)))
            .andExpect(status().isBadRequest());

        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomEventRepository.findAll().size();
        // set the field null
        roomEvent.setEndTime(null);

        // Create the RoomEvent, which fails.
        RoomEventDTO roomEventDTO = roomEventMapper.toDto(roomEvent);

        restRoomEventMockMvc.perform(post("/api/room-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomEventDTO)))
            .andExpect(status().isBadRequest());

        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomEvents() throws Exception {
        // Initialize the database
        roomEventRepository.saveAndFlush(roomEvent);

        // Get all the roomEventList
        restRoomEventMockMvc.perform(get("/api/room-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getRoomEvent() throws Exception {
        // Initialize the database
        roomEventRepository.saveAndFlush(roomEvent);

        // Get the roomEvent
        restRoomEventMockMvc.perform(get("/api/room-events/{id}", roomEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomEvent.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isPrivate").value(DEFAULT_IS_PRIVATE.booleanValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomEvent() throws Exception {
        // Get the roomEvent
        restRoomEventMockMvc.perform(get("/api/room-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomEvent() throws Exception {
        // Initialize the database
        roomEventRepository.saveAndFlush(roomEvent);

        int databaseSizeBeforeUpdate = roomEventRepository.findAll().size();

        // Update the roomEvent
        RoomEvent updatedRoomEvent = roomEventRepository.findById(roomEvent.getId()).get();
        // Disconnect from session so that the updates on updatedRoomEvent are not directly saved in db
        em.detach(updatedRoomEvent);
        updatedRoomEvent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .isPrivate(UPDATED_IS_PRIVATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        RoomEventDTO roomEventDTO = roomEventMapper.toDto(updatedRoomEvent);

        restRoomEventMockMvc.perform(put("/api/room-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomEventDTO)))
            .andExpect(status().isOk());

        // Validate the RoomEvent in the database
        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeUpdate);
        RoomEvent testRoomEvent = roomEventList.get(roomEventList.size() - 1);
        assertThat(testRoomEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRoomEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoomEvent.isIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testRoomEvent.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRoomEvent.getEndTime()).isEqualTo(UPDATED_END_TIME);

        // Validate the RoomEvent in Elasticsearch
        verify(mockRoomEventSearchRepository, times(1)).save(testRoomEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomEvent() throws Exception {
        int databaseSizeBeforeUpdate = roomEventRepository.findAll().size();

        // Create the RoomEvent
        RoomEventDTO roomEventDTO = roomEventMapper.toDto(roomEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomEventMockMvc.perform(put("/api/room-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomEvent in the database
        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RoomEvent in Elasticsearch
        verify(mockRoomEventSearchRepository, times(0)).save(roomEvent);
    }

    @Test
    @Transactional
    public void deleteRoomEvent() throws Exception {
        // Initialize the database
        roomEventRepository.saveAndFlush(roomEvent);

        int databaseSizeBeforeDelete = roomEventRepository.findAll().size();

        // Delete the roomEvent
        restRoomEventMockMvc.perform(delete("/api/room-events/{id}", roomEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoomEvent> roomEventList = roomEventRepository.findAll();
        assertThat(roomEventList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RoomEvent in Elasticsearch
        verify(mockRoomEventSearchRepository, times(1)).deleteById(roomEvent.getId());
    }

    @Test
    @Transactional
    public void searchRoomEvent() throws Exception {
        // Initialize the database
        roomEventRepository.saveAndFlush(roomEvent);
        when(mockRoomEventSearchRepository.search(queryStringQuery("id:" + roomEvent.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(roomEvent), PageRequest.of(0, 1), 1));
        // Search the roomEvent
        restRoomEventMockMvc.perform(get("/api/_search/room-events?query=id:" + roomEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomEvent.class);
        RoomEvent roomEvent1 = new RoomEvent();
        roomEvent1.setId(1L);
        RoomEvent roomEvent2 = new RoomEvent();
        roomEvent2.setId(roomEvent1.getId());
        assertThat(roomEvent1).isEqualTo(roomEvent2);
        roomEvent2.setId(2L);
        assertThat(roomEvent1).isNotEqualTo(roomEvent2);
        roomEvent1.setId(null);
        assertThat(roomEvent1).isNotEqualTo(roomEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomEventDTO.class);
        RoomEventDTO roomEventDTO1 = new RoomEventDTO();
        roomEventDTO1.setId(1L);
        RoomEventDTO roomEventDTO2 = new RoomEventDTO();
        assertThat(roomEventDTO1).isNotEqualTo(roomEventDTO2);
        roomEventDTO2.setId(roomEventDTO1.getId());
        assertThat(roomEventDTO1).isEqualTo(roomEventDTO2);
        roomEventDTO2.setId(2L);
        assertThat(roomEventDTO1).isNotEqualTo(roomEventDTO2);
        roomEventDTO1.setId(null);
        assertThat(roomEventDTO1).isNotEqualTo(roomEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomEventMapper.fromId(null)).isNull();
    }
}
