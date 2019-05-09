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

import pk.waqaskhawaja.ma.domain.DataRecord;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.DataRecordRepository;
import pk.waqaskhawaja.ma.repository.search.DataRecordSearchRepository;
import pk.waqaskhawaja.ma.service.dto.DataRecordCriteria;

/**
 * Service for executing complex queries for DataRecord entities in the database.
 * The main input is a {@link DataRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DataRecord} or a {@link Page} of {@link DataRecord} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DataRecordQueryService extends QueryService<DataRecord> {

    private final Logger log = LoggerFactory.getLogger(DataRecordQueryService.class);

    private final DataRecordRepository dataRecordRepository;

    private final DataRecordSearchRepository dataRecordSearchRepository;

    public DataRecordQueryService(DataRecordRepository dataRecordRepository, DataRecordSearchRepository dataRecordSearchRepository) {
        this.dataRecordRepository = dataRecordRepository;
        this.dataRecordSearchRepository = dataRecordSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DataRecord} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataRecord> findByCriteria(DataRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DataRecord> specification = createSpecification(criteria);
        return dataRecordRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DataRecord} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataRecord> findByCriteria(DataRecordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DataRecord> specification = createSpecification(criteria);
        return dataRecordRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DataRecord> specification = createSpecification(criteria);
        return dataRecordRepository.count(specification);
    }

    /**
     * Function to convert DataRecordCriteria to a {@link Specification}
     */
    private Specification<DataRecord> createSpecification(DataRecordCriteria criteria) {
        Specification<DataRecord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DataRecord_.id));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), DataRecord_.duration));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), DataRecord_.text));
            }
            if (criteria.getSourceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceId(), DataRecord_.sourceId));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), DataRecord_.time));
            }
            if (criteria.getSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getSessionId(),
                    root -> root.join(DataRecord_.session, JoinType.LEFT).get(Session_.id)));
            }
            if (criteria.getInteractionTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getInteractionTypeId(),
                    root -> root.join(DataRecord_.interactionType, JoinType.LEFT).get(InteractionType_.id)));
            }
            if (criteria.getAnnotationId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnnotationId(),
                    root -> root.join(DataRecord_.annotations, JoinType.LEFT).get(Annotation_.id)));
            }
        }
        return specification;
    }
}
