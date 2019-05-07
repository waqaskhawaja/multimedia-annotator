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

import pk.waqaskhawaja.ma.domain.Session;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.SessionRepository;
import pk.waqaskhawaja.ma.repository.search.SessionSearchRepository;
import pk.waqaskhawaja.ma.service.dto.SessionCriteria;

/**
 * Service for executing complex queries for Session entities in the database.
 * The main input is a {@link SessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Session} or a {@link Page} of {@link Session} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SessionQueryService extends QueryService<Session> {

    private final Logger log = LoggerFactory.getLogger(SessionQueryService.class);

    private final SessionRepository sessionRepository;

    private final SessionSearchRepository sessionSearchRepository;

    public SessionQueryService(SessionRepository sessionRepository, SessionSearchRepository sessionSearchRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionSearchRepository = sessionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Session} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Session> findByCriteria(SessionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Session> specification = createSpecification(criteria);
        return sessionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Session} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Session> findByCriteria(SessionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Session> specification = createSpecification(criteria);
        return sessionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SessionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Session> specification = createSpecification(criteria);
        return sessionRepository.count(specification);
    }

    /**
     * Function to convert SessionCriteria to a {@link Specification}
     */
    private Specification<Session> createSpecification(SessionCriteria criteria) {
        Specification<Session> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Session_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Session_.name));
            }
            if (criteria.getDataTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getDataTypeId(),
                    root -> root.join(Session_.dataType, JoinType.LEFT).get(DataType_.id)));
            }
            if (criteria.getScenarioId() != null) {
                specification = specification.and(buildSpecification(criteria.getScenarioId(),
                    root -> root.join(Session_.scenario, JoinType.LEFT).get(Scenario_.id)));
            }
        }
        return specification;
    }
}
