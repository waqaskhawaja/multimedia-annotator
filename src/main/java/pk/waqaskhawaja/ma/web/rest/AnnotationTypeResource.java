package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.domain.AnnotationType;
import pk.waqaskhawaja.ma.service.AnnotationTypeService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link pk.waqaskhawaja.ma.domain.AnnotationType}.
 */
@RestController
@RequestMapping("/api")
public class AnnotationTypeResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationTypeResource.class);

    private static final String ENTITY_NAME = "annotationType";


    private final AnnotationTypeService annotationTypeService;

    public AnnotationTypeResource(AnnotationTypeService annotationTypeService) {
        this.annotationTypeService = annotationTypeService;
    }

    /**
     * {@code POST  /annotation-types} : Create a new annotationType.
     *
     * @param annotationType the annotationType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annotationType, or with status {@code 400 (Bad Request)} if the annotationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annotation-types")
    public ResponseEntity<AnnotationType> createAnnotationType(@RequestBody AnnotationType annotationType) throws URISyntaxException {
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
     * {@code PUT  /annotation-types} : Updates an existing annotationType.
     *
     * @param annotationType the annotationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annotationType,
     * or with status {@code 400 (Bad Request)} if the annotationType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annotationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annotation-types")
    public ResponseEntity<AnnotationType> updateAnnotationType(@RequestBody AnnotationType annotationType) throws URISyntaxException {
        log.debug("REST request to update AnnotationType : {}", annotationType);
        if (annotationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnotationType result = annotationTypeService.save(annotationType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, annotationType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /annotation-types} : get all the annotationTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annotationTypes in body.
     */
    @GetMapping("/annotation-types")
    public List<AnnotationType> getAllAnnotationTypes() {
        log.debug("REST request to get all AnnotationTypes");
        return annotationTypeService.findAll();
    }

    /**
     * {@code GET  /annotation-types/:id} : get the "id" annotationType.
     *
     * @param id the id of the annotationType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annotationType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annotation-types/{id}")
    public ResponseEntity<AnnotationType> getAnnotationType(@PathVariable Long id) {
        log.debug("REST request to get AnnotationType : {}", id);
        Optional<AnnotationType> annotationType = annotationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annotationType);
    }

    /**
     * {@code DELETE  /annotation-types/:id} : delete the "id" annotationType.
     *
     * @param id the id of the annotationType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annotation-types/{id}")
    public ResponseEntity<Void> deleteAnnotationType(@PathVariable Long id) {
        log.debug("REST request to delete AnnotationType : {}", id);
        annotationTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/annotation-types?query=:query} : search for the annotationType corresponding
     * to the query.
     *
     * @param query the query of the annotationType search.
     * @return the result of the search.
     */
    @GetMapping("/_search/annotation-types")
    public List<AnnotationType> searchAnnotationTypes(@RequestParam String query) {
        log.debug("REST request to search AnnotationTypes for query {}", query);
        return annotationTypeService.search(query);
    }
}
