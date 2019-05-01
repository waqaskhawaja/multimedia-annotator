package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.Analyst;
import pk.waqaskhawaja.ma.repository.AnalystRepository;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Analyst.
 */
@RestController
@RequestMapping("/api")
public class AnalystResource {

    private final Logger log = LoggerFactory.getLogger(AnalystResource.class);

    private static final String ENTITY_NAME = "analyst";

    private final AnalystRepository analystRepository;

    public AnalystResource(AnalystRepository analystRepository) {
        this.analystRepository = analystRepository;
    }

    /**
     * POST  /analysts : Create a new analyst.
     *
     * @param analyst the analyst to create
     * @return the ResponseEntity with status 201 (Created) and with body the new analyst, or with status 400 (Bad Request) if the analyst has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/analysts")
    public ResponseEntity<Analyst> createAnalyst(@RequestBody Analyst analyst) throws URISyntaxException {
        log.debug("REST request to save Analyst : {}", analyst);
        if (analyst.getId() != null) {
            throw new BadRequestAlertException("A new analyst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Analyst result = analystRepository.save(analyst);
        return ResponseEntity.created(new URI("/api/analysts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /analysts : Updates an existing analyst.
     *
     * @param analyst the analyst to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated analyst,
     * or with status 400 (Bad Request) if the analyst is not valid,
     * or with status 500 (Internal Server Error) if the analyst couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/analysts")
    public ResponseEntity<Analyst> updateAnalyst(@RequestBody Analyst analyst) throws URISyntaxException {
        log.debug("REST request to update Analyst : {}", analyst);
        if (analyst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Analyst result = analystRepository.save(analyst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analyst.getId().toString()))
            .body(result);
    }

    /**
     * GET  /analysts : get all the analysts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of analysts in body
     */
    @GetMapping("/analysts")
    public List<Analyst> getAllAnalysts() {
        log.debug("REST request to get all Analysts");
        return analystRepository.findAll();
    }

    /**
     * GET  /analysts/:id : get the "id" analyst.
     *
     * @param id the id of the analyst to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analyst, or with status 404 (Not Found)
     */
    @GetMapping("/analysts/{id}")
    public ResponseEntity<Analyst> getAnalyst(@PathVariable Long id) {
        log.debug("REST request to get Analyst : {}", id);
        Optional<Analyst> analyst = analystRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(analyst);
    }

    /**
     * DELETE  /analysts/:id : delete the "id" analyst.
     *
     * @param id the id of the analyst to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/analysts/{id}")
    public ResponseEntity<Void> deleteAnalyst(@PathVariable Long id) {
        log.debug("REST request to delete Analyst : {}", id);
        analystRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
