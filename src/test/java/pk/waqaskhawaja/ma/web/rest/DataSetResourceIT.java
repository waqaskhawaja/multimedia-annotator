package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;
import pk.waqaskhawaja.ma.domain.DataSet;
import pk.waqaskhawaja.ma.repository.DataSetRepository;
import pk.waqaskhawaja.ma.repository.search.DataSetSearchRepository;
import pk.waqaskhawaja.ma.service.DataSetService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link DataSetResource} REST controller.
 */
@SpringBootTest(classes = MaApp.class)
public class DataSetResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENTS = "AAAAAAAAAA";
    private static final String UPDATED_CONTENTS = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    @Autowired
    private DataSetRepository dataSetRepository;

    @Autowired
    private DataSetService dataSetService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.DataSetSearchRepositoryMockConfiguration
     */
    @Autowired
    private DataSetSearchRepository mockDataSetSearchRepository;

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

    private MockMvc restDataSetMockMvc;

    private DataSet dataSet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataSetResource dataSetResource = new DataSetResource(dataSetService);
        this.restDataSetMockMvc = MockMvcBuilders.standaloneSetup(dataSetResource)
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
    public static DataSet createEntity(EntityManager em) {
        DataSet dataSet = new DataSet()
            .title(DEFAULT_TITLE)
            .date(DEFAULT_DATE)
            .type(DEFAULT_TYPE)
            .contents(DEFAULT_CONTENTS)
            .identifier(DEFAULT_IDENTIFIER);
        return dataSet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSet createUpdatedEntity(EntityManager em) {
        DataSet dataSet = new DataSet()
            .title(UPDATED_TITLE)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .contents(UPDATED_CONTENTS)
            .identifier(UPDATED_IDENTIFIER);
        return dataSet;
    }

    @BeforeEach
    public void initTest() {
        dataSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataSet() throws Exception {
        int databaseSizeBeforeCreate = dataSetRepository.findAll().size();

        // Create the DataSet
        restDataSetMockMvc.perform(post("/api/data-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSet)))
            .andExpect(status().isCreated());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeCreate + 1);
        DataSet testDataSet = dataSetList.get(dataSetList.size() - 1);
        assertThat(testDataSet.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDataSet.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDataSet.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDataSet.getContents()).isEqualTo(DEFAULT_CONTENTS);
        assertThat(testDataSet.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the DataSet in Elasticsearch
        verify(mockDataSetSearchRepository, times(1)).save(testDataSet);
    }

    @Test
    @Transactional
    public void createDataSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataSetRepository.findAll().size();

        // Create the DataSet with an existing ID
        dataSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataSetMockMvc.perform(post("/api/data-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSet)))
            .andExpect(status().isBadRequest());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeCreate);

        // Validate the DataSet in Elasticsearch
        verify(mockDataSetSearchRepository, times(0)).save(dataSet);
    }


    @Test
    @Transactional
    public void getAllDataSets() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        // Get all the dataSetList
        restDataSetMockMvc.perform(get("/api/data-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].contents").value(hasItem(DEFAULT_CONTENTS.toString())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)));
    }

    @Test
    @Transactional
    public void getDataSet() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        // Get the dataSet
        restDataSetMockMvc.perform(get("/api/data-sets/{id}", dataSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataSet.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.contents").value(DEFAULT_CONTENTS.toString()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER));
    }

    @Test
    @Transactional
    public void getNonExistingDataSet() throws Exception {
        // Get the dataSet
        restDataSetMockMvc.perform(get("/api/data-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataSet() throws Exception {
        // Initialize the database
        dataSetService.save(dataSet);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDataSetSearchRepository);

        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();

        // Update the dataSet
        DataSet updatedDataSet = dataSetRepository.findById(dataSet.getId()).get();
        // Disconnect from session so that the updates on updatedDataSet are not directly saved in db
        em.detach(updatedDataSet);
        updatedDataSet
            .title(UPDATED_TITLE)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .contents(UPDATED_CONTENTS)
            .identifier(UPDATED_IDENTIFIER);

        restDataSetMockMvc.perform(put("/api/data-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataSet)))
            .andExpect(status().isOk());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);
        DataSet testDataSet = dataSetList.get(dataSetList.size() - 1);
        assertThat(testDataSet.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDataSet.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDataSet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDataSet.getContents()).isEqualTo(UPDATED_CONTENTS);
        assertThat(testDataSet.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the DataSet in Elasticsearch
        verify(mockDataSetSearchRepository, times(1)).save(testDataSet);
    }

    @Test
    @Transactional
    public void updateNonExistingDataSet() throws Exception {
        int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();

        // Create the DataSet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataSetMockMvc.perform(put("/api/data-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSet)))
            .andExpect(status().isBadRequest());

        // Validate the DataSet in the database
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DataSet in Elasticsearch
        verify(mockDataSetSearchRepository, times(0)).save(dataSet);
    }

    @Test
    @Transactional
    public void deleteDataSet() throws Exception {
        // Initialize the database
        dataSetService.save(dataSet);

        int databaseSizeBeforeDelete = dataSetRepository.findAll().size();

        // Delete the dataSet
        restDataSetMockMvc.perform(delete("/api/data-sets/{id}", dataSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataSet> dataSetList = dataSetRepository.findAll();
        assertThat(dataSetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DataSet in Elasticsearch
        verify(mockDataSetSearchRepository, times(1)).deleteById(dataSet.getId());
    }

    @Test
    @Transactional
    public void searchDataSet() throws Exception {
        // Initialize the database
        dataSetService.save(dataSet);
        when(mockDataSetSearchRepository.search(queryStringQuery("id:" + dataSet.getId())))
            .thenReturn(Collections.singletonList(dataSet));
        // Search the dataSet
        restDataSetMockMvc.perform(get("/api/_search/data-sets?query=id:" + dataSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].contents").value(hasItem(DEFAULT_CONTENTS.toString())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)));
    }
}
