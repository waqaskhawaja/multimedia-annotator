package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.AnalysisSession;
import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import pk.waqaskhawaja.ma.repository.AnalysisSessionRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisSessionSearchRepository;
import pk.waqaskhawaja.ma.service.AnalysisSessionService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.AnalysisSessionCriteria;
import pk.waqaskhawaja.ma.service.AnalysisSessionQueryService;

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
 * Test class for the AnalysisSessionResource REST controller.
 *
 * @see AnalysisSessionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class AnalysisSessionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SOURCE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SOURCE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SOURCE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SOURCE_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private AnalysisSessionRepository analysisSessionRepository;

    @Autowired
    private AnalysisSessionService analysisSessionService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.AnalysisSessionSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnalysisSessionSearchRepository mockAnalysisSessionSearchRepository;

    @Autowired
    private AnalysisSessionQueryService analysisSessionQueryService;

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

    private MockMvc restAnalysisSessionMockMvc;

    private AnalysisSession analysisSession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnalysisSessionResource analysisSessionResource = new AnalysisSessionResource(analysisSessionService, analysisSessionQueryService);
        this.restAnalysisSessionMockMvc = MockMvcBuilders.standaloneSetup(analysisSessionResource)
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
    public static AnalysisSession createEntity(EntityManager em) {
        AnalysisSession analysisSession = new AnalysisSession()
            .name(DEFAULT_NAME)
            .sourceFile(DEFAULT_SOURCE_FILE)
            .sourceFileContentType(DEFAULT_SOURCE_FILE_CONTENT_TYPE)
            .url(DEFAULT_URL);
        return analysisSession;
    }

    @Before
    public void initTest() {
        analysisSession = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalysisSession() throws Exception {
        int databaseSizeBeforeCreate = analysisSessionRepository.findAll().size();

        // Create the AnalysisSession
        restAnalysisSessionMockMvc.perform(post("/api/analysis-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisSession)))
            .andExpect(status().isCreated());

        // Validate the AnalysisSession in the database
        List<AnalysisSession> analysisSessionList = analysisSessionRepository.findAll();
        assertThat(analysisSessionList).hasSize(databaseSizeBeforeCreate + 1);
        AnalysisSession testAnalysisSession = analysisSessionList.get(analysisSessionList.size() - 1);
        assertThat(testAnalysisSession.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAnalysisSession.getSourceFile()).isEqualTo(DEFAULT_SOURCE_FILE);
        assertThat(testAnalysisSession.getSourceFileContentType()).isEqualTo(DEFAULT_SOURCE_FILE_CONTENT_TYPE);
        assertThat(testAnalysisSession.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the AnalysisSession in Elasticsearch
        verify(mockAnalysisSessionSearchRepository, times(1)).save(testAnalysisSession);
    }

    @Test
    @Transactional
    public void createAnalysisSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analysisSessionRepository.findAll().size();

        // Create the AnalysisSession with an existing ID
        analysisSession.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalysisSessionMockMvc.perform(post("/api/analysis-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisSession)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisSession in the database
        List<AnalysisSession> analysisSessionList = analysisSessionRepository.findAll();
        assertThat(analysisSessionList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnalysisSession in Elasticsearch
        verify(mockAnalysisSessionSearchRepository, times(0)).save(analysisSession);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessions() throws Exception {
        // Initialize the database
        analysisSessionRepository.saveAndFlush(analysisSession);

        // Get all the analysisSessionList
        restAnalysisSessionMockMvc.perform(get("/api/analysis-sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getAnalysisSession() throws Exception {
        // Initialize the database
        analysisSessionRepository.saveAndFlush(analysisSession);

        // Get the analysisSession
        restAnalysisSessionMockMvc.perform(get("/api/analysis-sessions/{id}", analysisSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analysisSession.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sourceFileContentType").value(DEFAULT_SOURCE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.sourceFile").value(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        analysisSessionRepository.saveAndFlush(analysisSession);

        // Get all the analysisSessionList where name equals to DEFAULT_NAME
        defaultAnalysisSessionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the analysisSessionList where name equals to UPDATED_NAME
        defaultAnalysisSessionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        analysisSessionRepository.saveAndFlush(analysisSession);

        // Get all the analysisSessionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAnalysisSessionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the analysisSessionList where name equals to UPDATED_NAME
        defaultAnalysisSessionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        analysisSessionRepository.saveAndFlush(analysisSession);

        // Get all the analysisSessionList where name is not null
        defaultAnalysisSessionShouldBeFound("name.specified=true");

        // Get all the analysisSessionList where name is null
        defaultAnalysisSessionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        analysisSessionRepository.saveAndFlush(analysisSession);

        // Get all the analysisSessionList where url equals to DEFAULT_URL
        defaultAnalysisSessionShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the analysisSessionList where url equals to UPDATED_URL
        defaultAnalysisSessionShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        analysisSessionRepository.saveAndFlush(analysisSession);

        // Get all the analysisSessionList where url in DEFAULT_URL or UPDATED_URL
        defaultAnalysisSessionShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the analysisSessionList where url equals to UPDATED_URL
        defaultAnalysisSessionShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        analysisSessionRepository.saveAndFlush(analysisSession);

        // Get all the analysisSessionList where url is not null
        defaultAnalysisSessionShouldBeFound("url.specified=true");

        // Get all the analysisSessionList where url is null
        defaultAnalysisSessionShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionsByAnalysisScenarioIsEqualToSomething() throws Exception {
        // Initialize the database
        AnalysisScenario analysisScenario = AnalysisScenarioResourceIntTest.createEntity(em);
        em.persist(analysisScenario);
        em.flush();
        analysisSession.setAnalysisScenario(analysisScenario);
        analysisSessionRepository.saveAndFlush(analysisSession);
        Long analysisScenarioId = analysisScenario.getId();

        // Get all the analysisSessionList where analysisScenario equals to analysisScenarioId
        defaultAnalysisSessionShouldBeFound("analysisScenarioId.equals=" + analysisScenarioId);

        // Get all the analysisSessionList where analysisScenario equals to analysisScenarioId + 1
        defaultAnalysisSessionShouldNotBeFound("analysisScenarioId.equals=" + (analysisScenarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAnalysisSessionShouldBeFound(String filter) throws Exception {
        restAnalysisSessionMockMvc.perform(get("/api/analysis-sessions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restAnalysisSessionMockMvc.perform(get("/api/analysis-sessions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAnalysisSessionShouldNotBeFound(String filter) throws Exception {
        restAnalysisSessionMockMvc.perform(get("/api/analysis-sessions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnalysisSessionMockMvc.perform(get("/api/analysis-sessions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnalysisSession() throws Exception {
        // Get the analysisSession
        restAnalysisSessionMockMvc.perform(get("/api/analysis-sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalysisSession() throws Exception {
        // Initialize the database
        analysisSessionService.save(analysisSession);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAnalysisSessionSearchRepository);

        int databaseSizeBeforeUpdate = analysisSessionRepository.findAll().size();

        // Update the analysisSession
        AnalysisSession updatedAnalysisSession = analysisSessionRepository.findById(analysisSession.getId()).get();
        // Disconnect from session so that the updates on updatedAnalysisSession are not directly saved in db
        em.detach(updatedAnalysisSession);
        updatedAnalysisSession
            .name(UPDATED_NAME)
            .sourceFile(UPDATED_SOURCE_FILE)
            .sourceFileContentType(UPDATED_SOURCE_FILE_CONTENT_TYPE)
            .url(UPDATED_URL);

        restAnalysisSessionMockMvc.perform(put("/api/analysis-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalysisSession)))
            .andExpect(status().isOk());

        // Validate the AnalysisSession in the database
        List<AnalysisSession> analysisSessionList = analysisSessionRepository.findAll();
        assertThat(analysisSessionList).hasSize(databaseSizeBeforeUpdate);
        AnalysisSession testAnalysisSession = analysisSessionList.get(analysisSessionList.size() - 1);
        assertThat(testAnalysisSession.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAnalysisSession.getSourceFile()).isEqualTo(UPDATED_SOURCE_FILE);
        assertThat(testAnalysisSession.getSourceFileContentType()).isEqualTo(UPDATED_SOURCE_FILE_CONTENT_TYPE);
        assertThat(testAnalysisSession.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the AnalysisSession in Elasticsearch
        verify(mockAnalysisSessionSearchRepository, times(1)).save(testAnalysisSession);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalysisSession() throws Exception {
        int databaseSizeBeforeUpdate = analysisSessionRepository.findAll().size();

        // Create the AnalysisSession

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalysisSessionMockMvc.perform(put("/api/analysis-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisSession)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisSession in the database
        List<AnalysisSession> analysisSessionList = analysisSessionRepository.findAll();
        assertThat(analysisSessionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalysisSession in Elasticsearch
        verify(mockAnalysisSessionSearchRepository, times(0)).save(analysisSession);
    }

    @Test
    @Transactional
    public void deleteAnalysisSession() throws Exception {
        // Initialize the database
        analysisSessionService.save(analysisSession);

        int databaseSizeBeforeDelete = analysisSessionRepository.findAll().size();

        // Delete the analysisSession
        restAnalysisSessionMockMvc.perform(delete("/api/analysis-sessions/{id}", analysisSession.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AnalysisSession> analysisSessionList = analysisSessionRepository.findAll();
        assertThat(analysisSessionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnalysisSession in Elasticsearch
        verify(mockAnalysisSessionSearchRepository, times(1)).deleteById(analysisSession.getId());
    }

    @Test
    @Transactional
    public void searchAnalysisSession() throws Exception {
        // Initialize the database
        analysisSessionService.save(analysisSession);
        when(mockAnalysisSessionSearchRepository.search(queryStringQuery("id:" + analysisSession.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(analysisSession), PageRequest.of(0, 1), 1));
        // Search the analysisSession
        restAnalysisSessionMockMvc.perform(get("/api/_search/analysis-sessions?query=id:" + analysisSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalysisSession.class);
        AnalysisSession analysisSession1 = new AnalysisSession();
        analysisSession1.setId(1L);
        AnalysisSession analysisSession2 = new AnalysisSession();
        analysisSession2.setId(analysisSession1.getId());
        assertThat(analysisSession1).isEqualTo(analysisSession2);
        analysisSession2.setId(2L);
        assertThat(analysisSession1).isNotEqualTo(analysisSession2);
        analysisSession1.setId(null);
        assertThat(analysisSession1).isNotEqualTo(analysisSession2);
    }
}
