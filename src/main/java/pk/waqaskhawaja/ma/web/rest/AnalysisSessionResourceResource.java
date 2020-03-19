package pk.waqaskhawaja.ma.web.rest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import pk.waqaskhawaja.ma.domain.InteractionRecord;
import pk.waqaskhawaja.ma.service.AnalysisSessionResourceService;
import pk.waqaskhawaja.ma.service.InteractionRecordService;
import pk.waqaskhawaja.ma.service.InteractionTypeService;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.AnalysisSessionResourceCriteria;
import pk.waqaskhawaja.ma.service.AnalysisSessionResourceQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * REST controller for managing AnalysisSessionResource.
 */
@RestController
@RequestMapping("/api")
public class AnalysisSessionResourceResource {

    private final Logger log = LoggerFactory.getLogger(AnalysisSessionResourceResource.class);

    private static final String ENTITY_NAME = "analysisSessionResource";

    private final AnalysisSessionResourceService analysisSessionResourceService;
    private final InteractionRecordService interactionRecordService;
    private final InteractionTypeService interactionTypeService;

    private final AnalysisSessionResourceQueryService analysisSessionResourceQueryService;

    public AnalysisSessionResourceResource(AnalysisSessionResourceService analysisSessionResourceService, AnalysisSessionResourceQueryService analysisSessionResourceQueryService,
                                           InteractionRecordService interactionRecordService, InteractionTypeService interactionTypeService) {
        this.analysisSessionResourceService = analysisSessionResourceService;
        this.analysisSessionResourceQueryService = analysisSessionResourceQueryService;
        this.interactionRecordService = interactionRecordService;
        this.interactionTypeService = interactionTypeService;
    }

