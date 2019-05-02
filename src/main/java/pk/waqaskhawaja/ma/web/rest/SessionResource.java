package pk.waqaskhawaja.ma.web.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pk.waqaskhawaja.ma.domain.DataRecord;
import pk.waqaskhawaja.ma.domain.Session;
import pk.waqaskhawaja.ma.repository.DataRecordRepository;
import pk.waqaskhawaja.ma.repository.InteractionTypeRepository;
import pk.waqaskhawaja.ma.repository.SessionRepository;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;
import java.util.function.Consumer;

/**
 * REST controller for managing Session.
 */
@RestController
@RequestMapping("/api")
public class SessionResource {

    private final Logger log = LoggerFactory.getLogger(SessionResource.class);

    private static final String ENTITY_NAME = "session";

    private final SessionRepository sessionRepository;
    private final InteractionTypeRepository interactionTypeRepository;
    private final DataRecordRepository dataRecordRepository;

    public SessionResource(SessionRepository sessionRepository, InteractionTypeRepository interactionTypeRepository,
                            DataRecordRepository dataRecordRepository) {
        this.sessionRepository = sessionRepository;
        this.interactionTypeRepository  = interactionTypeRepository;
        this.dataRecordRepository = dataRecordRepository;
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

        Set<DataRecord> dataRecords = new HashSet<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode uploadedJson = null;

        log.debug("REST request to save Session : {}", session);
        if (session.getId() != null) {
            throw new BadRequestAlertException("A new session cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String jsonString = new String(session.getSourceFile());

        try {
            uploadedJson = mapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Consumer<JsonNode> data = (JsonNode node) -> {
            Iterator<Map.Entry<String, JsonNode>> jsonIterator = node.fields();
            DataRecord dataRecord = new DataRecord();
            while(jsonIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = jsonIterator.next();
                String key = entry.getKey();
                switch(key){
                    case "duration":
                        dataRecord.setDuration(entry.getValue().asInt());
                        break;
                    case "Text":
                        dataRecord.setText(entry.getValue().asText());
                        break;
                    case "InteractionType":
                        dataRecord.setInteractionType(interactionTypeRepository.findByName(entry.getValue().asText()));
                        break;
                    case "ID":
                        dataRecord.setSourceId(entry.getValue().asText());
                        break;
                    case "time":
                        dataRecord.setTime(entry.getValue().asInt());
                        break;
                    default:
                }
            }
            dataRecord.setSession(session);
            dataRecords.add(dataRecord);
        };
        uploadedJson.forEach(data);

        Session result = sessionRepository.save(session);
        dataRecordRepository.saveAll(dataRecords);

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
        Session result = sessionRepository.save(session);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, session.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sessions : get all the sessions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sessions in body
     */
    @GetMapping("/sessions")
    public List<Session> getAllSessions() {
        log.debug("REST request to get all Sessions");
        return sessionRepository.findAll();
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
        Optional<Session> session = sessionRepository.findById(id);
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
        sessionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
