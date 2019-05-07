package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.Scenario;
import pk.waqaskhawaja.ma.service.ScenarioService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.ScenarioCriteria;
import pk.waqaskhawaja.ma.service.ScenarioQueryService;
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
 * REST controller for managing Scenario.
 */
@RestController
@RequestMapping("/api")
public class ScenarioResource {

    private final Logger log = LoggerFactory.getLogger(ScenarioResource.class);

    private static final String ENTITY_NAME = "scenario";

    private final ScenarioService scenarioService;

    private final ScenarioQueryService scenarioQueryService;

    public ScenarioResource(ScenarioService scenarioService, ScenarioQueryService scenarioQueryService) {
        this.scenarioService = scenarioService;
        this.scenarioQueryService = scenarioQueryService;
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
        Scenario result = scenarioService.save(scenario);
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
        Scenario result = scenarioService.save(scenario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scenario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scenarios : get all the scenarios.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of scenarios in body
     */
    @GetMapping("/scenarios")
    public ResponseEntity<List<Scenario>> getAllScenarios(ScenarioCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Scenarios by criteria: {}", criteria);
        Page<Scenario> page = scenarioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scenarios");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /scenarios/count : count all the scenarios.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/scenarios/count")
    public ResponseEntity<Long> countScenarios(ScenarioCriteria criteria) {
        log.debug("REST request to count Scenarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(scenarioQueryService.countByCriteria(criteria));
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
        Optional<Scenario> scenario = scenarioService.findOne(id);
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
        scenarioService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/scenarios?query=:query : search for the scenario corresponding
     * to the query.
     *
     * @param query the query of the scenario search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/scenarios")
    public ResponseEntity<List<Scenario>> searchScenarios(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Scenarios for query {}", query);
        Page<Scenario> page = scenarioService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/scenarios");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
