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

import pk.waqaskhawaja.ma.domain.InteractionType;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.InteractionTypeRepository;
import pk.waqaskhawaja.ma.repository.search.InteractionTypeSearchRepository;
import pk.waqaskhawaja.ma.service.dto.InteractionTypeCriteria;

/**
 * Service for executing complex queries for InteractionType entities in the database.
 * The main input is a {@link InteractionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InteractionType} or a {@link Page} of {@link InteractionType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InteractionTypeQueryService extends QueryService<InteractionType> {

    private final Logger log = LoggerFactory.getLogger(InteractionTypeQueryService.class);

    private final InteractionTypeRepository interactionTypeRepository;

    private final InteractionTypeSearchRepository interactionTypeSearchRepository;

    public InteractionTypeQueryService(InteractionTypeRepository interactionTypeRepository, InteractionTypeSearchRepository interactionTypeSearchRepository) {
        this.interactionTypeRepository = interactionTypeRepository;
        this.interactionTypeSearchRepository = interactionTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InteractionType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InteractionType> findByCriteria(InteractionTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InteractionType> specification = createSpecification(criteria);
        return interactionTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InteractionType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InteractionType> findByCriteria(InteractionTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InteractionType> specification = createSpecification(criteria);
        return interactionTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InteractionTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InteractionType> specification = createSpecification(criteria);
        return interactionTypeRepository.count(specification);
    }

    /**
     * Function to convert InteractionTypeCriteria to a {@link Specification}
     */
    private Specification<InteractionType> createSpecification(InteractionTypeCriteria criteria) {
        Specification<InteractionType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InteractionType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), InteractionType_.name));
            }
            if (criteria.getDataRecordId() != null) {
                specification = specification.and(buildSpecification(criteria.getDataRecordId(),
                    root -> root.join(InteractionType_.dataRecords, JoinType.LEFT).get(DataRecord_.id)));
            }
        }
        return specification;
    }
}
