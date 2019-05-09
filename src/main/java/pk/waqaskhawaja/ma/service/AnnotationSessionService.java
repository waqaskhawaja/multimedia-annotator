package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.AnnotationSession;
import pk.waqaskhawaja.ma.repository.AnnotationSessionRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationSessionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AnnotationSession.
 */
@Service
@Transactional
public class AnnotationSessionService {

    private final Logger log = LoggerFactory.getLogger(AnnotationSessionService.class);

    private final AnnotationSessionRepository annotationSessionRepository;

    private final AnnotationSessionSearchRepository annotationSessionSearchRepository;

    public AnnotationSessionService(AnnotationSessionRepository annotationSessionRepository, AnnotationSessionSearchRepository annotationSessionSearchRepository) {
        this.annotationSessionRepository = annotationSessionRepository;
        this.annotationSessionSearchRepository = annotationSessionSearchRepository;
    }

    /**
     * Save a annotationSession.
     *
     * @param annotationSession the entity to save
     * @return the persisted entity
     */
    public AnnotationSession save(AnnotationSession annotationSession) {
        log.debug("Request to save AnnotationSession : {}", annotationSession);
        AnnotationSession result = annotationSessionRepository.save(annotationSession);
        annotationSessionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the annotationSessions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnnotationSession> findAll(Pageable pageable) {
        log.debug("Request to get all AnnotationSessions");
        return annotationSessionRepository.findAll(pageable);
    }


    /**
     * Get one annotationSession by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AnnotationSession> findOne(Long id) {
        log.debug("Request to get AnnotationSession : {}", id);
        return annotationSessionRepository.findById(id);
    }

    /**
     * Delete the annotationSession by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AnnotationSession : {}", id);
        annotationSessionRepository.deleteById(id);
        annotationSessionSearchRepository.deleteById(id);
    }

    /**
     * Search for the annotationSession corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnnotationSession> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnnotationSessions for query {}", query);
        return annotationSessionSearchRepository.search(queryStringQuery(query), pageable);    }
}
