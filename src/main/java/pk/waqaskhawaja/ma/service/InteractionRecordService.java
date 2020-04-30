package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.InteractionRecord;
import pk.waqaskhawaja.ma.repository.InteractionRecordRepository;
import pk.waqaskhawaja.ma.repository.search.InteractionRecordSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.waqaskhawaja.ma.service.dto.InteractionRecordDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InteractionRecord.
 */
@Service
@Transactional
public class InteractionRecordService {

    private final Logger log = LoggerFactory.getLogger(InteractionRecordService.class);

    private final InteractionRecordRepository interactionRecordRepository;

    private final InteractionRecordSearchRepository interactionRecordSearchRepository;

    public InteractionRecordService(InteractionRecordRepository interactionRecordRepository, InteractionRecordSearchRepository interactionRecordSearchRepository) {
        this.interactionRecordRepository = interactionRecordRepository;
        this.interactionRecordSearchRepository = interactionRecordSearchRepository;
    }
    private  static double numbertoAdd  = 0.1;

    /**
     * Save a interactionRecord.
     *
     * @param interactionRecord the entity to save
     * @return the persisted entity
     */
    public InteractionRecord save(InteractionRecord interactionRecord) {
        log.debug("Request to save InteractionRecord : {}", interactionRecord);
        InteractionRecord result = interactionRecordRepository.save(interactionRecord);
        interactionRecordSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the interactionRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InteractionRecord> findAll(Pageable pageable) {
        log.debug("Request to get all InteractionRecords");
        return interactionRecordRepository.findAll(pageable);
    }


    /**
     * Get one interactionRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<InteractionRecord> findOne(Long id) {
        log.debug("Request to get InteractionRecord : {}", id);
        return interactionRecordRepository.findById(id);
    }

    /**
     * Delete the interactionRecord by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InteractionRecord : {}", id);
        interactionRecordRepository.deleteById(id);
        interactionRecordSearchRepository.deleteById(id);
    }

    /**
     * Search for the interactionRecord corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InteractionRecord> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InteractionRecords for query {}", query);
        return interactionRecordSearchRepository.search(queryStringQuery(query), pageable);    }




    @Transactional(readOnly = true)
    public InteractionRecord searchByTime(Integer time) {
        log.debug("Request to search for a page of InteractionRecords for query {}", time);
        return interactionRecordRepository.findOneByTime(time);
    }



    @Transactional(readOnly = true)
    public InteractionRecord searchByDuration(Integer duration) {
        log.debug("Request to search for a page of InteractionRecords for query {}", duration);
        return interactionRecordRepository.findByDuration(duration);
    }

    /**
     *
     * Search All InteractionRecord which are present before duration {RequestParam} and convert them to InteractionRecordDto
     * @param duration
     * @return Return List all InteractionRecordDto before duration
     *
     * */
    @Transactional(readOnly = true)
    public List<InteractionRecordDTO> searchListByDuration(Integer duration) {
        log.debug("Request to search for a page of InteractionRecords for query {}", duration);
        return interactionRecordRepository.findAll().stream().filter(interactionRecord -> interactionRecord.getDuration()<=duration)
            .map(interactionRecord -> new InteractionRecordDTO(interactionRecord.getId(),interactionRecord.getDuration(),interactionRecord.getText(),interactionRecord.getSourceId(),
                    interactionRecord.getTime(),interactionRecord.getInteractionType())).collect(Collectors.toList());
    }


    /**Delete All InteractionRecords*/
    public void deleteAllRecord() {
        log.debug("Request to delete All InteractionRecord : {}");
        interactionRecordRepository.deleteAll();
        interactionRecordSearchRepository.deleteAll();
    }



    /**Search By time
     * @param time Search all List of InteractionRecord by time
     * @return Return List of Interaction Records
     * */
    @Transactional(readOnly = true)
    public List<InteractionRecord> searchAllByTime(Integer time) {
        log.debug("Request to search for a page of InteractionRecords for query {}", time);
        return interactionRecordRepository.findAllByTime(time);
    }



    /**
     * Get All InteractionRecord and Convert them to InteractionRecordDto
     * @return Return List of All InteractionRecordDto
     * */
    @Transactional(readOnly = true)
    public List<InteractionRecordDTO> getAllRecords()
    {
        return interactionRecordRepository.findAll().stream().map(interactionRecord->
            new InteractionRecordDTO(interactionRecord.getId(),interactionRecord.getDuration(),interactionRecord.getText(),interactionRecord.getSourceId(),
                                    interactionRecord.getTime(),interactionRecord.getInteractionType()))
                                    .collect(Collectors.toList());
    }
}
