package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.Roomie;
import com.cosmicode.roomie.repository.RoomieRepository;
import com.cosmicode.roomie.repository.search.RoomieSearchRepository;
import com.cosmicode.roomie.service.RoomieService;
import com.cosmicode.roomie.service.dto.RoomieDTO;
import com.cosmicode.roomie.service.mapper.RoomieMapper;
import com.cosmicode.roomie.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.cosmicode.roomie.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cosmicode.roomie.domain.enumeration.Gender;
/**
 * Test class for the RoomieResource REST controller.
 *
 * @see RoomieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomieResourceIntTest {

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BIOGRAPHY = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHY = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_PHONE = "+1$(1).-.7270 -307";
    private static final String UPDATED_PHONE = "43551";

    private static final String DEFAULT_MOBILE_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_DEVICE_ID = "BBBBBBBBBB";

    @Autowired
    private RoomieRepository roomieRepository;

    @Mock
    private RoomieRepository roomieRepositoryMock;

    @Autowired
    private RoomieMapper roomieMapper;

    @Mock
    private RoomieService roomieServiceMock;

    @Autowired
    private RoomieService roomieService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.RoomieSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoomieSearchRepository mockRoomieSearchRepository;

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

    private MockMvc restRoomieMockMvc;

    private Roomie roomie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomieResource roomieResource = new RoomieResource(roomieService);
        this.restRoomieMockMvc = MockMvcBuilders.standaloneSetup(roomieResource)
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
    public static Roomie createEntity(EntityManager em) {
        Roomie roomie = new Roomie()
            .birthDate(DEFAULT_BIRTH_DATE)
            .biography(DEFAULT_BIOGRAPHY)
            .picture(DEFAULT_PICTURE)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .mobileDeviceID(DEFAULT_MOBILE_DEVICE_ID);
        return roomie;
    }

    @Before
    public void initTest() {
        roomie = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomie() throws Exception {
        int databaseSizeBeforeCreate = roomieRepository.findAll().size();

        // Create the Roomie
        RoomieDTO roomieDTO = roomieMapper.toDto(roomie);
        restRoomieMockMvc.perform(post("/api/roomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieDTO)))
            .andExpect(status().isCreated());

        // Validate the Roomie in the database
        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeCreate + 1);
        Roomie testRoomie = roomieList.get(roomieList.size() - 1);
        assertThat(testRoomie.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testRoomie.getBiography()).isEqualTo(DEFAULT_BIOGRAPHY);
        assertThat(testRoomie.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testRoomie.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testRoomie.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testRoomie.getMobileDeviceID()).isEqualTo(DEFAULT_MOBILE_DEVICE_ID);

        // Validate the Roomie in Elasticsearch
        verify(mockRoomieSearchRepository, times(1)).save(testRoomie);
    }

    @Test
    @Transactional
    public void createRoomieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomieRepository.findAll().size();

        // Create the Roomie with an existing ID
        roomie.setId(1L);
        RoomieDTO roomieDTO = roomieMapper.toDto(roomie);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomieMockMvc.perform(post("/api/roomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roomie in the database
        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeCreate);

        // Validate the Roomie in Elasticsearch
        verify(mockRoomieSearchRepository, times(0)).save(roomie);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomieRepository.findAll().size();
        // set the field null
        roomie.setBirthDate(null);

        // Create the Roomie, which fails.
        RoomieDTO roomieDTO = roomieMapper.toDto(roomie);

        restRoomieMockMvc.perform(post("/api/roomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieDTO)))
            .andExpect(status().isBadRequest());

        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPictureIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomieRepository.findAll().size();
        // set the field null
        roomie.setPicture(null);

        // Create the Roomie, which fails.
        RoomieDTO roomieDTO = roomieMapper.toDto(roomie);

        restRoomieMockMvc.perform(post("/api/roomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieDTO)))
            .andExpect(status().isBadRequest());

        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomieRepository.findAll().size();
        // set the field null
        roomie.setGender(null);

        // Create the Roomie, which fails.
        RoomieDTO roomieDTO = roomieMapper.toDto(roomie);

        restRoomieMockMvc.perform(post("/api/roomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieDTO)))
            .andExpect(status().isBadRequest());

        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMobileDeviceIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomieRepository.findAll().size();
        // set the field null
        roomie.setMobileDeviceID(null);

        // Create the Roomie, which fails.
        RoomieDTO roomieDTO = roomieMapper.toDto(roomie);

        restRoomieMockMvc.perform(post("/api/roomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieDTO)))
            .andExpect(status().isBadRequest());

        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomies() throws Exception {
        // Initialize the database
        roomieRepository.saveAndFlush(roomie);

        // Get all the roomieList
        restRoomieMockMvc.perform(get("/api/roomies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomie.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].biography").value(hasItem(DEFAULT_BIOGRAPHY.toString())))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].mobileDeviceID").value(hasItem(DEFAULT_MOBILE_DEVICE_ID.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllRoomiesWithEagerRelationshipsIsEnabled() throws Exception {
        RoomieResource roomieResource = new RoomieResource(roomieServiceMock);
        when(roomieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restRoomieMockMvc = MockMvcBuilders.standaloneSetup(roomieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRoomieMockMvc.perform(get("/api/roomies?eagerload=true"))
        .andExpect(status().isOk());

        verify(roomieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllRoomiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        RoomieResource roomieResource = new RoomieResource(roomieServiceMock);
            when(roomieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restRoomieMockMvc = MockMvcBuilders.standaloneSetup(roomieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRoomieMockMvc.perform(get("/api/roomies?eagerload=true"))
        .andExpect(status().isOk());

            verify(roomieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getRoomie() throws Exception {
        // Initialize the database
        roomieRepository.saveAndFlush(roomie);

        // Get the roomie
        restRoomieMockMvc.perform(get("/api/roomies/{id}", roomie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomie.getId().intValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.biography").value(DEFAULT_BIOGRAPHY.toString()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.mobileDeviceID").value(DEFAULT_MOBILE_DEVICE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomie() throws Exception {
        // Get the roomie
        restRoomieMockMvc.perform(get("/api/roomies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomie() throws Exception {
        // Initialize the database
        roomieRepository.saveAndFlush(roomie);

        int databaseSizeBeforeUpdate = roomieRepository.findAll().size();

        // Update the roomie
        Roomie updatedRoomie = roomieRepository.findById(roomie.getId()).get();
        // Disconnect from session so that the updates on updatedRoomie are not directly saved in db
        em.detach(updatedRoomie);
        updatedRoomie
            .birthDate(UPDATED_BIRTH_DATE)
            .biography(UPDATED_BIOGRAPHY)
            .picture(UPDATED_PICTURE)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .mobileDeviceID(UPDATED_MOBILE_DEVICE_ID);
        RoomieDTO roomieDTO = roomieMapper.toDto(updatedRoomie);

        restRoomieMockMvc.perform(put("/api/roomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieDTO)))
            .andExpect(status().isOk());

        // Validate the Roomie in the database
        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeUpdate);
        Roomie testRoomie = roomieList.get(roomieList.size() - 1);
        assertThat(testRoomie.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testRoomie.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testRoomie.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testRoomie.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testRoomie.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRoomie.getMobileDeviceID()).isEqualTo(UPDATED_MOBILE_DEVICE_ID);

        // Validate the Roomie in Elasticsearch
        verify(mockRoomieSearchRepository, times(1)).save(testRoomie);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomie() throws Exception {
        int databaseSizeBeforeUpdate = roomieRepository.findAll().size();

        // Create the Roomie
        RoomieDTO roomieDTO = roomieMapper.toDto(roomie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomieMockMvc.perform(put("/api/roomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roomie in the database
        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Roomie in Elasticsearch
        verify(mockRoomieSearchRepository, times(0)).save(roomie);
    }

    @Test
    @Transactional
    public void deleteRoomie() throws Exception {
        // Initialize the database
        roomieRepository.saveAndFlush(roomie);

        int databaseSizeBeforeDelete = roomieRepository.findAll().size();

        // Get the roomie
        restRoomieMockMvc.perform(delete("/api/roomies/{id}", roomie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Roomie> roomieList = roomieRepository.findAll();
        assertThat(roomieList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Roomie in Elasticsearch
        verify(mockRoomieSearchRepository, times(1)).deleteById(roomie.getId());
    }

    @Test
    @Transactional
    public void searchRoomie() throws Exception {
        // Initialize the database
        roomieRepository.saveAndFlush(roomie);
        when(mockRoomieSearchRepository.search(queryStringQuery("id:" + roomie.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(roomie), PageRequest.of(0, 1), 1));
        // Search the roomie
        restRoomieMockMvc.perform(get("/api/_search/roomies?query=id:" + roomie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomie.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].biography").value(hasItem(DEFAULT_BIOGRAPHY)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].mobileDeviceID").value(hasItem(DEFAULT_MOBILE_DEVICE_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roomie.class);
        Roomie roomie1 = new Roomie();
        roomie1.setId(1L);
        Roomie roomie2 = new Roomie();
        roomie2.setId(roomie1.getId());
        assertThat(roomie1).isEqualTo(roomie2);
        roomie2.setId(2L);
        assertThat(roomie1).isNotEqualTo(roomie2);
        roomie1.setId(null);
        assertThat(roomie1).isNotEqualTo(roomie2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomieDTO.class);
        RoomieDTO roomieDTO1 = new RoomieDTO();
        roomieDTO1.setId(1L);
        RoomieDTO roomieDTO2 = new RoomieDTO();
        assertThat(roomieDTO1).isNotEqualTo(roomieDTO2);
        roomieDTO2.setId(roomieDTO1.getId());
        assertThat(roomieDTO1).isEqualTo(roomieDTO2);
        roomieDTO2.setId(2L);
        assertThat(roomieDTO1).isNotEqualTo(roomieDTO2);
        roomieDTO1.setId(null);
        assertThat(roomieDTO1).isNotEqualTo(roomieDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomieMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomieMapper.fromId(null)).isNull();
    }
}
