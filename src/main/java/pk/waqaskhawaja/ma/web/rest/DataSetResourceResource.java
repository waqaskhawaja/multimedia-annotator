package pk.waqaskhawaja.ma.web.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.OAuth2Definition;
import org.springframework.beans.factory.annotation.Autowired;
import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import pk.waqaskhawaja.ma.domain.DataSet;
import pk.waqaskhawaja.ma.domain.DataSetResource;
import pk.waqaskhawaja.ma.domain.InteractionRecord;
import pk.waqaskhawaja.ma.service.DataSetResourceService;
import pk.waqaskhawaja.ma.service.DataSetService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link pk.waqaskhawaja.ma.domain.DataSetResource}.
 */
@RestController
@RequestMapping("/api")
public class DataSetResourceResource {

    private final Logger log = LoggerFactory.getLogger(DataSetResourceResource.class);

    private static final String ENTITY_NAME = "dataSetResource";

    @Autowired
    private DataSetService dataSetService;

    private final DataSetResourceService dataSetResourceService;

    public DataSetResourceResource(DataSetResourceService dataSetResourceService) {
        this.dataSetResourceService = dataSetResourceService;
    }

    /**
     * {@code POST  /data-set-resources} : Create a new dataSetResource.
     *
     * @param dataSetResource the dataSetResource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataSetResource, or with status {@code 400 (Bad Request)} if the dataSetResource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-set-resources")
    public ResponseEntity<DataSetResource> createDataSetResource(@RequestBody DataSetResource dataSetResource) throws URISyntaxException {
        log.debug("REST request to save DataSetResource : {}", dataSetResource);
        if (dataSetResource.getId() != null) {
            throw new BadRequestAlertException("A new dataSetResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Set<DataSet> dataSets = new HashSet<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode uploadedJson = null;


        if(dataSetResource.getName().equals("Data")){
            String jsonString = new String(dataSetResource.getSourceFile());

            try {
                uploadedJson = mapper.readTree(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }


            List<Integer> arrayOfInteractionRecord = new ArrayList<>();
            List <Integer> arrayOfIn= new ArrayList<>();

            final int i = 0;
            Consumer<JsonNode> data = (JsonNode node) -> {
                Iterator<Map.Entry<String, JsonNode>> jsonIterator = node.fields();
                DataSet dataSet = new DataSet();
                while(jsonIterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = jsonIterator.next();
                    String key = entry.getKey();
                    switch(key){
                        case "id":
                            dataSet.setIdentifier(entry.getValue().asText());
                            break;

                        case "title":
                            dataSet.setTitle(entry.getValue().asText());
                            break;


                        case "date":
                            break;

                        case "type":
                            dataSet.setType(entry.getValue().asText());
                            break;
                        case "contents":
                            dataSet.setContents(entry.getValue().asText());
                            break;
                        default:
                    }
                }
                dataSets.add(dataSet);
            };
            uploadedJson.forEach(data);
        }

        DataSetResource result = dataSetResourceService.save(dataSetResource);
        dataSets.forEach(dataSet -> dataSetService.save(dataSet));

/*        return ResponseEntity.created(new URI("/api/analysis-session-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
        DataSetResource result = dataSetResourceService.save(dataSetResource);*/
        return ResponseEntity.created(new URI("/api/data-set-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-set-resources} : Updates an existing dataSetResource.
     *
     * @param dataSetResource the dataSetResource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataSetResource,
     * or with status {@code 400 (Bad Request)} if the dataSetResource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataSetResource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-set-resources")
    public ResponseEntity<DataSetResource> updateDataSetResource(@RequestBody DataSetResource dataSetResource) throws URISyntaxException {
        log.debug("REST request to update DataSetResource : {}", dataSetResource);
        if (dataSetResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataSetResource result = dataSetResourceService.save(dataSetResource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataSetResource.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /data-set-resources} : get all the dataSetResources.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataSetResources in body.
     */
    @GetMapping("/data-set-resources")
    public List<DataSetResource> getAllDataSetResources() {
        log.debug("REST request to get all DataSetResources");
        return dataSetResourceService.findAll();
    }

    /**
     * {@code GET  /data-set-resources/:id} : get the "id" dataSetResource.
     *
     * @param id the id of the dataSetResource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataSetResource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-set-resources/{id}")
    public ResponseEntity<DataSetResource> getDataSetResource(@PathVariable Long id) {
        log.debug("REST request to get DataSetResource : {}", id);
        Optional<DataSetResource> dataSetResource = dataSetResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataSetResource);
    }

    /**
     * {@code DELETE  /data-set-resources/:id} : delete the "id" dataSetResource.
     *
     * @param id the id of the dataSetResource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-set-resources/{id}")
    public ResponseEntity<Void> deleteDataSetResource(@PathVariable Long id) {
        log.debug("REST request to delete DataSetResource : {}", id);
        dataSetResourceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/data-set-resources?query=:query} : search for the dataSetResource corresponding
     * to the query.
     *
     * @param query the query of the dataSetResource search.
     * @return the result of the search.
     */
    @GetMapping("/_search/data-set-resources")
    public List<DataSetResource> searchDataSetResources(@RequestParam String query) {
        log.debug("REST request to search DataSetResources for query {}", query);
        return dataSetResourceService.search(query);
    }
}
