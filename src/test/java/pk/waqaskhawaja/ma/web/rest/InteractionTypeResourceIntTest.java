package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.InteractionType;
import pk.waqaskhawaja.ma.domain.DataRecord;
import pk.waqaskhawaja.ma.repository.InteractionTypeRepository;
import pk.waqaskhawaja.ma.repository.search.InteractionTypeSearchRepository;
import pk.waqaskhawaja.ma.service.InteractionTypeService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.InteractionTypeCriteria;
import pk.waqaskhawaja.ma.service.InteractionTypeQueryService;

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
 * Test class for the InteractionTypeResource REST controller.
 *
 * @see InteractionTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class InteractionTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private InteractionTypeRepository interactionTypeRepository;

    @Autowired
    private InteractionTypeService interactionTypeService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.InteractionTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private InteractionTypeSearchRepository mockInteractionTypeSearchRepository;

    @Autowired
    private InteractionTypeQueryService interactionTypeQueryService;

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

    private MockMvc restInteractionTypeMockMvc;

    private InteractionType interactionType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InteractionTypeResource interactionTypeResource = new InteractionTypeResource(interactionTypeService, interactionTypeQueryService);
        this.restInteractionTypeMockMvc = MockMvcBuilders.standaloneSetup(interactionTypeResource)
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
    public static InteractionType createEntity(EntityManager em) {
        InteractionType interactionType = new InteractionType()
            .name(DEFAULT_NAME);
        return interactionType;
    }

    @Before
    public void initTest() {
        interactionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createInteractionType() throws Exception {
        int databaseSizeBeforeCreate = interactionTypeRepository.findAll().size();

        // Create the InteractionType
        restInteractionTypeMockMvc.perform(post("/api/interaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interactionType)))
            .andExpect(status().isCreated());

        // Validate the InteractionType in the database
        List<InteractionType> interactionTypeList = interactionTypeRepository.findAll();
        assertThat(interactionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InteractionType testInteractionType = interactionTypeList.get(interactionTypeList.size() - 1);
        assertThat(testInteractionType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the InteractionType in Elasticsearch
        verify(mockInteractionTypeSearchRepository, times(1)).save(testInteractionType);
    }

    @Test
    @Transactional
    public void createInteractionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interactionTypeRepository.findAll().size();

        // Create the InteractionType with an existing ID
        interactionType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInteractionTypeMockMvc.perform(post("/api/interaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interactionType)))
            .andExpect(status().isBadRequest());

        // Validate the InteractionType in the database
        List<InteractionType> interactionTypeList = interactionTypeRepository.findAll();
        assertThat(interactionTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the InteractionType in Elasticsearch
        verify(mockInteractionTypeSearchRepository, times(0)).save(interactionType);
    }

    @Test
    @Transactional
    public void getAllInteractionTypes() throws Exception {
        // Initialize the database
        interactionTypeRepository.saveAndFlush(interactionType);

        // Get all the interactionTypeList
        restInteractionTypeMockMvc.perform(get("/api/interaction-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getInteractionType() throws Exception {
        // Initialize the database
        interactionTypeRepository.saveAndFlush(interactionType);

        // Get the interactionType
        restInteractionTypeMockMvc.perform(get("/api/interaction-types/{id}", interactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(interactionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllInteractionTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        interactionTypeRepository.saveAndFlush(interactionType);

        // Get all the interactionTypeList where name equals to DEFAULT_NAME
        defaultInteractionTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the interactionTypeList where name equals to UPDATED_NAME
        defaultInteractionTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInteractionTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        interactionTypeRepository.saveAndFlush(interactionType);

        // Get all the interactionTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultInteractionTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the interactionTypeList where name equals to UPDATED_NAME
        defaultInteractionTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInteractionTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        interactionTypeRepository.saveAndFlush(interactionType);

        // Get all the interactionTypeList where name is not null
        defaultInteractionTypeShouldBeFound("name.specified=true");

        // Get all the interactionTypeList where name is null
        defaultInteractionTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllInteractionTypesByDataRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        DataRecord dataRecord = DataRecordResourceIntTest.createEntity(em);
        em.persist(dataRecord);
        em.flush();
        interactionType.addDataRecord(dataRecord);
        interactionTypeRepository.saveAndFlush(interactionType);
        Long dataRecordId = dataRecord.getId();

        // Get all the interactionTypeList where dataRecord equals to dataRecordId
        defaultInteractionTypeShouldBeFound("dataRecordId.equals=" + dataRecordId);

        // Get all the interactionTypeList where dataRecord equals to dataRecordId + 1
        defaultInteractionTypeShouldNotBeFound("dataRecordId.equals=" + (dataRecordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInteractionTypeShouldBeFound(String filter) throws Exception {
        restInteractionTypeMockMvc.perform(get("/api/interaction-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restInteractionTypeMockMvc.perform(get("/api/interaction-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInteractionTypeShouldNotBeFound(String filter) throws Exception {
        restInteractionTypeMockMvc.perform(get("/api/interaction-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInteractionTypeMockMvc.perform(get("/api/interaction-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingInteractionType() throws Exception {
        // Get the interactionType
        restInteractionTypeMockMvc.perform(get("/api/interaction-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInteractionType() throws Exception {
        // Initialize the database
        interactionTypeService.save(interactionType);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockInteractionTypeSearchRepository);

        int databaseSizeBeforeUpdate = interactionTypeRepository.findAll().size();

        // Update the interactionType
        InteractionType updatedInteractionType = interactionTypeRepository.findById(interactionType.getId()).get();
        // Disconnect from session so that the updates on updatedInteractionType are not directly saved in db
        em.detach(updatedInteractionType);
        updatedInteractionType
            .name(UPDATED_NAME);

        restInteractionTypeMockMvc.perform(put("/api/interaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInteractionType)))
            .andExpect(status().isOk());

        // Validate the InteractionType in the database
        List<InteractionType> interactionTypeList = interactionTypeRepository.findAll();
        assertThat(interactionTypeList).hasSize(databaseSizeBeforeUpdate);
        InteractionType testInteractionType = interactionTypeList.get(interactionTypeList.size() - 1);
        assertThat(testInteractionType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the InteractionType in Elasticsearch
        verify(mockInteractionTypeSearchRepository, times(1)).save(testInteractionType);
    }

    @Test
    @Transactional
    public void updateNonExistingInteractionType() throws Exception {
        int databaseSizeBeforeUpdate = interactionTypeRepository.findAll().size();

        // Create the InteractionType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInteractionTypeMockMvc.perform(put("/api/interaction-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interactionType)))
            .andExpect(status().isBadRequest());

        // Validate the InteractionType in the database
        List<InteractionType> interactionTypeList = interactionTypeRepository.findAll();
        assertThat(interactionTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InteractionType in Elasticsearch
        verify(mockInteractionTypeSearchRepository, times(0)).save(interactionType);
    }

    @Test
    @Transactional
    public void deleteInteractionType() throws Exception {
        // Initialize the database
        interactionTypeService.save(interactionType);

        int databaseSizeBeforeDelete = interactionTypeRepository.findAll().size();

        // Delete the interactionType
        restInteractionTypeMockMvc.perform(delete("/api/interaction-types/{id}", interactionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InteractionType> interactionTypeList = interactionTypeRepository.findAll();
        assertThat(interactionTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InteractionType in Elasticsearch
        verify(mockInteractionTypeSearchRepository, times(1)).deleteById(interactionType.getId());
    }

    @Test
    @Transactional
    public void searchInteractionType() throws Exception {
        // Initialize the database
        interactionTypeService.save(interactionType);
        when(mockInteractionTypeSearchRepository.search(queryStringQuery("id:" + interactionType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(interactionType), PageRequest.of(0, 1), 1));
        // Search the interactionType
        restInteractionTypeMockMvc.perform(get("/api/_search/interaction-types?query=id:" + interactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InteractionType.class);
        InteractionType interactionType1 = new InteractionType();
        interactionType1.setId(1L);
        InteractionType interactionType2 = new InteractionType();
        interactionType2.setId(interactionType1.getId());
        assertThat(interactionType1).isEqualTo(interactionType2);
        interactionType2.setId(2L);
        assertThat(interactionType1).isNotEqualTo(interactionType2);
        interactionType1.setId(null);
        assertThat(interactionType1).isNotEqualTo(interactionType2);
    }
}
