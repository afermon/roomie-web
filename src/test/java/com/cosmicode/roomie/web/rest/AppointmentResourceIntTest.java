package com.cosmicode.roomie.web.rest;

import com.cosmicode.roomie.RoomieApp;

import com.cosmicode.roomie.domain.Appointment;
import com.cosmicode.roomie.repository.AppointmentRepository;
import com.cosmicode.roomie.repository.search.AppointmentSearchRepository;
import com.cosmicode.roomie.service.AppointmentService;
import com.cosmicode.roomie.service.dto.AppointmentDTO;
import com.cosmicode.roomie.service.mapper.AppointmentMapper;
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

import com.cosmicode.roomie.domain.enumeration.AppointmentState;
/**
 * Test class for the AppointmentResource REST controller.
 *
 * @see AppointmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoomieApp.class)
public class AppointmentResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AppointmentState DEFAULT_STATE = AppointmentState.ACCEPTED;
    private static final AppointmentState UPDATED_STATE = AppointmentState.DECLINED;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private AppointmentService appointmentService;

    /**
     * This repository is mocked in the com.cosmicode.roomie.repository.search test package.
     *
     * @see com.cosmicode.roomie.repository.search.AppointmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private AppointmentSearchRepository mockAppointmentSearchRepository;

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

    private MockMvc restAppointmentMockMvc;

    private Appointment appointment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppointmentResource appointmentResource = new AppointmentResource(appointmentService);
        this.restAppointmentMockMvc = MockMvcBuilders.standaloneSetup(appointmentResource)
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
    public static Appointment createEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .description(DEFAULT_DESCRIPTION)
            .dateTime(DEFAULT_DATE_TIME)
            .state(DEFAULT_STATE);
        return appointment;
    }

    @Before
    public void initTest() {
        appointment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppointment() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);
        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate + 1);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAppointment.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testAppointment.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(1)).save(testAppointment);
    }

    @Test
    @Transactional
    public void createAppointmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment with an existing ID
        appointment.setId(1L);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setDescription(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setDateTime(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setState(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateTime").value(DEFAULT_DATE_TIME.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment
        Appointment updatedAppointment = appointmentRepository.findById(appointment.getId()).get();
        // Disconnect from session so that the updates on updatedAppointment are not directly saved in db
        em.detach(updatedAppointment);
        updatedAppointment
            .description(UPDATED_DESCRIPTION)
            .dateTime(UPDATED_DATE_TIME)
            .state(UPDATED_STATE);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(updatedAppointment);

        restAppointmentMockMvc.perform(put("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAppointment.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testAppointment.getState()).isEqualTo(UPDATED_STATE);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(1)).save(testAppointment);
    }

    @Test
    @Transactional
    public void updateNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentMockMvc.perform(put("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    public void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

        // Delete the appointment
        restAppointmentMockMvc.perform(delete("/api/appointments/{id}", appointment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(1)).deleteById(appointment.getId());
    }

    @Test
    @Transactional
    public void searchAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        when(mockAppointmentSearchRepository.search(queryStringQuery("id:" + appointment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(appointment), PageRequest.of(0, 1), 1));
        // Search the appointment
        restAppointmentMockMvc.perform(get("/api/_search/appointments?query=id:" + appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        Appointment appointment2 = new Appointment();
        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);
        appointment2.setId(2L);
        assertThat(appointment1).isNotEqualTo(appointment2);
        appointment1.setId(null);
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentDTO.class);
        AppointmentDTO appointmentDTO1 = new AppointmentDTO();
        appointmentDTO1.setId(1L);
        AppointmentDTO appointmentDTO2 = new AppointmentDTO();
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
        appointmentDTO2.setId(appointmentDTO1.getId());
        assertThat(appointmentDTO1).isEqualTo(appointmentDTO2);
        appointmentDTO2.setId(2L);
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
        appointmentDTO1.setId(null);
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appointmentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appointmentMapper.fromId(null)).isNull();
    }
}
