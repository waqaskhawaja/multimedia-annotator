package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.AnnotationSession;
import pk.waqaskhawaja.ma.service.AnnotationSessionService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.AnnotationSessionCriteria;
import pk.waqaskhawaja.ma.service.AnnotationSessionQueryService;
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
 * REST controller for managing AnnotationSession.
 */
@RestController
@RequestMapping("/api")
public class AnnotationSessionResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationSessionResource.class);

    private static final String ENTITY_NAME = "annotationSession";

    private final AnnotationSessionService annotationSessionService;

    private final AnnotationSessionQueryService annotationSessionQueryService;

    public AnnotationSessionResource(AnnotationSessionService annotationSessionService, AnnotationSessionQueryService annotationSessionQueryService) {
        this.annotationSessionService = annotationSessionService;
        this.annotationSessionQueryService = annotationSessionQueryService;
    }

    /**
     * POST  /annotation-sessions : Create a new annotationSession.
     *
     * @param annotationSession the annotationSession to create
     * @return the ResponseEntity with status 201 (Created) and with body the new annotationSession, or with status 400 (Bad Request) if the annotationSession has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/annotation-sessions")
    public ResponseEntity<AnnotationSession> createAnnotationSession(@RequestBody AnnotationSession annotationSession) throws URISyntaxException {
        log.debug("REST request to save AnnotationSession : {}", annotationSession);
        if (annotationSession.getId() != null) {
            throw new BadRequestAlertException("A new annotationSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnotationSession result = annotationSessionService.save(annotationSession);
        return ResponseEntity.created(new URI("/api/annotation-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /annotation-sessions : Updates an existing annotationSession.
     *
     * @param annotationSession the annotationSession to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated annotationSession,
     * or with status 400 (Bad Request) if the annotationSession is not valid,
     * or with status 500 (Internal Server Error) if the annotationSession couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/annotation-sessions")
    public ResponseEntity<AnnotationSession> updateAnnotationSession(@RequestBody AnnotationSession annotationSession) throws URISyntaxException {
        log.debug("REST request to update AnnotationSession : {}", annotationSession);
        if (annotationSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnotationSession result = annotationSessionService.save(annotationSession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, annotationSession.getId().toString()))
            .body(result);
    }

    /**
     * GET  /annotation-sessions : get all the annotationSessions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of annotationSessions in body
     */
    @GetMapping("/annotation-sessions")
    public ResponseEntity<List<AnnotationSession>> getAllAnnotationSessions(AnnotationSessionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnnotationSessions by criteria: {}", criteria);
        Page<AnnotationSession> page = annotationSessionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/annotation-sessions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /annotation-sessions/count : count all the annotationSessions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/annotation-sessions/count")
    public ResponseEntity<Long> countAnnotationSessions(AnnotationSessionCriteria criteria) {
        log.debug("REST request to count AnnotationSessions by criteria: {}", criteria);
        return ResponseEntity.ok().body(annotationSessionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /annotation-sessions/:id : get the "id" annotationSession.
     *
     * @param id the id of the annotationSession to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the annotationSession, or with status 404 (Not Found)
     */
    @GetMapping("/annotation-sessions/{id}")
    public ResponseEntity<AnnotationSession> getAnnotationSession(@PathVariable Long id) {
        log.debug("REST request to get AnnotationSession : {}", id);
        Optional<AnnotationSession> annotationSession = annotationSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annotationSession);
    }

    /**
     * DELETE  /annotation-sessions/:id : delete the "id" annotationSession.
     *
     * @param id the id of the annotationSession to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/annotation-sessions/{id}")
    public ResponseEntity<Void> deleteAnnotationSession(@PathVariable Long id) {
        log.debug("REST request to delete AnnotationSession : {}", id);
        annotationSessionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/annotation-sessions?query=:query : search for the annotationSession corresponding
     * to the query.
     *
     * @param query the query of the annotationSession search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/annotation-sessions")
    public ResponseEntity<List<AnnotationSession>> searchAnnotationSessions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnnotationSessions for query {}", query);
        Page<AnnotationSession> page = annotationSessionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/annotation-sessions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
