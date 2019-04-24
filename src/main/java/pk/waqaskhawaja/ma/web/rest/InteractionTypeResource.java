package pk.waqaskhawaja.ma.web.rest;
import pk.waqaskhawaja.ma.domain.InteractionType;
import pk.waqaskhawaja.ma.repository.InteractionTypeRepository;
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
 * REST controller for managing InteractionType.
 */
@RestController
@RequestMapping("/api")
public class InteractionTypeResource {

    private final Logger log = LoggerFactory.getLogger(InteractionTypeResource.class);

    private static final String ENTITY_NAME = "interactionType";

    private final InteractionTypeRepository interactionTypeRepository;

    public InteractionTypeResource(InteractionTypeRepository interactionTypeRepository) {
        this.interactionTypeRepository = interactionTypeRepository;
    }

    /**
     * POST  /interaction-types : Create a new interactionType.
     *
     * @param interactionType the interactionType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interactionType, or with status 400 (Bad Request) if the interactionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interaction-types")
    public ResponseEntity<InteractionType> createInteractionType(@RequestBody InteractionType interactionType) throws URISyntaxException {
        log.debug("REST request to save InteractionType : {}", interactionType);
        if (interactionType.getId() != null) {
            throw new BadRequestAlertException("A new interactionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InteractionType result = interactionTypeRepository.save(interactionType);
        return ResponseEntity.created(new URI("/api/interaction-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interaction-types : Updates an existing interactionType.
     *
     * @param interactionType the interactionType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interactionType,
     * or with status 400 (Bad Request) if the interactionType is not valid,
     * or with status 500 (Internal Server Error) if the interactionType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interaction-types")
    public ResponseEntity<InteractionType> updateInteractionType(@RequestBody InteractionType interactionType) throws URISyntaxException {
        log.debug("REST request to update InteractionType : {}", interactionType);
        if (interactionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InteractionType result = interactionTypeRepository.save(interactionType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, interactionType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interaction-types : get all the interactionTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of interactionTypes in body
     */
    @GetMapping("/interaction-types")
    public List<InteractionType> getAllInteractionTypes() {
        log.debug("REST request to get all InteractionTypes");
        return interactionTypeRepository.findAll();
    }

    /**
     * GET  /interaction-types/:id : get the "id" interactionType.
     *
     * @param id the id of the interactionType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interactionType, or with status 404 (Not Found)
     */
    @GetMapping("/interaction-types/{id}")
    public ResponseEntity<InteractionType> getInteractionType(@PathVariable Long id) {
        log.debug("REST request to get InteractionType : {}", id);
        Optional<InteractionType> interactionType = interactionTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(interactionType);
    }

    /**
     * DELETE  /interaction-types/:id : delete the "id" interactionType.
     *
     * @param id the id of the interactionType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interaction-types/{id}")
    public ResponseEntity<Void> deleteInteractionType(@PathVariable Long id) {
        log.debug("REST request to delete InteractionType : {}", id);
        interactionTypeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
