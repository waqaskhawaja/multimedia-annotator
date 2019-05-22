package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;

import pk.waqaskhawaja.ma.domain.Annotation;
import pk.waqaskhawaja.ma.domain.AnnotationSession;
import pk.waqaskhawaja.ma.domain.InteractionRecord;
import pk.waqaskhawaja.ma.repository.AnnotationRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationSearchRepository;
import pk.waqaskhawaja.ma.service.AnnotationService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;
import pk.waqaskhawaja.ma.service.dto.AnnotationCriteria;
import pk.waqaskhawaja.ma.service.AnnotationQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the AnnotationResource REST controller.
 *
 * @see AnnotationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaApp.class)
public class AnnotationResourceIntTest {

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ANNOTATION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ANNOTATION_TEXT = "BBBBBBBBBB";

    @Autowired
    private AnnotationRepository annotationRepository;

    @Mock
    private AnnotationRepository annotationRepositoryMock;

    @Mock
    private AnnotationService annotationServiceMock;

    @Autowired
    private AnnotationService annotationService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.AnnotationSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnnotationSearchRepository mockAnnotationSearchRepository;

    @Autowired
    private AnnotationQueryService annotationQueryService;

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

    private MockMvc restAnnotationMockMvc;

    private Annotation annotation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnnotationResource annotationResource = new AnnotationResource(annotationService, annotationQueryService);
        this.restAnnotationMockMvc = MockMvcBuilders.standaloneSetup(annotationResource)
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
    public static Annotation createEntity(EntityManager em) {
        Annotation annotation = new Annotation()
            .start(DEFAULT_START)
            .end(DEFAULT_END)
            .annotationText(DEFAULT_ANNOTATION_TEXT);
        return annotation;
    }

