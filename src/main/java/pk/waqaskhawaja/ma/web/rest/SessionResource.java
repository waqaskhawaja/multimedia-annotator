package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.Session;
import pk.waqaskhawaja.ma.service.SessionService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.SessionCriteria;
import pk.waqaskhawaja.ma.service.SessionQueryService;
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
 * REST controller for managing Session.
 */
@RestController
@RequestMapping("/api")
public class SessionResource {

    private final Logger log = LoggerFactory.getLogger(SessionResource.class);

    private static final String ENTITY_NAME = "session";

    private final SessionService sessionService;

    private final SessionQueryService sessionQueryService;

    public SessionResource(SessionService sessionService, SessionQueryService sessionQueryService) {
        this.sessionService = sessionService;
        this.sessionQueryService = sessionQueryService;
    }

    /**
     * POST  /sessions : Create a new session.
     *
     * @param session the session to create
     * @return the ResponseEntity with status 201 (Created) and with body the new session, or with status 400 (Bad Request) if the session has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sessions")
    public ResponseEntity<Session> createSession(@RequestBody Session session) throws URISyntaxException {
        log.debug("REST request to save Session : {}", session);
        if (session.getId() != null) {
            throw new BadRequestAlertException("A new session cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Session result = sessionService.save(session);
        return ResponseEntity.created(new URI("/api/sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sessions : Updates an existing session.
     *
     * @param session the session to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated session,
     * or with status 400 (Bad Request) if the session is not valid,
     * or with status 500 (Internal Server Error) if the session couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sessions")
    public ResponseEntity<Session> updateSession(@RequestBody Session session) throws URISyntaxException {
        log.debug("REST request to update Session : {}", session);
        if (session.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Session result = sessionService.save(session);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, session.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sessions : get all the sessions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sessions in body
     */
    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getAllSessions(SessionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sessions by criteria: {}", criteria);
        Page<Session> page = sessionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sessions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /sessions/count : count all the sessions.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/sessions/count")
    public ResponseEntity<Long> countSessions(SessionCriteria criteria) {
        log.debug("REST request to count Sessions by criteria: {}", criteria);
        return ResponseEntity.ok().body(sessionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /sessions/:id : get the "id" session.
     *
     * @param id the id of the session to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the session, or with status 404 (Not Found)
     */
    @GetMapping("/sessions/{id}")
    public ResponseEntity<Session> getSession(@PathVariable Long id) {
        log.debug("REST request to get Session : {}", id);
        Optional<Session> session = sessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(session);
    }

    /**
     * DELETE  /sessions/:id : delete the "id" session.
     *
     * @param id the id of the session to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        log.debug("REST request to delete Session : {}", id);
        sessionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sessions?query=:query : search for the session corresponding
     * to the query.
     *
     * @param query the query of the session search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sessions")
    public ResponseEntity<List<Session>> searchSessions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sessions for query {}", query);
        Page<Session> page = sessionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sessions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
