package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import pk.waqaskhawaja.ma.repository.AnalysisScenarioRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisScenarioSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AnalysisScenario.
 */
@Service
@Transactional
public class AnalysisScenarioService {

    private final Logger log = LoggerFactory.getLogger(AnalysisScenarioService.class);

    private final AnalysisScenarioRepository analysisScenarioRepository;

    private final AnalysisScenarioSearchRepository analysisScenarioSearchRepository;

    public AnalysisScenarioService(AnalysisScenarioRepository analysisScenarioRepository, AnalysisScenarioSearchRepository analysisScenarioSearchRepository) {
        this.analysisScenarioRepository = analysisScenarioRepository;
        this.analysisScenarioSearchRepository = analysisScenarioSearchRepository;
    }

    /**
     * Save a analysisScenario.
     *
     * @param analysisScenario the entity to save
     * @return the persisted entity
     */
    public AnalysisScenario save(AnalysisScenario analysisScenario) {
        log.debug("Request to save AnalysisScenario : {}", analysisScenario);
        AnalysisScenario result = analysisScenarioRepository.save(analysisScenario);
        analysisScenarioSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the analysisScenarios.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnalysisScenario> findAll(Pageable pageable) {
        log.debug("Request to get all AnalysisScenarios");
        return analysisScenarioRepository.findAll(pageable);
    }


    /**
     * Get one analysisScenario by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AnalysisScenario> findOne(Long id) {
        log.debug("Request to get AnalysisScenario : {}", id);
        return analysisScenarioRepository.findById(id);
    }

    /**
     * Delete the analysisScenario by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AnalysisScenario : {}", id);
        analysisScenarioRepository.deleteById(id);
        analysisScenarioSearchRepository.deleteById(id);
    }

    /**
     * Search for the analysisScenario corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnalysisScenario> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnalysisScenarios for query {}", query);
        return analysisScenarioSearchRepository.search(queryStringQuery(query), pageable);    }
}
