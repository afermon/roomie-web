package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.UserReport;
import com.cosmicode.roomie.repository.UserReportRepository;
import com.cosmicode.roomie.repository.search.UserReportSearchRepository;
import com.cosmicode.roomie.service.UserReportService;
import com.cosmicode.roomie.service.dto.UserReportDTO;
import com.cosmicode.roomie.service.mapper.UserReportMapper;
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

import com.cosmicode.roomie.domain.enumeration.ReportType;
/**
 * Test class for the UserReportResource REST controller.
 *
 * @see UserReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class UserReportResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ReportType DEFAULT_TYPE = ReportType.USER;
    private static final ReportType UPDATED_TYPE = ReportType.ROOM;

    @Autowired
    private UserReportRepository userReportRepository;

    @Autowired
    private UserReportMapper userReportMapper;

    @Autowired
    private UserReportService userReportService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.UserReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserReportSearchRepository mockUserReportSearchRepository;

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

    private MockMvc restUserReportMockMvc;

    private UserReport userReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserReportResource userReportResource = new UserReportResource(userReportService);
        this.restUserReportMockMvc = MockMvcBuilders.standaloneSetup(userReportResource)
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
    public static UserReport createEntity(EntityManager em) {
        UserReport userReport = new UserReport()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE);
        return userReport;
    }

    @Before
    public void initTest() {
        userReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserReport() throws Exception {
        int databaseSizeBeforeCreate = userReportRepository.findAll().size();

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);
        restUserReportMockMvc.perform(post("/api/user-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReportDTO)))
            .andExpect(status().isCreated());

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll();
        assertThat(userReportList).hasSize(databaseSizeBeforeCreate + 1);
        UserReport testUserReport = userReportList.get(userReportList.size() - 1);
        assertThat(testUserReport.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testUserReport.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserReport.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the UserReport in Elasticsearch
        verify(mockUserReportSearchRepository, times(1)).save(testUserReport);
    }

    @Test
    @Transactional
    public void createUserReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userReportRepository.findAll().size();

        // Create the UserReport with an existing ID
        userReport.setId(1L);
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserReportMockMvc.perform(post("/api/user-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll();
        assertThat(userReportList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserReport in Elasticsearch
        verify(mockUserReportSearchRepository, times(0)).save(userReport);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReportRepository.findAll().size();
        // set the field null
        userReport.setDate(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc.perform(post("/api/user-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        List<UserReport> userReportList = userReportRepository.findAll();
        assertThat(userReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReportRepository.findAll().size();
        // set the field null
        userReport.setDescription(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc.perform(post("/api/user-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        List<UserReport> userReportList = userReportRepository.findAll();
        assertThat(userReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserReports() throws Exception {
        // Initialize the database
        userReportRepository.saveAndFlush(userReport);

        // Get all the userReportList
        restUserReportMockMvc.perform(get("/api/user-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getUserReport() throws Exception {
        // Initialize the database
        userReportRepository.saveAndFlush(userReport);

        // Get the userReport
        restUserReportMockMvc.perform(get("/api/user-reports/{id}", userReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userReport.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserReport() throws Exception {
        // Get the userReport
        restUserReportMockMvc.perform(get("/api/user-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserReport() throws Exception {
        // Initialize the database
        userReportRepository.saveAndFlush(userReport);

        int databaseSizeBeforeUpdate = userReportRepository.findAll().size();

        // Update the userReport
        UserReport updatedUserReport = userReportRepository.findById(userReport.getId()).get();
        // Disconnect from session so that the updates on updatedUserReport are not directly saved in db
        em.detach(updatedUserReport);
        updatedUserReport
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE);
        UserReportDTO userReportDTO = userReportMapper.toDto(updatedUserReport);

        restUserReportMockMvc.perform(put("/api/user-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReportDTO)))
            .andExpect(status().isOk());

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        UserReport testUserReport = userReportList.get(userReportList.size() - 1);
        assertThat(testUserReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserReport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserReport.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the UserReport in Elasticsearch
        verify(mockUserReportSearchRepository, times(1)).save(testUserReport);
    }

    @Test
    @Transactional
    public void updateNonExistingUserReport() throws Exception {
        int databaseSizeBeforeUpdate = userReportRepository.findAll().size();

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserReportMockMvc.perform(put("/api/user-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserReport in Elasticsearch
        verify(mockUserReportSearchRepository, times(0)).save(userReport);
    }

    @Test
    @Transactional
    public void deleteUserReport() throws Exception {
        // Initialize the database
        userReportRepository.saveAndFlush(userReport);

        int databaseSizeBeforeDelete = userReportRepository.findAll().size();

        // Delete the userReport
        restUserReportMockMvc.perform(delete("/api/user-reports/{id}", userReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserReport> userReportList = userReportRepository.findAll();
        assertThat(userReportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserReport in Elasticsearch
        verify(mockUserReportSearchRepository, times(1)).deleteById(userReport.getId());
    }

    @Test
    @Transactional
    public void searchUserReport() throws Exception {
        // Initialize the database
        userReportRepository.saveAndFlush(userReport);
        when(mockUserReportSearchRepository.search(queryStringQuery("id:" + userReport.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userReport), PageRequest.of(0, 1), 1));
        // Search the userReport
        restUserReportMockMvc.perform(get("/api/_search/user-reports?query=id:" + userReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReport.class);
        UserReport userReport1 = new UserReport();
        userReport1.setId(1L);
        UserReport userReport2 = new UserReport();
        userReport2.setId(userReport1.getId());
        assertThat(userReport1).isEqualTo(userReport2);
        userReport2.setId(2L);
        assertThat(userReport1).isNotEqualTo(userReport2);
        userReport1.setId(null);
        assertThat(userReport1).isNotEqualTo(userReport2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserReportDTO.class);
        UserReportDTO userReportDTO1 = new UserReportDTO();
        userReportDTO1.setId(1L);
        UserReportDTO userReportDTO2 = new UserReportDTO();
        assertThat(userReportDTO1).isNotEqualTo(userReportDTO2);
        userReportDTO2.setId(userReportDTO1.getId());
        assertThat(userReportDTO1).isEqualTo(userReportDTO2);
        userReportDTO2.setId(2L);
        assertThat(userReportDTO1).isNotEqualTo(userReportDTO2);
        userReportDTO1.setId(null);
        assertThat(userReportDTO1).isNotEqualTo(userReportDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userReportMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userReportMapper.fromId(null)).isNull();
    }
}
