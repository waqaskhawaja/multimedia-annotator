package pk.waqaskhawaja.ma.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import pk.waqaskhawaja.ma.domain.DataSet;
import pk.waqaskhawaja.ma.repository.DataSetRepository;
import pk.waqaskhawaja.ma.repository.search.DataSetSearchRepository;

/**
 * Service Implementation for managing {@link DataSet}.
 */
@Service
@Transactional
public class DataSetService {

    private final Logger log = LoggerFactory.getLogger(DataSetService.class);

    private final DataSetRepository dataSetRepository;

    private final DataSetSearchRepository dataSetSearchRepository;

    public DataSetService(DataSetRepository dataSetRepository, DataSetSearchRepository dataSetSearchRepository) {
        this.dataSetRepository = dataSetRepository;
        this.dataSetSearchRepository = dataSetSearchRepository;
    }

    /**
     * Save a dataSet.
     *
     * @param dataSet the entity to save.
     * @return the persisted entity.
     */
    public DataSet save(DataSet dataSet) {
        log.debug("Request to save DataSet : {}", dataSet);
        DataSet result = dataSetRepository.save(dataSet);
        dataSetSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the dataSets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DataSet> findAll() {
        log.debug("Request to get all DataSets");
        return dataSetRepository.findAll();
    }


    /**
     * Get one dataSet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataSet> findOne(Long id) {
        log.debug("Request to get DataSet : {}", id);
        return dataSetRepository.findById(id);
    }
    
    /**
     * Get one dataSet by identifier and AnalysisScenario.
     *
     * @param identifier the identifier of the entity.
     * @param analysisScenario the AnalysisScenario of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataSet> findByIdentifierAndAnalysisScenario(String identifier, AnalysisScenario analysisScenario) {
        log.debug("Request to get DataSet : {} and AnalysisScenario {}", identifier, analysisScenario.getId());
        return dataSetRepository.findByIdentifierAndAnalysisScenario(identifier, analysisScenario);
    }

    /**
     * Delete the dataSet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DataSet : {}", id);
        dataSetRepository.deleteById(id);
        dataSetSearchRepository.deleteById(id);
    }

    /**
     * Search for the dataSet corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DataSet> search(String query) {
        log.debug("Request to search DataSets for query {}", query);
        return StreamSupport
            .stream(dataSetSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
