package pk.waqaskhawaja.ma.web.rest;

import pk.waqaskhawaja.ma.domain.DataSet;
import pk.waqaskhawaja.ma.service.DataSetService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;

import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link pk.waqaskhawaja.ma.domain.DataSet}.
 */
@RestController
@RequestMapping("/api")
public class DataSetResource {

    private final Logger log = LoggerFactory.getLogger(DataSetResource.class);

    private static final String ENTITY_NAME = "dataSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataSetService dataSetService;

    public DataSetResource(DataSetService dataSetService) {
        this.dataSetService = dataSetService;
    }

    /**
     * {@code POST  /data-sets} : Create a new dataSet.
     *
     * @param dataSet the dataSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataSet, or with status {@code 400 (Bad Request)} if the dataSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-sets")
    public ResponseEntity<DataSet> createDataSet(@RequestBody DataSet dataSet) throws URISyntaxException {
        log.debug("REST request to save DataSet : {}", dataSet);
        if (dataSet.getId() != null) {
            throw new BadRequestAlertException("A new dataSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataSet result = dataSetService.save(dataSet);
        return ResponseEntity.created(new URI("/api/data-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-sets} : Updates an existing dataSet.
     *
     * @param dataSet the dataSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataSet,
     * or with status {@code 400 (Bad Request)} if the dataSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-sets")
    public ResponseEntity<DataSet> updateDataSet(@RequestBody DataSet dataSet) throws URISyntaxException {
        log.debug("REST request to update DataSet : {}", dataSet);
        if (dataSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataSet result = dataSetService.save(dataSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /data-sets} : get all the dataSets.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataSets in body.
     */
    @GetMapping("/data-sets")
    public List<DataSet> getAllDataSets() {
        log.debug("REST request to get all DataSets");
        return dataSetService.findAll();
    }

    /**
     * {@code GET  /data-sets/:id} : get the "id" dataSet.
     *
     * @param id the id of the dataSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-sets/{id}")
    public ResponseEntity<DataSet> getDataSet(@PathVariable Long id) {
        log.debug("REST request to get DataSet : {}", id);
        Optional<DataSet> dataSet = dataSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataSet);
    }

    /**
     * {@code DELETE  /data-sets/:id} : delete the "id" dataSet.
     *
     * @param id the id of the dataSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-sets/{id}")
    public ResponseEntity<Void> deleteDataSet(@PathVariable Long id) {
        log.debug("REST request to delete DataSet : {}", id);
        dataSetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/data-sets?query=:query} : search for the dataSet corresponding
     * to the query.
     *
     * @param query the query of the dataSet search.
     * @return the result of the search.
     */
    @GetMapping("/_search/data-sets")
    public List<DataSet> searchDataSets(@RequestParam String query) {
        log.debug("REST request to search DataSets for query {}", query);
        return dataSetService.search(query);
    }
}
