package pk.waqaskhawaja.ma.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import pk.waqaskhawaja.ma.domain.Annotation;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.AnnotationRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationSearchRepository;
import pk.waqaskhawaja.ma.service.dto.AnnotationCriteria;

/**
 * Service for executing complex queries for Annotation entities in the database.
 * The main input is a {@link AnnotationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Annotation} or a {@link Page} of {@link Annotation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnnotationQueryService extends QueryService<Annotation> {

    private final Logger log = LoggerFactory.getLogger(AnnotationQueryService.class);

    private final AnnotationRepository annotationRepository;

    private final AnnotationSearchRepository annotationSearchRepository;

    public AnnotationQueryService(AnnotationRepository annotationRepository, AnnotationSearchRepository annotationSearchRepository) {
        this.annotationRepository = annotationRepository;
        this.annotationSearchRepository = annotationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Annotation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Annotation> findByCriteria(AnnotationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Annotation> specification = createSpecification(criteria);
        return annotationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Annotation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Annotation> findByCriteria(AnnotationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Annotation> specification = createSpecification(criteria);
        return annotationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnotationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Annotation> specification = createSpecification(criteria);
        return annotationRepository.count(specification);
    }

    /**
     * Function to convert AnnotationCriteria to a {@link Specification}
     */
    private Specification<Annotation> createSpecification(AnnotationCriteria criteria) {
        Specification<Annotation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Annotation_.id));
            }
            if (criteria.getStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStart(), Annotation_.start));
            }
            if (criteria.getEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnd(), Annotation_.end));
            }
            if (criteria.getAnnotationText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnnotationText(), Annotation_.annotationText));
            }
            if (criteria.getAnnotationSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnnotationSessionId(),
                    root -> root.join(Annotation_.annotationSession, JoinType.LEFT).get(AnnotationSession_.id)));
            }
            if (criteria.getDataRecordId() != null) {
                specification = specification.and(buildSpecification(criteria.getDataRecordId(),
                    root -> root.join(Annotation_.dataRecords, JoinType.LEFT).get(DataRecord_.id)));
            }
        }
        return specification;
    }
}
