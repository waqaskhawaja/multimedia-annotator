package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.AnnotationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AnnotationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnotationTypeRepository extends JpaRepository<AnnotationType, Long>, JpaSpecificationExecutor<AnnotationType> {

}
