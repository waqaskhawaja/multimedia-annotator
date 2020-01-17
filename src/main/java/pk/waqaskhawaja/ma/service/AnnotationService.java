package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.Annotation;
import pk.waqaskhawaja.ma.repository.AnnotationRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Annotation.
 */
@Service
@Transactional
public class AnnotationService {

    private final Logger log = LoggerFactory.getLogger(AnnotationService.class);

    private final AnnotationRepository annotationRepository;

    private final AnnotationSearchRepository annotationSearchRepository;

    public AnnotationService(AnnotationRepository annotationRepository, AnnotationSearchRepository annotationSearchRepository) {
        this.annotationRepository = annotationRepository;
        this.annotationSearchRepository = annotationSearchRepository;
    }

    /**
     * Save a annotation.
     *
     * @param annotation the entity to save
     * @return the persisted entity
     */
    public Annotation save(Annotation annotation) {
        log.debug("Request to save Annotation : {}", annotation);
        Annotation result = annotationRepository.save(annotation);
        annotationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the annotations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Annotation> findAll(Pageable pageable) {
        log.debug("Request to get all Annotations");
        return annotationRepository.findAll(pageable);
    }

    /**
     * Get all the Annotation with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Annotation> findAllWithEagerRelationships(Pageable pageable) {
        return annotationRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one annotation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Annotation> findOne(Long id) {
        log.debug("Request to get Annotation : {}", id);
        return annotationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the annotation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Annotation : {}", id);
        annotationRepository.deleteById(id);
        annotationSearchRepository.deleteById(id);
    }

    /**
     * Search for the annotation corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Annotation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Annotations for query {}", query);
        return annotationSearchRepository.search(queryStringQuery(query), pageable);    }
}
