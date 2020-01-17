package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.DataRecord;
import pk.waqaskhawaja.ma.repository.DataRecordRepository;
import pk.waqaskhawaja.ma.repository.search.DataRecordSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DataRecord.
 */
@Service
@Transactional
public class DataRecordService {

    private final Logger log = LoggerFactory.getLogger(DataRecordService.class);

    private final DataRecordRepository dataRecordRepository;

    private final DataRecordSearchRepository dataRecordSearchRepository;

    public DataRecordService(DataRecordRepository dataRecordRepository, DataRecordSearchRepository dataRecordSearchRepository) {
        this.dataRecordRepository = dataRecordRepository;
        this.dataRecordSearchRepository = dataRecordSearchRepository;
    }

    /**
     * Save a dataRecord.
     *
     * @param dataRecord the entity to save
     * @return the persisted entity
     */
    public DataRecord save(DataRecord dataRecord) {
        log.debug("Request to save DataRecord : {}", dataRecord);
        DataRecord result = dataRecordRepository.save(dataRecord);
        dataRecordSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the dataRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DataRecord> findAll(Pageable pageable) {
        log.debug("Request to get all DataRecords");
        return dataRecordRepository.findAll(pageable);
    }


    /**
     * Get one dataRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DataRecord> findOne(Long id) {
        log.debug("Request to get DataRecord : {}", id);
        return dataRecordRepository.findById(id);
    }

    /**
     * Delete the dataRecord by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DataRecord : {}", id);
        dataRecordRepository.deleteById(id);
        dataRecordSearchRepository.deleteById(id);
    }

    /**
     * Search for the dataRecord corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DataRecord> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DataRecords for query {}", query);
        return dataRecordSearchRepository.search(queryStringQuery(query), pageable);    }
}
