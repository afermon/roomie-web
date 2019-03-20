package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.RoomTask;
import com.cosmicode.roomie.repository.RoomTaskRepository;
import com.cosmicode.roomie.repository.search.RoomTaskSearchRepository;
import com.cosmicode.roomie.service.RoomTaskService;
import com.cosmicode.roomie.service.dto.RoomTaskDTO;
import com.cosmicode.roomie.service.mapper.RoomTaskMapper;
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

import com.cosmicode.roomie.domain.enumeration.RoomTaskState;
/**
 * Test class for the RoomTaskResource REST controller.
 *
 * @see RoomTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomTaskResourceIntTest {

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DEADLINE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEADLINE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final RoomTaskState DEFAULT_STATE = RoomTaskState.PENDING;
    private static final RoomTaskState UPDATED_STATE = RoomTaskState.COMPLETED;

    @Autowired
    private RoomTaskRepository roomTaskRepository;

    @Autowired
    private RoomTaskMapper roomTaskMapper;

    @Autowired
    private RoomTaskService roomTaskService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.RoomTaskSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoomTaskSearchRepository mockRoomTaskSearchRepository;

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

    private MockMvc restRoomTaskMockMvc;

    private RoomTask roomTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomTaskResource roomTaskResource = new RoomTaskResource(roomTaskService);
        this.restRoomTaskMockMvc = MockMvcBuilders.standaloneSetup(roomTaskResource)
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
    public static RoomTask createEntity(EntityManager em) {
        RoomTask roomTask = new RoomTask()
            .created(DEFAULT_CREATED)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .deadline(DEFAULT_DEADLINE)
            .state(DEFAULT_STATE);
        return roomTask;
    }

    @Before
    public void initTest() {
        roomTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomTask() throws Exception {
        int databaseSizeBeforeCreate = roomTaskRepository.findAll().size();

        // Create the RoomTask
        RoomTaskDTO roomTaskDTO = roomTaskMapper.toDto(roomTask);
        restRoomTaskMockMvc.perform(post("/api/room-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomTask in the database
        List<RoomTask> roomTaskList = roomTaskRepository.findAll();
        assertThat(roomTaskList).hasSize(databaseSizeBeforeCreate + 1);
        RoomTask testRoomTask = roomTaskList.get(roomTaskList.size() - 1);
        assertThat(testRoomTask.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testRoomTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRoomTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoomTask.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
        assertThat(testRoomTask.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the RoomTask in Elasticsearch
        verify(mockRoomTaskSearchRepository, times(1)).save(testRoomTask);
    }

    @Test
    @Transactional
    public void createRoomTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomTaskRepository.findAll().size();

        // Create the RoomTask with an existing ID
        roomTask.setId(1L);
        RoomTaskDTO roomTaskDTO = roomTaskMapper.toDto(roomTask);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomTaskMockMvc.perform(post("/api/room-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomTask in the database
        List<RoomTask> roomTaskList = roomTaskRepository.findAll();
        assertThat(roomTaskList).hasSize(databaseSizeBeforeCreate);

        // Validate the RoomTask in Elasticsearch
        verify(mockRoomTaskSearchRepository, times(0)).save(roomTask);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomTaskRepository.findAll().size();
        // set the field null
        roomTask.setCreated(null);

        // Create the RoomTask, which fails.
        RoomTaskDTO roomTaskDTO = roomTaskMapper.toDto(roomTask);

        restRoomTaskMockMvc.perform(post("/api/room-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomTaskDTO)))
            .andExpect(status().isBadRequest());

        List<RoomTask> roomTaskList = roomTaskRepository.findAll();
        assertThat(roomTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomTaskRepository.findAll().size();
        // set the field null
        roomTask.setTitle(null);

        // Create the RoomTask, which fails.
        RoomTaskDTO roomTaskDTO = roomTaskMapper.toDto(roomTask);

        restRoomTaskMockMvc.perform(post("/api/room-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomTaskDTO)))
            .andExpect(status().isBadRequest());

        List<RoomTask> roomTaskList = roomTaskRepository.findAll();
        assertThat(roomTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomTaskRepository.findAll().size();
        // set the field null
        roomTask.setState(null);

        // Create the RoomTask, which fails.
        RoomTaskDTO roomTaskDTO = roomTaskMapper.toDto(roomTask);

        restRoomTaskMockMvc.perform(post("/api/room-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomTaskDTO)))
            .andExpect(status().isBadRequest());

        List<RoomTask> roomTaskList = roomTaskRepository.findAll();
        assertThat(roomTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomTasks() throws Exception {
        // Initialize the database
        roomTaskRepository.saveAndFlush(roomTask);

        // Get all the roomTaskList
        restRoomTaskMockMvc.perform(get("/api/room-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRoomTask() throws Exception {
        // Initialize the database
        roomTaskRepository.saveAndFlush(roomTask);

        // Get the roomTask
        restRoomTaskMockMvc.perform(get("/api/room-tasks/{id}", roomTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomTask.getId().intValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomTask() throws Exception {
        // Get the roomTask
        restRoomTaskMockMvc.perform(get("/api/room-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomTask() throws Exception {
        // Initialize the database
        roomTaskRepository.saveAndFlush(roomTask);

        int databaseSizeBeforeUpdate = roomTaskRepository.findAll().size();

        // Update the roomTask
        RoomTask updatedRoomTask = roomTaskRepository.findById(roomTask.getId()).get();
        // Disconnect from session so that the updates on updatedRoomTask are not directly saved in db
        em.detach(updatedRoomTask);
        updatedRoomTask
            .created(UPDATED_CREATED)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .deadline(UPDATED_DEADLINE)
            .state(UPDATED_STATE);
        RoomTaskDTO roomTaskDTO = roomTaskMapper.toDto(updatedRoomTask);

        restRoomTaskMockMvc.perform(put("/api/room-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomTaskDTO)))
            .andExpect(status().isOk());

        // Validate the RoomTask in the database
        List<RoomTask> roomTaskList = roomTaskRepository.findAll();
        assertThat(roomTaskList).hasSize(databaseSizeBeforeUpdate);
        RoomTask testRoomTask = roomTaskList.get(roomTaskList.size() - 1);
        assertThat(testRoomTask.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testRoomTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRoomTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoomTask.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testRoomTask.getState()).isEqualTo(UPDATED_STATE);

        // Validate the RoomTask in Elasticsearch
        verify(mockRoomTaskSearchRepository, times(1)).save(testRoomTask);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomTask() throws Exception {
        int databaseSizeBeforeUpdate = roomTaskRepository.findAll().size();

        // Create the RoomTask
        RoomTaskDTO roomTaskDTO = roomTaskMapper.toDto(roomTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomTaskMockMvc.perform(put("/api/room-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomTask in the database
        List<RoomTask> roomTaskList = roomTaskRepository.findAll();
        assertThat(roomTaskList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RoomTask in Elasticsearch
        verify(mockRoomTaskSearchRepository, times(0)).save(roomTask);
    }

    @Test
    @Transactional
    public void deleteRoomTask() throws Exception {
        // Initialize the database
        roomTaskRepository.saveAndFlush(roomTask);

        int databaseSizeBeforeDelete = roomTaskRepository.findAll().size();

        // Delete the roomTask
        restRoomTaskMockMvc.perform(delete("/api/room-tasks/{id}", roomTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoomTask> roomTaskList = roomTaskRepository.findAll();
        assertThat(roomTaskList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RoomTask in Elasticsearch
        verify(mockRoomTaskSearchRepository, times(1)).deleteById(roomTask.getId());
    }

    @Test
    @Transactional
    public void searchRoomTask() throws Exception {
        // Initialize the database
        roomTaskRepository.saveAndFlush(roomTask);
        when(mockRoomTaskSearchRepository.search(queryStringQuery("id:" + roomTask.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(roomTask), PageRequest.of(0, 1), 1));
        // Search the roomTask
        restRoomTaskMockMvc.perform(get("/api/_search/room-tasks?query=id:" + roomTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomTask.class);
        RoomTask roomTask1 = new RoomTask();
        roomTask1.setId(1L);
        RoomTask roomTask2 = new RoomTask();
        roomTask2.setId(roomTask1.getId());
        assertThat(roomTask1).isEqualTo(roomTask2);
        roomTask2.setId(2L);
        assertThat(roomTask1).isNotEqualTo(roomTask2);
        roomTask1.setId(null);
        assertThat(roomTask1).isNotEqualTo(roomTask2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomTaskDTO.class);
        RoomTaskDTO roomTaskDTO1 = new RoomTaskDTO();
        roomTaskDTO1.setId(1L);
        RoomTaskDTO roomTaskDTO2 = new RoomTaskDTO();
        assertThat(roomTaskDTO1).isNotEqualTo(roomTaskDTO2);
        roomTaskDTO2.setId(roomTaskDTO1.getId());
        assertThat(roomTaskDTO1).isEqualTo(roomTaskDTO2);
        roomTaskDTO2.setId(2L);
        assertThat(roomTaskDTO1).isNotEqualTo(roomTaskDTO2);
        roomTaskDTO1.setId(null);
        assertThat(roomTaskDTO1).isNotEqualTo(roomTaskDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomTaskMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomTaskMapper.fromId(null)).isNull();
    }
}
