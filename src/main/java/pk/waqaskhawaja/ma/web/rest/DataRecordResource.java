package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.DataRecord;
import pk.waqaskhawaja.ma.repository.DataRecordRepository;
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
 * REST controller for managing DataRecord.
 */
@RestController
@RequestMapping("/api")
public class DataRecordResource {

    private final Logger log = LoggerFactory.getLogger(DataRecordResource.class);

    private static final String ENTITY_NAME = "dataRecord";

    private final DataRecordRepository dataRecordRepository;

    public DataRecordResource(DataRecordRepository dataRecordRepository) {
        this.dataRecordRepository = dataRecordRepository;
    }

    /**
     * POST  /data-records : Create a new dataRecord.
     *
     * @param dataRecord the dataRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataRecord, or with status 400 (Bad Request) if the dataRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-records")
    public ResponseEntity<DataRecord> createDataRecord(@RequestBody DataRecord dataRecord) throws URISyntaxException {
        log.debug("REST request to save DataRecord : {}", dataRecord);
        if (dataRecord.getId() != null) {
            throw new BadRequestAlertException("A new dataRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataRecord result = dataRecordRepository.save(dataRecord);
        return ResponseEntity.created(new URI("/api/data-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-records : Updates an existing dataRecord.
     *
     * @param dataRecord the dataRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataRecord,
     * or with status 400 (Bad Request) if the dataRecord is not valid,
     * or with status 500 (Internal Server Error) if the dataRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-records")
    public ResponseEntity<DataRecord> updateDataRecord(@RequestBody DataRecord dataRecord) throws URISyntaxException {
        log.debug("REST request to update DataRecord : {}", dataRecord);
        if (dataRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataRecord result = dataRecordRepository.save(dataRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-records : get all the dataRecords.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dataRecords in body
     */
    @GetMapping("/data-records")
    public List<DataRecord> getAllDataRecords() {
        log.debug("REST request to get all DataRecords");
        return dataRecordRepository.findAll();
    }

    /**
     * GET  /data-records/:id : get the "id" dataRecord.
     *
     * @param id the id of the dataRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataRecord, or with status 404 (Not Found)
     */
    @GetMapping("/data-records/{id}")
    public ResponseEntity<DataRecord> getDataRecord(@PathVariable Long id) {
        log.debug("REST request to get DataRecord : {}", id);
        Optional<DataRecord> dataRecord = dataRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dataRecord);
    }

    /**
     * DELETE  /data-records/:id : delete the "id" dataRecord.
     *
     * @param id the id of the dataRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-records/{id}")
    public ResponseEntity<Void> deleteDataRecord(@PathVariable Long id) {
        log.debug("REST request to delete DataRecord : {}", id);
        dataRecordRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