    @Before
    public void initTest() {
        annotation = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnnotation() throws Exception {
        int databaseSizeBeforeCreate = annotationRepository.findAll().size();

        // Create the Annotation
        restAnnotationMockMvc.perform(post("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotation)))
            .andExpect(status().isCreated());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeCreate + 1);
        Annotation testAnnotation = annotationList.get(annotationList.size() - 1);
        assertThat(testAnnotation.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testAnnotation.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testAnnotation.getAnnotationText()).isEqualTo(DEFAULT_ANNOTATION_TEXT);

        // Validate the Annotation in Elasticsearch
        verify(mockAnnotationSearchRepository, times(1)).save(testAnnotation);
    }

    @Test
    @Transactional
    public void createAnnotationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = annotationRepository.findAll().size();

        // Create the Annotation with an existing ID
        annotation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnotationMockMvc.perform(post("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotation)))
            .andExpect(status().isBadRequest());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Annotation in Elasticsearch
        verify(mockAnnotationSearchRepository, times(0)).save(annotation);
    }

    @Test
    @Transactional
    public void getAllAnnotations() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList
        restAnnotationMockMvc.perform(get("/api/annotations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].annotationText").value(hasItem(DEFAULT_ANNOTATION_TEXT.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllAnnotationsWithEagerRelationshipsIsEnabled() throws Exception {
        AnnotationResource annotationResource = new AnnotationResource(annotationServiceMock, annotationQueryService);
        when(annotationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restAnnotationMockMvc = MockMvcBuilders.standaloneSetup(annotationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restAnnotationMockMvc.perform(get("/api/annotations?eagerload=true"))
        .andExpect(status().isOk());

        verify(annotationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllAnnotationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        AnnotationResource annotationResource = new AnnotationResource(annotationServiceMock, annotationQueryService);
            when(annotationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restAnnotationMockMvc = MockMvcBuilders.standaloneSetup(annotationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restAnnotationMockMvc.perform(get("/api/annotations?eagerload=true"))
        .andExpect(status().isOk());

            verify(annotationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getAnnotation() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get the annotation
        restAnnotationMockMvc.perform(get("/api/annotations/{id}", annotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(annotation.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()))
            .andExpect(jsonPath("$.annotationText").value(DEFAULT_ANNOTATION_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start equals to DEFAULT_START
        defaultAnnotationShouldBeFound("start.equals=" + DEFAULT_START);

        // Get all the annotationList where start equals to UPDATED_START
        defaultAnnotationShouldNotBeFound("start.equals=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsInShouldWork() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start in DEFAULT_START or UPDATED_START
        defaultAnnotationShouldBeFound("start.in=" + DEFAULT_START + "," + UPDATED_START);

        // Get all the annotationList where start equals to UPDATED_START
        defaultAnnotationShouldNotBeFound("start.in=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start is not null
        defaultAnnotationShouldBeFound("start.specified=true");

        // Get all the annotationList where start is null
        defaultAnnotationShouldNotBeFound("start.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationsByEndIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where end equals to DEFAULT_END
        defaultAnnotationShouldBeFound("end.equals=" + DEFAULT_END);

        // Get all the annotationList where end equals to UPDATED_END
        defaultAnnotationShouldNotBeFound("end.equals=" + UPDATED_END);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByEndIsInShouldWork() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where end in DEFAULT_END or UPDATED_END
        defaultAnnotationShouldBeFound("end.in=" + DEFAULT_END + "," + UPDATED_END);

        // Get all the annotationList where end equals to UPDATED_END
        defaultAnnotationShouldNotBeFound("end.in=" + UPDATED_END);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where end is not null
        defaultAnnotationShouldBeFound("end.specified=true");

        // Get all the annotationList where end is null
        defaultAnnotationShouldNotBeFound("end.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationsByAnnotationTextIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where annotationText equals to DEFAULT_ANNOTATION_TEXT
        defaultAnnotationShouldBeFound("annotationText.equals=" + DEFAULT_ANNOTATION_TEXT);

        // Get all the annotationList where annotationText equals to UPDATED_ANNOTATION_TEXT
        defaultAnnotationShouldNotBeFound("annotationText.equals=" + UPDATED_ANNOTATION_TEXT);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByAnnotationTextIsInShouldWork() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where annotationText in DEFAULT_ANNOTATION_TEXT or UPDATED_ANNOTATION_TEXT
        defaultAnnotationShouldBeFound("annotationText.in=" + DEFAULT_ANNOTATION_TEXT + "," + UPDATED_ANNOTATION_TEXT);

        // Get all the annotationList where annotationText equals to UPDATED_ANNOTATION_TEXT
        defaultAnnotationShouldNotBeFound("annotationText.in=" + UPDATED_ANNOTATION_TEXT);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByAnnotationTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where annotationText is not null
        defaultAnnotationShouldBeFound("annotationText.specified=true");

        // Get all the annotationList where annotationText is null
        defaultAnnotationShouldNotBeFound("annotationText.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationsByAnnotationSessionIsEqualToSomething() throws Exception {
        // Initialize the database
        AnnotationSession annotationSession = AnnotationSessionResourceIntTest.createEntity(em);
        em.persist(annotationSession);
        em.flush();
        annotation.setAnnotationSession(annotationSession);
        annotationRepository.saveAndFlush(annotation);
        Long annotationSessionId = annotationSession.getId();

        // Get all the annotationList where annotationSession equals to annotationSessionId
        defaultAnnotationShouldBeFound("annotationSessionId.equals=" + annotationSessionId);

        // Get all the annotationList where annotationSession equals to annotationSessionId + 1
        defaultAnnotationShouldNotBeFound("annotationSessionId.equals=" + (annotationSessionId + 1));
    }


    @Test
    @Transactional
    public void getAllAnnotationsByInteractionRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        InteractionRecord interactionRecord = InteractionRecordResourceIntTest.createEntity(em);
        em.persist(interactionRecord);
        em.flush();
        annotation.addInteractionRecord(interactionRecord);
        annotationRepository.saveAndFlush(annotation);
        Long interactionRecordId = interactionRecord.getId();

        // Get all the annotationList where interactionRecord equals to interactionRecordId
        defaultAnnotationShouldBeFound("interactionRecordId.equals=" + interactionRecordId);

        // Get all the annotationList where interactionRecord equals to interactionRecordId + 1
        defaultAnnotationShouldNotBeFound("interactionRecordId.equals=" + (interactionRecordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAnnotationShouldBeFound(String filter) throws Exception {
        restAnnotationMockMvc.perform(get("/api/annotations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].annotationText").value(hasItem(DEFAULT_ANNOTATION_TEXT)));

        // Check, that the count call also returns 1
        restAnnotationMockMvc.perform(get("/api/annotations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAnnotationShouldNotBeFound(String filter) throws Exception {
        restAnnotationMockMvc.perform(get("/api/annotations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnotationMockMvc.perform(get("/api/annotations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnnotation() throws Exception {
        // Get the annotation
        restAnnotationMockMvc.perform(get("/api/annotations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnotation() throws Exception {
        // Initialize the database
        annotationService.save(annotation);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAnnotationSearchRepository);

        int databaseSizeBeforeUpdate = annotationRepository.findAll().size();

        // Update the annotation
        Annotation updatedAnnotation = annotationRepository.findById(annotation.getId()).get();
        // Disconnect from session so that the updates on updatedAnnotation are not directly saved in db
        em.detach(updatedAnnotation);
        updatedAnnotation
            .start(UPDATED_START)
            .end(UPDATED_END)
            .annotationText(UPDATED_ANNOTATION_TEXT);

        restAnnotationMockMvc.perform(put("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnnotation)))
            .andExpect(status().isOk());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeUpdate);
        Annotation testAnnotation = annotationList.get(annotationList.size() - 1);
        assertThat(testAnnotation.getStart()).isEqualTo(UPDATED_START);
        assertThat(testAnnotation.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testAnnotation.getAnnotationText()).isEqualTo(UPDATED_ANNOTATION_TEXT);

        // Validate the Annotation in Elasticsearch
        verify(mockAnnotationSearchRepository, times(1)).save(testAnnotation);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnotation() throws Exception {
        int databaseSizeBeforeUpdate = annotationRepository.findAll().size();

        // Create the Annotation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnotationMockMvc.perform(put("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annotation)))
            .andExpect(status().isBadRequest());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Annotation in Elasticsearch
        verify(mockAnnotationSearchRepository, times(0)).save(annotation);
    }

    @Test
    @Transactional
    public void deleteAnnotation() throws Exception {
        // Initialize the database
        annotationService.save(annotation);

        int databaseSizeBeforeDelete = annotationRepository.findAll().size();

        // Delete the annotation
        restAnnotationMockMvc.perform(delete("/api/annotations/{id}", annotation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Annotation in Elasticsearch
        verify(mockAnnotationSearchRepository, times(1)).deleteById(annotation.getId());
    }

    @Test
    @Transactional
    public void searchAnnotation() throws Exception {
        // Initialize the database
        annotationService.save(annotation);
        when(mockAnnotationSearchRepository.search(queryStringQuery("id:" + annotation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(annotation), PageRequest.of(0, 1), 1));
        // Search the annotation
        restAnnotationMockMvc.perform(get("/api/_search/annotations?query=id:" + annotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].annotationText").value(hasItem(DEFAULT_ANNOTATION_TEXT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annotation.class);
        Annotation annotation1 = new Annotation();
        annotation1.setId(1L);
        Annotation annotation2 = new Annotation();
        annotation2.setId(annotation1.getId());
        assertThat(annotation1).isEqualTo(annotation2);
        annotation2.setId(2L);
        assertThat(annotation1).isNotEqualTo(annotation2);
        annotation1.setId(null);
        assertThat(annotation1).isNotEqualTo(annotation2);
    }
}
