package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.AnnotationSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the AnnotationSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnotationSessionRepository extends JpaRepository<AnnotationSession, Long>, JpaSpecificationExecutor<AnnotationSession> {

    @Query("select annotation_session from AnnotationSession annotation_session where annotation_session.annotator.login = ?#{principal.username}")
    List<AnnotationSession> findByAnnotatorIsCurrentUser();

}
