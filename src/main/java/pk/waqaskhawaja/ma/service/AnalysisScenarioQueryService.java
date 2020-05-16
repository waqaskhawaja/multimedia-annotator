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

import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.AnalysisScenarioRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisScenarioSearchRepository;
import pk.waqaskhawaja.ma.service.dto.AnalysisScenarioCriteria;

/**
 * Service for executing complex queries for {@link AnalysisScenario} entities in the database.
 * The main input is a {@link AnalysisScenarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnalysisScenario} or a {@link Page} of {@link AnalysisScenario} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnalysisScenarioQueryService extends QueryService<AnalysisScenario> {

    private final Logger log = LoggerFactory.getLogger(AnalysisScenarioQueryService.class);

    private final AnalysisScenarioRepository analysisScenarioRepository;

    private final AnalysisScenarioSearchRepository analysisScenarioSearchRepository;

    public AnalysisScenarioQueryService(AnalysisScenarioRepository analysisScenarioRepository, AnalysisScenarioSearchRepository analysisScenarioSearchRepository) {
        this.analysisScenarioRepository = analysisScenarioRepository;
        this.analysisScenarioSearchRepository = analysisScenarioSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AnalysisScenario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnalysisScenario> findByCriteria(AnalysisScenarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnalysisScenario> specification = createSpecification(criteria);
        return analysisScenarioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AnalysisScenario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnalysisScenario> findByCriteria(AnalysisScenarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnalysisScenario> specification = createSpecification(criteria);
        return analysisScenarioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnalysisScenarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnalysisScenario> specification = createSpecification(criteria);
        return analysisScenarioRepository.count(specification);
    }

    /**
     * Function to convert {@link AnalysisScenarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnalysisScenario> createSpecification(AnalysisScenarioCriteria criteria) {
        Specification<AnalysisScenario> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnalysisScenario_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AnalysisScenario_.name));
            }
            if (criteria.getAnalysisSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnalysisSessionId(),
                    root -> root.join(AnalysisScenario_.analysisSessions, JoinType.LEFT).get(AnalysisSession_.id)));
            }
            if (criteria.getDataSetId() != null) {
                specification = specification.and(buildSpecification(criteria.getDataSetId(),
                    root -> root.join(AnalysisScenario_.dataSets, JoinType.LEFT).get(DataSet_.id)));
            }
        }
        return specification;
    }
}
