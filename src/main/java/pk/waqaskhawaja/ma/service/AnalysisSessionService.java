package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.AnalysisSession;
import pk.waqaskhawaja.ma.repository.AnalysisSessionRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisSessionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AnalysisSession.
 */
@Service
@Transactional
public class AnalysisSessionService {

    private final Logger log = LoggerFactory.getLogger(AnalysisSessionService.class);

    private final AnalysisSessionRepository analysisSessionRepository;

    private final AnalysisSessionSearchRepository analysisSessionSearchRepository;

    public AnalysisSessionService(AnalysisSessionRepository analysisSessionRepository, AnalysisSessionSearchRepository analysisSessionSearchRepository) {
        this.analysisSessionRepository = analysisSessionRepository;
        this.analysisSessionSearchRepository = analysisSessionSearchRepository;
    }

    /**
     * Save a analysisSession.
     *
     * @param analysisSession the entity to save
     * @return the persisted entity
     */
    public AnalysisSession save(AnalysisSession analysisSession) {
        log.debug("Request to save AnalysisSession : {}", analysisSession);
        AnalysisSession result = analysisSessionRepository.save(analysisSession);
        analysisSessionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the analysisSessions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnalysisSession> findAll(Pageable pageable) {
        log.debug("Request to get all AnalysisSessions");
        return analysisSessionRepository.findAll(pageable);
    }


    /**
     * Get one analysisSession by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AnalysisSession> findOne(Long id) {
        log.debug("Request to get AnalysisSession : {}", id);
        return analysisSessionRepository.findById(id);
    }

    /**
     * Delete the analysisSession by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AnalysisSession : {}", id);
        analysisSessionRepository.deleteById(id);
        analysisSessionSearchRepository.deleteById(id);
    }

    /**
     * Search for the analysisSession corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnalysisSession> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnalysisSessions for query {}", query);
        return analysisSessionSearchRepository.search(queryStringQuery(query), pageable);    }
}
