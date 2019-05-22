package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.InteractionType;
import pk.waqaskhawaja.ma.repository.InteractionTypeRepository;
import pk.waqaskhawaja.ma.repository.search.InteractionTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InteractionType.
 */
@Service
@Transactional
public class InteractionTypeService {

    private final Logger log = LoggerFactory.getLogger(InteractionTypeService.class);

    private final InteractionTypeRepository interactionTypeRepository;

    private final InteractionTypeSearchRepository interactionTypeSearchRepository;

    public InteractionTypeService(InteractionTypeRepository interactionTypeRepository, InteractionTypeSearchRepository interactionTypeSearchRepository) {
        this.interactionTypeRepository = interactionTypeRepository;
        this.interactionTypeSearchRepository = interactionTypeSearchRepository;
    }

    /**
     * Save a interactionType.
     *
     * @param interactionType the entity to save
     * @return the persisted entity
     */
    public InteractionType save(InteractionType interactionType) {
        log.debug("Request to save InteractionType : {}", interactionType);
        InteractionType result = interactionTypeRepository.save(interactionType);
        interactionTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the interactionTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InteractionType> findAll(Pageable pageable) {
        log.debug("Request to get all InteractionTypes");
        return interactionTypeRepository.findAll(pageable);
    }


    /**
     * Get one interactionType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<InteractionType> findOne(Long id) {
        log.debug("Request to get InteractionType : {}", id);
        return interactionTypeRepository.findById(id);
    }

    /**
     * Delete the interactionType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InteractionType : {}", id);
        interactionTypeRepository.deleteById(id);
        interactionTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the interactionType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InteractionType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InteractionTypes for query {}", query);
        return interactionTypeSearchRepository.search(queryStringQuery(query), pageable);    }
}
