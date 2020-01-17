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

import pk.waqaskhawaja.ma.domain.AnnotationType;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.AnnotationTypeRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationTypeSearchRepository;
import pk.waqaskhawaja.ma.service.dto.AnnotationTypeCriteria;

/**
 * Service for executing complex queries for AnnotationType entities in the database.
 * The main input is a {@link AnnotationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnnotationType} or a {@link Page} of {@link AnnotationType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnnotationTypeQueryService extends QueryService<AnnotationType> {

    private final Logger log = LoggerFactory.getLogger(AnnotationTypeQueryService.class);

    private final AnnotationTypeRepository annotationTypeRepository;

    private final AnnotationTypeSearchRepository annotationTypeSearchRepository;

    public AnnotationTypeQueryService(AnnotationTypeRepository annotationTypeRepository, AnnotationTypeSearchRepository annotationTypeSearchRepository) {
        this.annotationTypeRepository = annotationTypeRepository;
        this.annotationTypeSearchRepository = annotationTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnnotationType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnnotationType> findByCriteria(AnnotationTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnnotationType> specification = createSpecification(criteria);
        return annotationTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnnotationType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnnotationType> findByCriteria(AnnotationTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnnotationType> specification = createSpecification(criteria);
        return annotationTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnotationTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnnotationType> specification = createSpecification(criteria);
        return annotationTypeRepository.count(specification);
    }

    /**
     * Function to convert AnnotationTypeCriteria to a {@link Specification}
     */
    private Specification<AnnotationType> createSpecification(AnnotationTypeCriteria criteria) {
        Specification<AnnotationType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AnnotationType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AnnotationType_.name));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildSpecification(criteria.getParentId(),
                    root -> root.join(AnnotationType_.parent, JoinType.LEFT).get(AnnotationType_.id)));
            }
        }
        return specification;
    }
}
