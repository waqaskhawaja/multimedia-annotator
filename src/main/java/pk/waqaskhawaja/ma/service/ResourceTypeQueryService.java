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

import pk.waqaskhawaja.ma.domain.ResourceType;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.ResourceTypeRepository;
import pk.waqaskhawaja.ma.repository.search.ResourceTypeSearchRepository;
import pk.waqaskhawaja.ma.service.dto.ResourceTypeCriteria;

/**
 * Service for executing complex queries for ResourceType entities in the database.
 * The main input is a {@link ResourceTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ResourceType} or a {@link Page} of {@link ResourceType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ResourceTypeQueryService extends QueryService<ResourceType> {

    private final Logger log = LoggerFactory.getLogger(ResourceTypeQueryService.class);

    private final ResourceTypeRepository resourceTypeRepository;

    private final ResourceTypeSearchRepository resourceTypeSearchRepository;

    public ResourceTypeQueryService(ResourceTypeRepository resourceTypeRepository, ResourceTypeSearchRepository resourceTypeSearchRepository) {
        this.resourceTypeRepository = resourceTypeRepository;
        this.resourceTypeSearchRepository = resourceTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ResourceType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ResourceType> findByCriteria(ResourceTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ResourceType> specification = createSpecification(criteria);
        return resourceTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ResourceType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResourceType> findByCriteria(ResourceTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ResourceType> specification = createSpecification(criteria);
        return resourceTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResourceTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ResourceType> specification = createSpecification(criteria);
        return resourceTypeRepository.count(specification);
    }

    /**
     * Function to convert ResourceTypeCriteria to a {@link Specification}
     */
    private Specification<ResourceType> createSpecification(ResourceTypeCriteria criteria) {
        Specification<ResourceType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ResourceType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ResourceType_.name));
            }
        }
        return specification;
    }
}
