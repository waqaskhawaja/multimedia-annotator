package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.Annotation;
import pk.waqaskhawaja.ma.service.AnnotationService;
import pk.waqaskhawaja.ma.service.dto.InteractionRecordDTO;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.AnnotationCriteria;
import pk.waqaskhawaja.ma.service.AnnotationQueryService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Annotation.
 */
@RestController
@RequestMapping("/api")
public class AnnotationResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationResource.class);

    private static final String ENTITY_NAME = "annotation";

    private final AnnotationService annotationService;

    private final AnnotationQueryService annotationQueryService;

    public AnnotationResource(AnnotationService annotationService, AnnotationQueryService annotationQueryService) {
        this.annotationService = annotationService;
        this.annotationQueryService = annotationQueryService;
    }

    /**
     * POST  /annotations : Create a new annotation.
     *
     * @param annotation the annotation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new annotation, or with status 400 (Bad Request) if the annotation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/annotations")
    public ResponseEntity<Annotation> createAnnotation(@RequestBody Annotation annotation) throws URISyntaxException {
        log.debug("REST request to save Annotation : {}", annotation);
        if (annotation.getId() != null) {
            throw new BadRequestAlertException("A new annotation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Annotation result = annotationService.save(annotation);
        return ResponseEntity.created(new URI("/api/annotations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /annotations : Updates an existing annotation.
     *
     * @param annotation the annotation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated annotation,
     * or with status 400 (Bad Request) if the annotation is not valid,
     * or with status 500 (Internal Server Error) if the annotation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/annotations")
    public ResponseEntity<Annotation> updateAnnotation(@RequestBody Annotation annotation) throws URISyntaxException {
        log.debug("REST request to update Annotation : {}", annotation);
        if (annotation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Annotation result = annotationService.save(annotation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, annotation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /annotations : get all the annotations.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of annotations in body
     */
    @GetMapping("/annotations")
    public ResponseEntity<List<Annotation>> getAllAnnotations(AnnotationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Annotations by criteria: {}", criteria);
        Page<Annotation> page = annotationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/annotations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /annotations/count : count all the annotations.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/annotations/count")
    public ResponseEntity<Long> countAnnotations(AnnotationCriteria criteria) {
        log.debug("REST request to count Annotations by criteria: {}", criteria);
        return ResponseEntity.ok().body(annotationQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /annotations/:id : get the "id" annotation.
     *
     * @param id the id of the annotation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the annotation, or with status 404 (Not Found)
     */
    @GetMapping("/annotations/{id}")
    public ResponseEntity<Annotation> getAnnotation(@PathVariable Long id) {
        log.debug("REST request to get Annotation : {}", id);
        Optional<Annotation> annotation = annotationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annotation);
    }

    /**
     * DELETE  /annotations/:id : delete the "id" annotation.
     *
     * @param id the id of the annotation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/annotations/{id}")
    public ResponseEntity<Void> deleteAnnotation(@PathVariable Long id) {
        log.debug("REST request to delete Annotation : {}", id);
        annotationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/annotations?query=:query : search for the annotation corresponding
     * to the query.
     *
     * @param query the query of the annotation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/annotations")
    public ResponseEntity<List<Annotation>> searchAnnotations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Annotations for query {}", query);
        Page<Annotation> page = annotationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/annotations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }




    @PostMapping("/annotations/save")
    public String saveFromInteractionDto(@RequestParam Long[] interactionRecordDTOS,@RequestParam String Text, @RequestParam Long annotationID)
    {
        List<Long> check= Arrays.asList(interactionRecordDTOS);
       return annotationService.saveAndConvertFromInteractionDtoToAnnotation(interactionRecordDTOS,Text,annotationID);
    }

}
