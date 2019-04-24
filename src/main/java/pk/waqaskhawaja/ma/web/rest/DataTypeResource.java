package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.DataType;
import pk.waqaskhawaja.ma.repository.DataTypeRepository;
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
 * REST controller for managing DataType.
 */
@RestController
@RequestMapping("/api")
public class DataTypeResource {

    private final Logger log = LoggerFactory.getLogger(DataTypeResource.class);

    private static final String ENTITY_NAME = "dataType";

    private final DataTypeRepository dataTypeRepository;

    public DataTypeResource(DataTypeRepository dataTypeRepository) {
        this.dataTypeRepository = dataTypeRepository;
    }

    /**
     * POST  /data-types : Create a new dataType.
     *
     * @param dataType the dataType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataType, or with status 400 (Bad Request) if the dataType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-types")
    public ResponseEntity<DataType> createDataType(@RequestBody DataType dataType) throws URISyntaxException {
        log.debug("REST request to save DataType : {}", dataType);
        if (dataType.getId() != null) {
            throw new BadRequestAlertException("A new dataType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataType result = dataTypeRepository.save(dataType);
        return ResponseEntity.created(new URI("/api/data-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-types : Updates an existing dataType.
     *
     * @param dataType the dataType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataType,
     * or with status 400 (Bad Request) if the dataType is not valid,
     * or with status 500 (Internal Server Error) if the dataType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-types")
    public ResponseEntity<DataType> updateDataType(@RequestBody DataType dataType) throws URISyntaxException {
        log.debug("REST request to update DataType : {}", dataType);
        if (dataType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataType result = dataTypeRepository.save(dataType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-types : get all the dataTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dataTypes in body
     */
    @GetMapping("/data-types")
    public List<DataType> getAllDataTypes() {
        log.debug("REST request to get all DataTypes");
        return dataTypeRepository.findAll();
    }

    /**
     * GET  /data-types/:id : get the "id" dataType.
     *
     * @param id the id of the dataType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataType, or with status 404 (Not Found)
     */
    @GetMapping("/data-types/{id}")
    public ResponseEntity<DataType> getDataType(@PathVariable Long id) {
        log.debug("REST request to get DataType : {}", id);
        Optional<DataType> dataType = dataTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dataType);
    }

    /**
     * DELETE  /data-types/:id : delete the "id" dataType.
     *
     * @param id the id of the dataType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-types/{id}")
    public ResponseEntity<Void> deleteDataType(@PathVariable Long id) {
        log.debug("REST request to delete DataType : {}", id);
        dataTypeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
