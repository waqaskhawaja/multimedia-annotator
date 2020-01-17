package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.Session;
import pk.waqaskhawaja.ma.repository.SessionRepository;
import pk.waqaskhawaja.ma.repository.search.SessionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Session.
 */
@Service
@Transactional
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    private final SessionSearchRepository sessionSearchRepository;

    public SessionService(SessionRepository sessionRepository, SessionSearchRepository sessionSearchRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionSearchRepository = sessionSearchRepository;
    }

    /**
     * Save a session.
     *
     * @param session the entity to save
     * @return the persisted entity
     */
    public Session save(Session session) {
        log.debug("Request to save Session : {}", session);
        Session result = sessionRepository.save(session);
        sessionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sessions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Session> findAll(Pageable pageable) {
        log.debug("Request to get all Sessions");
        return sessionRepository.findAll(pageable);
    }


    /**
     * Get one session by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Session> findOne(Long id) {
        log.debug("Request to get Session : {}", id);
        return sessionRepository.findById(id);
    }

    /**
     * Delete the session by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Session : {}", id);
        sessionRepository.deleteById(id);
        sessionSearchRepository.deleteById(id);
    }

    /**
     * Search for the session corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Session> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sessions for query {}", query);
        return sessionSearchRepository.search(queryStringQuery(query), pageable);    }
}
