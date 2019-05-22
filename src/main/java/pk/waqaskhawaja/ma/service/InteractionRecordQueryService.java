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

import pk.waqaskhawaja.ma.domain.InteractionRecord;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.InteractionRecordRepository;
import pk.waqaskhawaja.ma.repository.search.InteractionRecordSearchRepository;
import pk.waqaskhawaja.ma.service.dto.InteractionRecordCriteria;

/**
 * Service for executing complex queries for InteractionRecord entities in the database.
 * The main input is a {@link InteractionRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InteractionRecord} or a {@link Page} of {@link InteractionRecord} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InteractionRecordQueryService extends QueryService<InteractionRecord> {

    private final Logger log = LoggerFactory.getLogger(InteractionRecordQueryService.class);

    private final InteractionRecordRepository interactionRecordRepository;

    private final InteractionRecordSearchRepository interactionRecordSearchRepository;

    public InteractionRecordQueryService(InteractionRecordRepository interactionRecordRepository, InteractionRecordSearchRepository interactionRecordSearchRepository) {
        this.interactionRecordRepository = interactionRecordRepository;
        this.interactionRecordSearchRepository = interactionRecordSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InteractionRecord} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InteractionRecord> findByCriteria(InteractionRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InteractionRecord> specification = createSpecification(criteria);
        return interactionRecordRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InteractionRecord} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InteractionRecord> findByCriteria(InteractionRecordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InteractionRecord> specification = createSpecification(criteria);
        return interactionRecordRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InteractionRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InteractionRecord> specification = createSpecification(criteria);
        return interactionRecordRepository.count(specification);
    }

    /**
     * Function to convert InteractionRecordCriteria to a {@link Specification}
     */
    private Specification<InteractionRecord> createSpecification(InteractionRecordCriteria criteria) {
        Specification<InteractionRecord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InteractionRecord_.id));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), InteractionRecord_.duration));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), InteractionRecord_.text));
            }
            if (criteria.getSourceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceId(), InteractionRecord_.sourceId));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), InteractionRecord_.time));
            }
            if (criteria.getInteractionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getInteractionTypeId(),
                    root -> root.join(InteractionRecord_.interactionType, JoinType.LEFT).get(InteractionType_.id)));
            }
            if (criteria.getAnalysisSessionResourceId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnalysisSessionResourceId(),
                    root -> root.join(InteractionRecord_.analysisSessionResource, JoinType.LEFT).get(AnalysisSessionResource_.id)));
            }
            if (criteria.getAnnotationId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnnotationId(),
                    root -> root.join(InteractionRecord_.annotations, JoinType.LEFT).get(Annotation_.id)));
            }
        }
        return specification;
    }
}
