package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.InteractionRecord;
import pk.waqaskhawaja.ma.domain.InteractionType;
import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import pk.waqaskhawaja.ma.domain.Annotation;
import pk.waqaskhawaja.ma.repository.InteractionRecordRepository;
import pk.waqaskhawaja.ma.repository.search.InteractionRecordSearchRepository;
import pk.waqaskhawaja.ma.service.InteractionRecordService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.InteractionRecordCriteria;
import pk.waqaskhawaja.ma.service.InteractionRecordQueryService;

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
 * Test class for the InteractionRecordResource REST controller.
 *
 * @see InteractionRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class InteractionRecordResourceIntTest {

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME = 1;
    private static final Integer UPDATED_TIME = 2;

    @Autowired
    private InteractionRecordRepository interactionRecordRepository;

    @Autowired
    private InteractionRecordService interactionRecordService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.InteractionRecordSearchRepositoryMockConfiguration
     */
    @Autowired
    private InteractionRecordSearchRepository mockInteractionRecordSearchRepository;

    @Autowired
    private InteractionRecordQueryService interactionRecordQueryService;

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

    private MockMvc restInteractionRecordMockMvc;

    private InteractionRecord interactionRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InteractionRecordResource interactionRecordResource = new InteractionRecordResource(interactionRecordService, interactionRecordQueryService);
        this.restInteractionRecordMockMvc = MockMvcBuilders.standaloneSetup(interactionRecordResource)
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
    public static InteractionRecord createEntity(EntityManager em) {
        InteractionRecord interactionRecord = new InteractionRecord()
            .duration(DEFAULT_DURATION)
            .text(DEFAULT_TEXT)
            .sourceId(DEFAULT_SOURCE_ID)
            .time(DEFAULT_TIME);
        return interactionRecord;
    }

    @Before
    public void initTest() {
        interactionRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createInteractionRecord() throws Exception {
        int databaseSizeBeforeCreate = interactionRecordRepository.findAll().size();

        // Create the InteractionRecord
        restInteractionRecordMockMvc.perform(post("/api/interaction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interactionRecord)))
            .andExpect(status().isCreated());

        // Validate the InteractionRecord in the database
        List<InteractionRecord> interactionRecordList = interactionRecordRepository.findAll();
        assertThat(interactionRecordList).hasSize(databaseSizeBeforeCreate + 1);
        InteractionRecord testInteractionRecord = interactionRecordList.get(interactionRecordList.size() - 1);
        assertThat(testInteractionRecord.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testInteractionRecord.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testInteractionRecord.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testInteractionRecord.getTime()).isEqualTo(DEFAULT_TIME);

        // Validate the InteractionRecord in Elasticsearch
        verify(mockInteractionRecordSearchRepository, times(1)).save(testInteractionRecord);
    }

    @Test
    @Transactional
    public void createInteractionRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interactionRecordRepository.findAll().size();

        // Create the InteractionRecord with an existing ID
        interactionRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInteractionRecordMockMvc.perform(post("/api/interaction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interactionRecord)))
            .andExpect(status().isBadRequest());

        // Validate the InteractionRecord in the database
        List<InteractionRecord> interactionRecordList = interactionRecordRepository.findAll();
        assertThat(interactionRecordList).hasSize(databaseSizeBeforeCreate);

        // Validate the InteractionRecord in Elasticsearch
        verify(mockInteractionRecordSearchRepository, times(0)).save(interactionRecord);
    }

    @Test
    @Transactional
    public void getAllInteractionRecords() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList
        restInteractionRecordMockMvc.perform(get("/api/interaction-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interactionRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));
    }
    
    @Test
    @Transactional
    public void getInteractionRecord() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get the interactionRecord
        restInteractionRecordMockMvc.perform(get("/api/interaction-records/{id}", interactionRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(interactionRecord.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME));
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where duration equals to DEFAULT_DURATION
        defaultInteractionRecordShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the interactionRecordList where duration equals to UPDATED_DURATION
        defaultInteractionRecordShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultInteractionRecordShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the interactionRecordList where duration equals to UPDATED_DURATION
        defaultInteractionRecordShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where duration is not null
        defaultInteractionRecordShouldBeFound("duration.specified=true");

        // Get all the interactionRecordList where duration is null
        defaultInteractionRecordShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where duration greater than or equals to DEFAULT_DURATION
        defaultInteractionRecordShouldBeFound("duration.greaterOrEqualThan=" + DEFAULT_DURATION);

        // Get all the interactionRecordList where duration greater than or equals to UPDATED_DURATION
        defaultInteractionRecordShouldNotBeFound("duration.greaterOrEqualThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where duration less than or equals to DEFAULT_DURATION
        defaultInteractionRecordShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the interactionRecordList where duration less than or equals to UPDATED_DURATION
        defaultInteractionRecordShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }


    @Test
    @Transactional
    public void getAllInteractionRecordsByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where text equals to DEFAULT_TEXT
        defaultInteractionRecordShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the interactionRecordList where text equals to UPDATED_TEXT
        defaultInteractionRecordShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByTextIsInShouldWork() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultInteractionRecordShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the interactionRecordList where text equals to UPDATED_TEXT
        defaultInteractionRecordShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where text is not null
        defaultInteractionRecordShouldBeFound("text.specified=true");

        // Get all the interactionRecordList where text is null
        defaultInteractionRecordShouldNotBeFound("text.specified=false");
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsBySourceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where sourceId equals to DEFAULT_SOURCE_ID
        defaultInteractionRecordShouldBeFound("sourceId.equals=" + DEFAULT_SOURCE_ID);

        // Get all the interactionRecordList where sourceId equals to UPDATED_SOURCE_ID
        defaultInteractionRecordShouldNotBeFound("sourceId.equals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsBySourceIdIsInShouldWork() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where sourceId in DEFAULT_SOURCE_ID or UPDATED_SOURCE_ID
        defaultInteractionRecordShouldBeFound("sourceId.in=" + DEFAULT_SOURCE_ID + "," + UPDATED_SOURCE_ID);

        // Get all the interactionRecordList where sourceId equals to UPDATED_SOURCE_ID
        defaultInteractionRecordShouldNotBeFound("sourceId.in=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsBySourceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where sourceId is not null
        defaultInteractionRecordShouldBeFound("sourceId.specified=true");

        // Get all the interactionRecordList where sourceId is null
        defaultInteractionRecordShouldNotBeFound("sourceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where time equals to DEFAULT_TIME
        defaultInteractionRecordShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the interactionRecordList where time equals to UPDATED_TIME
        defaultInteractionRecordShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where time in DEFAULT_TIME or UPDATED_TIME
        defaultInteractionRecordShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the interactionRecordList where time equals to UPDATED_TIME
        defaultInteractionRecordShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where time is not null
        defaultInteractionRecordShouldBeFound("time.specified=true");

        // Get all the interactionRecordList where time is null
        defaultInteractionRecordShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where time greater than or equals to DEFAULT_TIME
        defaultInteractionRecordShouldBeFound("time.greaterOrEqualThan=" + DEFAULT_TIME);

        // Get all the interactionRecordList where time greater than or equals to UPDATED_TIME
        defaultInteractionRecordShouldNotBeFound("time.greaterOrEqualThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllInteractionRecordsByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        interactionRecordRepository.saveAndFlush(interactionRecord);

        // Get all the interactionRecordList where time less than or equals to DEFAULT_TIME
        defaultInteractionRecordShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the interactionRecordList where time less than or equals to UPDATED_TIME
        defaultInteractionRecordShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }


    @Test
    @Transactional
    public void getAllInteractionRecordsByInteractionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        InteractionType interactionType = InteractionTypeResourceIntTest.createEntity(em);
        em.persist(interactionType);
        em.flush();
        interactionRecord.setInteractionType(interactionType);
        interactionRecordRepository.saveAndFlush(interactionRecord);
        Long interactionTypeId = interactionType.getId();

        // Get all the interactionRecordList where interactionType equals to interactionTypeId
        defaultInteractionRecordShouldBeFound("interactionTypeId.equals=" + interactionTypeId);

        // Get all the interactionRecordList where interactionType equals to interactionTypeId + 1
        defaultInteractionRecordShouldNotBeFound("interactionTypeId.equals=" + (interactionTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllInteractionRecordsByAnalysisSessionResourceIsEqualToSomething() throws Exception {
        // Initialize the database
        AnalysisSessionResource analysisSessionResource = AnalysisSessionResourceResourceIntTest.createEntity(em);
        em.persist(analysisSessionResource);
        em.flush();
        interactionRecord.setAnalysisSessionResource(analysisSessionResource);
        interactionRecordRepository.saveAndFlush(interactionRecord);
        Long analysisSessionResourceId = analysisSessionResource.getId();

        // Get all the interactionRecordList where analysisSessionResource equals to analysisSessionResourceId
        defaultInteractionRecordShouldBeFound("analysisSessionResourceId.equals=" + analysisSessionResourceId);

        // Get all the interactionRecordList where analysisSessionResource equals to analysisSessionResourceId + 1
        defaultInteractionRecordShouldNotBeFound("analysisSessionResourceId.equals=" + (analysisSessionResourceId + 1));
    }


    @Test
    @Transactional
    public void getAllInteractionRecordsByAnnotationIsEqualToSomething() throws Exception {
        // Initialize the database
        Annotation annotation = AnnotationResourceIntTest.createEntity(em);
        em.persist(annotation);
        em.flush();
        interactionRecord.addAnnotation(annotation);
        interactionRecordRepository.saveAndFlush(interactionRecord);
        Long annotationId = annotation.getId();

        // Get all the interactionRecordList where annotation equals to annotationId
        defaultInteractionRecordShouldBeFound("annotationId.equals=" + annotationId);

        // Get all the interactionRecordList where annotation equals to annotationId + 1
        defaultInteractionRecordShouldNotBeFound("annotationId.equals=" + (annotationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInteractionRecordShouldBeFound(String filter) throws Exception {
        restInteractionRecordMockMvc.perform(get("/api/interaction-records?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interactionRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));

        // Check, that the count call also returns 1
        restInteractionRecordMockMvc.perform(get("/api/interaction-records/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInteractionRecordShouldNotBeFound(String filter) throws Exception {
        restInteractionRecordMockMvc.perform(get("/api/interaction-records?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInteractionRecordMockMvc.perform(get("/api/interaction-records/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInteractionRecord() throws Exception {
        // Get the interactionRecord
        restInteractionRecordMockMvc.perform(get("/api/interaction-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInteractionRecord() throws Exception {
        // Initialize the database
        interactionRecordService.save(interactionRecord);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockInteractionRecordSearchRepository);

        int databaseSizeBeforeUpdate = interactionRecordRepository.findAll().size();

        // Update the interactionRecord
        InteractionRecord updatedInteractionRecord = interactionRecordRepository.findById(interactionRecord.getId()).get();
        // Disconnect from session so that the updates on updatedInteractionRecord are not directly saved in db
        em.detach(updatedInteractionRecord);
        updatedInteractionRecord
            .duration(UPDATED_DURATION)
            .text(UPDATED_TEXT)
            .sourceId(UPDATED_SOURCE_ID)
            .time(UPDATED_TIME);

        restInteractionRecordMockMvc.perform(put("/api/interaction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInteractionRecord)))
            .andExpect(status().isOk());

        // Validate the InteractionRecord in the database
        List<InteractionRecord> interactionRecordList = interactionRecordRepository.findAll();
        assertThat(interactionRecordList).hasSize(databaseSizeBeforeUpdate);
        InteractionRecord testInteractionRecord = interactionRecordList.get(interactionRecordList.size() - 1);
        assertThat(testInteractionRecord.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testInteractionRecord.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testInteractionRecord.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testInteractionRecord.getTime()).isEqualTo(UPDATED_TIME);

        // Validate the InteractionRecord in Elasticsearch
        verify(mockInteractionRecordSearchRepository, times(1)).save(testInteractionRecord);
    }

    @Test
    @Transactional
    public void updateNonExistingInteractionRecord() throws Exception {
        int databaseSizeBeforeUpdate = interactionRecordRepository.findAll().size();

        // Create the InteractionRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInteractionRecordMockMvc.perform(put("/api/interaction-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interactionRecord)))
            .andExpect(status().isBadRequest());

        // Validate the InteractionRecord in the database
        List<InteractionRecord> interactionRecordList = interactionRecordRepository.findAll();
        assertThat(interactionRecordList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InteractionRecord in Elasticsearch
        verify(mockInteractionRecordSearchRepository, times(0)).save(interactionRecord);
    }

    @Test
    @Transactional
    public void deleteInteractionRecord() throws Exception {
        // Initialize the database
        interactionRecordService.save(interactionRecord);

        int databaseSizeBeforeDelete = interactionRecordRepository.findAll().size();

        // Delete the interactionRecord
        restInteractionRecordMockMvc.perform(delete("/api/interaction-records/{id}", interactionRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InteractionRecord> interactionRecordList = interactionRecordRepository.findAll();
        assertThat(interactionRecordList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InteractionRecord in Elasticsearch
        verify(mockInteractionRecordSearchRepository, times(1)).deleteById(interactionRecord.getId());
    }

    @Test
    @Transactional
    public void searchInteractionRecord() throws Exception {
        // Initialize the database
        interactionRecordService.save(interactionRecord);
        when(mockInteractionRecordSearchRepository.search(queryStringQuery("id:" + interactionRecord.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(interactionRecord), PageRequest.of(0, 1), 1));
        // Search the interactionRecord
        restInteractionRecordMockMvc.perform(get("/api/_search/interaction-records?query=id:" + interactionRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interactionRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InteractionRecord.class);
        InteractionRecord interactionRecord1 = new InteractionRecord();
        interactionRecord1.setId(1L);
        InteractionRecord interactionRecord2 = new InteractionRecord();
        interactionRecord2.setId(interactionRecord1.getId());
        assertThat(interactionRecord1).isEqualTo(interactionRecord2);
        interactionRecord2.setId(2L);
        assertThat(interactionRecord1).isNotEqualTo(interactionRecord2);
        interactionRecord1.setId(null);
        assertThat(interactionRecord1).isNotEqualTo(interactionRecord2);
    }
}
