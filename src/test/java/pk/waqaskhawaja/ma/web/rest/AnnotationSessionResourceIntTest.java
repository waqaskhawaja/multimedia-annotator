package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.AnnotationSession;
import pk.waqaskhawaja.ma.domain.Session;
import pk.waqaskhawaja.ma.domain.User;
import pk.waqaskhawaja.ma.repository.AnnotationSessionRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationSessionSearchRepository;
import pk.waqaskhawaja.ma.service.AnnotationSessionService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.AnnotationSessionCriteria;
import pk.waqaskhawaja.ma.service.AnnotationSessionQueryService;

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


import static pk.waqaskhawaja.ma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AnnotationSessionResource REST controller.
 *
 * @see AnnotationSessionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class AnnotationSessionResourceIntTest {

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AnnotationSessionRepository annotationSessionRepository;

    @Autowired
    private AnnotationSessionService annotationSessionService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.AnnotationSessionSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnnotationSessionSearchRepository mockAnnotationSessionSearchRepository;

    @Autowired
    private AnnotationSessionQueryService annotationSessionQueryService;

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

    private MockMvc restAnnotationSessionMockMvc;

    private AnnotationSession annotationSession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnnotationSessionResource annotationSessionResource = new AnnotationSessionResource(annotationSessionService, annotationSessionQueryService);
        this.restAnnotationSessionMockMvc = MockMvcBuilders.standaloneSetup(annotationSessionResource)
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
    public static AnnotationSession createEntity(EntityManager em) {
        AnnotationSession annotationSession = new AnnotationSession()
            .start(DEFAULT_START)
            .end(DEFAULT_END)
            .name(DEFAULT_NAME);
        return annotationSession;
    }

    @Before
    public void initTest() {
        annotationSession = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnnotationSession() throws Exception {
        int databaseSizeBeforeCreate = annotationSessionRepository.findAll().size();

        // Create the AnnotationSession
        restAnnotationSessionMockMvc.perform(post("/api/annotation-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotationSession)))
            .andExpect(status().isCreated());

        // Validate the AnnotationSession in the database
        List<AnnotationSession> annotationSessionList = annotationSessionRepository.findAll();
        assertThat(annotationSessionList).hasSize(databaseSizeBeforeCreate + 1);
        AnnotationSession testAnnotationSession = annotationSessionList.get(annotationSessionList.size() - 1);
        assertThat(testAnnotationSession.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testAnnotationSession.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testAnnotationSession.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the AnnotationSession in Elasticsearch
        verify(mockAnnotationSessionSearchRepository, times(1)).save(testAnnotationSession);
    }

    @Test
    @Transactional
    public void createAnnotationSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = annotationSessionRepository.findAll().size();

        // Create the AnnotationSession with an existing ID
        annotationSession.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnotationSessionMockMvc.perform(post("/api/annotation-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotationSession)))
            .andExpect(status().isBadRequest());

        // Validate the AnnotationSession in the database
        List<AnnotationSession> annotationSessionList = annotationSessionRepository.findAll();
        assertThat(annotationSessionList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnnotationSession in Elasticsearch
        verify(mockAnnotationSessionSearchRepository, times(0)).save(annotationSession);
    }

    @Test
    @Transactional
    public void getAllAnnotationSessions() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList
        restAnnotationSessionMockMvc.perform(get("/api/annotation-sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotationSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getAnnotationSession() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get the annotationSession
        restAnnotationSessionMockMvc.perform(get("/api/annotation-sessions/{id}", annotationSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(annotationSession.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByStartIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where start equals to DEFAULT_START
        defaultAnnotationSessionShouldBeFound("start.equals=" + DEFAULT_START);

        // Get all the annotationSessionList where start equals to UPDATED_START
        defaultAnnotationSessionShouldNotBeFound("start.equals=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByStartIsInShouldWork() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where start in DEFAULT_START or UPDATED_START
        defaultAnnotationSessionShouldBeFound("start.in=" + DEFAULT_START + "," + UPDATED_START);

        // Get all the annotationSessionList where start equals to UPDATED_START
        defaultAnnotationSessionShouldNotBeFound("start.in=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where start is not null
        defaultAnnotationSessionShouldBeFound("start.specified=true");

        // Get all the annotationSessionList where start is null
        defaultAnnotationSessionShouldNotBeFound("start.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByEndIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where end equals to DEFAULT_END
        defaultAnnotationSessionShouldBeFound("end.equals=" + DEFAULT_END);

        // Get all the annotationSessionList where end equals to UPDATED_END
        defaultAnnotationSessionShouldNotBeFound("end.equals=" + UPDATED_END);
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByEndIsInShouldWork() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where end in DEFAULT_END or UPDATED_END
        defaultAnnotationSessionShouldBeFound("end.in=" + DEFAULT_END + "," + UPDATED_END);

        // Get all the annotationSessionList where end equals to UPDATED_END
        defaultAnnotationSessionShouldNotBeFound("end.in=" + UPDATED_END);
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where end is not null
        defaultAnnotationSessionShouldBeFound("end.specified=true");

        // Get all the annotationSessionList where end is null
        defaultAnnotationSessionShouldNotBeFound("end.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where name equals to DEFAULT_NAME
        defaultAnnotationSessionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the annotationSessionList where name equals to UPDATED_NAME
        defaultAnnotationSessionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAnnotationSessionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the annotationSessionList where name equals to UPDATED_NAME
        defaultAnnotationSessionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationSessionRepository.saveAndFlush(annotationSession);

        // Get all the annotationSessionList where name is not null
        defaultAnnotationSessionShouldBeFound("name.specified=true");

        // Get all the annotationSessionList where name is null
        defaultAnnotationSessionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationSessionsBySessionIsEqualToSomething() throws Exception {
        // Initialize the database
        Session session = SessionResourceIntTest.createEntity(em);
        em.persist(session);
        em.flush();
        annotationSession.setSession(session);
        annotationSessionRepository.saveAndFlush(annotationSession);
        Long sessionId = session.getId();

        // Get all the annotationSessionList where session equals to sessionId
        defaultAnnotationSessionShouldBeFound("sessionId.equals=" + sessionId);

        // Get all the annotationSessionList where session equals to sessionId + 1
        defaultAnnotationSessionShouldNotBeFound("sessionId.equals=" + (sessionId + 1));
    }


    @Test
    @Transactional
    public void getAllAnnotationSessionsByAnnotatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User annotator = UserResourceIntTest.createEntity(em);
        em.persist(annotator);
        em.flush();
        annotationSession.setAnnotator(annotator);
        annotationSessionRepository.saveAndFlush(annotationSession);
        Long annotatorId = annotator.getId();

        // Get all the annotationSessionList where annotator equals to annotatorId
        defaultAnnotationSessionShouldBeFound("annotatorId.equals=" + annotatorId);

        // Get all the annotationSessionList where annotator equals to annotatorId + 1
        defaultAnnotationSessionShouldNotBeFound("annotatorId.equals=" + (annotatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAnnotationSessionShouldBeFound(String filter) throws Exception {
        restAnnotationSessionMockMvc.perform(get("/api/annotation-sessions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotationSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAnnotationSessionMockMvc.perform(get("/api/annotation-sessions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAnnotationSessionShouldNotBeFound(String filter) throws Exception {
        restAnnotationSessionMockMvc.perform(get("/api/annotation-sessions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnotationSessionMockMvc.perform(get("/api/annotation-sessions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnnotationSession() throws Exception {
        // Get the annotationSession
        restAnnotationSessionMockMvc.perform(get("/api/annotation-sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnotationSession() throws Exception {
        // Initialize the database
        annotationSessionService.save(annotationSession);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAnnotationSessionSearchRepository);

        int databaseSizeBeforeUpdate = annotationSessionRepository.findAll().size();

        // Update the annotationSession
        AnnotationSession updatedAnnotationSession = annotationSessionRepository.findById(annotationSession.getId()).get();
        // Disconnect from session so that the updates on updatedAnnotationSession are not directly saved in db
        em.detach(updatedAnnotationSession);
        updatedAnnotationSession
            .start(UPDATED_START)
            .end(UPDATED_END)
            .name(UPDATED_NAME);

        restAnnotationSessionMockMvc.perform(put("/api/annotation-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnnotationSession)))
            .andExpect(status().isOk());

        // Validate the AnnotationSession in the database
        List<AnnotationSession> annotationSessionList = annotationSessionRepository.findAll();
        assertThat(annotationSessionList).hasSize(databaseSizeBeforeUpdate);
        AnnotationSession testAnnotationSession = annotationSessionList.get(annotationSessionList.size() - 1);
        assertThat(testAnnotationSession.getStart()).isEqualTo(UPDATED_START);
        assertThat(testAnnotationSession.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testAnnotationSession.getName()).isEqualTo(UPDATED_NAME);

        // Validate the AnnotationSession in Elasticsearch
        verify(mockAnnotationSessionSearchRepository, times(1)).save(testAnnotationSession);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnotationSession() throws Exception {
        int databaseSizeBeforeUpdate = annotationSessionRepository.findAll().size();

        // Create the AnnotationSession

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnotationSessionMockMvc.perform(put("/api/annotation-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotationSession)))
            .andExpect(status().isBadRequest());

        // Validate the AnnotationSession in the database
        List<AnnotationSession> annotationSessionList = annotationSessionRepository.findAll();
        assertThat(annotationSessionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnnotationSession in Elasticsearch
        verify(mockAnnotationSessionSearchRepository, times(0)).save(annotationSession);
    }

    @Test
    @Transactional
    public void deleteAnnotationSession() throws Exception {
        // Initialize the database
        annotationSessionService.save(annotationSession);

        int databaseSizeBeforeDelete = annotationSessionRepository.findAll().size();

        // Delete the annotationSession
        restAnnotationSessionMockMvc.perform(delete("/api/annotation-sessions/{id}", annotationSession.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AnnotationSession> annotationSessionList = annotationSessionRepository.findAll();
        assertThat(annotationSessionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnnotationSession in Elasticsearch
        verify(mockAnnotationSessionSearchRepository, times(1)).deleteById(annotationSession.getId());
    }

    @Test
    @Transactional
    public void searchAnnotationSession() throws Exception {
        // Initialize the database
        annotationSessionService.save(annotationSession);
        when(mockAnnotationSessionSearchRepository.search(queryStringQuery("id:" + annotationSession.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(annotationSession), PageRequest.of(0, 1), 1));
        // Search the annotationSession
        restAnnotationSessionMockMvc.perform(get("/api/_search/annotation-sessions?query=id:" + annotationSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotationSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnotationSession.class);
        AnnotationSession annotationSession1 = new AnnotationSession();
        annotationSession1.setId(1L);
        AnnotationSession annotationSession2 = new AnnotationSession();
        annotationSession2.setId(annotationSession1.getId());
        assertThat(annotationSession1).isEqualTo(annotationSession2);
        annotationSession2.setId(2L);
        assertThat(annotationSession1).isNotEqualTo(annotationSession2);
        annotationSession1.setId(null);
        assertThat(annotationSession1).isNotEqualTo(annotationSession2);
    }
}