    /**
     * POST  /analysis-session-resources : Create a new analysisSessionResource.
     *
     * @param analysisSessionResource the analysisSessionResource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new analysisSessionResource, or with status 400 (Bad Request) if the analysisSessionResource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/analysis-session-resources")
    public ResponseEntity<AnalysisSessionResource> createAnalysisSessionResource(@Valid @RequestBody AnalysisSessionResource analysisSessionResource) throws URISyntaxException {
        log.debug("REST request to save AnalysisSessionResource : {}", analysisSessionResource);

        if (analysisSessionResource.getId() != null) {
            throw new BadRequestAlertException("A new analysisSessionResource cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Set<InteractionRecord> interactionRecords = new HashSet<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode uploadedJson = null;

        if(analysisSessionResource.getResourceType().getName().equals("Interaction Log")){
            String jsonString = new String(analysisSessionResource.getSourceFile());

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
                InteractionRecord interactionRecord = new InteractionRecord();
                while(jsonIterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = jsonIterator.next();
                    String key = entry.getKey();
                    switch(key){
                        case "duration":
                            arrayOfIn.add(entry.getValue().asInt());
                            Integer value  = arrayOfIn.stream().mapToInt(k-> k.intValue()).sum();
                            try {
                                FileWriter writer = new FileWriter("/opt/jsondata/duration.txt", true);
                                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                                bufferedWriter.write("duration");
                                bufferedWriter.write("\r\n");   // write new line
                                bufferedWriter.write(value.toString());
                                bufferedWriter.write("\r\n");   // write new line
                                bufferedWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            interactionRecord.setDuration(value);
                            break;
                        case "Text":
                            interactionRecord.setText(entry.getValue().asText());
                            break;
                        case "InteractionType":
                            interactionRecord.setInteractionType(interactionTypeService.findByName(entry.getValue().asText()).get());
                            break;
                        case "ID":
                            interactionRecord.setSourceId(entry.getValue().asText());
                            break;
                        case "time":
                            interactionRecord.setTime(entry.getValue().asInt());
                            break;
                        default:
                    }
                }
                interactionRecord.setAnalysisSessionResource(analysisSessionResource);
                interactionRecords.add(interactionRecord);
            };
            uploadedJson.forEach(data);
        }

        AnalysisSessionResource result = analysisSessionResourceService.save(analysisSessionResource);
        interactionRecords.forEach(interactionRecord -> interactionRecordService.save(interactionRecord));

        return ResponseEntity.created(new URI("/api/analysis-session-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /analysis-session-resources : Updates an existing analysisSessionResource.
     *
     * @param analysisSessionResource the analysisSessionResource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated analysisSessionResource,
     * or with status 400 (Bad Request) if the analysisSessionResource is not valid,
     * or with status 500 (Internal Server Error) if the analysisSessionResource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/analysis-session-resources")
    public ResponseEntity<AnalysisSessionResource> updateAnalysisSessionResource(@Valid @RequestBody AnalysisSessionResource analysisSessionResource) throws URISyntaxException {
        log.debug("REST request to update AnalysisSessionResource : {}", analysisSessionResource);
        if (analysisSessionResource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnalysisSessionResource result = analysisSessionResourceService.save(analysisSessionResource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analysisSessionResource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /analysis-session-resources : get all the analysisSessionResources.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of analysisSessionResources in body
     */
    @GetMapping("/analysis-session-resources")
    public ResponseEntity<List<AnalysisSessionResource>> getAllAnalysisSessionResources(AnalysisSessionResourceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnalysisSessionResources by criteria: {}", criteria);
        Page<AnalysisSessionResource> page = analysisSessionResourceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/analysis-session-resources");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /analysis-session-resources/count : count all the analysisSessionResources.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/analysis-session-resources/count")
    public ResponseEntity<Long> countAnalysisSessionResources(AnalysisSessionResourceCriteria criteria) {
        log.debug("REST request to count AnalysisSessionResources by criteria: {}", criteria);
        return ResponseEntity.ok().body(analysisSessionResourceQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /analysis-session-resources/:id : get the "id" analysisSessionResource.
     *
     * @param id the id of the analysisSessionResource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analysisSessionResource, or with status 404 (Not Found)
     */
    @GetMapping("/analysis-session-resources/{id}")
    public ResponseEntity<AnalysisSessionResource> getAnalysisSessionResource(@PathVariable Long id) {
        log.debug("REST request to get AnalysisSessionResource : {}", id);
        Optional<AnalysisSessionResource> analysisSessionResource = analysisSessionResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(analysisSessionResource);
    }

    /**
     * GET  /analysis-session-resources/video-by-analysis-session:analysisSessionId : get the "analysisSessionId" analysisSessionResource.
     *
     * @param analysisSessionId the id of the analysisSession to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analysisSessionResource, or with status 404 (Not Found)
     */
    @GetMapping("/analysis-session-resources/video-by-analysis-session/{analysisSessionId}")
    public ResponseEntity<AnalysisSessionResource> getAnalysisSessionVideoResourceByAnalysisSession(@PathVariable Long analysisSessionId) {
        log.debug("REST request to get AnalysisSessionResourceByAnalysisSessionId : {}", analysisSessionId);
        Optional<AnalysisSessionResource> analysisSessionResource = analysisSessionResourceService.findVideoByAnalysisSessionId(analysisSessionId);
        return ResponseUtil.wrapOrNotFound(analysisSessionResource);
    }

    /**
     * DELETE  /analysis-session-resources/:id : delete the "id" analysisSessionResource.
     *
     * @param id the id of the analysisSessionResource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/analysis-session-resources/{id}")
    public ResponseEntity<Void> deleteAnalysisSessionResource(@PathVariable Long id) {
        log.debug("REST request to delete AnalysisSessionResource : {}", id);
        analysisSessionResourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/analysis-session-resources?query=:query : search for the analysisSessionResource corresponding
     * to the query.
     *
     * @param query the query of the analysisSessionResource search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/analysis-session-resources")
    public ResponseEntity<List<AnalysisSessionResource>> searchAnalysisSessionResources(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AnalysisSessionResources for query {}", query);
        Page<AnalysisSessionResource> page = analysisSessionResourceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/analysis-session-resources");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
