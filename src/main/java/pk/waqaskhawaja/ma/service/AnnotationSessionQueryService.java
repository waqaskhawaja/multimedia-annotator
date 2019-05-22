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

import pk.waqaskhawaja.ma.domain.AnnotationSession;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.AnnotationSessionRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationSessionSearchRepository;
import pk.waqaskhawaja.ma.service.dto.AnnotationSessionCriteria;

/**
 * Service for executing complex queries for AnnotationSession entities in the database.
 * The main input is a {@link AnnotationSessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnnotationSession} or a {@link Page} of {@link AnnotationSession} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnnotationSessionQueryService extends QueryService<AnnotationSession> {

    private final Logger log = LoggerFactory.getLogger(AnnotationSessionQueryService.class);

    private final AnnotationSessionRepository annotationSessionRepository;

    private final AnnotationSessionSearchRepository annotationSessionSearchRepository;

    public AnnotationSessionQueryService(AnnotationSessionRepository annotationSessionRepository, AnnotationSessionSearchRepository annotationSessionSearchRepository) {
        this.annotationSessionRepository = annotationSessionRepository;
        this.annotationSessionSearchRepository = annotationSessionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnnotationSession} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnnotationSession> findByCriteria(AnnotationSessionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnnotationSession> specification = createSpecification(criteria);
        return annotationSessionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnnotationSession} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnnotationSession> findByCriteria(AnnotationSessionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnnotationSession> specification = createSpecification(criteria);
        return annotationSessionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnotationSessionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnnotationSession> specification = createSpecification(criteria);
        return annotationSessionRepository.count(specification);
    }

    /**
     * Function to convert AnnotationSessionCriteria to a {@link Specification}
     */
    private Specification<AnnotationSession> createSpecification(AnnotationSessionCriteria criteria) {
        Specification<AnnotationSession> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AnnotationSession_.id));
            }
            if (criteria.getStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStart(), AnnotationSession_.start));
            }
            if (criteria.getEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnd(), AnnotationSession_.end));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AnnotationSession_.name));
            }
            if (criteria.getAnalysisSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnalysisSessionId(),
                    root -> root.join(AnnotationSession_.analysisSession, JoinType.LEFT).get(AnalysisSession_.id)));
            }
            if (criteria.getAnnotatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnnotatorId(),
                    root -> root.join(AnnotationSession_.annotator, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
