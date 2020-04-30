package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.InteractionRecord;
import pk.waqaskhawaja.ma.service.InteractionRecordService;
import pk.waqaskhawaja.ma.service.dto.InteractionRecordDTO;
import pk.waqaskhawaja.ma.web.rest.errors.BadRequestAlertException;
import pk.waqaskhawaja.ma.web.rest.util.HeaderUtil;
import pk.waqaskhawaja.ma.web.rest.util.PaginationUtil;
import pk.waqaskhawaja.ma.service.dto.InteractionRecordCriteria;
import pk.waqaskhawaja.ma.service.InteractionRecordQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InteractionRecord.
 */
@RestController
@RequestMapping("/api")
public class InteractionRecordResource {

    private final Logger log = LoggerFactory.getLogger(InteractionRecordResource.class);

    private static final String ENTITY_NAME = "interactionRecord";

    private final InteractionRecordService interactionRecordService;

    private final InteractionRecordQueryService interactionRecordQueryService;

    public InteractionRecordResource(InteractionRecordService interactionRecordService, InteractionRecordQueryService interactionRecordQueryService) {
        this.interactionRecordService = interactionRecordService;
        this.interactionRecordQueryService = interactionRecordQueryService;
    }

    /**
     * POST  /interaction-records : Create a new interactionRecord.
     *
     * @param interactionRecord the interactionRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interactionRecord, or with status 400 (Bad Request) if the interactionRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interaction-records")
    public ResponseEntity<InteractionRecord> createInteractionRecord(@RequestBody InteractionRecord interactionRecord) throws URISyntaxException {
        log.debug("REST request to save InteractionRecord : {}", interactionRecord);
        if (interactionRecord.getId() != null) {
            throw new BadRequestAlertException("A new interactionRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InteractionRecord result = interactionRecordService.save(interactionRecord);
        return ResponseEntity.created(new URI("/api/interaction-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interaction-records : Updates an existing interactionRecord.
     *
     * @param interactionRecord the interactionRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interactionRecord,
     * or with status 400 (Bad Request) if the interactionRecord is not valid,
     * or with status 500 (Internal Server Error) if the interactionRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interaction-records")
    public ResponseEntity<InteractionRecord> updateInteractionRecord(@RequestBody InteractionRecord interactionRecord) throws URISyntaxException {
        log.debug("REST request to update InteractionRecord : {}", interactionRecord);
        if (interactionRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InteractionRecord result = interactionRecordService.save(interactionRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, interactionRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interaction-records : get all the interactionRecords.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of interactionRecords in body
     */
    @GetMapping("/interaction-records")
    public ResponseEntity<List<InteractionRecord>> getAllInteractionRecords(InteractionRecordCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InteractionRecords by criteria: {}", criteria);
        Page<InteractionRecord> page = interactionRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/interaction-records");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }






    /**
    * GET  /interaction-records/count : count all the interactionRecords.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/interaction-records/count")
    public ResponseEntity<Long> countInteractionRecords(InteractionRecordCriteria criteria) {
        log.debug("REST request to count InteractionRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(interactionRecordQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /interaction-records/:id : get the "id" interactionRecord.
     *
     * @param id the id of the interactionRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interactionRecord, or with status 404 (Not Found)
     */
    @GetMapping("/interaction-records/{id}")
    public ResponseEntity<InteractionRecord> getInteractionRecord(@PathVariable Long id) {
        log.debug("REST request to get InteractionRecord : {}", id);
        Optional<InteractionRecord> interactionRecord = interactionRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interactionRecord);
    }

    /**
     * DELETE  /interaction-records/:id : delete the "id" interactionRecord.
     *
     * @param id the id of the interactionRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interaction-records/{id}")
    public ResponseEntity<Void> deleteInteractionRecord(@PathVariable Long id) {
        log.debug("REST request to delete InteractionRecord : {}", id);
        interactionRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @DeleteMapping("/deleteAll/interaction-records")
    public String deleteAll()
    {
        interactionRecordService.deleteAllRecord();
        return "All Records Deleted";
    }

    /**
     * SEARCH  /_search/interaction-records?query=:query : search for the interactionRecord corresponding
     * to the query.
     *
     * @param query the query of the interactionRecord search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/interaction-records")
    public ResponseEntity<List<InteractionRecord>> searchInteractionRecords(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InteractionRecords for query {}", query);
        Page<InteractionRecord> page = interactionRecordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/interaction-records");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }





    /**
     * SEARCH  /_search/interaction-records?query=:query : search for the interactionRecord corresponding
     * to the query.
     *
     *
     * @param time the pagination information
     * @return the result of the search
     */
    @GetMapping("/interaction-records/getByTime")
    public List<InteractionRecord> searchInteractionRecordByTime(@RequestParam Integer time) {
        log.debug("REST request to search for a page of InteractionRecords for time {}", time);
   /*     Page<InteractionRecord> page = interactionRecordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/interaction-records");
        return ResponseEntity.ok().headers(headers).body(page.getContent());*/
        return  interactionRecordService.searchAllByTime(time);


    }




    /**
     * SEARCH  /_search/interaction-records?query=:query : search for the interactionRecord corresponding
     * to the query.
     *
     *
     * @param duration the pagination information
     * @return the result of the search
     */
    @GetMapping("/interaction-records/getByDuration")
    public InteractionRecord searchInteractionRecordByDuration(@RequestParam Integer duration) {
        log.debug("REST request to search for a page of InteractionRecords for time {}", duration);
        return  interactionRecordService.searchByDuration(duration);

    }


    /**
     * SEARCH  /_search/interaction-records?query=:query : search for the interactionRecord corresponding
     * to the query.
     *
     *
     * @param duration the pagination information
     * @return the result of the search
     */
    @GetMapping("/interaction-records/getListByDuration")
    public List<InteractionRecordDTO> searchInteractionRecordListByDuration(@RequestParam Integer duration) {
        log.debug("REST request to search for a page of InteractionRecords for time {}", duration);
        return  interactionRecordService.searchListByDuration(duration);

    }

    /**
     *
     * Get All Records of Interaction Records
     * @return return List of All InteractionRecordDto
     * */

    @GetMapping("/interaction-records/all")
    public List<InteractionRecordDTO> getAllInteractionRecordsAll() {
        return interactionRecordService.getAllRecords();
    }





}
