package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.SourceDataType;
import pk.waqaskhawaja.ma.repository.SourceDataTypeRepository;
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
 * REST controller for managing SourceDataType.
 */
@RestController
@RequestMapping("/api")
public class SourceDataTypeResource {

    private final Logger log = LoggerFactory.getLogger(SourceDataTypeResource.class);

    private static final String ENTITY_NAME = "sourceDataType";

    private final SourceDataTypeRepository sourceDataTypeRepository;

    public SourceDataTypeResource(SourceDataTypeRepository sourceDataTypeRepository) {
        this.sourceDataTypeRepository = sourceDataTypeRepository;
    }

    /**
     * POST  /source-data-types : Create a new sourceDataType.
     *
     * @param sourceDataType the sourceDataType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sourceDataType, or with status 400 (Bad Request) if the sourceDataType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/source-data-types")
    public ResponseEntity<SourceDataType> createSourceDataType(@RequestBody SourceDataType sourceDataType) throws URISyntaxException {
        log.debug("REST request to save SourceDataType : {}", sourceDataType);
        if (sourceDataType.getId() != null) {
            throw new BadRequestAlertException("A new sourceDataType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourceDataType result = sourceDataTypeRepository.save(sourceDataType);
        return ResponseEntity.created(new URI("/api/source-data-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /source-data-types : Updates an existing sourceDataType.
     *
     * @param sourceDataType the sourceDataType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sourceDataType,
     * or with status 400 (Bad Request) if the sourceDataType is not valid,
     * or with status 500 (Internal Server Error) if the sourceDataType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/source-data-types")
    public ResponseEntity<SourceDataType> updateSourceDataType(@RequestBody SourceDataType sourceDataType) throws URISyntaxException {
        log.debug("REST request to update SourceDataType : {}", sourceDataType);
        if (sourceDataType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SourceDataType result = sourceDataTypeRepository.save(sourceDataType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sourceDataType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /source-data-types : get all the sourceDataTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sourceDataTypes in body
     */
    @GetMapping("/source-data-types")
    public List<SourceDataType> getAllSourceDataTypes() {
        log.debug("REST request to get all SourceDataTypes");
        return sourceDataTypeRepository.findAll();
    }

    /**
     * GET  /source-data-types/:id : get the "id" sourceDataType.
     *
     * @param id the id of the sourceDataType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sourceDataType, or with status 404 (Not Found)
     */
    @GetMapping("/source-data-types/{id}")
    public ResponseEntity<SourceDataType> getSourceDataType(@PathVariable Long id) {
        log.debug("REST request to get SourceDataType : {}", id);
        Optional<SourceDataType> sourceDataType = sourceDataTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sourceDataType);
    }

    /**
     * DELETE  /source-data-types/:id : delete the "id" sourceDataType.
     *
     * @param id the id of the sourceDataType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/source-data-types/{id}")
    public ResponseEntity<Void> deleteSourceDataType(@PathVariable Long id) {
        log.debug("REST request to delete SourceDataType : {}", id);
        sourceDataTypeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
