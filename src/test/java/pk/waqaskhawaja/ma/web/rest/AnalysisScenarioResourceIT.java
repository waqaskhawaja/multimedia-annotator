package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;
import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import pk.waqaskhawaja.ma.domain.AnalysisSession;
import pk.waqaskhawaja.ma.domain.DataSet;
import pk.waqaskhawaja.ma.repository.AnalysisScenarioRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisScenarioSearchRepository;
import pk.waqaskhawaja.ma.service.AnalysisScenarioService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.AnalysisScenarioCriteria;
import pk.waqaskhawaja.ma.service.AnalysisScenarioQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
 * Integration tests for the {@link AnalysisScenarioResource} REST controller.
 */
@SpringBootTest(classes = MaApp.class)
public class AnalysisScenarioResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AnalysisScenarioRepository analysisScenarioRepository;

    @Autowired
    private AnalysisScenarioService analysisScenarioService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.AnalysisScenarioSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnalysisScenarioSearchRepository mockAnalysisScenarioSearchRepository;

    @Autowired
    private AnalysisScenarioQueryService analysisScenarioQueryService;

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

    private MockMvc restAnalysisScenarioMockMvc;

    private AnalysisScenario analysisScenario;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnalysisScenarioResource analysisScenarioResource = new AnalysisScenarioResource(analysisScenarioService, analysisScenarioQueryService);
        this.restAnalysisScenarioMockMvc = MockMvcBuilders.standaloneSetup(analysisScenarioResource)
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
    public static AnalysisScenario createEntity(EntityManager em) {
        AnalysisScenario analysisScenario = new AnalysisScenario()
            .name(DEFAULT_NAME);
        return analysisScenario;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalysisScenario createUpdatedEntity(EntityManager em) {
        AnalysisScenario analysisScenario = new AnalysisScenario()
            .name(UPDATED_NAME);
        return analysisScenario;
    }

    @BeforeEach
    public void initTest() {
        analysisScenario = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalysisScenario() throws Exception {
        int databaseSizeBeforeCreate = analysisScenarioRepository.findAll().size();

        // Create the AnalysisScenario
        restAnalysisScenarioMockMvc.perform(post("/api/analysis-scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisScenario)))
            .andExpect(status().isCreated());

        // Validate the AnalysisScenario in the database
        List<AnalysisScenario> analysisScenarioList = analysisScenarioRepository.findAll();
        assertThat(analysisScenarioList).hasSize(databaseSizeBeforeCreate + 1);
        AnalysisScenario testAnalysisScenario = analysisScenarioList.get(analysisScenarioList.size() - 1);
        assertThat(testAnalysisScenario.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the AnalysisScenario in Elasticsearch
        verify(mockAnalysisScenarioSearchRepository, times(1)).save(testAnalysisScenario);
    }

    @Test
    @Transactional
    public void createAnalysisScenarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analysisScenarioRepository.findAll().size();

        // Create the AnalysisScenario with an existing ID
        analysisScenario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalysisScenarioMockMvc.perform(post("/api/analysis-scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisScenario)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisScenario in the database
        List<AnalysisScenario> analysisScenarioList = analysisScenarioRepository.findAll();
        assertThat(analysisScenarioList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnalysisScenario in Elasticsearch
        verify(mockAnalysisScenarioSearchRepository, times(0)).save(analysisScenario);
    }


    @Test
    @Transactional
    public void getAllAnalysisScenarios() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        // Get all the analysisScenarioList
        restAnalysisScenarioMockMvc.perform(get("/api/analysis-scenarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisScenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void getAnalysisScenario() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        // Get the analysisScenario
        restAnalysisScenarioMockMvc.perform(get("/api/analysis-scenarios/{id}", analysisScenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analysisScenario.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getAnalysisScenariosByIdFiltering() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        Long id = analysisScenario.getId();

        defaultAnalysisScenarioShouldBeFound("id.equals=" + id);
        defaultAnalysisScenarioShouldNotBeFound("id.notEquals=" + id);

        defaultAnalysisScenarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnalysisScenarioShouldNotBeFound("id.greaterThan=" + id);

        defaultAnalysisScenarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnalysisScenarioShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnalysisScenariosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        // Get all the analysisScenarioList where name equals to DEFAULT_NAME
        defaultAnalysisScenarioShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the analysisScenarioList where name equals to UPDATED_NAME
        defaultAnalysisScenarioShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnalysisScenariosByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        // Get all the analysisScenarioList where name not equals to DEFAULT_NAME
        defaultAnalysisScenarioShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the analysisScenarioList where name not equals to UPDATED_NAME
        defaultAnalysisScenarioShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnalysisScenariosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        // Get all the analysisScenarioList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAnalysisScenarioShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the analysisScenarioList where name equals to UPDATED_NAME
        defaultAnalysisScenarioShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnalysisScenariosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        // Get all the analysisScenarioList where name is not null
        defaultAnalysisScenarioShouldBeFound("name.specified=true");

        // Get all the analysisScenarioList where name is null
        defaultAnalysisScenarioShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnalysisScenariosByNameContainsSomething() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        // Get all the analysisScenarioList where name contains DEFAULT_NAME
        defaultAnalysisScenarioShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the analysisScenarioList where name contains UPDATED_NAME
        defaultAnalysisScenarioShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnalysisScenariosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);

        // Get all the analysisScenarioList where name does not contain DEFAULT_NAME
        defaultAnalysisScenarioShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the analysisScenarioList where name does not contain UPDATED_NAME
        defaultAnalysisScenarioShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


//    @Test
//    @Transactional
//    public void getAllAnalysisScenariosByAnalysisSessionIsEqualToSomething() throws Exception {
//        // Initialize the database
//        analysisScenarioRepository.saveAndFlush(analysisScenario);
//        AnalysisSession analysisSession = AnalysisSessionResourceIT.createEntity(em);
//        em.persist(analysisSession);
//        em.flush();
//        analysisScenario.addAnalysisSession(analysisSession);
//        analysisScenarioRepository.saveAndFlush(analysisScenario);
//        Long analysisSessionId = analysisSession.getId();
//
//        // Get all the analysisScenarioList where analysisSession equals to analysisSessionId
//        defaultAnalysisScenarioShouldBeFound("analysisSessionId.equals=" + analysisSessionId);
//
//        // Get all the analysisScenarioList where analysisSession equals to analysisSessionId + 1
//        defaultAnalysisScenarioShouldNotBeFound("analysisSessionId.equals=" + (analysisSessionId + 1));
//    }


    @Test
    @Transactional
    public void getAllAnalysisScenariosByDataSetIsEqualToSomething() throws Exception {
        // Initialize the database
        analysisScenarioRepository.saveAndFlush(analysisScenario);
        DataSet dataSet = DataSetResourceIT.createEntity(em);
        em.persist(dataSet);
        em.flush();
        analysisScenario.addDataSet(dataSet);
        analysisScenarioRepository.saveAndFlush(analysisScenario);
        Long dataSetId = dataSet.getId();

        // Get all the analysisScenarioList where dataSet equals to dataSetId
        defaultAnalysisScenarioShouldBeFound("dataSetId.equals=" + dataSetId);

        // Get all the analysisScenarioList where dataSet equals to dataSetId + 1
        defaultAnalysisScenarioShouldNotBeFound("dataSetId.equals=" + (dataSetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnalysisScenarioShouldBeFound(String filter) throws Exception {
        restAnalysisScenarioMockMvc.perform(get("/api/analysis-scenarios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisScenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAnalysisScenarioMockMvc.perform(get("/api/analysis-scenarios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnalysisScenarioShouldNotBeFound(String filter) throws Exception {
        restAnalysisScenarioMockMvc.perform(get("/api/analysis-scenarios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnalysisScenarioMockMvc.perform(get("/api/analysis-scenarios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnalysisScenario() throws Exception {
        // Get the analysisScenario
        restAnalysisScenarioMockMvc.perform(get("/api/analysis-scenarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalysisScenario() throws Exception {
        // Initialize the database
        analysisScenarioService.save(analysisScenario);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAnalysisScenarioSearchRepository);

        int databaseSizeBeforeUpdate = analysisScenarioRepository.findAll().size();

        // Update the analysisScenario
        AnalysisScenario updatedAnalysisScenario = analysisScenarioRepository.findById(analysisScenario.getId()).get();
        // Disconnect from session so that the updates on updatedAnalysisScenario are not directly saved in db
        em.detach(updatedAnalysisScenario);
        updatedAnalysisScenario
            .name(UPDATED_NAME);

        restAnalysisScenarioMockMvc.perform(put("/api/analysis-scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalysisScenario)))
            .andExpect(status().isOk());

        // Validate the AnalysisScenario in the database
        List<AnalysisScenario> analysisScenarioList = analysisScenarioRepository.findAll();
        assertThat(analysisScenarioList).hasSize(databaseSizeBeforeUpdate);
        AnalysisScenario testAnalysisScenario = analysisScenarioList.get(analysisScenarioList.size() - 1);
        assertThat(testAnalysisScenario.getName()).isEqualTo(UPDATED_NAME);

        // Validate the AnalysisScenario in Elasticsearch
        verify(mockAnalysisScenarioSearchRepository, times(1)).save(testAnalysisScenario);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalysisScenario() throws Exception {
        int databaseSizeBeforeUpdate = analysisScenarioRepository.findAll().size();

        // Create the AnalysisScenario

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalysisScenarioMockMvc.perform(put("/api/analysis-scenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisScenario)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisScenario in the database
        List<AnalysisScenario> analysisScenarioList = analysisScenarioRepository.findAll();
        assertThat(analysisScenarioList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalysisScenario in Elasticsearch
        verify(mockAnalysisScenarioSearchRepository, times(0)).save(analysisScenario);
    }

    @Test
    @Transactional
    public void deleteAnalysisScenario() throws Exception {
        // Initialize the database
        analysisScenarioService.save(analysisScenario);

        int databaseSizeBeforeDelete = analysisScenarioRepository.findAll().size();

        // Delete the analysisScenario
        restAnalysisScenarioMockMvc.perform(delete("/api/analysis-scenarios/{id}", analysisScenario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnalysisScenario> analysisScenarioList = analysisScenarioRepository.findAll();
        assertThat(analysisScenarioList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnalysisScenario in Elasticsearch
        verify(mockAnalysisScenarioSearchRepository, times(1)).deleteById(analysisScenario.getId());
    }

    @Test
    @Transactional
    public void searchAnalysisScenario() throws Exception {
        // Initialize the database
        analysisScenarioService.save(analysisScenario);
        when(mockAnalysisScenarioSearchRepository.search(queryStringQuery("id:" + analysisScenario.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(analysisScenario), PageRequest.of(0, 1), 1));
        // Search the analysisScenario
        restAnalysisScenarioMockMvc.perform(get("/api/_search/analysis-scenarios?query=id:" + analysisScenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisScenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
