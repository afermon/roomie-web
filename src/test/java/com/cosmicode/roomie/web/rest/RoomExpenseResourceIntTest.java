package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.RoomExpense;
import com.cosmicode.roomie.repository.RoomExpenseRepository;
import com.cosmicode.roomie.repository.search.RoomExpenseSearchRepository;
import com.cosmicode.roomie.service.RoomExpenseService;
import com.cosmicode.roomie.service.dto.RoomExpenseDTO;
import com.cosmicode.roomie.service.mapper.RoomExpenseMapper;
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

import com.cosmicode.roomie.domain.enumeration.CurrencyType;
/**
 * Test class for the RoomExpenseResource REST controller.
 *
 * @see RoomExpenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomExpenseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final CurrencyType DEFAULT_CURRENCY = CurrencyType.COLON;
    private static final CurrencyType UPDATED_CURRENCY = CurrencyType.DOLLAR;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Integer DEFAULT_PERIODICITY = 1;
    private static final Integer UPDATED_PERIODICITY = 2;

    private static final Integer DEFAULT_MONTH_DAY = 1;
    private static final Integer UPDATED_MONTH_DAY = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FINISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINISH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RoomExpenseRepository roomExpenseRepository;

    @Autowired
    private RoomExpenseMapper roomExpenseMapper;

    @Autowired
    private RoomExpenseService roomExpenseService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.RoomExpenseSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoomExpenseSearchRepository mockRoomExpenseSearchRepository;

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

    private MockMvc restRoomExpenseMockMvc;

    private RoomExpense roomExpense;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomExpenseResource roomExpenseResource = new RoomExpenseResource(roomExpenseService);
        this.restRoomExpenseMockMvc = MockMvcBuilders.standaloneSetup(roomExpenseResource)
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
    public static RoomExpense createEntity(EntityManager em) {
        RoomExpense roomExpense = new RoomExpense()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .currency(DEFAULT_CURRENCY)
            .amount(DEFAULT_AMOUNT)
            .periodicity(DEFAULT_PERIODICITY)
            .monthDay(DEFAULT_MONTH_DAY)
            .startDate(DEFAULT_START_DATE)
            .finishDate(DEFAULT_FINISH_DATE);
        return roomExpense;
    }

    @Before
    public void initTest() {
        roomExpense = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomExpense() throws Exception {
        int databaseSizeBeforeCreate = roomExpenseRepository.findAll().size();

        // Create the RoomExpense
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(roomExpense);
        restRoomExpenseMockMvc.perform(post("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomExpense in the database
        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeCreate + 1);
        RoomExpense testRoomExpense = roomExpenseList.get(roomExpenseList.size() - 1);
        assertThat(testRoomExpense.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoomExpense.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoomExpense.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testRoomExpense.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRoomExpense.getPeriodicity()).isEqualTo(DEFAULT_PERIODICITY);
        assertThat(testRoomExpense.getMonthDay()).isEqualTo(DEFAULT_MONTH_DAY);
        assertThat(testRoomExpense.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testRoomExpense.getFinishDate()).isEqualTo(DEFAULT_FINISH_DATE);

        // Validate the RoomExpense in Elasticsearch
        verify(mockRoomExpenseSearchRepository, times(1)).save(testRoomExpense);
    }

    @Test
    @Transactional
    public void createRoomExpenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomExpenseRepository.findAll().size();

        // Create the RoomExpense with an existing ID
        roomExpense.setId(1L);
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(roomExpense);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomExpenseMockMvc.perform(post("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomExpense in the database
        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeCreate);

        // Validate the RoomExpense in Elasticsearch
        verify(mockRoomExpenseSearchRepository, times(0)).save(roomExpense);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomExpenseRepository.findAll().size();
        // set the field null
        roomExpense.setName(null);

        // Create the RoomExpense, which fails.
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(roomExpense);

        restRoomExpenseMockMvc.perform(post("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isBadRequest());

        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomExpenseRepository.findAll().size();
        // set the field null
        roomExpense.setCurrency(null);

        // Create the RoomExpense, which fails.
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(roomExpense);

        restRoomExpenseMockMvc.perform(post("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isBadRequest());

        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomExpenseRepository.findAll().size();
        // set the field null
        roomExpense.setAmount(null);

        // Create the RoomExpense, which fails.
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(roomExpense);

        restRoomExpenseMockMvc.perform(post("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isBadRequest());

        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeriodicityIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomExpenseRepository.findAll().size();
        // set the field null
        roomExpense.setPeriodicity(null);

        // Create the RoomExpense, which fails.
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(roomExpense);

        restRoomExpenseMockMvc.perform(post("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isBadRequest());

        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomExpenseRepository.findAll().size();
        // set the field null
        roomExpense.setMonthDay(null);

        // Create the RoomExpense, which fails.
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(roomExpense);

        restRoomExpenseMockMvc.perform(post("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isBadRequest());

        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomExpenses() throws Exception {
        // Initialize the database
        roomExpenseRepository.saveAndFlush(roomExpense);

        // Get all the roomExpenseList
        restRoomExpenseMockMvc.perform(get("/api/room-expenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomExpense.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].periodicity").value(hasItem(DEFAULT_PERIODICITY)))
            .andExpect(jsonPath("$.[*].monthDay").value(hasItem(DEFAULT_MONTH_DAY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRoomExpense() throws Exception {
        // Initialize the database
        roomExpenseRepository.saveAndFlush(roomExpense);

        // Get the roomExpense
        restRoomExpenseMockMvc.perform(get("/api/room-expenses/{id}", roomExpense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomExpense.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.periodicity").value(DEFAULT_PERIODICITY))
            .andExpect(jsonPath("$.monthDay").value(DEFAULT_MONTH_DAY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.finishDate").value(DEFAULT_FINISH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomExpense() throws Exception {
        // Get the roomExpense
        restRoomExpenseMockMvc.perform(get("/api/room-expenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomExpense() throws Exception {
        // Initialize the database
        roomExpenseRepository.saveAndFlush(roomExpense);

        int databaseSizeBeforeUpdate = roomExpenseRepository.findAll().size();

        // Update the roomExpense
        RoomExpense updatedRoomExpense = roomExpenseRepository.findById(roomExpense.getId()).get();
        // Disconnect from session so that the updates on updatedRoomExpense are not directly saved in db
        em.detach(updatedRoomExpense);
        updatedRoomExpense
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .amount(UPDATED_AMOUNT)
            .periodicity(UPDATED_PERIODICITY)
            .monthDay(UPDATED_MONTH_DAY)
            .startDate(UPDATED_START_DATE)
            .finishDate(UPDATED_FINISH_DATE);
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(updatedRoomExpense);

        restRoomExpenseMockMvc.perform(put("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isOk());

        // Validate the RoomExpense in the database
        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeUpdate);
        RoomExpense testRoomExpense = roomExpenseList.get(roomExpenseList.size() - 1);
        assertThat(testRoomExpense.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoomExpense.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoomExpense.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testRoomExpense.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRoomExpense.getPeriodicity()).isEqualTo(UPDATED_PERIODICITY);
        assertThat(testRoomExpense.getMonthDay()).isEqualTo(UPDATED_MONTH_DAY);
        assertThat(testRoomExpense.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testRoomExpense.getFinishDate()).isEqualTo(UPDATED_FINISH_DATE);

        // Validate the RoomExpense in Elasticsearch
        verify(mockRoomExpenseSearchRepository, times(1)).save(testRoomExpense);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomExpense() throws Exception {
        int databaseSizeBeforeUpdate = roomExpenseRepository.findAll().size();

        // Create the RoomExpense
        RoomExpenseDTO roomExpenseDTO = roomExpenseMapper.toDto(roomExpense);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomExpenseMockMvc.perform(put("/api/room-expenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomExpense in the database
        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RoomExpense in Elasticsearch
        verify(mockRoomExpenseSearchRepository, times(0)).save(roomExpense);
    }

    @Test
    @Transactional
    public void deleteRoomExpense() throws Exception {
        // Initialize the database
        roomExpenseRepository.saveAndFlush(roomExpense);

        int databaseSizeBeforeDelete = roomExpenseRepository.findAll().size();

        // Get the roomExpense
        restRoomExpenseMockMvc.perform(delete("/api/room-expenses/{id}", roomExpense.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoomExpense> roomExpenseList = roomExpenseRepository.findAll();
        assertThat(roomExpenseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RoomExpense in Elasticsearch
        verify(mockRoomExpenseSearchRepository, times(1)).deleteById(roomExpense.getId());
    }

    @Test
    @Transactional
    public void searchRoomExpense() throws Exception {
        // Initialize the database
        roomExpenseRepository.saveAndFlush(roomExpense);
        when(mockRoomExpenseSearchRepository.search(queryStringQuery("id:" + roomExpense.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(roomExpense), PageRequest.of(0, 1), 1));
        // Search the roomExpense
        restRoomExpenseMockMvc.perform(get("/api/_search/room-expenses?query=id:" + roomExpense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomExpense.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].periodicity").value(hasItem(DEFAULT_PERIODICITY)))
            .andExpect(jsonPath("$.[*].monthDay").value(hasItem(DEFAULT_MONTH_DAY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomExpense.class);
        RoomExpense roomExpense1 = new RoomExpense();
        roomExpense1.setId(1L);
        RoomExpense roomExpense2 = new RoomExpense();
        roomExpense2.setId(roomExpense1.getId());
        assertThat(roomExpense1).isEqualTo(roomExpense2);
        roomExpense2.setId(2L);
        assertThat(roomExpense1).isNotEqualTo(roomExpense2);
        roomExpense1.setId(null);
        assertThat(roomExpense1).isNotEqualTo(roomExpense2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomExpenseDTO.class);
        RoomExpenseDTO roomExpenseDTO1 = new RoomExpenseDTO();
        roomExpenseDTO1.setId(1L);
        RoomExpenseDTO roomExpenseDTO2 = new RoomExpenseDTO();
        assertThat(roomExpenseDTO1).isNotEqualTo(roomExpenseDTO2);
        roomExpenseDTO2.setId(roomExpenseDTO1.getId());
        assertThat(roomExpenseDTO1).isEqualTo(roomExpenseDTO2);
        roomExpenseDTO2.setId(2L);
        assertThat(roomExpenseDTO1).isNotEqualTo(roomExpenseDTO2);
        roomExpenseDTO1.setId(null);
        assertThat(roomExpenseDTO1).isNotEqualTo(roomExpenseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomExpenseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomExpenseMapper.fromId(null)).isNull();
    }
}
