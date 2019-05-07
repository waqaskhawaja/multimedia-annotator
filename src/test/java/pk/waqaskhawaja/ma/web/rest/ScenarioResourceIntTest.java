package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.Scenario;
import pk.waqaskhawaja.ma.domain.Session;
import pk.waqaskhawaja.ma.repository.ScenarioRepository;
import pk.waqaskhawaja.ma.repository.search.ScenarioSearchRepository;
import pk.waqaskhawaja.ma.service.ScenarioService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.ScenarioCriteria;
import pk.waqaskhawaja.ma.service.ScenarioQueryService;

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
 * Test class for the ScenarioResource REST controller.
 *
 * @see ScenarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class ScenarioResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private ScenarioService scenarioService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.ScenarioSearchRepositoryMockConfiguration
     */
    @Autowired
    private ScenarioSearchRepository mockScenarioSearchRepository;

    @Autowired
    private ScenarioQueryService scenarioQueryService;

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

    private MockMvc restScenarioMockMvc;

    private Scenario scenario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScenarioResource scenarioResource = new ScenarioResource(scenarioService, scenarioQueryService);
        this.restScenarioMockMvc = MockMvcBuilders.standaloneSetup(scenarioResource)
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
    public static Scenario createEntity(EntityManager em) {
        Scenario scenario = new Scenario()
            .name(DEFAULT_NAME);
        return scenario;
    }

    @Before
    public void initTest() {
        scenario = createEntity(em);
    }

    @Test
    @Transactional
    public void createScenario() throws Exception {
        int databaseSizeBeforeCreate = scenarioRepository.findAll().size();

        // Create the Scenario
        restScenarioMockMvc.perform(post("/api/scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scenario)))
            .andExpect(status().isCreated());

        // Validate the Scenario in the database
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeCreate + 1);
        Scenario testScenario = scenarioList.get(scenarioList.size() - 1);
        assertThat(testScenario.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Scenario in Elasticsearch
        verify(mockScenarioSearchRepository, times(1)).save(testScenario);
    }

    @Test
    @Transactional
    public void createScenarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scenarioRepository.findAll().size();

        // Create the Scenario with an existing ID
        scenario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScenarioMockMvc.perform(post("/api/scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scenario)))
            .andExpect(status().isBadRequest());

        // Validate the Scenario in the database
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeCreate);

        // Validate the Scenario in Elasticsearch
        verify(mockScenarioSearchRepository, times(0)).save(scenario);
    }

    @Test
    @Transactional
    public void getAllScenarios() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        // Get all the scenarioList
        restScenarioMockMvc.perform(get("/api/scenarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getScenario() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        // Get the scenario
        restScenarioMockMvc.perform(get("/api/scenarios/{id}", scenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scenario.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllScenariosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        // Get all the scenarioList where name equals to DEFAULT_NAME
        defaultScenarioShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the scenarioList where name equals to UPDATED_NAME
        defaultScenarioShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllScenariosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        // Get all the scenarioList where name in DEFAULT_NAME or UPDATED_NAME
        defaultScenarioShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the scenarioList where name equals to UPDATED_NAME
        defaultScenarioShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllScenariosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        scenarioRepository.saveAndFlush(scenario);

        // Get all the scenarioList where name is not null
        defaultScenarioShouldBeFound("name.specified=true");

        // Get all the scenarioList where name is null
        defaultScenarioShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllScenariosBySessionIsEqualToSomething() throws Exception {
        // Initialize the database
        Session session = SessionResourceIntTest.createEntity(em);
        em.persist(session);
        em.flush();
        scenario.addSession(session);
        scenarioRepository.saveAndFlush(scenario);
        Long sessionId = session.getId();

        // Get all the scenarioList where session equals to sessionId
        defaultScenarioShouldBeFound("sessionId.equals=" + sessionId);

        // Get all the scenarioList where session equals to sessionId + 1
        defaultScenarioShouldNotBeFound("sessionId.equals=" + (sessionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultScenarioShouldBeFound(String filter) throws Exception {
        restScenarioMockMvc.perform(get("/api/scenarios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restScenarioMockMvc.perform(get("/api/scenarios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultScenarioShouldNotBeFound(String filter) throws Exception {
        restScenarioMockMvc.perform(get("/api/scenarios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScenarioMockMvc.perform(get("/api/scenarios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingScenario() throws Exception {
        // Get the scenario
        restScenarioMockMvc.perform(get("/api/scenarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScenario() throws Exception {
        // Initialize the database
        scenarioService.save(scenario);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockScenarioSearchRepository);

        int databaseSizeBeforeUpdate = scenarioRepository.findAll().size();

        // Update the scenario
        Scenario updatedScenario = scenarioRepository.findById(scenario.getId()).get();
        // Disconnect from session so that the updates on updatedScenario are not directly saved in db
        em.detach(updatedScenario);
        updatedScenario
            .name(UPDATED_NAME);

        restScenarioMockMvc.perform(put("/api/scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScenario)))
            .andExpect(status().isOk());

        // Validate the Scenario in the database
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeUpdate);
        Scenario testScenario = scenarioList.get(scenarioList.size() - 1);
        assertThat(testScenario.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Scenario in Elasticsearch
        verify(mockScenarioSearchRepository, times(1)).save(testScenario);
    }

    @Test
    @Transactional
    public void updateNonExistingScenario() throws Exception {
        int databaseSizeBeforeUpdate = scenarioRepository.findAll().size();

        // Create the Scenario

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScenarioMockMvc.perform(put("/api/scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scenario)))
            .andExpect(status().isBadRequest());

        // Validate the Scenario in the database
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Scenario in Elasticsearch
        verify(mockScenarioSearchRepository, times(0)).save(scenario);
    }

    @Test
    @Transactional
    public void deleteScenario() throws Exception {
        // Initialize the database
        scenarioService.save(scenario);

        int databaseSizeBeforeDelete = scenarioRepository.findAll().size();

        // Delete the scenario
        restScenarioMockMvc.perform(delete("/api/scenarios/{id}", scenario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Scenario> scenarioList = scenarioRepository.findAll();
        assertThat(scenarioList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Scenario in Elasticsearch
        verify(mockScenarioSearchRepository, times(1)).deleteById(scenario.getId());
    }

    @Test
    @Transactional
    public void searchScenario() throws Exception {
        // Initialize the database
        scenarioService.save(scenario);
        when(mockScenarioSearchRepository.search(queryStringQuery("id:" + scenario.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(scenario), PageRequest.of(0, 1), 1));
        // Search the scenario
        restScenarioMockMvc.perform(get("/api/_search/scenarios?query=id:" + scenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scenario.class);
        Scenario scenario1 = new Scenario();
        scenario1.setId(1L);
        Scenario scenario2 = new Scenario();
        scenario2.setId(scenario1.getId());
        assertThat(scenario1).isEqualTo(scenario2);
        scenario2.setId(2L);
        assertThat(scenario1).isNotEqualTo(scenario2);
        scenario1.setId(null);
        assertThat(scenario1).isNotEqualTo(scenario2);
    }
}
