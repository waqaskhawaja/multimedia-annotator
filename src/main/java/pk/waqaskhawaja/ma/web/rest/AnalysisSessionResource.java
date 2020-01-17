package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.AnalysisSession;
import pk.waqaskhawaja.ma.service.AnalysisSessionService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.AnalysisSessionCriteria;
import pk.waqaskhawaja.ma.service.AnalysisSessionQueryService;
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
 * REST controller for managing AnalysisSession.
 */
@RestController
@RequestMapping("/api")
public class AnalysisSessionResource {

    private final Logger log = LoggerFactory.getLogger(AnalysisSessionResource.class);

    private static final String ENTITY_NAME = "analysisSession";

    private final AnalysisSessionService analysisSessionService;

    private final AnalysisSessionQueryService analysisSessionQueryService;

    public AnalysisSessionResource(AnalysisSessionService analysisSessionService, AnalysisSessionQueryService analysisSessionQueryService) {
        this.analysisSessionService = analysisSessionService;
        this.analysisSessionQueryService = analysisSessionQueryService;
    }

    /**
     * POST  /analysis-sessions : Create a new analysisSession.
     *
     * @param analysisSession the analysisSession to create
     * @return the ResponseEntity with status 201 (Created) and with body the new analysisSession, or with status 400 (Bad Request) if the analysisSession has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/analysis-sessions")
    public ResponseEntity<AnalysisSession> createAnalysisSession(@RequestBody AnalysisSession analysisSession) throws URISyntaxException {
        log.debug("REST request to save AnalysisSession : {}", analysisSession);
        if (analysisSession.getId() != null) {
            throw new BadRequestAlertException("A new analysisSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalysisSession result = analysisSessionService.save(analysisSession);
        return ResponseEntity.created(new URI("/api/analysis-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /analysis-sessions : Updates an existing analysisSession.
     *
     * @param analysisSession the analysisSession to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated analysisSession,
     * or with status 400 (Bad Request) if the analysisSession is not valid,
     * or with status 500 (Internal Server Error) if the analysisSession couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/analysis-sessions")
    public ResponseEntity<AnalysisSession> updateAnalysisSession(@RequestBody AnalysisSession analysisSession) throws URISyntaxException {
        log.debug("REST request to update AnalysisSession : {}", analysisSession);
        if (analysisSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnalysisSession result = analysisSessionService.save(analysisSession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analysisSession.getId().toString()))
            .body(result);
    }

    /**
     * GET  /analysis-sessions : get all the analysisSessions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of analysisSessions in body
     */
    @GetMapping("/analysis-sessions")
    public ResponseEntity<List<AnalysisSession>> getAllAnalysisSessions(AnalysisSessionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnalysisSessions by criteria: {}", criteria);
        Page<AnalysisSession> page = analysisSessionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/analysis-sessions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /analysis-sessions/count : count all the analysisSessions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/analysis-sessions/count")
    public ResponseEntity<Long> countAnalysisSessions(AnalysisSessionCriteria criteria) {
        log.debug("REST request to count AnalysisSessions by criteria: {}", criteria);
        return ResponseEntity.ok().body(analysisSessionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /analysis-sessions/:id : get the "id" analysisSession.
     *
     * @param id the id of the analysisSession to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analysisSession, or with status 404 (Not Found)
     */
    @GetMapping("/analysis-sessions/{id}")
    public ResponseEntity<AnalysisSession> getAnalysisSession(@PathVariable Long id) {
        log.debug("REST request to get AnalysisSession : {}", id);
        Optional<AnalysisSession> analysisSession = analysisSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(analysisSession);
    }

    /**
     * DELETE  /analysis-sessions/:id : delete the "id" analysisSession.
     *
     * @param id the id of the analysisSession to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/analysis-sessions/{id}")
    public ResponseEntity<Void> deleteAnalysisSession(@PathVariable Long id) {
        log.debug("REST request to delete AnalysisSession : {}", id);
        analysisSessionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/analysis-sessions?query=:query : search for the analysisSession corresponding
     * to the query.
     *
     * @param query the query of the analysisSession search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/analysis-sessions")
    public ResponseEntity<List<AnalysisSession>> searchAnalysisSessions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnalysisSessions for query {}", query);
        Page<AnalysisSession> page = analysisSessionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/analysis-sessions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
