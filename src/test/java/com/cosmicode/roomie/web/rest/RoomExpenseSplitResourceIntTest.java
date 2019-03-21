package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;
import com.cosmicode.roomie.domain.RoomExpenseSplit;
import com.cosmicode.roomie.repository.RoomExpenseSplitRepository;
import com.cosmicode.roomie.service.RoomExpenseSplitService;
import com.cosmicode.roomie.service.dto.RoomExpenseSplitDTO;
import com.cosmicode.roomie.service.mapper.RoomExpenseSplitMapper;
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
import java.util.List;

import static com.cosmicode.roomie.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoomExpenseSplitResource REST controller.
 *
 * @see RoomExpenseSplitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomExpenseSplitResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private RoomExpenseSplitRepository roomExpenseSplitRepository;

    @Autowired
    private RoomExpenseSplitMapper roomExpenseSplitMapper;

    @Autowired
    private RoomExpenseSplitService roomExpenseSplitService;

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

    private MockMvc restRoomExpenseSplitMockMvc;

    private RoomExpenseSplit roomExpenseSplit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomExpenseSplitResource roomExpenseSplitResource = new RoomExpenseSplitResource(roomExpenseSplitService);
        this.restRoomExpenseSplitMockMvc = MockMvcBuilders.standaloneSetup(roomExpenseSplitResource)
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
    public static RoomExpenseSplit createEntity(EntityManager em) {
        RoomExpenseSplit roomExpenseSplit = new RoomExpenseSplit()
            .amount(DEFAULT_AMOUNT);
        return roomExpenseSplit;
    }

    @Before
    public void initTest() {
        roomExpenseSplit = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomExpenseSplit() throws Exception {
        int databaseSizeBeforeCreate = roomExpenseSplitRepository.findAll().size();

        // Create the RoomExpenseSplit
        RoomExpenseSplitDTO roomExpenseSplitDTO = roomExpenseSplitMapper.toDto(roomExpenseSplit);
        restRoomExpenseSplitMockMvc.perform(post("/api/room-expense-splits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomExpenseSplit in the database
        List<RoomExpenseSplit> roomExpenseSplitList = roomExpenseSplitRepository.findAll();
        assertThat(roomExpenseSplitList).hasSize(databaseSizeBeforeCreate + 1);
        RoomExpenseSplit testRoomExpenseSplit = roomExpenseSplitList.get(roomExpenseSplitList.size() - 1);
        assertThat(testRoomExpenseSplit.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createRoomExpenseSplitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomExpenseSplitRepository.findAll().size();

        // Create the RoomExpenseSplit with an existing ID
        roomExpenseSplit.setId(1L);
        RoomExpenseSplitDTO roomExpenseSplitDTO = roomExpenseSplitMapper.toDto(roomExpenseSplit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomExpenseSplitMockMvc.perform(post("/api/room-expense-splits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomExpenseSplit in the database
        List<RoomExpenseSplit> roomExpenseSplitList = roomExpenseSplitRepository.findAll();
        assertThat(roomExpenseSplitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomExpenseSplitRepository.findAll().size();
        // set the field null
        roomExpenseSplit.setAmount(null);

        // Create the RoomExpenseSplit, which fails.
        RoomExpenseSplitDTO roomExpenseSplitDTO = roomExpenseSplitMapper.toDto(roomExpenseSplit);

        restRoomExpenseSplitMockMvc.perform(post("/api/room-expense-splits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitDTO)))
            .andExpect(status().isBadRequest());

        List<RoomExpenseSplit> roomExpenseSplitList = roomExpenseSplitRepository.findAll();
        assertThat(roomExpenseSplitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomExpenseSplits() throws Exception {
        // Initialize the database
        roomExpenseSplitRepository.saveAndFlush(roomExpenseSplit);

        // Get all the roomExpenseSplitList
        restRoomExpenseSplitMockMvc.perform(get("/api/room-expense-splits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomExpenseSplit.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getRoomExpenseSplit() throws Exception {
        // Initialize the database
        roomExpenseSplitRepository.saveAndFlush(roomExpenseSplit);

        // Get the roomExpenseSplit
        restRoomExpenseSplitMockMvc.perform(get("/api/room-expense-splits/{id}", roomExpenseSplit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomExpenseSplit.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomExpenseSplit() throws Exception {
        // Get the roomExpenseSplit
        restRoomExpenseSplitMockMvc.perform(get("/api/room-expense-splits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomExpenseSplit() throws Exception {
        // Initialize the database
        roomExpenseSplitRepository.saveAndFlush(roomExpenseSplit);

        int databaseSizeBeforeUpdate = roomExpenseSplitRepository.findAll().size();

        // Update the roomExpenseSplit
        RoomExpenseSplit updatedRoomExpenseSplit = roomExpenseSplitRepository.findById(roomExpenseSplit.getId()).get();
        // Disconnect from session so that the updates on updatedRoomExpenseSplit are not directly saved in db
        em.detach(updatedRoomExpenseSplit);
        updatedRoomExpenseSplit
            .amount(UPDATED_AMOUNT);
        RoomExpenseSplitDTO roomExpenseSplitDTO = roomExpenseSplitMapper.toDto(updatedRoomExpenseSplit);

        restRoomExpenseSplitMockMvc.perform(put("/api/room-expense-splits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitDTO)))
            .andExpect(status().isOk());

        // Validate the RoomExpenseSplit in the database
        List<RoomExpenseSplit> roomExpenseSplitList = roomExpenseSplitRepository.findAll();
        assertThat(roomExpenseSplitList).hasSize(databaseSizeBeforeUpdate);
        RoomExpenseSplit testRoomExpenseSplit = roomExpenseSplitList.get(roomExpenseSplitList.size() - 1);
        assertThat(testRoomExpenseSplit.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomExpenseSplit() throws Exception {
        int databaseSizeBeforeUpdate = roomExpenseSplitRepository.findAll().size();

        // Create the RoomExpenseSplit
        RoomExpenseSplitDTO roomExpenseSplitDTO = roomExpenseSplitMapper.toDto(roomExpenseSplit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomExpenseSplitMockMvc.perform(put("/api/room-expense-splits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomExpenseSplitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomExpenseSplit in the database
        List<RoomExpenseSplit> roomExpenseSplitList = roomExpenseSplitRepository.findAll();
        assertThat(roomExpenseSplitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoomExpenseSplit() throws Exception {
        // Initialize the database
        roomExpenseSplitRepository.saveAndFlush(roomExpenseSplit);

        int databaseSizeBeforeDelete = roomExpenseSplitRepository.findAll().size();

        // Delete the roomExpenseSplit
        restRoomExpenseSplitMockMvc.perform(delete("/api/room-expense-splits/{id}", roomExpenseSplit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoomExpenseSplit> roomExpenseSplitList = roomExpenseSplitRepository.findAll();
        assertThat(roomExpenseSplitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomExpenseSplit.class);
        RoomExpenseSplit roomExpenseSplit1 = new RoomExpenseSplit();
        roomExpenseSplit1.setId(1L);
        RoomExpenseSplit roomExpenseSplit2 = new RoomExpenseSplit();
        roomExpenseSplit2.setId(roomExpenseSplit1.getId());
        assertThat(roomExpenseSplit1).isEqualTo(roomExpenseSplit2);
        roomExpenseSplit2.setId(2L);
        assertThat(roomExpenseSplit1).isNotEqualTo(roomExpenseSplit2);
        roomExpenseSplit1.setId(null);
        assertThat(roomExpenseSplit1).isNotEqualTo(roomExpenseSplit2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomExpenseSplitDTO.class);
        RoomExpenseSplitDTO roomExpenseSplitDTO1 = new RoomExpenseSplitDTO();
        roomExpenseSplitDTO1.setId(1L);
        RoomExpenseSplitDTO roomExpenseSplitDTO2 = new RoomExpenseSplitDTO();
        assertThat(roomExpenseSplitDTO1).isNotEqualTo(roomExpenseSplitDTO2);
        roomExpenseSplitDTO2.setId(roomExpenseSplitDTO1.getId());
        assertThat(roomExpenseSplitDTO1).isEqualTo(roomExpenseSplitDTO2);
        roomExpenseSplitDTO2.setId(2L);
        assertThat(roomExpenseSplitDTO1).isNotEqualTo(roomExpenseSplitDTO2);
        roomExpenseSplitDTO1.setId(null);
        assertThat(roomExpenseSplitDTO1).isNotEqualTo(roomExpenseSplitDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomExpenseSplitMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomExpenseSplitMapper.fromId(null)).isNull();
    }
}
