package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.ResourceType;
import pk.waqaskhawaja.ma.repository.ResourceTypeRepository;
import pk.waqaskhawaja.ma.repository.search.ResourceTypeSearchRepository;
import pk.waqaskhawaja.ma.service.ResourceTypeService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.ResourceTypeCriteria;
import pk.waqaskhawaja.ma.service.ResourceTypeQueryService;

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
 * Test class for the ResourceTypeResource REST controller.
 *
 * @see ResourceTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class ResourceTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ResourceTypeRepository resourceTypeRepository;

    @Autowired
    private ResourceTypeService resourceTypeService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.ResourceTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ResourceTypeSearchRepository mockResourceTypeSearchRepository;

    @Autowired
    private ResourceTypeQueryService resourceTypeQueryService;

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

    private MockMvc restResourceTypeMockMvc;

    private ResourceType resourceType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResourceTypeResource resourceTypeResource = new ResourceTypeResource(resourceTypeService, resourceTypeQueryService);
        this.restResourceTypeMockMvc = MockMvcBuilders.standaloneSetup(resourceTypeResource)
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
    public static ResourceType createEntity(EntityManager em) {
        ResourceType resourceType = new ResourceType()
            .name(DEFAULT_NAME);
        return resourceType;
    }

    @Before
    public void initTest() {
        resourceType = createEntity(em);
    }

    @Test
    @Transactional
    public void createResourceType() throws Exception {
        int databaseSizeBeforeCreate = resourceTypeRepository.findAll().size();

        // Create the ResourceType
        restResourceTypeMockMvc.perform(post("/api/resource-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceType)))
            .andExpect(status().isCreated());

        // Validate the ResourceType in the database
        List<ResourceType> resourceTypeList = resourceTypeRepository.findAll();
        assertThat(resourceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceType testResourceType = resourceTypeList.get(resourceTypeList.size() - 1);
        assertThat(testResourceType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ResourceType in Elasticsearch
        verify(mockResourceTypeSearchRepository, times(1)).save(testResourceType);
    }

    @Test
    @Transactional
    public void createResourceTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceTypeRepository.findAll().size();

        // Create the ResourceType with an existing ID
        resourceType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceTypeMockMvc.perform(post("/api/resource-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceType)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceType in the database
        List<ResourceType> resourceTypeList = resourceTypeRepository.findAll();
        assertThat(resourceTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ResourceType in Elasticsearch
        verify(mockResourceTypeSearchRepository, times(0)).save(resourceType);
    }

    @Test
    @Transactional
    public void getAllResourceTypes() throws Exception {
        // Initialize the database
        resourceTypeRepository.saveAndFlush(resourceType);

        // Get all the resourceTypeList
        restResourceTypeMockMvc.perform(get("/api/resource-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getResourceType() throws Exception {
        // Initialize the database
        resourceTypeRepository.saveAndFlush(resourceType);

        // Get the resourceType
        restResourceTypeMockMvc.perform(get("/api/resource-types/{id}", resourceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resourceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllResourceTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        resourceTypeRepository.saveAndFlush(resourceType);

        // Get all the resourceTypeList where name equals to DEFAULT_NAME
        defaultResourceTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the resourceTypeList where name equals to UPDATED_NAME
        defaultResourceTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllResourceTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        resourceTypeRepository.saveAndFlush(resourceType);

        // Get all the resourceTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultResourceTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the resourceTypeList where name equals to UPDATED_NAME
        defaultResourceTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllResourceTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        resourceTypeRepository.saveAndFlush(resourceType);

        // Get all the resourceTypeList where name is not null
        defaultResourceTypeShouldBeFound("name.specified=true");

        // Get all the resourceTypeList where name is null
        defaultResourceTypeShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultResourceTypeShouldBeFound(String filter) throws Exception {
        restResourceTypeMockMvc.perform(get("/api/resource-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restResourceTypeMockMvc.perform(get("/api/resource-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultResourceTypeShouldNotBeFound(String filter) throws Exception {
        restResourceTypeMockMvc.perform(get("/api/resource-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResourceTypeMockMvc.perform(get("/api/resource-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingResourceType() throws Exception {
        // Get the resourceType
        restResourceTypeMockMvc.perform(get("/api/resource-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResourceType() throws Exception {
        // Initialize the database
        resourceTypeService.save(resourceType);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockResourceTypeSearchRepository);

        int databaseSizeBeforeUpdate = resourceTypeRepository.findAll().size();

        // Update the resourceType
        ResourceType updatedResourceType = resourceTypeRepository.findById(resourceType.getId()).get();
        // Disconnect from session so that the updates on updatedResourceType are not directly saved in db
        em.detach(updatedResourceType);
        updatedResourceType
            .name(UPDATED_NAME);

        restResourceTypeMockMvc.perform(put("/api/resource-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResourceType)))
            .andExpect(status().isOk());

        // Validate the ResourceType in the database
        List<ResourceType> resourceTypeList = resourceTypeRepository.findAll();
        assertThat(resourceTypeList).hasSize(databaseSizeBeforeUpdate);
        ResourceType testResourceType = resourceTypeList.get(resourceTypeList.size() - 1);
        assertThat(testResourceType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ResourceType in Elasticsearch
        verify(mockResourceTypeSearchRepository, times(1)).save(testResourceType);
    }

    @Test
    @Transactional
    public void updateNonExistingResourceType() throws Exception {
        int databaseSizeBeforeUpdate = resourceTypeRepository.findAll().size();

        // Create the ResourceType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceTypeMockMvc.perform(put("/api/resource-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceType)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceType in the database
        List<ResourceType> resourceTypeList = resourceTypeRepository.findAll();
        assertThat(resourceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ResourceType in Elasticsearch
        verify(mockResourceTypeSearchRepository, times(0)).save(resourceType);
    }

    @Test
    @Transactional
    public void deleteResourceType() throws Exception {
        // Initialize the database
        resourceTypeService.save(resourceType);

        int databaseSizeBeforeDelete = resourceTypeRepository.findAll().size();

        // Delete the resourceType
        restResourceTypeMockMvc.perform(delete("/api/resource-types/{id}", resourceType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ResourceType> resourceTypeList = resourceTypeRepository.findAll();
        assertThat(resourceTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ResourceType in Elasticsearch
        verify(mockResourceTypeSearchRepository, times(1)).deleteById(resourceType.getId());
    }

    @Test
    @Transactional
    public void searchResourceType() throws Exception {
        // Initialize the database
        resourceTypeService.save(resourceType);
        when(mockResourceTypeSearchRepository.search(queryStringQuery("id:" + resourceType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(resourceType), PageRequest.of(0, 1), 1));
        // Search the resourceType
        restResourceTypeMockMvc.perform(get("/api/_search/resource-types?query=id:" + resourceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceType.class);
        ResourceType resourceType1 = new ResourceType();
        resourceType1.setId(1L);
        ResourceType resourceType2 = new ResourceType();
        resourceType2.setId(resourceType1.getId());
        assertThat(resourceType1).isEqualTo(resourceType2);
        resourceType2.setId(2L);
        assertThat(resourceType1).isNotEqualTo(resourceType2);
        resourceType1.setId(null);
        assertThat(resourceType1).isNotEqualTo(resourceType2);
    }
}
