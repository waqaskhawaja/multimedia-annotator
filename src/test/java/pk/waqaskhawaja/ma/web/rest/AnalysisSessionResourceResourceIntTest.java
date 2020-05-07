package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import pk.waqaskhawaja.ma.domain.ResourceType;
import pk.waqaskhawaja.ma.domain.AnalysisSession;
import pk.waqaskhawaja.ma.repository.AnalysisSessionResourceRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisSessionResourceSearchRepository;
import pk.waqaskhawaja.ma.service.*;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;

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
 * Test class for the AnalysisSessionResourceResource REST controller.
 *
 * @see AnalysisSessionResourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class AnalysisSessionResourceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SOURCE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SOURCE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SOURCE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SOURCE_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private AnalysisSessionResourceRepository analysisSessionResourceRepository;

    @Autowired
    private AnalysisSessionResourceService analysisSessionResourceService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.AnalysisSessionResourceSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnalysisSessionResourceSearchRepository mockAnalysisSessionResourceSearchRepository;

    @Autowired
    private AnalysisSessionResourceQueryService analysisSessionResourceQueryService;

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

    @Autowired
    private InteractionRecordService interactionRecordService;

    @Autowired
    private InteractionTypeService interactionTypeService;

    @Autowired
    private DataSetService dataSetService;

    private MockMvc restAnalysisSessionResourceMockMvc;

    private AnalysisSessionResource analysisSessionResource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnalysisSessionResourceResource analysisSessionResourceResource = new AnalysisSessionResourceResource(
            analysisSessionResourceService, analysisSessionResourceQueryService, interactionRecordService, interactionTypeService,dataSetService);
        this.restAnalysisSessionResourceMockMvc = MockMvcBuilders.standaloneSetup(analysisSessionResourceResource)
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
    public static AnalysisSessionResource createEntity(EntityManager em) {
        AnalysisSessionResource analysisSessionResource = new AnalysisSessionResource()
            .name(DEFAULT_NAME)
            .sourceFile(DEFAULT_SOURCE_FILE)
            .sourceFileContentType(DEFAULT_SOURCE_FILE_CONTENT_TYPE)
            .url(DEFAULT_URL);
        return analysisSessionResource;
    }

    @Before
    public void initTest() {
        analysisSessionResource = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalysisSessionResource() throws Exception {
        int databaseSizeBeforeCreate = analysisSessionResourceRepository.findAll().size();

        // Create the AnalysisSessionResource
        restAnalysisSessionResourceMockMvc.perform(post("/api/analysis-session-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisSessionResource)))
            .andExpect(status().isCreated());

        // Validate the AnalysisSessionResource in the database
        List<AnalysisSessionResource> analysisSessionResourceList = analysisSessionResourceRepository.findAll();
        assertThat(analysisSessionResourceList).hasSize(databaseSizeBeforeCreate + 1);
        AnalysisSessionResource testAnalysisSessionResource = analysisSessionResourceList.get(analysisSessionResourceList.size() - 1);
        assertThat(testAnalysisSessionResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAnalysisSessionResource.getSourceFile()).isEqualTo(DEFAULT_SOURCE_FILE);
        assertThat(testAnalysisSessionResource.getSourceFileContentType()).isEqualTo(DEFAULT_SOURCE_FILE_CONTENT_TYPE);
        assertThat(testAnalysisSessionResource.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the AnalysisSessionResource in Elasticsearch
        verify(mockAnalysisSessionResourceSearchRepository, times(1)).save(testAnalysisSessionResource);
    }

    @Test
    @Transactional
    public void createAnalysisSessionResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analysisSessionResourceRepository.findAll().size();

        // Create the AnalysisSessionResource with an existing ID
        analysisSessionResource.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalysisSessionResourceMockMvc.perform(post("/api/analysis-session-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisSessionResource)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisSessionResource in the database
        List<AnalysisSessionResource> analysisSessionResourceList = analysisSessionResourceRepository.findAll();
        assertThat(analysisSessionResourceList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnalysisSessionResource in Elasticsearch
        verify(mockAnalysisSessionResourceSearchRepository, times(0)).save(analysisSessionResource);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionResources() throws Exception {
        // Initialize the database
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);

        // Get all the analysisSessionResourceList
        restAnalysisSessionResourceMockMvc.perform(get("/api/analysis-session-resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisSessionResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getAnalysisSessionResource() throws Exception {
        // Initialize the database
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);

        // Get the analysisSessionResource
        restAnalysisSessionResourceMockMvc.perform(get("/api/analysis-session-resources/{id}", analysisSessionResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analysisSessionResource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sourceFileContentType").value(DEFAULT_SOURCE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.sourceFile").value(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionResourcesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);

        // Get all the analysisSessionResourceList where name equals to DEFAULT_NAME
        defaultAnalysisSessionResourceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the analysisSessionResourceList where name equals to UPDATED_NAME
        defaultAnalysisSessionResourceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionResourcesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);

        // Get all the analysisSessionResourceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAnalysisSessionResourceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the analysisSessionResourceList where name equals to UPDATED_NAME
        defaultAnalysisSessionResourceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionResourcesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);

        // Get all the analysisSessionResourceList where name is not null
        defaultAnalysisSessionResourceShouldBeFound("name.specified=true");

        // Get all the analysisSessionResourceList where name is null
        defaultAnalysisSessionResourceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionResourcesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);

        // Get all the analysisSessionResourceList where url equals to DEFAULT_URL
        defaultAnalysisSessionResourceShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the analysisSessionResourceList where url equals to UPDATED_URL
        defaultAnalysisSessionResourceShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionResourcesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);

        // Get all the analysisSessionResourceList where url in DEFAULT_URL or UPDATED_URL
        defaultAnalysisSessionResourceShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the analysisSessionResourceList where url equals to UPDATED_URL
        defaultAnalysisSessionResourceShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionResourcesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);

        // Get all the analysisSessionResourceList where url is not null
        defaultAnalysisSessionResourceShouldBeFound("url.specified=true");

        // Get all the analysisSessionResourceList where url is null
        defaultAnalysisSessionResourceShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnalysisSessionResourcesByResourceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ResourceType resourceType = ResourceTypeResourceIntTest.createEntity(em);
        em.persist(resourceType);
        em.flush();
        analysisSessionResource.setResourceType(resourceType);
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);
        Long resourceTypeId = resourceType.getId();

        // Get all the analysisSessionResourceList where resourceType equals to resourceTypeId
        defaultAnalysisSessionResourceShouldBeFound("resourceTypeId.equals=" + resourceTypeId);

        // Get all the analysisSessionResourceList where resourceType equals to resourceTypeId + 1
        defaultAnalysisSessionResourceShouldNotBeFound("resourceTypeId.equals=" + (resourceTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllAnalysisSessionResourcesByAnalysisSessionIsEqualToSomething() throws Exception {
        // Initialize the database
        AnalysisSession analysisSession = AnalysisSessionResourceIntTest.createEntity(em);
        em.persist(analysisSession);
        em.flush();
        analysisSessionResource.setAnalysisSession(analysisSession);
        analysisSessionResourceRepository.saveAndFlush(analysisSessionResource);
        Long analysisSessionId = analysisSession.getId();

        // Get all the analysisSessionResourceList where analysisSession equals to analysisSessionId
        defaultAnalysisSessionResourceShouldBeFound("analysisSessionId.equals=" + analysisSessionId);

        // Get all the analysisSessionResourceList where analysisSession equals to analysisSessionId + 1
        defaultAnalysisSessionResourceShouldNotBeFound("analysisSessionId.equals=" + (analysisSessionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAnalysisSessionResourceShouldBeFound(String filter) throws Exception {
        restAnalysisSessionResourceMockMvc.perform(get("/api/analysis-session-resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisSessionResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restAnalysisSessionResourceMockMvc.perform(get("/api/analysis-session-resources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAnalysisSessionResourceShouldNotBeFound(String filter) throws Exception {
        restAnalysisSessionResourceMockMvc.perform(get("/api/analysis-session-resources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnalysisSessionResourceMockMvc.perform(get("/api/analysis-session-resources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnalysisSessionResource() throws Exception {
        // Get the analysisSessionResource
        restAnalysisSessionResourceMockMvc.perform(get("/api/analysis-session-resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalysisSessionResource() throws Exception {
        // Initialize the database
        analysisSessionResourceService.save(analysisSessionResource);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAnalysisSessionResourceSearchRepository);

        int databaseSizeBeforeUpdate = analysisSessionResourceRepository.findAll().size();

        // Update the analysisSessionResource
        AnalysisSessionResource updatedAnalysisSessionResource = analysisSessionResourceRepository.findById(analysisSessionResource.getId()).get();
        // Disconnect from session so that the updates on updatedAnalysisSessionResource are not directly saved in db
        em.detach(updatedAnalysisSessionResource);
        updatedAnalysisSessionResource
            .name(UPDATED_NAME)
            .sourceFile(UPDATED_SOURCE_FILE)
            .sourceFileContentType(UPDATED_SOURCE_FILE_CONTENT_TYPE)
            .url(UPDATED_URL);

        restAnalysisSessionResourceMockMvc.perform(put("/api/analysis-session-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalysisSessionResource)))
            .andExpect(status().isOk());

        // Validate the AnalysisSessionResource in the database
        List<AnalysisSessionResource> analysisSessionResourceList = analysisSessionResourceRepository.findAll();
        assertThat(analysisSessionResourceList).hasSize(databaseSizeBeforeUpdate);
        AnalysisSessionResource testAnalysisSessionResource = analysisSessionResourceList.get(analysisSessionResourceList.size() - 1);
        assertThat(testAnalysisSessionResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAnalysisSessionResource.getSourceFile()).isEqualTo(UPDATED_SOURCE_FILE);
        assertThat(testAnalysisSessionResource.getSourceFileContentType()).isEqualTo(UPDATED_SOURCE_FILE_CONTENT_TYPE);
        assertThat(testAnalysisSessionResource.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the AnalysisSessionResource in Elasticsearch
        verify(mockAnalysisSessionResourceSearchRepository, times(1)).save(testAnalysisSessionResource);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalysisSessionResource() throws Exception {
        int databaseSizeBeforeUpdate = analysisSessionResourceRepository.findAll().size();

        // Create the AnalysisSessionResource

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalysisSessionResourceMockMvc.perform(put("/api/analysis-session-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysisSessionResource)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisSessionResource in the database
        List<AnalysisSessionResource> analysisSessionResourceList = analysisSessionResourceRepository.findAll();
        assertThat(analysisSessionResourceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnalysisSessionResource in Elasticsearch
        verify(mockAnalysisSessionResourceSearchRepository, times(0)).save(analysisSessionResource);
    }

    @Test
    @Transactional
    public void deleteAnalysisSessionResource() throws Exception {
        // Initialize the database
        analysisSessionResourceService.save(analysisSessionResource);

        int databaseSizeBeforeDelete = analysisSessionResourceRepository.findAll().size();

        // Delete the analysisSessionResource
        restAnalysisSessionResourceMockMvc.perform(delete("/api/analysis-session-resources/{id}", analysisSessionResource.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AnalysisSessionResource> analysisSessionResourceList = analysisSessionResourceRepository.findAll();
        assertThat(analysisSessionResourceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnalysisSessionResource in Elasticsearch
        verify(mockAnalysisSessionResourceSearchRepository, times(1)).deleteById(analysisSessionResource.getId());
    }

    @Test
    @Transactional
    public void searchAnalysisSessionResource() throws Exception {
        // Initialize the database
        analysisSessionResourceService.save(analysisSessionResource);
        when(mockAnalysisSessionResourceSearchRepository.search(queryStringQuery("id:" + analysisSessionResource.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(analysisSessionResource), PageRequest.of(0, 1), 1));
        // Search the analysisSessionResource
        restAnalysisSessionResourceMockMvc.perform(get("/api/_search/analysis-session-resources?query=id:" + analysisSessionResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisSessionResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalysisSessionResource.class);
        AnalysisSessionResource analysisSessionResource1 = new AnalysisSessionResource();
        analysisSessionResource1.setId(1L);
        AnalysisSessionResource analysisSessionResource2 = new AnalysisSessionResource();
        analysisSessionResource2.setId(analysisSessionResource1.getId());
        assertThat(analysisSessionResource1).isEqualTo(analysisSessionResource2);
        analysisSessionResource2.setId(2L);
        assertThat(analysisSessionResource1).isNotEqualTo(analysisSessionResource2);
        analysisSessionResource1.setId(null);
        assertThat(analysisSessionResource1).isNotEqualTo(analysisSessionResource2);
    }
}
