package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.RoomPicture;
import com.cosmicode.roomie.repository.RoomPictureRepository;
import com.cosmicode.roomie.repository.search.RoomPictureSearchRepository;
import com.cosmicode.roomie.service.RoomPictureService;
import com.cosmicode.roomie.service.dto.RoomPictureDTO;
import com.cosmicode.roomie.service.mapper.RoomPictureMapper;
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
 * Test class for the RoomPictureResource REST controller.
 *
 * @see RoomPictureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomPictureResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MAIN = false;
    private static final Boolean UPDATED_IS_MAIN = true;

    @Autowired
    private RoomPictureRepository roomPictureRepository;

    @Autowired
    private RoomPictureMapper roomPictureMapper;

    @Autowired
    private RoomPictureService roomPictureService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.RoomPictureSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoomPictureSearchRepository mockRoomPictureSearchRepository;

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

    private MockMvc restRoomPictureMockMvc;

    private RoomPicture roomPicture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomPictureResource roomPictureResource = new RoomPictureResource(roomPictureService);
        this.restRoomPictureMockMvc = MockMvcBuilders.standaloneSetup(roomPictureResource)
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
    public static RoomPicture createEntity(EntityManager em) {
        RoomPicture roomPicture = new RoomPicture()
            .url(DEFAULT_URL)
            .isMain(DEFAULT_IS_MAIN);
        return roomPicture;
    }

    @Before
    public void initTest() {
        roomPicture = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomPicture() throws Exception {
        int databaseSizeBeforeCreate = roomPictureRepository.findAll().size();

        // Create the RoomPicture
        RoomPictureDTO roomPictureDTO = roomPictureMapper.toDto(roomPicture);
        restRoomPictureMockMvc.perform(post("/api/room-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomPictureDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomPicture in the database
        List<RoomPicture> roomPictureList = roomPictureRepository.findAll();
        assertThat(roomPictureList).hasSize(databaseSizeBeforeCreate + 1);
        RoomPicture testRoomPicture = roomPictureList.get(roomPictureList.size() - 1);
        assertThat(testRoomPicture.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testRoomPicture.isIsMain()).isEqualTo(DEFAULT_IS_MAIN);

        // Validate the RoomPicture in Elasticsearch
        verify(mockRoomPictureSearchRepository, times(1)).save(testRoomPicture);
    }

    @Test
    @Transactional
    public void createRoomPictureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomPictureRepository.findAll().size();

        // Create the RoomPicture with an existing ID
        roomPicture.setId(1L);
        RoomPictureDTO roomPictureDTO = roomPictureMapper.toDto(roomPicture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomPictureMockMvc.perform(post("/api/room-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomPictureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomPicture in the database
        List<RoomPicture> roomPictureList = roomPictureRepository.findAll();
        assertThat(roomPictureList).hasSize(databaseSizeBeforeCreate);

        // Validate the RoomPicture in Elasticsearch
        verify(mockRoomPictureSearchRepository, times(0)).save(roomPicture);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomPictureRepository.findAll().size();
        // set the field null
        roomPicture.setUrl(null);

        // Create the RoomPicture, which fails.
        RoomPictureDTO roomPictureDTO = roomPictureMapper.toDto(roomPicture);

        restRoomPictureMockMvc.perform(post("/api/room-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomPictureDTO)))
            .andExpect(status().isBadRequest());

        List<RoomPicture> roomPictureList = roomPictureRepository.findAll();
        assertThat(roomPictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsMainIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomPictureRepository.findAll().size();
        // set the field null
        roomPicture.setIsMain(null);

        // Create the RoomPicture, which fails.
        RoomPictureDTO roomPictureDTO = roomPictureMapper.toDto(roomPicture);

        restRoomPictureMockMvc.perform(post("/api/room-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomPictureDTO)))
            .andExpect(status().isBadRequest());

        List<RoomPicture> roomPictureList = roomPictureRepository.findAll();
        assertThat(roomPictureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomPictures() throws Exception {
        // Initialize the database
        roomPictureRepository.saveAndFlush(roomPicture);

        // Get all the roomPictureList
        restRoomPictureMockMvc.perform(get("/api/room-pictures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomPicture.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].isMain").value(hasItem(DEFAULT_IS_MAIN.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getRoomPicture() throws Exception {
        // Initialize the database
        roomPictureRepository.saveAndFlush(roomPicture);

        // Get the roomPicture
        restRoomPictureMockMvc.perform(get("/api/room-pictures/{id}", roomPicture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomPicture.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.isMain").value(DEFAULT_IS_MAIN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomPicture() throws Exception {
        // Get the roomPicture
        restRoomPictureMockMvc.perform(get("/api/room-pictures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomPicture() throws Exception {
        // Initialize the database
        roomPictureRepository.saveAndFlush(roomPicture);

        int databaseSizeBeforeUpdate = roomPictureRepository.findAll().size();

        // Update the roomPicture
        RoomPicture updatedRoomPicture = roomPictureRepository.findById(roomPicture.getId()).get();
        // Disconnect from session so that the updates on updatedRoomPicture are not directly saved in db
        em.detach(updatedRoomPicture);
        updatedRoomPicture
            .url(UPDATED_URL)
            .isMain(UPDATED_IS_MAIN);
        RoomPictureDTO roomPictureDTO = roomPictureMapper.toDto(updatedRoomPicture);

        restRoomPictureMockMvc.perform(put("/api/room-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomPictureDTO)))
            .andExpect(status().isOk());

        // Validate the RoomPicture in the database
        List<RoomPicture> roomPictureList = roomPictureRepository.findAll();
        assertThat(roomPictureList).hasSize(databaseSizeBeforeUpdate);
        RoomPicture testRoomPicture = roomPictureList.get(roomPictureList.size() - 1);
        assertThat(testRoomPicture.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testRoomPicture.isIsMain()).isEqualTo(UPDATED_IS_MAIN);

        // Validate the RoomPicture in Elasticsearch
        verify(mockRoomPictureSearchRepository, times(1)).save(testRoomPicture);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomPicture() throws Exception {
        int databaseSizeBeforeUpdate = roomPictureRepository.findAll().size();

        // Create the RoomPicture
        RoomPictureDTO roomPictureDTO = roomPictureMapper.toDto(roomPicture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomPictureMockMvc.perform(put("/api/room-pictures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomPictureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomPicture in the database
        List<RoomPicture> roomPictureList = roomPictureRepository.findAll();
        assertThat(roomPictureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RoomPicture in Elasticsearch
        verify(mockRoomPictureSearchRepository, times(0)).save(roomPicture);
    }

    @Test
    @Transactional
    public void deleteRoomPicture() throws Exception {
        // Initialize the database
        roomPictureRepository.saveAndFlush(roomPicture);

        int databaseSizeBeforeDelete = roomPictureRepository.findAll().size();

        // Get the roomPicture
        restRoomPictureMockMvc.perform(delete("/api/room-pictures/{id}", roomPicture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoomPicture> roomPictureList = roomPictureRepository.findAll();
        assertThat(roomPictureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RoomPicture in Elasticsearch
        verify(mockRoomPictureSearchRepository, times(1)).deleteById(roomPicture.getId());
    }

    @Test
    @Transactional
    public void searchRoomPicture() throws Exception {
        // Initialize the database
        roomPictureRepository.saveAndFlush(roomPicture);
        when(mockRoomPictureSearchRepository.search(queryStringQuery("id:" + roomPicture.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(roomPicture), PageRequest.of(0, 1), 1));
        // Search the roomPicture
        restRoomPictureMockMvc.perform(get("/api/_search/room-pictures?query=id:" + roomPicture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomPicture.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isMain").value(hasItem(DEFAULT_IS_MAIN.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomPicture.class);
        RoomPicture roomPicture1 = new RoomPicture();
        roomPicture1.setId(1L);
        RoomPicture roomPicture2 = new RoomPicture();
        roomPicture2.setId(roomPicture1.getId());
        assertThat(roomPicture1).isEqualTo(roomPicture2);
        roomPicture2.setId(2L);
        assertThat(roomPicture1).isNotEqualTo(roomPicture2);
        roomPicture1.setId(null);
        assertThat(roomPicture1).isNotEqualTo(roomPicture2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomPictureDTO.class);
        RoomPictureDTO roomPictureDTO1 = new RoomPictureDTO();
        roomPictureDTO1.setId(1L);
        RoomPictureDTO roomPictureDTO2 = new RoomPictureDTO();
        assertThat(roomPictureDTO1).isNotEqualTo(roomPictureDTO2);
        roomPictureDTO2.setId(roomPictureDTO1.getId());
        assertThat(roomPictureDTO1).isEqualTo(roomPictureDTO2);
        roomPictureDTO2.setId(2L);
        assertThat(roomPictureDTO1).isNotEqualTo(roomPictureDTO2);
        roomPictureDTO1.setId(null);
        assertThat(roomPictureDTO1).isNotEqualTo(roomPictureDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomPictureMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomPictureMapper.fromId(null)).isNull();
    }
}
