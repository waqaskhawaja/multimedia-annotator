package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.AnnotationType;
import pk.waqaskhawaja.ma.service.AnnotationTypeService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.AnnotationTypeCriteria;
import pk.waqaskhawaja.ma.service.AnnotationTypeQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AnnotationType.
 */
@RestController
@RequestMapping("/api")
public class AnnotationTypeResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationTypeResource.class);

    private static final String ENTITY_NAME = "annotationType";

    private final AnnotationTypeService annotationTypeService;

    private final AnnotationTypeQueryService annotationTypeQueryService;

    public AnnotationTypeResource(AnnotationTypeService annotationTypeService, AnnotationTypeQueryService annotationTypeQueryService) {
        this.annotationTypeService = annotationTypeService;
        this.annotationTypeQueryService = annotationTypeQueryService;
    }

    /**
     * POST  /annotation-types : Create a new annotationType.
     *
     * @param annotationType the annotationType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new annotationType, or with status 400 (Bad Request) if the annotationType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/annotation-types")
    public ResponseEntity<AnnotationType> createAnnotationType(@Valid @RequestBody AnnotationType annotationType) throws URISyntaxException {
        log.debug("REST request to save AnnotationType : {}", annotationType);
        if (annotationType.getId() != null) {
            throw new BadRequestAlertException("A new annotationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnotationType result = annotationTypeService.save(annotationType);
        return ResponseEntity.created(new URI("/api/annotation-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /annotation-types : Updates an existing annotationType.
     *
     * @param annotationType the annotationType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated annotationType,
     * or with status 400 (Bad Request) if the annotationType is not valid,
     * or with status 500 (Internal Server Error) if the annotationType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/annotation-types")
    public ResponseEntity<AnnotationType> updateAnnotationType(@Valid @RequestBody AnnotationType annotationType) throws URISyntaxException {
        log.debug("REST request to update AnnotationType : {}", annotationType);
        if (annotationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnotationType result = annotationTypeService.save(annotationType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, annotationType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /annotation-types : get all the annotationTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of annotationTypes in body
     */
    @GetMapping("/annotation-types")
    public ResponseEntity<List<AnnotationType>> getAllAnnotationTypes(AnnotationTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnnotationTypes by criteria: {}", criteria);
        Page<AnnotationType> page = annotationTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/annotation-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /annotation-types/count : count all the annotationTypes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/annotation-types/count")
    public ResponseEntity<Long> countAnnotationTypes(AnnotationTypeCriteria criteria) {
        log.debug("REST request to count AnnotationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(annotationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /annotation-types/:id : get the "id" annotationType.
     *
     * @param id the id of the annotationType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the annotationType, or with status 404 (Not Found)
     */
    @GetMapping("/annotation-types/{id}")
    public ResponseEntity<AnnotationType> getAnnotationType(@PathVariable Long id) {
        log.debug("REST request to get AnnotationType : {}", id);
        Optional<AnnotationType> annotationType = annotationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annotationType);
    }

    /**
     * DELETE  /annotation-types/:id : delete the "id" annotationType.
     *
     * @param id the id of the annotationType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/annotation-types/{id}")
    public ResponseEntity<Void> deleteAnnotationType(@PathVariable Long id) {
        log.debug("REST request to delete AnnotationType : {}", id);
        annotationTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/annotation-types?query=:query : search for the annotationType corresponding
     * to the query.
     *
     * @param query the query of the annotationType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/annotation-types")
    public ResponseEntity<List<AnnotationType>> searchAnnotationTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnnotationTypes for query {}", query);
        Page<AnnotationType> page = annotationTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/annotation-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
