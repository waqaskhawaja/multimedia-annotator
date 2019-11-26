package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import pk.waqaskhawaja.ma.repository.AnalysisSessionResourceRepository;
import pk.waqaskhawaja.ma.repository.search.AnalysisSessionResourceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pk.waqaskhawaja.ma.service.util.RandomUtil;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AnalysisSessionResource.
 */
@Service
@Transactional
public class AnalysisSessionResourceService {

    private final Logger log = LoggerFactory.getLogger(AnalysisSessionResourceService.class);

    private final AnalysisSessionResourceRepository analysisSessionResourceRepository;

    private final AnalysisSessionResourceSearchRepository analysisSessionResourceSearchRepository;

    private final ResourceTypeService resourceTypeService;

    public AnalysisSessionResourceService(AnalysisSessionResourceRepository analysisSessionResourceRepository, AnalysisSessionResourceSearchRepository analysisSessionResourceSearchRepository,
                                          ResourceTypeService resourceTypeService) {
        this.analysisSessionResourceRepository = analysisSessionResourceRepository;
        this.analysisSessionResourceSearchRepository = analysisSessionResourceSearchRepository;
        this.resourceTypeService = resourceTypeService;
    }

    /**
     * Save a analysisSessionResource.
     *
     * @param analysisSessionResource the entity to save
     * @return the persisted entity
     */
    public AnalysisSessionResource save(AnalysisSessionResource analysisSessionResource) {
        log.debug("Request to save AnalysisSessionResource : {}", analysisSessionResource);
        AnalysisSessionResource result = analysisSessionResourceRepository.save(analysisSessionResource);
        analysisSessionResourceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the analysisSessionResources.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnalysisSessionResource> findAll(Pageable pageable) {
        log.debug("Request to get all AnalysisSessionResources");
        return analysisSessionResourceRepository.findAll(pageable);
    }


    /**
     * Get one analysisSessionResource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AnalysisSessionResource> findOne(Long id) {
        log.debug("Request to get AnalysisSessionResource : {}", id);
        return analysisSessionResourceRepository.findById(id);
    }

    /**
     * Get one analysisSessionResource by AnalysisSession.
     *
     * @param analysisSessionId the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AnalysisSessionResource> findVideoByAnalysisSessionId(Long analysisSessionId) {
        log.debug("Request to get AnalysisSessionResource : {}", analysisSessionId);
        Long videoResourceTypeId = resourceTypeService.findByName(RandomUtil.RESOURCE_VIDEO_TYPE_NAME).get().getId();
        return analysisSessionResourceRepository.findByAnalysisSessionIdAndResourceTypeId(analysisSessionId,videoResourceTypeId);
    }

    /**
     * Delete the analysisSessionResource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AnalysisSessionResource : {}", id);
        analysisSessionResourceRepository.deleteById(id);
        analysisSessionResourceSearchRepository.deleteById(id);
    }

    /**
     * Search for the analysisSessionResource corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AnalysisSessionResource> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnalysisSessionResources for query {}", query);
        return analysisSessionResourceSearchRepository.search(queryStringQuery(query), pageable);    }
}
