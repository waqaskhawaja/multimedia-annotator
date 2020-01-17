package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.Session;
import pk.waqaskhawaja.ma.domain.DataType;
import pk.waqaskhawaja.ma.domain.Scenario;
import pk.waqaskhawaja.ma.repository.SessionRepository;
import pk.waqaskhawaja.ma.repository.search.SessionSearchRepository;
import pk.waqaskhawaja.ma.service.SessionService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.SessionCriteria;
import pk.waqaskhawaja.ma.service.SessionQueryService;

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
import org.springframework.util.Base64Utils;
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
 * Test class for the SessionResource REST controller.
 *
 * @see SessionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class SessionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SOURCE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SOURCE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SOURCE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SOURCE_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionService sessionService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.SessionSearchRepositoryMockConfiguration
     */
    @Autowired
    private SessionSearchRepository mockSessionSearchRepository;

    @Autowired
    private SessionQueryService sessionQueryService;

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

    private MockMvc restSessionMockMvc;

    private Session session;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SessionResource sessionResource = new SessionResource(sessionService, sessionQueryService);
        this.restSessionMockMvc = MockMvcBuilders.standaloneSetup(sessionResource)
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
    public static Session createEntity(EntityManager em) {
        Session session = new Session()
            .name(DEFAULT_NAME)
            .sourceFile(DEFAULT_SOURCE_FILE)
            .sourceFileContentType(DEFAULT_SOURCE_FILE_CONTENT_TYPE)
            .url(DEFAULT_URL);
        return session;
    }

    @Before
    public void initTest() {
        session = createEntity(em);
    }

    @Test
    @Transactional
    public void createSession() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session
        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isCreated());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate + 1);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSession.getSourceFile()).isEqualTo(DEFAULT_SOURCE_FILE);
        assertThat(testSession.getSourceFileContentType()).isEqualTo(DEFAULT_SOURCE_FILE_CONTENT_TYPE);
        assertThat(testSession.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(1)).save(testSession);
    }

    @Test
    @Transactional
    public void createSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session with an existing ID
        session.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(0)).save(session);
    }

    @Test
    @Transactional
    public void getAllSessions() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList
        restSessionMockMvc.perform(get("/api/sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(session.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sourceFileContentType").value(DEFAULT_SOURCE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.sourceFile").value(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllSessionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList where name equals to DEFAULT_NAME
        defaultSessionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sessionList where name equals to UPDATED_NAME
        defaultSessionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSessionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSessionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sessionList where name equals to UPDATED_NAME
        defaultSessionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSessionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList where name is not null
        defaultSessionShouldBeFound("name.specified=true");

        // Get all the sessionList where name is null
        defaultSessionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllSessionsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList where url equals to DEFAULT_URL
        defaultSessionShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the sessionList where url equals to UPDATED_URL
        defaultSessionShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllSessionsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList where url in DEFAULT_URL or UPDATED_URL
        defaultSessionShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the sessionList where url equals to UPDATED_URL
        defaultSessionShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllSessionsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList where url is not null
        defaultSessionShouldBeFound("url.specified=true");

        // Get all the sessionList where url is null
        defaultSessionShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllSessionsByDataTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        DataType dataType = DataTypeResourceIntTest.createEntity(em);
        em.persist(dataType);
        em.flush();
        session.setDataType(dataType);
        sessionRepository.saveAndFlush(session);
        Long dataTypeId = dataType.getId();

        // Get all the sessionList where dataType equals to dataTypeId
        defaultSessionShouldBeFound("dataTypeId.equals=" + dataTypeId);

        // Get all the sessionList where dataType equals to dataTypeId + 1
        defaultSessionShouldNotBeFound("dataTypeId.equals=" + (dataTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllSessionsByScenarioIsEqualToSomething() throws Exception {
        // Initialize the database
        Scenario scenario = ScenarioResourceIntTest.createEntity(em);
        em.persist(scenario);
        em.flush();
        session.setScenario(scenario);
        sessionRepository.saveAndFlush(session);
        Long scenarioId = scenario.getId();

        // Get all the sessionList where scenario equals to scenarioId
        defaultSessionShouldBeFound("scenarioId.equals=" + scenarioId);

        // Get all the sessionList where scenario equals to scenarioId + 1
        defaultSessionShouldNotBeFound("scenarioId.equals=" + (scenarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSessionShouldBeFound(String filter) throws Exception {
        restSessionMockMvc.perform(get("/api/sessions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restSessionMockMvc.perform(get("/api/sessions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSessionShouldNotBeFound(String filter) throws Exception {
        restSessionMockMvc.perform(get("/api/sessions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSessionMockMvc.perform(get("/api/sessions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSession() throws Exception {
        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSession() throws Exception {
        // Initialize the database
        sessionService.save(session);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSessionSearchRepository);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session
        Session updatedSession = sessionRepository.findById(session.getId()).get();
        // Disconnect from session so that the updates on updatedSession are not directly saved in db
        em.detach(updatedSession);
        updatedSession
            .name(UPDATED_NAME)
            .sourceFile(UPDATED_SOURCE_FILE)
            .sourceFileContentType(UPDATED_SOURCE_FILE_CONTENT_TYPE)
            .url(UPDATED_URL);

        restSessionMockMvc.perform(put("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSession)))
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSession.getSourceFile()).isEqualTo(UPDATED_SOURCE_FILE);
        assertThat(testSession.getSourceFileContentType()).isEqualTo(UPDATED_SOURCE_FILE_CONTENT_TYPE);
        assertThat(testSession.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(1)).save(testSession);
    }

    @Test
    @Transactional
    public void updateNonExistingSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Create the Session

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMockMvc.perform(put("/api/sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(0)).save(session);
    }

    @Test
    @Transactional
    public void deleteSession() throws Exception {
        // Initialize the database
        sessionService.save(session);

        int databaseSizeBeforeDelete = sessionRepository.findAll().size();

        // Delete the session
        restSessionMockMvc.perform(delete("/api/sessions/{id}", session.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Session in Elasticsearch
        verify(mockSessionSearchRepository, times(1)).deleteById(session.getId());
    }

    @Test
    @Transactional
    public void searchSession() throws Exception {
        // Initialize the database
        sessionService.save(session);
        when(mockSessionSearchRepository.search(queryStringQuery("id:" + session.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(session), PageRequest.of(0, 1), 1));
        // Search the session
        restSessionMockMvc.perform(get("/api/_search/sessions?query=id:" + session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Session.class);
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(session1.getId());
        assertThat(session1).isEqualTo(session2);
        session2.setId(2L);
        assertThat(session1).isNotEqualTo(session2);
        session1.setId(null);
        assertThat(session1).isNotEqualTo(session2);
    }
}
