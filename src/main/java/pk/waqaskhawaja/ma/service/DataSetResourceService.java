package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.DataSetResource;
import pk.waqaskhawaja.ma.repository.DataSetResourceRepository;
import pk.waqaskhawaja.ma.repository.search.DataSetResourceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link DataSetResource}.
 */
@Service
@Transactional
public class DataSetResourceService {

    private final Logger log = LoggerFactory.getLogger(DataSetResourceService.class);

    private final DataSetResourceRepository dataSetResourceRepository;

    private final DataSetResourceSearchRepository dataSetResourceSearchRepository;

    public DataSetResourceService(DataSetResourceRepository dataSetResourceRepository, DataSetResourceSearchRepository dataSetResourceSearchRepository) {
        this.dataSetResourceRepository = dataSetResourceRepository;
        this.dataSetResourceSearchRepository = dataSetResourceSearchRepository;
    }

    /**
     * Save a dataSetResource.
     *
     * @param dataSetResource the entity to save.
     * @return the persisted entity.
     */
    public DataSetResource save(DataSetResource dataSetResource) {
        log.debug("Request to save DataSetResource : {}", dataSetResource);
        DataSetResource result = dataSetResourceRepository.save(dataSetResource);
        dataSetResourceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the dataSetResources.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DataSetResource> findAll() {
        log.debug("Request to get all DataSetResources");
        return dataSetResourceRepository.findAll();
    }


    /**
     * Get one dataSetResource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataSetResource> findOne(Long id) {
        log.debug("Request to get DataSetResource : {}", id);
        return dataSetResourceRepository.findById(id);
    }

    /**
     * Delete the dataSetResource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DataSetResource : {}", id);
        dataSetResourceRepository.deleteById(id);
        dataSetResourceSearchRepository.deleteById(id);
    }

    /**
     * Search for the dataSetResource corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DataSetResource> search(String query) {
        log.debug("Request to search DataSetResources for query {}", query);
        return StreamSupport
            .stream(dataSetResourceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
