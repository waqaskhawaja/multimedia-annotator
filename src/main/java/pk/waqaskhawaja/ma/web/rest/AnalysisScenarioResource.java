package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import pk.waqaskhawaja.ma.service.AnalysisScenarioService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.service.dto.AnalysisScenarioCriteria;
import pk.waqaskhawaja.ma.service.AnalysisScenarioQueryService;

import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link pk.waqaskhawaja.ma.domain.AnalysisScenario}.
 */
@RestController
@RequestMapping("/api")
public class AnalysisScenarioResource {

    private final Logger log = LoggerFactory.getLogger(AnalysisScenarioResource.class);

    private static final String ENTITY_NAME = "analysisScenario";
    
    private String applicationName= "ma";

    private final AnalysisScenarioService analysisScenarioService;

    private final AnalysisScenarioQueryService analysisScenarioQueryService;

    public AnalysisScenarioResource(AnalysisScenarioService analysisScenarioService, AnalysisScenarioQueryService analysisScenarioQueryService) {
        this.analysisScenarioService = analysisScenarioService;
        this.analysisScenarioQueryService = analysisScenarioQueryService;
    }

    /**
     * {@code POST  /analysis-scenarios} : Create a new analysisScenario.
     *
     * @param analysisScenario the analysisScenario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analysisScenario, or with status {@code 400 (Bad Request)} if the analysisScenario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/analysis-scenarios")
    public ResponseEntity<AnalysisScenario> createAnalysisScenario(@RequestBody AnalysisScenario analysisScenario) throws URISyntaxException {
        log.debug("REST request to save AnalysisScenario : {}", analysisScenario);
        if (analysisScenario.getId() != null) {
            throw new BadRequestAlertException("A new analysisScenario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalysisScenario result = analysisScenarioService.save(analysisScenario);
        return ResponseEntity.created(new URI("/api/analysis-scenarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /analysis-scenarios} : Updates an existing analysisScenario.
     *
     * @param analysisScenario the analysisScenario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analysisScenario,
     * or with status {@code 400 (Bad Request)} if the analysisScenario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analysisScenario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/analysis-scenarios")
    public ResponseEntity<AnalysisScenario> updateAnalysisScenario(@RequestBody AnalysisScenario analysisScenario) throws URISyntaxException {
        log.debug("REST request to update AnalysisScenario : {}", analysisScenario);
        if (analysisScenario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnalysisScenario result = analysisScenarioService.save(analysisScenario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analysisScenario.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /analysis-scenarios} : get all the analysisScenarios.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analysisScenarios in body.
     */
    @GetMapping("/analysis-scenarios")
    public ResponseEntity<List<AnalysisScenario>> getAllAnalysisScenarios(AnalysisScenarioCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnalysisScenarios by criteria: {}", criteria);
        Page<AnalysisScenario> page = analysisScenarioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/analysis-sceanrios");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /analysis-scenarios/count} : count all the analysisScenarios.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/analysis-scenarios/count")
    public ResponseEntity<Long> countAnalysisScenarios(AnalysisScenarioCriteria criteria) {
        log.debug("REST request to count AnalysisScenarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(analysisScenarioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /analysis-scenarios/:id} : get the "id" analysisScenario.
     *
     * @param id the id of the analysisScenario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analysisScenario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/analysis-scenarios/{id}")
    public ResponseEntity<AnalysisScenario> getAnalysisScenario(@PathVariable Long id) {
        log.debug("REST request to get AnalysisScenario : {}", id);
        Optional<AnalysisScenario> analysisScenario = analysisScenarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(analysisScenario);
    }

    /**
     * {@code DELETE  /analysis-scenarios/:id} : delete the "id" analysisScenario.
     *
     * @param id the id of the analysisScenario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/analysis-scenarios/{id}")
    public ResponseEntity<Void> deleteAnalysisScenario(@PathVariable Long id) {
        log.debug("REST request to delete AnalysisScenario : {}", id);
        analysisScenarioService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/analysis-scenarios?query=:query} : search for the analysisScenario corresponding
     * to the query.
     *
     * @param query the query of the analysisScenario search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/analysis-scenarios")
    public ResponseEntity<List<AnalysisScenario>> searchAnalysisScenarios(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnalysisScenarios for query {}", query);
        Page<AnalysisScenario> page = analysisScenarioService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,"/api/_search/analysis-scenarios");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
