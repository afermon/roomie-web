package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.RoomieState;
import com.cosmicode.roomie.repository.RoomieStateRepository;
import com.cosmicode.roomie.repository.search.RoomieStateSearchRepository;
import com.cosmicode.roomie.service.RoomieStateService;
import com.cosmicode.roomie.service.dto.RoomieStateDTO;
import com.cosmicode.roomie.service.mapper.RoomieStateMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.cosmicode.roomie.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cosmicode.roomie.domain.enumeration.AccountState;
/**
 * Test class for the RoomieStateResource REST controller.
 *
 * @see RoomieStateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomieStateResourceIntTest {

    private static final AccountState DEFAULT_STATE = AccountState.ACTIVE;
    private static final AccountState UPDATED_STATE = AccountState.SUSPENDED;

    private static final LocalDate DEFAULT_SUSPENDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUSPENDED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RoomieStateRepository roomieStateRepository;

    @Autowired
    private RoomieStateMapper roomieStateMapper;

    @Autowired
    private RoomieStateService roomieStateService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.RoomieStateSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoomieStateSearchRepository mockRoomieStateSearchRepository;

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

    private MockMvc restRoomieStateMockMvc;

    private RoomieState roomieState;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomieStateResource roomieStateResource = new RoomieStateResource(roomieStateService);
        this.restRoomieStateMockMvc = MockMvcBuilders.standaloneSetup(roomieStateResource)
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
    public static RoomieState createEntity(EntityManager em) {
        RoomieState roomieState = new RoomieState()
            .state(DEFAULT_STATE)
            .suspendedDate(DEFAULT_SUSPENDED_DATE);
        return roomieState;
    }

    @Before
    public void initTest() {
        roomieState = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomieState() throws Exception {
        int databaseSizeBeforeCreate = roomieStateRepository.findAll().size();

        // Create the RoomieState
        RoomieStateDTO roomieStateDTO = roomieStateMapper.toDto(roomieState);
        restRoomieStateMockMvc.perform(post("/api/roomie-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieStateDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomieState in the database
        List<RoomieState> roomieStateList = roomieStateRepository.findAll();
        assertThat(roomieStateList).hasSize(databaseSizeBeforeCreate + 1);
        RoomieState testRoomieState = roomieStateList.get(roomieStateList.size() - 1);
        assertThat(testRoomieState.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testRoomieState.getSuspendedDate()).isEqualTo(DEFAULT_SUSPENDED_DATE);

        // Validate the RoomieState in Elasticsearch
        verify(mockRoomieStateSearchRepository, times(1)).save(testRoomieState);
    }

    @Test
    @Transactional
    public void createRoomieStateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomieStateRepository.findAll().size();

        // Create the RoomieState with an existing ID
        roomieState.setId(1L);
        RoomieStateDTO roomieStateDTO = roomieStateMapper.toDto(roomieState);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomieStateMockMvc.perform(post("/api/roomie-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieStateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomieState in the database
        List<RoomieState> roomieStateList = roomieStateRepository.findAll();
        assertThat(roomieStateList).hasSize(databaseSizeBeforeCreate);

        // Validate the RoomieState in Elasticsearch
        verify(mockRoomieStateSearchRepository, times(0)).save(roomieState);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomieStateRepository.findAll().size();
        // set the field null
        roomieState.setState(null);

        // Create the RoomieState, which fails.
        RoomieStateDTO roomieStateDTO = roomieStateMapper.toDto(roomieState);

        restRoomieStateMockMvc.perform(post("/api/roomie-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieStateDTO)))
            .andExpect(status().isBadRequest());

        List<RoomieState> roomieStateList = roomieStateRepository.findAll();
        assertThat(roomieStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomieStates() throws Exception {
        // Initialize the database
        roomieStateRepository.saveAndFlush(roomieState);

        // Get all the roomieStateList
        restRoomieStateMockMvc.perform(get("/api/roomie-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomieState.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].suspendedDate").value(hasItem(DEFAULT_SUSPENDED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRoomieState() throws Exception {
        // Initialize the database
        roomieStateRepository.saveAndFlush(roomieState);

        // Get the roomieState
        restRoomieStateMockMvc.perform(get("/api/roomie-states/{id}", roomieState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomieState.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.suspendedDate").value(DEFAULT_SUSPENDED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomieState() throws Exception {
        // Get the roomieState
        restRoomieStateMockMvc.perform(get("/api/roomie-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomieState() throws Exception {
        // Initialize the database
        roomieStateRepository.saveAndFlush(roomieState);

        int databaseSizeBeforeUpdate = roomieStateRepository.findAll().size();

        // Update the roomieState
        RoomieState updatedRoomieState = roomieStateRepository.findById(roomieState.getId()).get();
        // Disconnect from session so that the updates on updatedRoomieState are not directly saved in db
        em.detach(updatedRoomieState);
        updatedRoomieState
            .state(UPDATED_STATE)
            .suspendedDate(UPDATED_SUSPENDED_DATE);
        RoomieStateDTO roomieStateDTO = roomieStateMapper.toDto(updatedRoomieState);

        restRoomieStateMockMvc.perform(put("/api/roomie-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieStateDTO)))
            .andExpect(status().isOk());

        // Validate the RoomieState in the database
        List<RoomieState> roomieStateList = roomieStateRepository.findAll();
        assertThat(roomieStateList).hasSize(databaseSizeBeforeUpdate);
        RoomieState testRoomieState = roomieStateList.get(roomieStateList.size() - 1);
        assertThat(testRoomieState.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testRoomieState.getSuspendedDate()).isEqualTo(UPDATED_SUSPENDED_DATE);

        // Validate the RoomieState in Elasticsearch
        verify(mockRoomieStateSearchRepository, times(1)).save(testRoomieState);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomieState() throws Exception {
        int databaseSizeBeforeUpdate = roomieStateRepository.findAll().size();

        // Create the RoomieState
        RoomieStateDTO roomieStateDTO = roomieStateMapper.toDto(roomieState);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomieStateMockMvc.perform(put("/api/roomie-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieStateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomieState in the database
        List<RoomieState> roomieStateList = roomieStateRepository.findAll();
        assertThat(roomieStateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RoomieState in Elasticsearch
        verify(mockRoomieStateSearchRepository, times(0)).save(roomieState);
    }

    @Test
    @Transactional
    public void deleteRoomieState() throws Exception {
        // Initialize the database
        roomieStateRepository.saveAndFlush(roomieState);

        int databaseSizeBeforeDelete = roomieStateRepository.findAll().size();

        // Delete the roomieState
        restRoomieStateMockMvc.perform(delete("/api/roomie-states/{id}", roomieState.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoomieState> roomieStateList = roomieStateRepository.findAll();
        assertThat(roomieStateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RoomieState in Elasticsearch
        verify(mockRoomieStateSearchRepository, times(1)).deleteById(roomieState.getId());
    }

    @Test
    @Transactional
    public void searchRoomieState() throws Exception {
        // Initialize the database
        roomieStateRepository.saveAndFlush(roomieState);
        when(mockRoomieStateSearchRepository.search(queryStringQuery("id:" + roomieState.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(roomieState), PageRequest.of(0, 1), 1));
        // Search the roomieState
        restRoomieStateMockMvc.perform(get("/api/_search/roomie-states?query=id:" + roomieState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomieState.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].suspendedDate").value(hasItem(DEFAULT_SUSPENDED_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomieState.class);
        RoomieState roomieState1 = new RoomieState();
        roomieState1.setId(1L);
        RoomieState roomieState2 = new RoomieState();
        roomieState2.setId(roomieState1.getId());
        assertThat(roomieState1).isEqualTo(roomieState2);
        roomieState2.setId(2L);
        assertThat(roomieState1).isNotEqualTo(roomieState2);
        roomieState1.setId(null);
        assertThat(roomieState1).isNotEqualTo(roomieState2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomieStateDTO.class);
        RoomieStateDTO roomieStateDTO1 = new RoomieStateDTO();
        roomieStateDTO1.setId(1L);
        RoomieStateDTO roomieStateDTO2 = new RoomieStateDTO();
        assertThat(roomieStateDTO1).isNotEqualTo(roomieStateDTO2);
        roomieStateDTO2.setId(roomieStateDTO1.getId());
        assertThat(roomieStateDTO1).isEqualTo(roomieStateDTO2);
        roomieStateDTO2.setId(2L);
        assertThat(roomieStateDTO1).isNotEqualTo(roomieStateDTO2);
        roomieStateDTO1.setId(null);
        assertThat(roomieStateDTO1).isNotEqualTo(roomieStateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomieStateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomieStateMapper.fromId(null)).isNull();
    }
}
