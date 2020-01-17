package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.AnnotationType;
import pk.waqaskhawaja.ma.repository.AnnotationTypeRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AnnotationType.
 */
@Service
@Transactional
public class AnnotationTypeService {

    private final Logger log = LoggerFactory.getLogger(AnnotationTypeService.class);

    private final AnnotationTypeRepository annotationTypeRepository;

    private final AnnotationTypeSearchRepository annotationTypeSearchRepository;

    public AnnotationTypeService(AnnotationTypeRepository annotationTypeRepository, AnnotationTypeSearchRepository annotationTypeSearchRepository) {
        this.annotationTypeRepository = annotationTypeRepository;
        this.annotationTypeSearchRepository = annotationTypeSearchRepository;
    }

    /**
     * Save a annotationType.
     *
     * @param annotationType the entity to save
     * @return the persisted entity
     */
    public AnnotationType save(AnnotationType annotationType) {
        log.debug("Request to save AnnotationType : {}", annotationType);
        AnnotationType result = annotationTypeRepository.save(annotationType);
        annotationTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the annotationTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnnotationType> findAll(Pageable pageable) {
        log.debug("Request to get all AnnotationTypes");
        return annotationTypeRepository.findAll(pageable);
    }


    /**
     * Get one annotationType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AnnotationType> findOne(Long id) {
        log.debug("Request to get AnnotationType : {}", id);
        return annotationTypeRepository.findById(id);
    }

    /**
     * Delete the annotationType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AnnotationType : {}", id);
        annotationTypeRepository.deleteById(id);
        annotationTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the annotationType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnnotationType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnnotationTypes for query {}", query);
        return annotationTypeSearchRepository.search(queryStringQuery(query), pageable);    }
}
