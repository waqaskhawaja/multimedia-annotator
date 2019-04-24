package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MultimediaAnnotatorApp;

import pk.waqaskhawaja.ma.domain.Analyst;
import pk.waqaskhawaja.ma.repository.AnalystRepository;
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
 * Test class for the AnalystResource REST controller.
 *
 * @see AnalystResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultimediaAnnotatorApp.class)
public class AnalystResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXPERIENCE = 1;
    private static final Integer UPDATED_EXPERIENCE = 2;

    @Autowired
    private AnalystRepository analystRepository;

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

    private MockMvc restAnalystMockMvc;

    private Analyst analyst;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnalystResource analystResource = new AnalystResource(analystRepository);
        this.restAnalystMockMvc = MockMvcBuilders.standaloneSetup(analystResource)
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
    public static Analyst createEntity(EntityManager em) {
        Analyst analyst = new Analyst()
            .name(DEFAULT_NAME)
            .experience(DEFAULT_EXPERIENCE);
        return analyst;
    }

    @Before
    public void initTest() {
        analyst = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalyst() throws Exception {
        int databaseSizeBeforeCreate = analystRepository.findAll().size();

        // Create the Analyst
        restAnalystMockMvc.perform(post("/api/analysts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyst)))
            .andExpect(status().isCreated());

        // Validate the Analyst in the database
        List<Analyst> analystList = analystRepository.findAll();
        assertThat(analystList).hasSize(databaseSizeBeforeCreate + 1);
        Analyst testAnalyst = analystList.get(analystList.size() - 1);
        assertThat(testAnalyst.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAnalyst.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
    }

    @Test
    @Transactional
    public void createAnalystWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analystRepository.findAll().size();

        // Create the Analyst with an existing ID
        analyst.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalystMockMvc.perform(post("/api/analysts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyst)))
            .andExpect(status().isBadRequest());

        // Validate the Analyst in the database
        List<Analyst> analystList = analystRepository.findAll();
        assertThat(analystList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAnalysts() throws Exception {
        // Initialize the database
        analystRepository.saveAndFlush(analyst);

        // Get all the analystList
        restAnalystMockMvc.perform(get("/api/analysts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyst.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE)));
    }
    
    @Test
    @Transactional
    public void getAnalyst() throws Exception {
        // Initialize the database
        analystRepository.saveAndFlush(analyst);

        // Get the analyst
        restAnalystMockMvc.perform(get("/api/analysts/{id}", analyst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analyst.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE));
    }

    @Test
    @Transactional
    public void getNonExistingAnalyst() throws Exception {
        // Get the analyst
        restAnalystMockMvc.perform(get("/api/analysts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalyst() throws Exception {
        // Initialize the database
        analystRepository.saveAndFlush(analyst);

        int databaseSizeBeforeUpdate = analystRepository.findAll().size();

        // Update the analyst
        Analyst updatedAnalyst = analystRepository.findById(analyst.getId()).get();
        // Disconnect from session so that the updates on updatedAnalyst are not directly saved in db
        em.detach(updatedAnalyst);
        updatedAnalyst
            .name(UPDATED_NAME)
            .experience(UPDATED_EXPERIENCE);

        restAnalystMockMvc.perform(put("/api/analysts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalyst)))
            .andExpect(status().isOk());

        // Validate the Analyst in the database
        List<Analyst> analystList = analystRepository.findAll();
        assertThat(analystList).hasSize(databaseSizeBeforeUpdate);
        Analyst testAnalyst = analystList.get(analystList.size() - 1);
        assertThat(testAnalyst.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAnalyst.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalyst() throws Exception {
        int databaseSizeBeforeUpdate = analystRepository.findAll().size();

        // Create the Analyst

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalystMockMvc.perform(put("/api/analysts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyst)))
            .andExpect(status().isBadRequest());

        // Validate the Analyst in the database
        List<Analyst> analystList = analystRepository.findAll();
        assertThat(analystList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnalyst() throws Exception {
        // Initialize the database
        analystRepository.saveAndFlush(analyst);

        int databaseSizeBeforeDelete = analystRepository.findAll().size();

        // Delete the analyst
        restAnalystMockMvc.perform(delete("/api/analysts/{id}", analyst.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Analyst> analystList = analystRepository.findAll();
        assertThat(analystList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Analyst.class);
        Analyst analyst1 = new Analyst();
        analyst1.setId(1L);
        Analyst analyst2 = new Analyst();
        analyst2.setId(analyst1.getId());
        assertThat(analyst1).isEqualTo(analyst2);
        analyst2.setId(2L);
        assertThat(analyst1).isNotEqualTo(analyst2);
        analyst1.setId(null);
        assertThat(analyst1).isNotEqualTo(analyst2);
    }
}
