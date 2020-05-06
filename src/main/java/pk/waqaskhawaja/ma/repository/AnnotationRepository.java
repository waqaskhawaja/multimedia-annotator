package pk.waqaskhawaja.ma.repository;
import pk.waqaskhawaja.ma.domain.Annotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Annotation entity.
 */
@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long>, JpaSpecificationExecutor<Annotation> {

    @Query(value = "select distinct annotation from Annotation annotation left join fetch annotation.interactionRecords",
        countQuery = "select count(distinct annotation) from Annotation annotation")
    Page<Annotation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct annotation from Annotation annotation left join fetch annotation.interactionRecords")
    List<Annotation> findAllWithEagerRelationships();

    @Query("select annotation from Annotation annotation left join fetch annotation.interactionRecords where annotation.id =:id")
    Optional<Annotation> findOneWithEagerRelationships(@Param("id") Long id);

}
