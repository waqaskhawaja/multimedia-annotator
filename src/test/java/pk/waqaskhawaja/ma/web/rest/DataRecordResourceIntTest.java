package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MultimediaAnnotatorApp;

import pk.waqaskhawaja.ma.domain.DataRecord;
import pk.waqaskhawaja.ma.repository.DataRecordRepository;
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
 * Test class for the DataRecordResource REST controller.
 *
 * @see DataRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultimediaAnnotatorApp.class)
public class DataRecordResourceIntTest {

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME = 1;
    private static final Integer UPDATED_TIME = 2;

    @Autowired
    private DataRecordRepository dataRecordRepository;

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

    private MockMvc restDataRecordMockMvc;

    private DataRecord dataRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataRecordResource dataRecordResource = new DataRecordResource(dataRecordRepository);
        this.restDataRecordMockMvc = MockMvcBuilders.standaloneSetup(dataRecordResource)
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
    public static DataRecord createEntity(EntityManager em) {
        DataRecord dataRecord = new DataRecord()
            .duration(DEFAULT_DURATION)
            .text(DEFAULT_TEXT)
            .sourceId(DEFAULT_SOURCE_ID)
            .time(DEFAULT_TIME);
        return dataRecord;
    }

    @Before
    public void initTest() {
        dataRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataRecord() throws Exception {
        int databaseSizeBeforeCreate = dataRecordRepository.findAll().size();

        // Create the DataRecord
        restDataRecordMockMvc.perform(post("/api/data-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataRecord)))
            .andExpect(status().isCreated());

        // Validate the DataRecord in the database
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeCreate + 1);
        DataRecord testDataRecord = dataRecordList.get(dataRecordList.size() - 1);
        assertThat(testDataRecord.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testDataRecord.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testDataRecord.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testDataRecord.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createDataRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataRecordRepository.findAll().size();

        // Create the DataRecord with an existing ID
        dataRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataRecordMockMvc.perform(post("/api/data-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataRecord)))
            .andExpect(status().isBadRequest());

        // Validate the DataRecord in the database
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDataRecords() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get all the dataRecordList
        restDataRecordMockMvc.perform(get("/api/data-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));
    }
    
    @Test
    @Transactional
    public void getDataRecord() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        // Get the dataRecord
        restDataRecordMockMvc.perform(get("/api/data-records/{id}", dataRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataRecord.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingDataRecord() throws Exception {
        // Get the dataRecord
        restDataRecordMockMvc.perform(get("/api/data-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataRecord() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        int databaseSizeBeforeUpdate = dataRecordRepository.findAll().size();

        // Update the dataRecord
        DataRecord updatedDataRecord = dataRecordRepository.findById(dataRecord.getId()).get();
        // Disconnect from session so that the updates on updatedDataRecord are not directly saved in db
        em.detach(updatedDataRecord);
        updatedDataRecord
            .duration(UPDATED_DURATION)
            .text(UPDATED_TEXT)
            .sourceId(UPDATED_SOURCE_ID)
            .time(UPDATED_TIME);

        restDataRecordMockMvc.perform(put("/api/data-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataRecord)))
            .andExpect(status().isOk());

        // Validate the DataRecord in the database
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeUpdate);
        DataRecord testDataRecord = dataRecordList.get(dataRecordList.size() - 1);
        assertThat(testDataRecord.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testDataRecord.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testDataRecord.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testDataRecord.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingDataRecord() throws Exception {
        int databaseSizeBeforeUpdate = dataRecordRepository.findAll().size();

        // Create the DataRecord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataRecordMockMvc.perform(put("/api/data-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataRecord)))
            .andExpect(status().isBadRequest());

        // Validate the DataRecord in the database
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDataRecord() throws Exception {
        // Initialize the database
        dataRecordRepository.saveAndFlush(dataRecord);

        int databaseSizeBeforeDelete = dataRecordRepository.findAll().size();

        // Delete the dataRecord
        restDataRecordMockMvc.perform(delete("/api/data-records/{id}", dataRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataRecord> dataRecordList = dataRecordRepository.findAll();
        assertThat(dataRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataRecord.class);
        DataRecord dataRecord1 = new DataRecord();
        dataRecord1.setId(1L);
        DataRecord dataRecord2 = new DataRecord();
        dataRecord2.setId(dataRecord1.getId());
        assertThat(dataRecord1).isEqualTo(dataRecord2);
        dataRecord2.setId(2L);
        assertThat(dataRecord1).isNotEqualTo(dataRecord2);
        dataRecord1.setId(null);
        assertThat(dataRecord1).isNotEqualTo(dataRecord2);
    }
}
