package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MultimediaAnnotatorApp;

import pk.waqaskhawaja.ma.domain.SourceDataType;
import pk.waqaskhawaja.ma.repository.SourceDataTypeRepository;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static pk.waqaskhawaja.ma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SourceDataTypeResource REST controller.
 *
 * @see SourceDataTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultimediaAnnotatorApp.class)
public class SourceDataTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_SOURCE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SOURCE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SOURCE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SOURCE_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private SourceDataTypeRepository sourceDataTypeRepository;

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

    private MockMvc restSourceDataTypeMockMvc;

    private SourceDataType sourceDataType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SourceDataTypeResource sourceDataTypeResource = new SourceDataTypeResource(sourceDataTypeRepository);
        this.restSourceDataTypeMockMvc = MockMvcBuilders.standaloneSetup(sourceDataTypeResource)
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
    public static SourceDataType createEntity(EntityManager em) {
        SourceDataType sourceDataType = new SourceDataType()
            .name(DEFAULT_NAME)
            .sourceFile(DEFAULT_SOURCE_FILE)
            .sourceFileContentType(DEFAULT_SOURCE_FILE_CONTENT_TYPE);
        return sourceDataType;
    }

    @Before
    public void initTest() {
        sourceDataType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourceDataType() throws Exception {
        int databaseSizeBeforeCreate = sourceDataTypeRepository.findAll().size();

        // Create the SourceDataType
        restSourceDataTypeMockMvc.perform(post("/api/source-data-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDataType)))
            .andExpect(status().isCreated());

        // Validate the SourceDataType in the database
        List<SourceDataType> sourceDataTypeList = sourceDataTypeRepository.findAll();
        assertThat(sourceDataTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SourceDataType testSourceDataType = sourceDataTypeList.get(sourceDataTypeList.size() - 1);
        assertThat(testSourceDataType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSourceDataType.getSourceFile()).isEqualTo(DEFAULT_SOURCE_FILE);
        assertThat(testSourceDataType.getSourceFileContentType()).isEqualTo(DEFAULT_SOURCE_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createSourceDataTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceDataTypeRepository.findAll().size();

        // Create the SourceDataType with an existing ID
        sourceDataType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceDataTypeMockMvc.perform(post("/api/source-data-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDataType)))
            .andExpect(status().isBadRequest());

        // Validate the SourceDataType in the database
        List<SourceDataType> sourceDataTypeList = sourceDataTypeRepository.findAll();
        assertThat(sourceDataTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSourceDataTypes() throws Exception {
        // Initialize the database
        sourceDataTypeRepository.saveAndFlush(sourceDataType);

        // Get all the sourceDataTypeList
        restSourceDataTypeMockMvc.perform(get("/api/source-data-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceDataType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sourceFileContentType").value(hasItem(DEFAULT_SOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sourceFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE))));
    }
    
    @Test
    @Transactional
    public void getSourceDataType() throws Exception {
        // Initialize the database
        sourceDataTypeRepository.saveAndFlush(sourceDataType);

        // Get the sourceDataType
        restSourceDataTypeMockMvc.perform(get("/api/source-data-types/{id}", sourceDataType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sourceDataType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sourceFileContentType").value(DEFAULT_SOURCE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.sourceFile").value(Base64Utils.encodeToString(DEFAULT_SOURCE_FILE)));
    }

    @Test
    @Transactional
    public void getNonExistingSourceDataType() throws Exception {
        // Get the sourceDataType
        restSourceDataTypeMockMvc.perform(get("/api/source-data-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourceDataType() throws Exception {
        // Initialize the database
        sourceDataTypeRepository.saveAndFlush(sourceDataType);

        int databaseSizeBeforeUpdate = sourceDataTypeRepository.findAll().size();

        // Update the sourceDataType
        SourceDataType updatedSourceDataType = sourceDataTypeRepository.findById(sourceDataType.getId()).get();
        // Disconnect from session so that the updates on updatedSourceDataType are not directly saved in db
        em.detach(updatedSourceDataType);
        updatedSourceDataType
            .name(UPDATED_NAME)
            .sourceFile(UPDATED_SOURCE_FILE)
            .sourceFileContentType(UPDATED_SOURCE_FILE_CONTENT_TYPE);

        restSourceDataTypeMockMvc.perform(put("/api/source-data-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSourceDataType)))
            .andExpect(status().isOk());

        // Validate the SourceDataType in the database
        List<SourceDataType> sourceDataTypeList = sourceDataTypeRepository.findAll();
        assertThat(sourceDataTypeList).hasSize(databaseSizeBeforeUpdate);
        SourceDataType testSourceDataType = sourceDataTypeList.get(sourceDataTypeList.size() - 1);
        assertThat(testSourceDataType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSourceDataType.getSourceFile()).isEqualTo(UPDATED_SOURCE_FILE);
        assertThat(testSourceDataType.getSourceFileContentType()).isEqualTo(UPDATED_SOURCE_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSourceDataType() throws Exception {
        int databaseSizeBeforeUpdate = sourceDataTypeRepository.findAll().size();

        // Create the SourceDataType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceDataTypeMockMvc.perform(put("/api/source-data-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDataType)))
            .andExpect(status().isBadRequest());

        // Validate the SourceDataType in the database
        List<SourceDataType> sourceDataTypeList = sourceDataTypeRepository.findAll();
        assertThat(sourceDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSourceDataType() throws Exception {
        // Initialize the database
        sourceDataTypeRepository.saveAndFlush(sourceDataType);

        int databaseSizeBeforeDelete = sourceDataTypeRepository.findAll().size();

        // Delete the sourceDataType
        restSourceDataTypeMockMvc.perform(delete("/api/source-data-types/{id}", sourceDataType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SourceDataType> sourceDataTypeList = sourceDataTypeRepository.findAll();
        assertThat(sourceDataTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceDataType.class);
        SourceDataType sourceDataType1 = new SourceDataType();
        sourceDataType1.setId(1L);
        SourceDataType sourceDataType2 = new SourceDataType();
        sourceDataType2.setId(sourceDataType1.getId());
        assertThat(sourceDataType1).isEqualTo(sourceDataType2);
        sourceDataType2.setId(2L);
        assertThat(sourceDataType1).isNotEqualTo(sourceDataType2);
        sourceDataType1.setId(null);
        assertThat(sourceDataType1).isNotEqualTo(sourceDataType2);
    }
}
