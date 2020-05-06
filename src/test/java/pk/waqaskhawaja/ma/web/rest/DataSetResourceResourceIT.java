package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MaApp;
import pk.waqaskhawaja.ma.domain.DataSetResource;
import pk.waqaskhawaja.ma.repository.DataSetResourceRepository;
import pk.waqaskhawaja.ma.repository.search.DataSetResourceSearchRepository;
import pk.waqaskhawaja.ma.service.DataSetResourceService;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
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
 * Integration tests for the {@link DataSetResourceResource} REST controller.
 */
@SpringBootTest(classes = MaApp.class)
public class DataSetResourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SOURCE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SOURCE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SOURCE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SOURCE_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private DataSetResourceRepository dataSetResourceRepository;

    @Autowired
    private DataSetResourceService dataSetResourceService;

    /**
     * This repository is mocked in the pk.waqaskhawaja.ma.repository.search test package.
     *
     * @see pk.waqaskhawaja.ma.repository.search.DataSetResourceSearchRepositoryMockConfiguration
     */
    @Autowired
    private DataSetResourceSearchRepository mockDataSetResourceSearchRepository;

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

    private MockMvc restDataSetResourceMockMvc;

    private DataSetResource dataSetResource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataSetResourceResource dataSetResourceResource = new DataSetResourceResource(dataSetResourceService);
        this.restDataSetResourceMockMvc = MockMvcBuilders.standaloneSetup(dataSetResourceResource)
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
    public static DataSetResource createEntity(EntityManager em) {
        DataSetResource dataSetResource = new DataSetResource()
            .name(DEFAULT_NAME)
            .sourceFile(DEFAULT_SOURCE_FILE)
            .sourceFileContentType(DEFAULT_SOURCE_FILE_CONTENT_TYPE);
        return dataSetResource;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataSetResource createUpdatedEntity(EntityManager em) {
        DataSetResource dataSetResource = new DataSetResource()
            .name(UPDATED_NAME)
            .sourceFile(UPDATED_SOURCE_FILE)
            .sourceFileContentType(UPDATED_SOURCE_FILE_CONTENT_TYPE);
        return dataSetResource;
    }

    @Before
    public void initTest() {
        dataSetResource = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataSetResource() throws Exception {
        int databaseSizeBeforeCreate = dataSetResourceRepository.findAll().size();

        // Create the DataSetResource
        restDataSetResourceMockMvc.perform(post("/api/data-set-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSetResource)))
            .andExpect(status().isCreated());

        // Validate the DataSetResource in the database
        List<DataSetResource> dataSetResourceList = dataSetResourceRepository.findAll();
        assertThat(dataSetResourceList).hasSize(databaseSizeBeforeCreate + 1);
        DataSetResource testDataSetResource = dataSetResourceList.get(dataSetResourceList.size() - 1);
        assertThat(testDataSetResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataSetResource.getSourceFile()).isEqualTo(DEFAULT_SOURCE_FILE);
        assertThat(testDataSetResource.getSourceFileContentType()).isEqualTo(DEFAULT_SOURCE_FILE_CONTENT_TYPE);

        // Validate the DataSetResource in Elasticsearch
        verify(mockDataSetResourceSearchRepository, times(1)).save(testDataSetResource);
    }

    @Test
    @Transactional
    public void createDataSetResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataSetResourceRepository.findAll().size();

        // Create the DataSetResource with an existing ID
        dataSetResource.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataSetResourceMockMvc.perform(post("/api/data-set-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSetResource)))
            .andExpect(status().isBadRequest());

        // Validate the DataSetResource in the database
        List<DataSetResource> dataSetResourceList = dataSetResourceRepository.findAll();
        assertThat(dataSetResourceList).hasSize(databaseSizeBeforeCreate);

        // Validate the DataSetResource in Elasticsearch
        verify(mockDataSetResourceSearchRepository, times(0)).save(dataSetResource);
    }


    @Test
    @Transactional
    public void getAllDataSetResources() throws Exception {
        // Initialize the database
        dataSetResourceRepository.saveAndFlush(dataSetResource);

        // Get all the dataSetResourceList
        restDataSetResourceMockMvc.perform(get("/api/data-set-resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSetResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))));
    }

    @Test
    @Transactional
    public void getDataSetResource() throws Exception {
        // Initialize the database
        dataSetResourceRepository.saveAndFlush(dataSetResource);

        // Get the dataSetResource
        restDataSetResourceMockMvc.perform(get("/api/data-set-resources/{id}", dataSetResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataSetResource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sourceFileContentType").value(DEFAULT_SOURCE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.sourceFile").value(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE)));
    }

    @Test
    @Transactional
    public void getNonExistingDataSetResource() throws Exception {
        // Get the dataSetResource
        restDataSetResourceMockMvc.perform(get("/api/data-set-resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataSetResource() throws Exception {
        // Initialize the database
        dataSetResourceService.save(dataSetResource);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDataSetResourceSearchRepository);

        int databaseSizeBeforeUpdate = dataSetResourceRepository.findAll().size();

        // Update the dataSetResource
        DataSetResource updatedDataSetResource = dataSetResourceRepository.findById(dataSetResource.getId()).get();
        // Disconnect from session so that the updates on updatedDataSetResource are not directly saved in db
        em.detach(updatedDataSetResource);
        updatedDataSetResource
            .name(UPDATED_NAME)
            .sourceFile(UPDATED_SOURCE_FILE)
            .sourceFileContentType(UPDATED_SOURCE_FILE_CONTENT_TYPE);

        restDataSetResourceMockMvc.perform(put("/api/data-set-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataSetResource)))
            .andExpect(status().isOk());

        // Validate the DataSetResource in the database
        List<DataSetResource> dataSetResourceList = dataSetResourceRepository.findAll();
        assertThat(dataSetResourceList).hasSize(databaseSizeBeforeUpdate);
        DataSetResource testDataSetResource = dataSetResourceList.get(dataSetResourceList.size() - 1);
        assertThat(testDataSetResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataSetResource.getSourceFile()).isEqualTo(UPDATED_SOURCE_FILE);
        assertThat(testDataSetResource.getSourceFileContentType()).isEqualTo(UPDATED_SOURCE_FILE_CONTENT_TYPE);

        // Validate the DataSetResource in Elasticsearch
        verify(mockDataSetResourceSearchRepository, times(1)).save(testDataSetResource);
    }

    @Test
    @Transactional
    public void updateNonExistingDataSetResource() throws Exception {
        int databaseSizeBeforeUpdate = dataSetResourceRepository.findAll().size();

        // Create the DataSetResource

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataSetResourceMockMvc.perform(put("/api/data-set-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataSetResource)))
            .andExpect(status().isBadRequest());

        // Validate the DataSetResource in the database
        List<DataSetResource> dataSetResourceList = dataSetResourceRepository.findAll();
        assertThat(dataSetResourceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DataSetResource in Elasticsearch
        verify(mockDataSetResourceSearchRepository, times(0)).save(dataSetResource);
    }

    @Test
    @Transactional
    public void deleteDataSetResource() throws Exception {
        // Initialize the database
        dataSetResourceService.save(dataSetResource);

        int databaseSizeBeforeDelete = dataSetResourceRepository.findAll().size();

        // Delete the dataSetResource
        restDataSetResourceMockMvc.perform(delete("/api/data-set-resources/{id}", dataSetResource.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataSetResource> dataSetResourceList = dataSetResourceRepository.findAll();
        assertThat(dataSetResourceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DataSetResource in Elasticsearch
        verify(mockDataSetResourceSearchRepository, times(1)).deleteById(dataSetResource.getId());
    }

    @Test
    @Transactional
    public void searchDataSetResource() throws Exception {
        // Initialize the database
        dataSetResourceService.save(dataSetResource);
        when(mockDataSetResourceSearchRepository.search(queryStringQuery("id:" + dataSetResource.getId())))
            .thenReturn(Collections.singletonList(dataSetResource));
        // Search the dataSetResource
        restDataSetResourceMockMvc.perform(get("/api/_search/data-set-resources?query=id:" + dataSetResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataSetResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))));
    }
}
