package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.RoomFeature;
import com.cosmicode.roomie.repository.RoomFeatureRepository;
import com.cosmicode.roomie.repository.search.RoomFeatureSearchRepository;
import com.cosmicode.roomie.service.RoomFeatureService;
import com.cosmicode.roomie.service.dto.RoomFeatureDTO;
import com.cosmicode.roomie.service.mapper.RoomFeatureMapper;
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

import com.cosmicode.roomie.domain.enumeration.Lang;
import com.cosmicode.roomie.domain.enumeration.FeatureType;
/**
 * Test class for the RoomFeatureResource REST controller.
 *
 * @see RoomFeatureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class RoomFeatureResourceIntTest {

    private static final Lang DEFAULT_LANG = Lang.ESP;
    private static final Lang UPDATED_LANG = Lang.ENG;

    private static final FeatureType DEFAULT_TYPE = FeatureType.AMENITIES;
    private static final FeatureType UPDATED_TYPE = FeatureType.RESTRICTIONS;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RoomFeatureRepository roomFeatureRepository;

    @Autowired
    private RoomFeatureMapper roomFeatureMapper;

    @Autowired
    private RoomFeatureService roomFeatureService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.RoomFeatureSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoomFeatureSearchRepository mockRoomFeatureSearchRepository;

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

    private MockMvc restRoomFeatureMockMvc;

    private RoomFeature roomFeature;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoomFeatureResource roomFeatureResource = new RoomFeatureResource(roomFeatureService);
        this.restRoomFeatureMockMvc = MockMvcBuilders.standaloneSetup(roomFeatureResource)
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
    public static RoomFeature createEntity(EntityManager em) {
        RoomFeature roomFeature = new RoomFeature()
            .lang(DEFAULT_LANG)
            .type(DEFAULT_TYPE)
            .name(DEFAULT_NAME)
            .icon(DEFAULT_ICON)
            .description(DEFAULT_DESCRIPTION);
        return roomFeature;
    }

    @Before
    public void initTest() {
        roomFeature = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomFeature() throws Exception {
        int databaseSizeBeforeCreate = roomFeatureRepository.findAll().size();

        // Create the RoomFeature
        RoomFeatureDTO roomFeatureDTO = roomFeatureMapper.toDto(roomFeature);
        restRoomFeatureMockMvc.perform(post("/api/room-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomFeatureDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomFeature in the database
        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeCreate + 1);
        RoomFeature testRoomFeature = roomFeatureList.get(roomFeatureList.size() - 1);
        assertThat(testRoomFeature.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testRoomFeature.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRoomFeature.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoomFeature.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testRoomFeature.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the RoomFeature in Elasticsearch
        verify(mockRoomFeatureSearchRepository, times(1)).save(testRoomFeature);
    }

    @Test
    @Transactional
    public void createRoomFeatureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomFeatureRepository.findAll().size();

        // Create the RoomFeature with an existing ID
        roomFeature.setId(1L);
        RoomFeatureDTO roomFeatureDTO = roomFeatureMapper.toDto(roomFeature);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomFeatureMockMvc.perform(post("/api/room-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomFeatureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomFeature in the database
        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeCreate);

        // Validate the RoomFeature in Elasticsearch
        verify(mockRoomFeatureSearchRepository, times(0)).save(roomFeature);
    }

    @Test
    @Transactional
    public void checkLangIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomFeatureRepository.findAll().size();
        // set the field null
        roomFeature.setLang(null);

        // Create the RoomFeature, which fails.
        RoomFeatureDTO roomFeatureDTO = roomFeatureMapper.toDto(roomFeature);

        restRoomFeatureMockMvc.perform(post("/api/room-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomFeatureDTO)))
            .andExpect(status().isBadRequest());

        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomFeatureRepository.findAll().size();
        // set the field null
        roomFeature.setType(null);

        // Create the RoomFeature, which fails.
        RoomFeatureDTO roomFeatureDTO = roomFeatureMapper.toDto(roomFeature);

        restRoomFeatureMockMvc.perform(post("/api/room-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomFeatureDTO)))
            .andExpect(status().isBadRequest());

        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomFeatureRepository.findAll().size();
        // set the field null
        roomFeature.setName(null);

        // Create the RoomFeature, which fails.
        RoomFeatureDTO roomFeatureDTO = roomFeatureMapper.toDto(roomFeature);

        restRoomFeatureMockMvc.perform(post("/api/room-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomFeatureDTO)))
            .andExpect(status().isBadRequest());

        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIconIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomFeatureRepository.findAll().size();
        // set the field null
        roomFeature.setIcon(null);

        // Create the RoomFeature, which fails.
        RoomFeatureDTO roomFeatureDTO = roomFeatureMapper.toDto(roomFeature);

        restRoomFeatureMockMvc.perform(post("/api/room-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomFeatureDTO)))
            .andExpect(status().isBadRequest());

        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoomFeatures() throws Exception {
        // Initialize the database
        roomFeatureRepository.saveAndFlush(roomFeature);

        // Get all the roomFeatureList
        restRoomFeatureMockMvc.perform(get("/api/room-features?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomFeature.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getRoomFeature() throws Exception {
        // Initialize the database
        roomFeatureRepository.saveAndFlush(roomFeature);

        // Get the roomFeature
        restRoomFeatureMockMvc.perform(get("/api/room-features/{id}", roomFeature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roomFeature.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoomFeature() throws Exception {
        // Get the roomFeature
        restRoomFeatureMockMvc.perform(get("/api/room-features/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomFeature() throws Exception {
        // Initialize the database
        roomFeatureRepository.saveAndFlush(roomFeature);

        int databaseSizeBeforeUpdate = roomFeatureRepository.findAll().size();

        // Update the roomFeature
        RoomFeature updatedRoomFeature = roomFeatureRepository.findById(roomFeature.getId()).get();
        // Disconnect from session so that the updates on updatedRoomFeature are not directly saved in db
        em.detach(updatedRoomFeature);
        updatedRoomFeature
            .lang(UPDATED_LANG)
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .icon(UPDATED_ICON)
            .description(UPDATED_DESCRIPTION);
        RoomFeatureDTO roomFeatureDTO = roomFeatureMapper.toDto(updatedRoomFeature);

        restRoomFeatureMockMvc.perform(put("/api/room-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomFeatureDTO)))
            .andExpect(status().isOk());

        // Validate the RoomFeature in the database
        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeUpdate);
        RoomFeature testRoomFeature = roomFeatureList.get(roomFeatureList.size() - 1);
        assertThat(testRoomFeature.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testRoomFeature.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRoomFeature.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoomFeature.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testRoomFeature.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the RoomFeature in Elasticsearch
        verify(mockRoomFeatureSearchRepository, times(1)).save(testRoomFeature);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomFeature() throws Exception {
        int databaseSizeBeforeUpdate = roomFeatureRepository.findAll().size();

        // Create the RoomFeature
        RoomFeatureDTO roomFeatureDTO = roomFeatureMapper.toDto(roomFeature);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomFeatureMockMvc.perform(put("/api/room-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roomFeatureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomFeature in the database
        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RoomFeature in Elasticsearch
        verify(mockRoomFeatureSearchRepository, times(0)).save(roomFeature);
    }

    @Test
    @Transactional
    public void deleteRoomFeature() throws Exception {
        // Initialize the database
        roomFeatureRepository.saveAndFlush(roomFeature);

        int databaseSizeBeforeDelete = roomFeatureRepository.findAll().size();

        // Get the roomFeature
        restRoomFeatureMockMvc.perform(delete("/api/room-features/{id}", roomFeature.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RoomFeature> roomFeatureList = roomFeatureRepository.findAll();
        assertThat(roomFeatureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RoomFeature in Elasticsearch
        verify(mockRoomFeatureSearchRepository, times(1)).deleteById(roomFeature.getId());
    }

    @Test
    @Transactional
    public void searchRoomFeature() throws Exception {
        // Initialize the database
        roomFeatureRepository.saveAndFlush(roomFeature);
        when(mockRoomFeatureSearchRepository.search(queryStringQuery("id:" + roomFeature.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(roomFeature), PageRequest.of(0, 1), 1));
        // Search the roomFeature
        restRoomFeatureMockMvc.perform(get("/api/_search/room-features?query=id:" + roomFeature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomFeature.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomFeature.class);
        RoomFeature roomFeature1 = new RoomFeature();
        roomFeature1.setId(1L);
        RoomFeature roomFeature2 = new RoomFeature();
        roomFeature2.setId(roomFeature1.getId());
        assertThat(roomFeature1).isEqualTo(roomFeature2);
        roomFeature2.setId(2L);
        assertThat(roomFeature1).isNotEqualTo(roomFeature2);
        roomFeature1.setId(null);
        assertThat(roomFeature1).isNotEqualTo(roomFeature2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomFeatureDTO.class);
        RoomFeatureDTO roomFeatureDTO1 = new RoomFeatureDTO();
        roomFeatureDTO1.setId(1L);
        RoomFeatureDTO roomFeatureDTO2 = new RoomFeatureDTO();
        assertThat(roomFeatureDTO1).isNotEqualTo(roomFeatureDTO2);
        roomFeatureDTO2.setId(roomFeatureDTO1.getId());
        assertThat(roomFeatureDTO1).isEqualTo(roomFeatureDTO2);
        roomFeatureDTO2.setId(2L);
        assertThat(roomFeatureDTO1).isNotEqualTo(roomFeatureDTO2);
        roomFeatureDTO1.setId(null);
        assertThat(roomFeatureDTO1).isNotEqualTo(roomFeatureDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roomFeatureMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roomFeatureMapper.fromId(null)).isNull();
    }
}
