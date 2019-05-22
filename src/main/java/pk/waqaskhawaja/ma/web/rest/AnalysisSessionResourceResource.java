package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import pk.waqaskhawaja.ma.service.AnalysisSessionResourceService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.AnalysisSessionResourceCriteria;
import pk.waqaskhawaja.ma.service.AnalysisSessionResourceQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AnalysisSessionResource.
 */
@RestController
@RequestMapping("/api")
public class AnalysisSessionResourceResource {

    private final Logger log = LoggerFactory.getLogger(AnalysisSessionResourceResource.class);

    private static final String ENTITY_NAME = "analysisSessionResource";

    private final AnalysisSessionResourceService analysisSessionResourceService;

    private final AnalysisSessionResourceQueryService analysisSessionResourceQueryService;

    public AnalysisSessionResourceResource(AnalysisSessionResourceService analysisSessionResourceService, AnalysisSessionResourceQueryService analysisSessionResourceQueryService) {
        this.analysisSessionResourceService = analysisSessionResourceService;
        this.analysisSessionResourceQueryService = analysisSessionResourceQueryService;
    }

    /**
     * POST  /analysis-session-resources : Create a new analysisSessionResource.
     *
     * @param analysisSessionResource the analysisSessionResource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new analysisSessionResource, or with status 400 (Bad Request) if the analysisSessionResource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/analysis-session-resources")
    public ResponseEntity<AnalysisSessionResource> createAnalysisSessionResource(@RequestBody AnalysisSessionResource analysisSessionResource) throws URISyntaxException {
        log.debug("REST request to save AnalysisSessionResource : {}", analysisSessionResource);
        if (analysisSessionResource.getId() != null) {
            throw new BadRequestAlertException("A new analysisSessionResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalysisSessionResource result = analysisSessionResourceService.save(analysisSessionResource);
        return ResponseEntity.created(new URI("/api/analysis-session-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /analysis-session-resources : Updates an existing analysisSessionResource.
     *
     * @param analysisSessionResource the analysisSessionResource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated analysisSessionResource,
     * or with status 400 (Bad Request) if the analysisSessionResource is not valid,
     * or with status 500 (Internal Server Error) if the analysisSessionResource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/analysis-session-resources")
    public ResponseEntity<AnalysisSessionResource> updateAnalysisSessionResource(@RequestBody AnalysisSessionResource analysisSessionResource) throws URISyntaxException {
        log.debug("REST request to update AnalysisSessionResource : {}", analysisSessionResource);
        if (analysisSessionResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnalysisSessionResource result = analysisSessionResourceService.save(analysisSessionResource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analysisSessionResource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /analysis-session-resources : get all the analysisSessionResources.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of analysisSessionResources in body
     */
    @GetMapping("/analysis-session-resources")
    public ResponseEntity<List<AnalysisSessionResource>> getAllAnalysisSessionResources(AnalysisSessionResourceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnalysisSessionResources by criteria: {}", criteria);
        Page<AnalysisSessionResource> page = analysisSessionResourceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/analysis-session-resources");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /analysis-session-resources/count : count all the analysisSessionResources.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/analysis-session-resources/count")
    public ResponseEntity<Long> countAnalysisSessionResources(AnalysisSessionResourceCriteria criteria) {
        log.debug("REST request to count AnalysisSessionResources by criteria: {}", criteria);
        return ResponseEntity.ok().body(analysisSessionResourceQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /analysis-session-resources/:id : get the "id" analysisSessionResource.
     *
     * @param id the id of the analysisSessionResource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analysisSessionResource, or with status 404 (Not Found)
     */
    @GetMapping("/analysis-session-resources/{id}")
    public ResponseEntity<AnalysisSessionResource> getAnalysisSessionResource(@PathVariable Long id) {
        log.debug("REST request to get AnalysisSessionResource : {}", id);
        Optional<AnalysisSessionResource> analysisSessionResource = analysisSessionResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(analysisSessionResource);
    }

    /**
     * DELETE  /analysis-session-resources/:id : delete the "id" analysisSessionResource.
     *
     * @param id the id of the analysisSessionResource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/analysis-session-resources/{id}")
    public ResponseEntity<Void> deleteAnalysisSessionResource(@PathVariable Long id) {
        log.debug("REST request to delete AnalysisSessionResource : {}", id);
        analysisSessionResourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/analysis-session-resources?query=:query : search for the analysisSessionResource corresponding
     * to the query.
     *
     * @param query the query of the analysisSessionResource search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/analysis-session-resources")
    public ResponseEntity<List<AnalysisSessionResource>> searchAnalysisSessionResources(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnalysisSessionResources for query {}", query);
        Page<AnalysisSessionResource> page = analysisSessionResourceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/analysis-session-resources");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
