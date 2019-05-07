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

import pk.waqaskhawaja.ma.domain.DataType;
import pk.waqaskhawaja.ma.domain.*; // for static metamodels
import pk.waqaskhawaja.ma.repository.DataTypeRepository;
import pk.waqaskhawaja.ma.repository.search.DataTypeSearchRepository;
import pk.waqaskhawaja.ma.service.dto.DataTypeCriteria;

/**
 * Service for executing complex queries for DataType entities in the database.
 * The main input is a {@link DataTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DataType} or a {@link Page} of {@link DataType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DataTypeQueryService extends QueryService<DataType> {

    private final Logger log = LoggerFactory.getLogger(DataTypeQueryService.class);

    private final DataTypeRepository dataTypeRepository;

    private final DataTypeSearchRepository dataTypeSearchRepository;

    public DataTypeQueryService(DataTypeRepository dataTypeRepository, DataTypeSearchRepository dataTypeSearchRepository) {
        this.dataTypeRepository = dataTypeRepository;
        this.dataTypeSearchRepository = dataTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DataType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataType> findByCriteria(DataTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DataType> specification = createSpecification(criteria);
        return dataTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DataType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataType> findByCriteria(DataTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DataType> specification = createSpecification(criteria);
        return dataTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DataTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DataType> specification = createSpecification(criteria);
        return dataTypeRepository.count(specification);
    }

    /**
     * Function to convert DataTypeCriteria to a {@link Specification}
     */
    private Specification<DataType> createSpecification(DataTypeCriteria criteria) {
        Specification<DataType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DataType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DataType_.name));
            }
            if (criteria.getSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getSessionId(),
                    root -> root.join(DataType_.sessions, JoinType.LEFT).get(Session_.id)));
            }
        }
        return specification;
    }
}
