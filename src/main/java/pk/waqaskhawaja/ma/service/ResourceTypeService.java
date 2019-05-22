package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.ResourceType;
import pk.waqaskhawaja.ma.repository.ResourceTypeRepository;
import pk.waqaskhawaja.ma.repository.search.ResourceTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ResourceType.
 */
@Service
@Transactional
public class ResourceTypeService {

    private final Logger log = LoggerFactory.getLogger(ResourceTypeService.class);

    private final ResourceTypeRepository resourceTypeRepository;

    private final ResourceTypeSearchRepository resourceTypeSearchRepository;

    public ResourceTypeService(ResourceTypeRepository resourceTypeRepository, ResourceTypeSearchRepository resourceTypeSearchRepository) {
        this.resourceTypeRepository = resourceTypeRepository;
        this.resourceTypeSearchRepository = resourceTypeSearchRepository;
    }

    /**
     * Save a resourceType.
     *
     * @param resourceType the entity to save
     * @return the persisted entity
     */
    public ResourceType save(ResourceType resourceType) {
        log.debug("Request to save ResourceType : {}", resourceType);
        ResourceType result = resourceTypeRepository.save(resourceType);
        resourceTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the resourceTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ResourceType> findAll(Pageable pageable) {
        log.debug("Request to get all ResourceTypes");
        return resourceTypeRepository.findAll(pageable);
    }


    /**
     * Get one resourceType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ResourceType> findOne(Long id) {
        log.debug("Request to get ResourceType : {}", id);
        return resourceTypeRepository.findById(id);
    }

    /**
     * Delete the resourceType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ResourceType : {}", id);
        resourceTypeRepository.deleteById(id);
        resourceTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the resourceType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ResourceType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ResourceTypes for query {}", query);
        return resourceTypeSearchRepository.search(queryStringQuery(query), pageable);    }
}
