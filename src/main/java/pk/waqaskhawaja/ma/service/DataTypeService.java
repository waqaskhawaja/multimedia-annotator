package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.DataType;
import pk.waqaskhawaja.ma.repository.DataTypeRepository;
import pk.waqaskhawaja.ma.repository.search.DataTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DataType.
 */
@Service
@Transactional
public class DataTypeService {

    private final Logger log = LoggerFactory.getLogger(DataTypeService.class);

    private final DataTypeRepository dataTypeRepository;

    private final DataTypeSearchRepository dataTypeSearchRepository;

    public DataTypeService(DataTypeRepository dataTypeRepository, DataTypeSearchRepository dataTypeSearchRepository) {
        this.dataTypeRepository = dataTypeRepository;
        this.dataTypeSearchRepository = dataTypeSearchRepository;
    }

    /**
     * Save a dataType.
     *
     * @param dataType the entity to save
     * @return the persisted entity
     */
    public DataType save(DataType dataType) {
        log.debug("Request to save DataType : {}", dataType);
        DataType result = dataTypeRepository.save(dataType);
        dataTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the dataTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DataType> findAll(Pageable pageable) {
        log.debug("Request to get all DataTypes");
        return dataTypeRepository.findAll(pageable);
    }


    /**
     * Get one dataType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DataType> findOne(Long id) {
        log.debug("Request to get DataType : {}", id);
        return dataTypeRepository.findById(id);
    }

    /**
     * Delete the dataType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DataType : {}", id);
        dataTypeRepository.deleteById(id);
        dataTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the dataType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DataType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DataTypes for query {}", query);
        return dataTypeSearchRepository.search(queryStringQuery(query), pageable);    }
}
