package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.UserPreferences;
import com.cosmicode.roomie.repository.UserPreferencesRepository;
import com.cosmicode.roomie.repository.search.UserPreferencesSearchRepository;
import com.cosmicode.roomie.service.UserPreferencesService;
import com.cosmicode.roomie.service.dto.UserPreferencesDTO;
import com.cosmicode.roomie.service.mapper.UserPreferencesMapper;
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

import com.cosmicode.roomie.domain.enumeration.CurrencyType;
/**
 * Test class for the UserPreferencesResource REST controller.
 *
 * @see UserPreferencesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class UserPreferencesResourceIntTest {

    private static final CurrencyType DEFAULT_CURRENCY = CurrencyType.COLON;
    private static final CurrencyType UPDATED_CURRENCY = CurrencyType.DOLLAR;

    private static final Boolean DEFAULT_TODO_LIST_NOTIFICATIONS = false;
    private static final Boolean UPDATED_TODO_LIST_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_CALENDAR_NOTIFICATIONS = false;
    private static final Boolean UPDATED_CALENDAR_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_PAYMENTS_NOTIFICATIONS = false;
    private static final Boolean UPDATED_PAYMENTS_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_APPOINTMENTS_NOTIFICATIONS = false;
    private static final Boolean UPDATED_APPOINTMENTS_NOTIFICATIONS = true;

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @Autowired
    private UserPreferencesMapper userPreferencesMapper;

    @Autowired
    private UserPreferencesService userPreferencesService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.UserPreferencesSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserPreferencesSearchRepository mockUserPreferencesSearchRepository;

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

    private MockMvc restUserPreferencesMockMvc;

    private UserPreferences userPreferences;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserPreferencesResource userPreferencesResource = new UserPreferencesResource(userPreferencesService);
        this.restUserPreferencesMockMvc = MockMvcBuilders.standaloneSetup(userPreferencesResource)
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
    public static UserPreferences createEntity(EntityManager em) {
        UserPreferences userPreferences = new UserPreferences()
            .currency(DEFAULT_CURRENCY)
            .todoListNotifications(DEFAULT_TODO_LIST_NOTIFICATIONS)
            .calendarNotifications(DEFAULT_CALENDAR_NOTIFICATIONS)
            .paymentsNotifications(DEFAULT_PAYMENTS_NOTIFICATIONS)
            .appointmentsNotifications(DEFAULT_APPOINTMENTS_NOTIFICATIONS);
        return userPreferences;
    }

    @Before
    public void initTest() {
        userPreferences = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserPreferences() throws Exception {
        int databaseSizeBeforeCreate = userPreferencesRepository.findAll().size();

        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);
        restUserPreferencesMockMvc.perform(post("/api/user-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPreferencesDTO)))
            .andExpect(status().isCreated());

        // Validate the UserPreferences in the database
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeCreate + 1);
        UserPreferences testUserPreferences = userPreferencesList.get(userPreferencesList.size() - 1);
        assertThat(testUserPreferences.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testUserPreferences.isTodoListNotifications()).isEqualTo(DEFAULT_TODO_LIST_NOTIFICATIONS);
        assertThat(testUserPreferences.isCalendarNotifications()).isEqualTo(DEFAULT_CALENDAR_NOTIFICATIONS);
        assertThat(testUserPreferences.isPaymentsNotifications()).isEqualTo(DEFAULT_PAYMENTS_NOTIFICATIONS);
        assertThat(testUserPreferences.isAppointmentsNotifications()).isEqualTo(DEFAULT_APPOINTMENTS_NOTIFICATIONS);

        // Validate the UserPreferences in Elasticsearch
        verify(mockUserPreferencesSearchRepository, times(1)).save(testUserPreferences);
    }

    @Test
    @Transactional
    public void createUserPreferencesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPreferencesRepository.findAll().size();

        // Create the UserPreferences with an existing ID
        userPreferences.setId(1L);
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPreferencesMockMvc.perform(post("/api/user-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPreferencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserPreferences in Elasticsearch
        verify(mockUserPreferencesSearchRepository, times(0)).save(userPreferences);
    }

    @Test
    @Transactional
    public void getAllUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        // Get all the userPreferencesList
        restUserPreferencesMockMvc.perform(get("/api/user-preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].todoListNotifications").value(hasItem(DEFAULT_TODO_LIST_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].calendarNotifications").value(hasItem(DEFAULT_CALENDAR_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentsNotifications").value(hasItem(DEFAULT_PAYMENTS_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].appointmentsNotifications").value(hasItem(DEFAULT_APPOINTMENTS_NOTIFICATIONS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        // Get the userPreferences
        restUserPreferencesMockMvc.perform(get("/api/user-preferences/{id}", userPreferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userPreferences.getId().intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.todoListNotifications").value(DEFAULT_TODO_LIST_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.calendarNotifications").value(DEFAULT_CALENDAR_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.paymentsNotifications").value(DEFAULT_PAYMENTS_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.appointmentsNotifications").value(DEFAULT_APPOINTMENTS_NOTIFICATIONS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserPreferences() throws Exception {
        // Get the userPreferences
        restUserPreferencesMockMvc.perform(get("/api/user-preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        int databaseSizeBeforeUpdate = userPreferencesRepository.findAll().size();

        // Update the userPreferences
        UserPreferences updatedUserPreferences = userPreferencesRepository.findById(userPreferences.getId()).get();
        // Disconnect from session so that the updates on updatedUserPreferences are not directly saved in db
        em.detach(updatedUserPreferences);
        updatedUserPreferences
            .currency(UPDATED_CURRENCY)
            .todoListNotifications(UPDATED_TODO_LIST_NOTIFICATIONS)
            .calendarNotifications(UPDATED_CALENDAR_NOTIFICATIONS)
            .paymentsNotifications(UPDATED_PAYMENTS_NOTIFICATIONS)
            .appointmentsNotifications(UPDATED_APPOINTMENTS_NOTIFICATIONS);
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(updatedUserPreferences);

        restUserPreferencesMockMvc.perform(put("/api/user-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPreferencesDTO)))
            .andExpect(status().isOk());

        // Validate the UserPreferences in the database
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeUpdate);
        UserPreferences testUserPreferences = userPreferencesList.get(userPreferencesList.size() - 1);
        assertThat(testUserPreferences.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testUserPreferences.isTodoListNotifications()).isEqualTo(UPDATED_TODO_LIST_NOTIFICATIONS);
        assertThat(testUserPreferences.isCalendarNotifications()).isEqualTo(UPDATED_CALENDAR_NOTIFICATIONS);
        assertThat(testUserPreferences.isPaymentsNotifications()).isEqualTo(UPDATED_PAYMENTS_NOTIFICATIONS);
        assertThat(testUserPreferences.isAppointmentsNotifications()).isEqualTo(UPDATED_APPOINTMENTS_NOTIFICATIONS);

        // Validate the UserPreferences in Elasticsearch
        verify(mockUserPreferencesSearchRepository, times(1)).save(testUserPreferences);
    }

    @Test
    @Transactional
    public void updateNonExistingUserPreferences() throws Exception {
        int databaseSizeBeforeUpdate = userPreferencesRepository.findAll().size();

        // Create the UserPreferences
        UserPreferencesDTO userPreferencesDTO = userPreferencesMapper.toDto(userPreferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferencesMockMvc.perform(put("/api/user-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPreferencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserPreferences in the database
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserPreferences in Elasticsearch
        verify(mockUserPreferencesSearchRepository, times(0)).save(userPreferences);
    }

    @Test
    @Transactional
    public void deleteUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);

        int databaseSizeBeforeDelete = userPreferencesRepository.findAll().size();

        // Delete the userPreferences
        restUserPreferencesMockMvc.perform(delete("/api/user-preferences/{id}", userPreferences.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserPreferences> userPreferencesList = userPreferencesRepository.findAll();
        assertThat(userPreferencesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserPreferences in Elasticsearch
        verify(mockUserPreferencesSearchRepository, times(1)).deleteById(userPreferences.getId());
    }

    @Test
    @Transactional
    public void searchUserPreferences() throws Exception {
        // Initialize the database
        userPreferencesRepository.saveAndFlush(userPreferences);
        when(mockUserPreferencesSearchRepository.search(queryStringQuery("id:" + userPreferences.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userPreferences), PageRequest.of(0, 1), 1));
        // Search the userPreferences
        restUserPreferencesMockMvc.perform(get("/api/_search/user-preferences?query=id:" + userPreferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].todoListNotifications").value(hasItem(DEFAULT_TODO_LIST_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].calendarNotifications").value(hasItem(DEFAULT_CALENDAR_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentsNotifications").value(hasItem(DEFAULT_PAYMENTS_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].appointmentsNotifications").value(hasItem(DEFAULT_APPOINTMENTS_NOTIFICATIONS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPreferences.class);
        UserPreferences userPreferences1 = new UserPreferences();
        userPreferences1.setId(1L);
        UserPreferences userPreferences2 = new UserPreferences();
        userPreferences2.setId(userPreferences1.getId());
        assertThat(userPreferences1).isEqualTo(userPreferences2);
        userPreferences2.setId(2L);
        assertThat(userPreferences1).isNotEqualTo(userPreferences2);
        userPreferences1.setId(null);
        assertThat(userPreferences1).isNotEqualTo(userPreferences2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPreferencesDTO.class);
        UserPreferencesDTO userPreferencesDTO1 = new UserPreferencesDTO();
        userPreferencesDTO1.setId(1L);
        UserPreferencesDTO userPreferencesDTO2 = new UserPreferencesDTO();
        assertThat(userPreferencesDTO1).isNotEqualTo(userPreferencesDTO2);
        userPreferencesDTO2.setId(userPreferencesDTO1.getId());
        assertThat(userPreferencesDTO1).isEqualTo(userPreferencesDTO2);
        userPreferencesDTO2.setId(2L);
        assertThat(userPreferencesDTO1).isNotEqualTo(userPreferencesDTO2);
        userPreferencesDTO1.setId(null);
        assertThat(userPreferencesDTO1).isNotEqualTo(userPreferencesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userPreferencesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userPreferencesMapper.fromId(null)).isNull();
    }
}
