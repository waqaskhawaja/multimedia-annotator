package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.InteractionType;
import pk.waqaskhawaja.ma.service.InteractionTypeService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.InteractionTypeCriteria;
import pk.waqaskhawaja.ma.service.InteractionTypeQueryService;
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
 * REST controller for managing InteractionType.
 */
@RestController
@RequestMapping("/api")
public class InteractionTypeResource {

    private final Logger log = LoggerFactory.getLogger(InteractionTypeResource.class);

    private static final String ENTITY_NAME = "interactionType";

    private final InteractionTypeService interactionTypeService;

    private final InteractionTypeQueryService interactionTypeQueryService;

    public InteractionTypeResource(InteractionTypeService interactionTypeService, InteractionTypeQueryService interactionTypeQueryService) {
        this.interactionTypeService = interactionTypeService;
        this.interactionTypeQueryService = interactionTypeQueryService;
    }

    /**
     * POST  /interaction-types : Create a new interactionType.
     *
     * @param interactionType the interactionType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interactionType, or with status 400 (Bad Request) if the interactionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interaction-types")
    public ResponseEntity<InteractionType> createInteractionType(@RequestBody InteractionType interactionType) throws URISyntaxException {
        log.debug("REST request to save InteractionType : {}", interactionType);
        if (interactionType.getId() != null) {
            throw new BadRequestAlertException("A new interactionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InteractionType result = interactionTypeService.save(interactionType);
        return ResponseEntity.created(new URI("/api/interaction-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interaction-types : Updates an existing interactionType.
     *
     * @param interactionType the interactionType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interactionType,
     * or with status 400 (Bad Request) if the interactionType is not valid,
     * or with status 500 (Internal Server Error) if the interactionType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interaction-types")
    public ResponseEntity<InteractionType> updateInteractionType(@RequestBody InteractionType interactionType) throws URISyntaxException {
        log.debug("REST request to update InteractionType : {}", interactionType);
        if (interactionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InteractionType result = interactionTypeService.save(interactionType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, interactionType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interaction-types : get all the interactionTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of interactionTypes in body
     */
    @GetMapping("/interaction-types")
    public ResponseEntity<List<InteractionType>> getAllInteractionTypes(InteractionTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InteractionTypes by criteria: {}", criteria);
        Page<InteractionType> page = interactionTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/interaction-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /interaction-types/count : count all the interactionTypes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/interaction-types/count")
    public ResponseEntity<Long> countInteractionTypes(InteractionTypeCriteria criteria) {
        log.debug("REST request to count InteractionTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(interactionTypeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /interaction-types/:id : get the "id" interactionType.
     *
     * @param id the id of the interactionType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interactionType, or with status 404 (Not Found)
     */
    @GetMapping("/interaction-types/{id}")
    public ResponseEntity<InteractionType> getInteractionType(@PathVariable Long id) {
        log.debug("REST request to get InteractionType : {}", id);
        Optional<InteractionType> interactionType = interactionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interactionType);
    }

    /**
     * DELETE  /interaction-types/:id : delete the "id" interactionType.
     *
     * @param id the id of the interactionType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interaction-types/{id}")
    public ResponseEntity<Void> deleteInteractionType(@PathVariable Long id) {
        log.debug("REST request to delete InteractionType : {}", id);
        interactionTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/interaction-types?query=:query : search for the interactionType corresponding
     * to the query.
     *
     * @param query the query of the interactionType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/interaction-types")
    public ResponseEntity<List<InteractionType>> searchInteractionTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InteractionTypes for query {}", query);
        Page<InteractionType> page = interactionTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/interaction-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
