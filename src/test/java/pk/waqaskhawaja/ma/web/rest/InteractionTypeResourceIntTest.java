package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.MultimediaAnnotatorApp;

import pk.waqaskhawaja.ma.domain.InteractionType;
import pk.waqaskhawaja.ma.repository.InteractionTypeRepository;
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
 * Test class for the InteractionTypeResource REST controller.
 *
 * @see InteractionTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultimediaAnnotatorApp.class)
public class InteractionTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private InteractionTypeRepository interactionTypeRepository;

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
        final InteractionTypeResource interactionTypeResource = new InteractionTypeResource(interactionTypeRepository);
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
    public void getNonExistingInteractionType() throws Exception {
        // Get the interactionType
        restInteractionTypeMockMvc.perform(get("/api/interaction-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInteractionType() throws Exception {
        // Initialize the database
        interactionTypeRepository.saveAndFlush(interactionType);

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
    }

    @Test
    @Transactional
    public void deleteInteractionType() throws Exception {
        // Initialize the database
        interactionTypeRepository.saveAndFlush(interactionType);

        int databaseSizeBeforeDelete = interactionTypeRepository.findAll().size();

        // Delete the interactionType
        restInteractionTypeMockMvc.perform(delete("/api/interaction-types/{id}", interactionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InteractionType> interactionTypeList = interactionTypeRepository.findAll();
        assertThat(interactionTypeList).hasSize(databaseSizeBeforeDelete - 1);
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
