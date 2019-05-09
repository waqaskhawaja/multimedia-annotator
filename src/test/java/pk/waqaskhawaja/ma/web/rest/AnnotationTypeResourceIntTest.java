package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.AnnotationType;
import pk.waqaskhawaja.ma.domain.AnnotationType;
import pk.waqaskhawaja.ma.repository.AnnotationTypeRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationTypeSearchRepository;
import pk.waqaskhawaja.ma.service.AnnotationTypeService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.AnnotationTypeCriteria;
import pk.waqaskhawaja.ma.service.AnnotationTypeQueryService;

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
 * Test class for the AnnotationTypeResource REST controller.
 *
 * @see AnnotationTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class AnnotationTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AnnotationTypeRepository annotationTypeRepository;

    @Autowired
    private AnnotationTypeService annotationTypeService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.AnnotationTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnnotationTypeSearchRepository mockAnnotationTypeSearchRepository;

    @Autowired
    private AnnotationTypeQueryService annotationTypeQueryService;

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

    private MockMvc restAnnotationTypeMockMvc;

    private AnnotationType annotationType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnnotationTypeResource annotationTypeResource = new AnnotationTypeResource(annotationTypeService, annotationTypeQueryService);
        this.restAnnotationTypeMockMvc = MockMvcBuilders.standaloneSetup(annotationTypeResource)
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
    public static AnnotationType createEntity(EntityManager em) {
        AnnotationType annotationType = new AnnotationType()
            .name(DEFAULT_NAME);
        return annotationType;
    }

    @Before
    public void initTest() {
        annotationType = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnnotationType() throws Exception {
        int databaseSizeBeforeCreate = annotationTypeRepository.findAll().size();

        // Create the AnnotationType
        restAnnotationTypeMockMvc.perform(post("/api/annotation-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotationType)))
            .andExpect(status().isCreated());

        // Validate the AnnotationType in the database
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        AnnotationType testAnnotationType = annotationTypeList.get(annotationTypeList.size() - 1);
        assertThat(testAnnotationType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the AnnotationType in Elasticsearch
        verify(mockAnnotationTypeSearchRepository, times(1)).save(testAnnotationType);
    }

    @Test
    @Transactional
    public void createAnnotationTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = annotationTypeRepository.findAll().size();

        // Create the AnnotationType with an existing ID
        annotationType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnotationTypeMockMvc.perform(post("/api/annotation-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotationType)))
            .andExpect(status().isBadRequest());

        // Validate the AnnotationType in the database
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnnotationType in Elasticsearch
        verify(mockAnnotationTypeSearchRepository, times(0)).save(annotationType);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypes() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList
        restAnnotationTypeMockMvc.perform(get("/api/annotation-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getAnnotationType() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get the annotationType
        restAnnotationTypeMockMvc.perform(get("/api/annotation-types/{id}", annotationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(annotationType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name equals to DEFAULT_NAME
        defaultAnnotationTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the annotationTypeList where name equals to UPDATED_NAME
        defaultAnnotationTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAnnotationTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the annotationTypeList where name equals to UPDATED_NAME
        defaultAnnotationTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name is not null
        defaultAnnotationTypeShouldBeFound("name.specified=true");

        // Get all the annotationTypeList where name is null
        defaultAnnotationTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        AnnotationType parent = AnnotationTypeResourceIntTest.createEntity(em);
        em.persist(parent);
        em.flush();
        annotationType.setParent(parent);
        annotationTypeRepository.saveAndFlush(annotationType);
        Long parentId = parent.getId();

        // Get all the annotationTypeList where parent equals to parentId
        defaultAnnotationTypeShouldBeFound("parentId.equals=" + parentId);

        // Get all the annotationTypeList where parent equals to parentId + 1
        defaultAnnotationTypeShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAnnotationTypeShouldBeFound(String filter) throws Exception {
        restAnnotationTypeMockMvc.perform(get("/api/annotation-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAnnotationTypeMockMvc.perform(get("/api/annotation-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAnnotationTypeShouldNotBeFound(String filter) throws Exception {
        restAnnotationTypeMockMvc.perform(get("/api/annotation-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnotationTypeMockMvc.perform(get("/api/annotation-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnnotationType() throws Exception {
        // Get the annotationType
        restAnnotationTypeMockMvc.perform(get("/api/annotation-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnotationType() throws Exception {
        // Initialize the database
        annotationTypeService.save(annotationType);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAnnotationTypeSearchRepository);

        int databaseSizeBeforeUpdate = annotationTypeRepository.findAll().size();

        // Update the annotationType
        AnnotationType updatedAnnotationType = annotationTypeRepository.findById(annotationType.getId()).get();
        // Disconnect from session so that the updates on updatedAnnotationType are not directly saved in db
        em.detach(updatedAnnotationType);
        updatedAnnotationType
            .name(UPDATED_NAME);

        restAnnotationTypeMockMvc.perform(put("/api/annotation-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnnotationType)))
            .andExpect(status().isOk());

        // Validate the AnnotationType in the database
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeUpdate);
        AnnotationType testAnnotationType = annotationTypeList.get(annotationTypeList.size() - 1);
        assertThat(testAnnotationType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the AnnotationType in Elasticsearch
        verify(mockAnnotationTypeSearchRepository, times(1)).save(testAnnotationType);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnotationType() throws Exception {
        int databaseSizeBeforeUpdate = annotationTypeRepository.findAll().size();

        // Create the AnnotationType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnotationTypeMockMvc.perform(put("/api/annotation-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotationType)))
            .andExpect(status().isBadRequest());

        // Validate the AnnotationType in the database
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnnotationType in Elasticsearch
        verify(mockAnnotationTypeSearchRepository, times(0)).save(annotationType);
    }

    @Test
    @Transactional
    public void deleteAnnotationType() throws Exception {
        // Initialize the database
        annotationTypeService.save(annotationType);

        int databaseSizeBeforeDelete = annotationTypeRepository.findAll().size();

        // Delete the annotationType
        restAnnotationTypeMockMvc.perform(delete("/api/annotation-types/{id}", annotationType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnnotationType in Elasticsearch
        verify(mockAnnotationTypeSearchRepository, times(1)).deleteById(annotationType.getId());
    }

    @Test
    @Transactional
    public void searchAnnotationType() throws Exception {
        // Initialize the database
        annotationTypeService.save(annotationType);
        when(mockAnnotationTypeSearchRepository.search(queryStringQuery("id:" + annotationType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(annotationType), PageRequest.of(0, 1), 1));
        // Search the annotationType
        restAnnotationTypeMockMvc.perform(get("/api/_search/annotation-types?query=id:" + annotationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnotationType.class);
        AnnotationType annotationType1 = new AnnotationType();
        annotationType1.setId(1L);
        AnnotationType annotationType2 = new AnnotationType();
        annotationType2.setId(annotationType1.getId());
        assertThat(annotationType1).isEqualTo(annotationType2);
        annotationType2.setId(2L);
        assertThat(annotationType1).isNotEqualTo(annotationType2);
        annotationType1.setId(null);
        assertThat(annotationType1).isNotEqualTo(annotationType2);
    }
}
