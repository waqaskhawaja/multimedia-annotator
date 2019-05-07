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

import pk.waqaskhawaja.ma.domain.Scenario;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.ScenarioRepository;
import pk.waqaskhawaja.ma.repository.search.ScenarioSearchRepository;
import pk.waqaskhawaja.ma.service.dto.ScenarioCriteria;

/**
 * Service for executing complex queries for Scenario entities in the database.
 * The main input is a {@link ScenarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Scenario} or a {@link Page} of {@link Scenario} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScenarioQueryService extends QueryService<Scenario> {

    private final Logger log = LoggerFactory.getLogger(ScenarioQueryService.class);

    private final ScenarioRepository scenarioRepository;

    private final ScenarioSearchRepository scenarioSearchRepository;

    public ScenarioQueryService(ScenarioRepository scenarioRepository, ScenarioSearchRepository scenarioSearchRepository) {
        this.scenarioRepository = scenarioRepository;
        this.scenarioSearchRepository = scenarioSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Scenario} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Scenario> findByCriteria(ScenarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Scenario> specification = createSpecification(criteria);
        return scenarioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Scenario} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Scenario> findByCriteria(ScenarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Scenario> specification = createSpecification(criteria);
        return scenarioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScenarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Scenario> specification = createSpecification(criteria);
        return scenarioRepository.count(specification);
    }

    /**
     * Function to convert ScenarioCriteria to a {@link Specification}
     */
    private Specification<Scenario> createSpecification(ScenarioCriteria criteria) {
        Specification<Scenario> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Scenario_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Scenario_.name));
            }
            if (criteria.getSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getSessionId(),
                    root -> root.join(Scenario_.sessions, JoinType.LEFT).get(Session_.id)));
            }
        }
        return specification;
    }
}
