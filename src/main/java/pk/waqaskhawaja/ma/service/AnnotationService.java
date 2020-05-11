package pk.waqaskhawaja.ma.service;

import pk.waqaskhawaja.ma.domain.Annotation;
import pk.waqaskhawaja.ma.domain.AnnotationSession;
import pk.waqaskhawaja.ma.domain.AnnotationType;
import pk.waqaskhawaja.ma.domain.InteractionRecord;
import pk.waqaskhawaja.ma.repository.AnnotationRepository;
import pk.waqaskhawaja.ma.repository.InteractionRecordRepository;
import pk.waqaskhawaja.ma.repository.search.AnnotationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Annotation}.
 */
@Service
@Transactional
public class AnnotationService {

    private final Logger log = LoggerFactory.getLogger(AnnotationService.class);

    private final AnnotationRepository annotationRepository;

    private final AnnotationSearchRepository annotationSearchRepository;

    private final InteractionRecordRepository interactionRecordRepository;

    private final AnnotationSessionService annotationSessionService;

    private final AnnotationTypeService annotationTypeService;


    public AnnotationService(AnnotationRepository annotationRepository, AnnotationSearchRepository annotationSearchRepository, InteractionRecordRepository interactionRecordRepository, AnnotationSessionService annotationSessionService, AnnotationTypeService annotationTypeService) {
        this.annotationRepository = annotationRepository;
        this.annotationSearchRepository = annotationSearchRepository;
        this.interactionRecordRepository = interactionRecordRepository;
        this.annotationSessionService = annotationSessionService;
        this.annotationTypeService = annotationTypeService;
    }

    /**
     * Save a annotation.
     *
     * @param annotation the entity to save.
     * @return the persisted entity.
     */
    public Annotation save(Annotation annotation) {
        log.debug("Request to save Annotation : {}", annotation);
        Annotation result = annotationRepository.save(annotation);
        annotationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the annotations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Annotation> findAll(Pageable pageable) {
        log.debug("Request to get all Annotations");
        return annotationRepository.findAll(pageable);
    }

    /**
     * Get all the annotations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Annotation> findAllWithEagerRelationships(Pageable pageable) {
        return annotationRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one annotation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Annotation> findOne(Long id) {
        log.debug("Request to get Annotation : {}", id);
        return annotationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the annotation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Annotation : {}", id);
        annotationRepository.deleteById(id);
        annotationSearchRepository.deleteById(id);
    }

    /**
     * Search for the annotation corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Annotation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Annotations for query {}", query);
        return annotationSearchRepository.search(queryStringQuery(query), pageable);    }




    public String saveAndConvertFromInteractionDtoToAnnotation(Long[]  interactionRecordDTO, String Text, Long annotationID, String annotationType) {
        int numberOfSelectedCheckboxs = interactionRecordDTO.length;
        List<InteractionRecord> interactionRecordList = new ArrayList<>();
        Instant instant = Instant.now();
        AnnotationType AnnotationType = annotationTypeService.findOneByName(annotationType);
        AnnotationSession annotationSession = annotationSessionService.findOne(annotationID).orElse(null);


        for (Long id:interactionRecordDTO) {
            InteractionRecord interactionRecord =  interactionRecordRepository.findById(id).orElse(null);
            if(interactionRecord!=null){
                interactionRecordList.add(interactionRecord);
            }
        }




        if (numberOfSelectedCheckboxs > 0)
        {
            for(int i=0; i<numberOfSelectedCheckboxs; i++)
            {

                Annotation annotation = new Annotation();
                annotation.setAnnotationText(Text);
                if(annotationSession.getId()!=null){
                    annotation.setAnnotationSession(annotationSession);
                }
                if(AnnotationType.getId()!=null)
                {
                    annotation.setAnnotationType(AnnotationType);
                }
                annotation.setStart(instant);
                annotation.setEnd(instant);
                annotationRepository.save(annotation);
            }

        }
        return "Saved";
    }


}
