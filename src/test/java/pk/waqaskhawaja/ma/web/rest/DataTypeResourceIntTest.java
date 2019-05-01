package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MultimediaAnnotatorApp;

import pk.waqaskhawaja.ma.domain.DataType;
import pk.waqaskhawaja.ma.repository.DataTypeRepository;
import pk.waqaskhawaja.ma.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static pk.waqaskhawaja.ma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DataTypeResource REST controller.
 *
 * @see DataTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultimediaAnnotatorApp.class)
public class DataTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DataTypeRepository dataTypeRepository;

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

    private MockMvc restDataTypeMockMvc;

    private DataType dataType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataTypeResource dataTypeResource = new DataTypeResource(dataTypeRepository);
        this.restDataTypeMockMvc = MockMvcBuilders.standaloneSetup(dataTypeResource)
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
    public static DataType createEntity(EntityManager em) {
        DataType dataType = new DataType()
            .name(DEFAULT_NAME);
        return dataType;
    }

    @Before
    public void initTest() {
        dataType = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataType() throws Exception {
        int databaseSizeBeforeCreate = dataTypeRepository.findAll().size();

        // Create the DataType
        restDataTypeMockMvc.perform(post("/api/data-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataType)))
            .andExpect(status().isCreated());

        // Validate the DataType in the database
        List<DataType> dataTypeList = dataTypeRepository.findAll();
        assertThat(dataTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DataType testDataType = dataTypeList.get(dataTypeList.size() - 1);
        assertThat(testDataType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDataTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataTypeRepository.findAll().size();

        // Create the DataType with an existing ID
        dataType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataTypeMockMvc.perform(post("/api/data-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataType)))
            .andExpect(status().isBadRequest());

        // Validate the DataType in the database
        List<DataType> dataTypeList = dataTypeRepository.findAll();
        assertThat(dataTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataTypes() throws Exception {
        // Initialize the database
        dataTypeRepository.saveAndFlush(dataType);

        // Get all the dataTypeList
        restDataTypeMockMvc.perform(get("/api/data-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getDataType() throws Exception {
        // Initialize the database
        dataTypeRepository.saveAndFlush(dataType);

        // Get the dataType
        restDataTypeMockMvc.perform(get("/api/data-types/{id}", dataType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDataType() throws Exception {
        // Get the dataType
        restDataTypeMockMvc.perform(get("/api/data-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataType() throws Exception {
        // Initialize the database
        dataTypeRepository.saveAndFlush(dataType);

        int databaseSizeBeforeUpdate = dataTypeRepository.findAll().size();

        // Update the dataType
        DataType updatedDataType = dataTypeRepository.findById(dataType.getId()).get();
        // Disconnect from session so that the updates on updatedDataType are not directly saved in db
        em.detach(updatedDataType);
        updatedDataType
            .name(UPDATED_NAME);

        restDataTypeMockMvc.perform(put("/api/data-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataType)))
            .andExpect(status().isOk());

        // Validate the DataType in the database
        List<DataType> dataTypeList = dataTypeRepository.findAll();
        assertThat(dataTypeList).hasSize(databaseSizeBeforeUpdate);
        DataType testDataType = dataTypeList.get(dataTypeList.size() - 1);
        assertThat(testDataType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDataType() throws Exception {
        int databaseSizeBeforeUpdate = dataTypeRepository.findAll().size();

        // Create the DataType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataTypeMockMvc.perform(put("/api/data-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataType)))
            .andExpect(status().isBadRequest());

        // Validate the DataType in the database
        List<DataType> dataTypeList = dataTypeRepository.findAll();
        assertThat(dataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDataType() throws Exception {
        // Initialize the database
        dataTypeRepository.saveAndFlush(dataType);

        int databaseSizeBeforeDelete = dataTypeRepository.findAll().size();

        // Delete the dataType
        restDataTypeMockMvc.perform(delete("/api/data-types/{id}", dataType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataType> dataTypeList = dataTypeRepository.findAll();
        assertThat(dataTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataType.class);
        DataType dataType1 = new DataType();
        dataType1.setId(1L);
        DataType dataType2 = new DataType();
        dataType2.setId(dataType1.getId());
        assertThat(dataType1).isEqualTo(dataType2);
        dataType2.setId(2L);
        assertThat(dataType1).isNotEqualTo(dataType2);
        dataType1.setId(null);
        assertThat(dataType1).isNotEqualTo(dataType2);
    }
}
