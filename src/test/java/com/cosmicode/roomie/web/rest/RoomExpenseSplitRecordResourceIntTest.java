package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;
import com.cosmicode.roomie.domain.RoomExpenseSplitRecord;
import com.cosmicode.roomie.repository.RoomExpenseSplitRecordRepository;
import com.cosmicode.roomie.service.RoomExpenseSplitRecordService;
import com.cosmicode.roomie.service.dto.RoomExpenseSplitRecordDTO;
import com.cosmicode.roomie.service.mapper.RoomExpenseSplitRecordMapper;
import com.cosmicode.roomie.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.List;

import static com.cosmicode.roomie.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoomExpenseSplitRecordResource REST controller.
 *
 * @see RoomExpenseSplitRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomExpenseSplitRecordResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    @Autowired
    private RoomExpenseSplitRecordRepository roomExpenseSplitRecordRepository;

    @Autowired
    private RoomExpenseSplitRecordMapper roomExpenseSplitRecordMapper;

    @Autowired
    private RoomExpenseSplitRecordService roomExpenseSplitRecordService;

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

    private MockMvc restRoomExpenseSplitRecordMockMvc;

    private RoomExpenseSplitRecord roomExpenseSplitRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomExpenseSplitRecordResource roomExpenseSplitRecordResource = new RoomExpenseSplitRecordResource(roomExpenseSplitRecordService);
        this.restRoomExpenseSplitRecordMockMvc = MockMvcBuilders.standaloneSetup(roomExpenseSplitRecordResource)
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
    public static RoomExpenseSplitRecord createEntity(EntityManager em) {
        RoomExpenseSplitRecord roomExpenseSplitRecord = new RoomExpenseSplitRecord()
            .date(DEFAULT_DATE)
            .state(DEFAULT_STATE);
        return roomExpenseSplitRecord;
    }

    @Before
    public void initTest() {
        roomExpenseSplitRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomExpenseSplitRecord() throws Exception {
        int databaseSizeBeforeCreate = roomExpenseSplitRecordRepository.findAll().size();

        // Create the RoomExpenseSplitRecord
        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO = roomExpenseSplitRecordMapper.toDto(roomExpenseSplitRecord);
        restRoomExpenseSplitRecordMockMvc.perform(post("/api/room-expense-split-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomExpenseSplitRecord in the database
        List<RoomExpenseSplitRecord> roomExpenseSplitRecordList = roomExpenseSplitRecordRepository.findAll();
        assertThat(roomExpenseSplitRecordList).hasSize(databaseSizeBeforeCreate + 1);
        RoomExpenseSplitRecord testRoomExpenseSplitRecord = roomExpenseSplitRecordList.get(roomExpenseSplitRecordList.size() - 1);
        assertThat(testRoomExpenseSplitRecord.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRoomExpenseSplitRecord.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createRoomExpenseSplitRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomExpenseSplitRecordRepository.findAll().size();

        // Create the RoomExpenseSplitRecord with an existing ID
        roomExpenseSplitRecord.setId(1L);
        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO = roomExpenseSplitRecordMapper.toDto(roomExpenseSplitRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomExpenseSplitRecordMockMvc.perform(post("/api/room-expense-split-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomExpenseSplitRecord in the database
        List<RoomExpenseSplitRecord> roomExpenseSplitRecordList = roomExpenseSplitRecordRepository.findAll();
        assertThat(roomExpenseSplitRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomExpenseSplitRecordRepository.findAll().size();
        // set the field null
        roomExpenseSplitRecord.setDate(null);

        // Create the RoomExpenseSplitRecord, which fails.
        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO = roomExpenseSplitRecordMapper.toDto(roomExpenseSplitRecord);

        restRoomExpenseSplitRecordMockMvc.perform(post("/api/room-expense-split-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitRecordDTO)))
            .andExpect(status().isBadRequest());

        List<RoomExpenseSplitRecord> roomExpenseSplitRecordList = roomExpenseSplitRecordRepository.findAll();
        assertThat(roomExpenseSplitRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomExpenseSplitRecordRepository.findAll().size();
        // set the field null
        roomExpenseSplitRecord.setState(null);

        // Create the RoomExpenseSplitRecord, which fails.
        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO = roomExpenseSplitRecordMapper.toDto(roomExpenseSplitRecord);

        restRoomExpenseSplitRecordMockMvc.perform(post("/api/room-expense-split-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitRecordDTO)))
            .andExpect(status().isBadRequest());

        List<RoomExpenseSplitRecord> roomExpenseSplitRecordList = roomExpenseSplitRecordRepository.findAll();
        assertThat(roomExpenseSplitRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomExpenseSplitRecords() throws Exception {
        // Initialize the database
        roomExpenseSplitRecordRepository.saveAndFlush(roomExpenseSplitRecord);

        // Get all the roomExpenseSplitRecordList
        restRoomExpenseSplitRecordMockMvc.perform(get("/api/room-expense-split-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomExpenseSplitRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRoomExpenseSplitRecord() throws Exception {
        // Initialize the database
        roomExpenseSplitRecordRepository.saveAndFlush(roomExpenseSplitRecord);

        // Get the roomExpenseSplitRecord
        restRoomExpenseSplitRecordMockMvc.perform(get("/api/room-expense-split-records/{id}", roomExpenseSplitRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomExpenseSplitRecord.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomExpenseSplitRecord() throws Exception {
        // Get the roomExpenseSplitRecord
        restRoomExpenseSplitRecordMockMvc.perform(get("/api/room-expense-split-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomExpenseSplitRecord() throws Exception {
        // Initialize the database
        roomExpenseSplitRecordRepository.saveAndFlush(roomExpenseSplitRecord);

        int databaseSizeBeforeUpdate = roomExpenseSplitRecordRepository.findAll().size();

        // Update the roomExpenseSplitRecord
        RoomExpenseSplitRecord updatedRoomExpenseSplitRecord = roomExpenseSplitRecordRepository.findById(roomExpenseSplitRecord.getId()).get();
        // Disconnect from session so that the updates on updatedRoomExpenseSplitRecord are not directly saved in db
        em.detach(updatedRoomExpenseSplitRecord);
        updatedRoomExpenseSplitRecord
            .date(UPDATED_DATE)
            .state(UPDATED_STATE);
        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO = roomExpenseSplitRecordMapper.toDto(updatedRoomExpenseSplitRecord);

        restRoomExpenseSplitRecordMockMvc.perform(put("/api/room-expense-split-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitRecordDTO)))
            .andExpect(status().isOk());

        // Validate the RoomExpenseSplitRecord in the database
        List<RoomExpenseSplitRecord> roomExpenseSplitRecordList = roomExpenseSplitRecordRepository.findAll();
        assertThat(roomExpenseSplitRecordList).hasSize(databaseSizeBeforeUpdate);
        RoomExpenseSplitRecord testRoomExpenseSplitRecord = roomExpenseSplitRecordList.get(roomExpenseSplitRecordList.size() - 1);
        assertThat(testRoomExpenseSplitRecord.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRoomExpenseSplitRecord.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomExpenseSplitRecord() throws Exception {
        int databaseSizeBeforeUpdate = roomExpenseSplitRecordRepository.findAll().size();

        // Create the RoomExpenseSplitRecord
        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO = roomExpenseSplitRecordMapper.toDto(roomExpenseSplitRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomExpenseSplitRecordMockMvc.perform(put("/api/room-expense-split-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomExpenseSplitRecord in the database
        List<RoomExpenseSplitRecord> roomExpenseSplitRecordList = roomExpenseSplitRecordRepository.findAll();
        assertThat(roomExpenseSplitRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoomExpenseSplitRecord() throws Exception {
        // Initialize the database
        roomExpenseSplitRecordRepository.saveAndFlush(roomExpenseSplitRecord);

        int databaseSizeBeforeDelete = roomExpenseSplitRecordRepository.findAll().size();

        // Delete the roomExpenseSplitRecord
        restRoomExpenseSplitRecordMockMvc.perform(delete("/api/room-expense-split-records/{id}", roomExpenseSplitRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoomExpenseSplitRecord> roomExpenseSplitRecordList = roomExpenseSplitRecordRepository.findAll();
        assertThat(roomExpenseSplitRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomExpenseSplitRecord.class);
        RoomExpenseSplitRecord roomExpenseSplitRecord1 = new RoomExpenseSplitRecord();
        roomExpenseSplitRecord1.setId(1L);
        RoomExpenseSplitRecord roomExpenseSplitRecord2 = new RoomExpenseSplitRecord();
        roomExpenseSplitRecord2.setId(roomExpenseSplitRecord1.getId());
        assertThat(roomExpenseSplitRecord1).isEqualTo(roomExpenseSplitRecord2);
        roomExpenseSplitRecord2.setId(2L);
        assertThat(roomExpenseSplitRecord1).isNotEqualTo(roomExpenseSplitRecord2);
        roomExpenseSplitRecord1.setId(null);
        assertThat(roomExpenseSplitRecord1).isNotEqualTo(roomExpenseSplitRecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomExpenseSplitRecordDTO.class);
        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO1 = new RoomExpenseSplitRecordDTO();
        roomExpenseSplitRecordDTO1.setId(1L);
        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO2 = new RoomExpenseSplitRecordDTO();
        assertThat(roomExpenseSplitRecordDTO1).isNotEqualTo(roomExpenseSplitRecordDTO2);
        roomExpenseSplitRecordDTO2.setId(roomExpenseSplitRecordDTO1.getId());
        assertThat(roomExpenseSplitRecordDTO1).isEqualTo(roomExpenseSplitRecordDTO2);
        roomExpenseSplitRecordDTO2.setId(2L);
        assertThat(roomExpenseSplitRecordDTO1).isNotEqualTo(roomExpenseSplitRecordDTO2);
        roomExpenseSplitRecordDTO1.setId(null);
        assertThat(roomExpenseSplitRecordDTO1).isNotEqualTo(roomExpenseSplitRecordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomExpenseSplitRecordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomExpenseSplitRecordMapper.fromId(null)).isNull();
    }
}
