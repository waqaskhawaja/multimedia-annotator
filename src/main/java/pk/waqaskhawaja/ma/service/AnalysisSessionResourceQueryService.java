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

import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.AnalysisSessionResourceRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisSessionResourceSearchRepository;
import pk.waqaskhawaja.ma.service.dto.AnalysisSessionResourceCriteria;

/**
 * Service for executing complex queries for AnalysisSessionResource entities in the database.
 * The main input is a {@link AnalysisSessionResourceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnalysisSessionResource} or a {@link Page} of {@link AnalysisSessionResource} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnalysisSessionResourceQueryService extends QueryService<AnalysisSessionResource> {

    private final Logger log = LoggerFactory.getLogger(AnalysisSessionResourceQueryService.class);

    private final AnalysisSessionResourceRepository analysisSessionResourceRepository;

    private final AnalysisSessionResourceSearchRepository analysisSessionResourceSearchRepository;

    public AnalysisSessionResourceQueryService(AnalysisSessionResourceRepository analysisSessionResourceRepository, AnalysisSessionResourceSearchRepository analysisSessionResourceSearchRepository) {
        this.analysisSessionResourceRepository = analysisSessionResourceRepository;
        this.analysisSessionResourceSearchRepository = analysisSessionResourceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnalysisSessionResource} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnalysisSessionResource> findByCriteria(AnalysisSessionResourceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnalysisSessionResource> specification = createSpecification(criteria);
        return analysisSessionResourceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnalysisSessionResource} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnalysisSessionResource> findByCriteria(AnalysisSessionResourceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnalysisSessionResource> specification = createSpecification(criteria);
        return analysisSessionResourceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnalysisSessionResourceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnalysisSessionResource> specification = createSpecification(criteria);
        return analysisSessionResourceRepository.count(specification);
    }

    /**
     * Function to convert AnalysisSessionResourceCriteria to a {@link Specification}
     */
    private Specification<AnalysisSessionResource> createSpecification(AnalysisSessionResourceCriteria criteria) {
        Specification<AnalysisSessionResource> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AnalysisSessionResource_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AnalysisSessionResource_.name));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), AnalysisSessionResource_.url));
            }
            if (criteria.getResourceTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getResourceTypeId(),
                    root -> root.join(AnalysisSessionResource_.resourceType, JoinType.LEFT).get(ResourceType_.id)));
            }
            if (criteria.getAnalysisSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnalysisSessionId(),
                    root -> root.join(AnalysisSessionResource_.analysisSession, JoinType.LEFT).get(AnalysisSession_.id)));
            }
        }
        return specification;
    }
}
