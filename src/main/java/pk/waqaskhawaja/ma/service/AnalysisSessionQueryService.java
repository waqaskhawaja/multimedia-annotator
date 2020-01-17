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

import pk.waqaskhawaja.ma.domain.AnalysisSession;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.AnalysisSessionRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisSessionSearchRepository;
import pk.waqaskhawaja.ma.service.dto.AnalysisSessionCriteria;

/**
 * Service for executing complex queries for AnalysisSession entities in the database.
 * The main input is a {@link AnalysisSessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnalysisSession} or a {@link Page} of {@link AnalysisSession} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnalysisSessionQueryService extends QueryService<AnalysisSession> {

    private final Logger log = LoggerFactory.getLogger(AnalysisSessionQueryService.class);

    private final AnalysisSessionRepository analysisSessionRepository;

    private final AnalysisSessionSearchRepository analysisSessionSearchRepository;

    public AnalysisSessionQueryService(AnalysisSessionRepository analysisSessionRepository, AnalysisSessionSearchRepository analysisSessionSearchRepository) {
        this.analysisSessionRepository = analysisSessionRepository;
        this.analysisSessionSearchRepository = analysisSessionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnalysisSession} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnalysisSession> findByCriteria(AnalysisSessionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnalysisSession> specification = createSpecification(criteria);
        return analysisSessionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnalysisSession} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnalysisSession> findByCriteria(AnalysisSessionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnalysisSession> specification = createSpecification(criteria);
        return analysisSessionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnalysisSessionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnalysisSession> specification = createSpecification(criteria);
        return analysisSessionRepository.count(specification);
    }

    /**
     * Function to convert AnalysisSessionCriteria to a {@link Specification}
     */
    private Specification<AnalysisSession> createSpecification(AnalysisSessionCriteria criteria) {
        Specification<AnalysisSession> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AnalysisSession_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AnalysisSession_.name));
            }
            if (criteria.getAnalysisScenarioId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnalysisScenarioId(),
                    root -> root.join(AnalysisSession_.analysisScenario, JoinType.LEFT).get(AnalysisScenario_.id)));
            }
        }
        return specification;
    }
}
