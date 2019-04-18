package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.Scenario;
import pk.waqaskhawaja.ma.repository.ScenarioRepository;
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
 * REST controller for managing Scenario.
 */
@RestController
@RequestMapping("/api")
public class ScenarioResource {

    private final Logger log = LoggerFactory.getLogger(ScenarioResource.class);

    private static final String ENTITY_NAME = "scenario";

    private final ScenarioRepository scenarioRepository;

    public ScenarioResource(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    /**
     * POST  /scenarios : Create a new scenario.
     *
     * @param scenario the scenario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scenario, or with status 400 (Bad Request) if the scenario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scenarios")
    public ResponseEntity<Scenario> createScenario(@RequestBody Scenario scenario) throws URISyntaxException {
        log.debug("REST request to save Scenario : {}", scenario);
        if (scenario.getId() != null) {
            throw new BadRequestAlertException("A new scenario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scenario result = scenarioRepository.save(scenario);
        return ResponseEntity.created(new URI("/api/scenarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scenarios : Updates an existing scenario.
     *
     * @param scenario the scenario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scenario,
     * or with status 400 (Bad Request) if the scenario is not valid,
     * or with status 500 (Internal Server Error) if the scenario couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scenarios")
    public ResponseEntity<Scenario> updateScenario(@RequestBody Scenario scenario) throws URISyntaxException {
        log.debug("REST request to update Scenario : {}", scenario);
        if (scenario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Scenario result = scenarioRepository.save(scenario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scenario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scenarios : get all the scenarios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of scenarios in body
     */
    @GetMapping("/scenarios")
    public List<Scenario> getAllScenarios() {
        log.debug("REST request to get all Scenarios");
        return scenarioRepository.findAll();
    }

    /**
     * GET  /scenarios/:id : get the "id" scenario.
     *
     * @param id the id of the scenario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scenario, or with status 404 (Not Found)
     */
    @GetMapping("/scenarios/{id}")
    public ResponseEntity<Scenario> getScenario(@PathVariable Long id) {
        log.debug("REST request to get Scenario : {}", id);
        Optional<Scenario> scenario = scenarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scenario);
    }

    /**
     * DELETE  /scenarios/:id : delete the "id" scenario.
     *
     * @param id the id of the scenario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scenarios/{id}")
    public ResponseEntity<Void> deleteScenario(@PathVariable Long id) {
        log.debug("REST request to delete Scenario : {}", id);
        scenarioRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
