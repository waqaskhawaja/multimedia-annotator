package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.DataRecord;
import pk.waqaskhawaja.ma.domain.Session;
import pk.waqaskhawaja.ma.domain.InteractionType;
import pk.waqaskhawaja.ma.domain.Annotation;
import pk.waqaskhawaja.ma.repository.DataRecordRepository;
import pk.waqaskhawaja.ma.repository.search.DataRecordSearchRepository;
import pk.waqaskhawaja.ma.service.DataRecordService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.DataRecordCriteria;
import pk.waqaskhawaja.ma.service.DataRecordQueryService;

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


import static pk.waqaskhawaja.ma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DataRecordResource REST controller.
 *
 * @see DataRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class DataRecordResourceIntTest {

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME = 1;
    private static final Integer UPDATED_TIME = 2;

    @Autowired
    private DataRecordRepository dataRecordRepository;

    @Autowired
    private DataRecordService dataRecordService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.DataRecordSearchRepositoryMockConfiguration
     */
    @Autowired
    private DataRecordSearchRepository mockDataRecordSearchRepository;

    @Autowired
    private DataRecordQueryService dataRecordQueryService;

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

    private MockMvc restDataRecordMockMvc;

    private DataRecord dataRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataRecordResource dataRecordResource = new DataRecordResource(dataRecordService, dataRecordQueryService);
        this.restDataRecordMockMvc = MockMvcBuilders.standaloneSetup(dataRecordResource)
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
    public static DataRecord createEntity(EntityManager em) {
        DataRecord dataRecord = new DataRecord()
            .duration(DEFAULT_DURATION)
            .text(DEFAULT_TEXT)
            .sourceId(DEFAULT_SOURCE_ID)
            .time(DEFAULT_TIME);
        return dataRecord;
    }

    @Before
    public void initTest() {
        dataRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataRecord() throws Exception {
        int databaseSizeBeforeCreate = dataRecordRepository.findAll().size();

        // Create the DataRecord
        restDataRecordMockMvc.perform(post("/api/data-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataRecord)))
            .andExpect(status().isCreated());

        // Validate the DataRecord in the database
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeCreate + 1);
        DataRecord testDataRecord = dataRecordList.get(dataRecordList.size() - 1);
        assertThat(testDataRecord.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testDataRecord.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testDataRecord.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testDataRecord.getTime()).isEqualTo(DEFAULT_TIME);

        // Validate the DataRecord in Elasticsearch
        verify(mockDataRecordSearchRepository, times(1)).save(testDataRecord);
    }

    @Test
    @Transactional
    public void createDataRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataRecordRepository.findAll().size();

        // Create the DataRecord with an existing ID
        dataRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataRecordMockMvc.perform(post("/api/data-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataRecord)))
            .andExpect(status().isBadRequest());

        // Validate the DataRecord in the database
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeCreate);

        // Validate the DataRecord in Elasticsearch
        verify(mockDataRecordSearchRepository, times(0)).save(dataRecord);
    }

    @Test
    @Transactional
    public void getAllDataRecords() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList
        restDataRecordMockMvc.perform(get("/api/data-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));
    }
    
    @Test
    @Transactional
    public void getDataRecord() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get the dataRecord
        restDataRecordMockMvc.perform(get("/api/data-records/{id}", dataRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataRecord.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME));
    }

    @Test
    @Transactional
    public void getAllDataRecordsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where duration equals to DEFAULT_DURATION
        defaultDataRecordShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the dataRecordList where duration equals to UPDATED_DURATION
        defaultDataRecordShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllDataRecordsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultDataRecordShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the dataRecordList where duration equals to UPDATED_DURATION
        defaultDataRecordShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllDataRecordsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where duration is not null
        defaultDataRecordShouldBeFound("duration.specified=true");

        // Get all the dataRecordList where duration is null
        defaultDataRecordShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataRecordsByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where duration greater than or equals to DEFAULT_DURATION
        defaultDataRecordShouldBeFound("duration.greaterOrEqualThan=" + DEFAULT_DURATION);

        // Get all the dataRecordList where duration greater than or equals to UPDATED_DURATION
        defaultDataRecordShouldNotBeFound("duration.greaterOrEqualThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllDataRecordsByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where duration less than or equals to DEFAULT_DURATION
        defaultDataRecordShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the dataRecordList where duration less than or equals to UPDATED_DURATION
        defaultDataRecordShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }


    @Test
    @Transactional
    public void getAllDataRecordsByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where text equals to DEFAULT_TEXT
        defaultDataRecordShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the dataRecordList where text equals to UPDATED_TEXT
        defaultDataRecordShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllDataRecordsByTextIsInShouldWork() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultDataRecordShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the dataRecordList where text equals to UPDATED_TEXT
        defaultDataRecordShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllDataRecordsByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where text is not null
        defaultDataRecordShouldBeFound("text.specified=true");

        // Get all the dataRecordList where text is null
        defaultDataRecordShouldNotBeFound("text.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataRecordsBySourceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where sourceId equals to DEFAULT_SOURCE_ID
        defaultDataRecordShouldBeFound("sourceId.equals=" + DEFAULT_SOURCE_ID);

        // Get all the dataRecordList where sourceId equals to UPDATED_SOURCE_ID
        defaultDataRecordShouldNotBeFound("sourceId.equals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllDataRecordsBySourceIdIsInShouldWork() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where sourceId in DEFAULT_SOURCE_ID or UPDATED_SOURCE_ID
        defaultDataRecordShouldBeFound("sourceId.in=" + DEFAULT_SOURCE_ID + "," + UPDATED_SOURCE_ID);

        // Get all the dataRecordList where sourceId equals to UPDATED_SOURCE_ID
        defaultDataRecordShouldNotBeFound("sourceId.in=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllDataRecordsBySourceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where sourceId is not null
        defaultDataRecordShouldBeFound("sourceId.specified=true");

        // Get all the dataRecordList where sourceId is null
        defaultDataRecordShouldNotBeFound("sourceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataRecordsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where time equals to DEFAULT_TIME
        defaultDataRecordShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the dataRecordList where time equals to UPDATED_TIME
        defaultDataRecordShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDataRecordsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where time in DEFAULT_TIME or UPDATED_TIME
        defaultDataRecordShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the dataRecordList where time equals to UPDATED_TIME
        defaultDataRecordShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDataRecordsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where time is not null
        defaultDataRecordShouldBeFound("time.specified=true");

        // Get all the dataRecordList where time is null
        defaultDataRecordShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllDataRecordsByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where time greater than or equals to DEFAULT_TIME
        defaultDataRecordShouldBeFound("time.greaterOrEqualThan=" + DEFAULT_TIME);

        // Get all the dataRecordList where time greater than or equals to UPDATED_TIME
        defaultDataRecordShouldNotBeFound("time.greaterOrEqualThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllDataRecordsByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList where time less than or equals to DEFAULT_TIME
        defaultDataRecordShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the dataRecordList where time less than or equals to UPDATED_TIME
        defaultDataRecordShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }


    @Test
    @Transactional
    public void getAllDataRecordsBySessionIsEqualToSomething() throws Exception {
        // Initialize the database
        Session session = SessionResourceIntTest.createEntity(em);
        em.persist(session);
        em.flush();
        dataRecord.setSession(session);
        dataRecordRepository.saveAndFlush(dataRecord);
        Long sessionId = session.getId();

        // Get all the dataRecordList where session equals to sessionId
        defaultDataRecordShouldBeFound("sessionId.equals=" + sessionId);

        // Get all the dataRecordList where session equals to sessionId + 1
        defaultDataRecordShouldNotBeFound("sessionId.equals=" + (sessionId + 1));
    }


    @Test
    @Transactional
    public void getAllDataRecordsByInteractionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        InteractionType interactionType = InteractionTypeResourceIntTest.createEntity(em);
        em.persist(interactionType);
        em.flush();
        dataRecord.setInteractionType(interactionType);
        dataRecordRepository.saveAndFlush(dataRecord);
        Long interactionTypeId = interactionType.getId();

        // Get all the dataRecordList where interactionType equals to interactionTypeId
        defaultDataRecordShouldBeFound("interactionTypeId.equals=" + interactionTypeId);

        // Get all the dataRecordList where interactionType equals to interactionTypeId + 1
        defaultDataRecordShouldNotBeFound("interactionTypeId.equals=" + (interactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllDataRecordsByAnnotationIsEqualToSomething() throws Exception {
        // Initialize the database
        Annotation annotation = AnnotationResourceIntTest.createEntity(em);
        em.persist(annotation);
        em.flush();
        dataRecord.addAnnotation(annotation);
        dataRecordRepository.saveAndFlush(dataRecord);
        Long annotationId = annotation.getId();

        // Get all the dataRecordList where annotation equals to annotationId
        defaultDataRecordShouldBeFound("annotationId.equals=" + annotationId);

        // Get all the dataRecordList where annotation equals to annotationId + 1
        defaultDataRecordShouldNotBeFound("annotationId.equals=" + (annotationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDataRecordShouldBeFound(String filter) throws Exception {
        restDataRecordMockMvc.perform(get("/api/data-records?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));

        // Check, that the count call also returns 1
        restDataRecordMockMvc.perform(get("/api/data-records/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDataRecordShouldNotBeFound(String filter) throws Exception {
        restDataRecordMockMvc.perform(get("/api/data-records?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDataRecordMockMvc.perform(get("/api/data-records/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDataRecord() throws Exception {
        // Get the dataRecord
        restDataRecordMockMvc.perform(get("/api/data-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataRecord() throws Exception {
        // Initialize the database
        dataRecordService.save(dataRecord);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDataRecordSearchRepository);

        int databaseSizeBeforeUpdate = dataRecordRepository.findAll().size();

        // Update the dataRecord
        DataRecord updatedDataRecord = dataRecordRepository.findById(dataRecord.getId()).get();
        // Disconnect from session so that the updates on updatedDataRecord are not directly saved in db
        em.detach(updatedDataRecord);
        updatedDataRecord
            .duration(UPDATED_DURATION)
            .text(UPDATED_TEXT)
            .sourceId(UPDATED_SOURCE_ID)
            .time(UPDATED_TIME);

        restDataRecordMockMvc.perform(put("/api/data-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataRecord)))
            .andExpect(status().isOk());

        // Validate the DataRecord in the database
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeUpdate);
        DataRecord testDataRecord = dataRecordList.get(dataRecordList.size() - 1);
        assertThat(testDataRecord.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testDataRecord.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDataRecord.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testDataRecord.getTime()).isEqualTo(UPDATED_TIME);

        // Validate the DataRecord in Elasticsearch
        verify(mockDataRecordSearchRepository, times(1)).save(testDataRecord);
    }

    @Test
    @Transactional
    public void updateNonExistingDataRecord() throws Exception {
        int databaseSizeBeforeUpdate = dataRecordRepository.findAll().size();

        // Create the DataRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataRecordMockMvc.perform(put("/api/data-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataRecord)))
            .andExpect(status().isBadRequest());

        // Validate the DataRecord in the database
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DataRecord in Elasticsearch
        verify(mockDataRecordSearchRepository, times(0)).save(dataRecord);
    }

    @Test
    @Transactional
    public void deleteDataRecord() throws Exception {
        // Initialize the database
        dataRecordService.save(dataRecord);

        int databaseSizeBeforeDelete = dataRecordRepository.findAll().size();

        // Delete the dataRecord
        restDataRecordMockMvc.perform(delete("/api/data-records/{id}", dataRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DataRecord in Elasticsearch
        verify(mockDataRecordSearchRepository, times(1)).deleteById(dataRecord.getId());
    }

    @Test
    @Transactional
    public void searchDataRecord() throws Exception {
        // Initialize the database
        dataRecordService.save(dataRecord);
        when(mockDataRecordSearchRepository.search(queryStringQuery("id:" + dataRecord.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dataRecord), PageRequest.of(0, 1), 1));
        // Search the dataRecord
        restDataRecordMockMvc.perform(get("/api/_search/data-records?query=id:" + dataRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataRecord.class);
        DataRecord dataRecord1 = new DataRecord();
        dataRecord1.setId(1L);
        DataRecord dataRecord2 = new DataRecord();
        dataRecord2.setId(dataRecord1.getId());
        assertThat(dataRecord1).isEqualTo(dataRecord2);
        dataRecord2.setId(2L);
        assertThat(dataRecord1).isNotEqualTo(dataRecord2);
        dataRecord1.setId(null);
        assertThat(dataRecord1).isNotEqualTo(dataRecord2);
    }
}
