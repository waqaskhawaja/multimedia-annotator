package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.AnalysisSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AnalysisSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalysisSessionRepository extends JpaRepository<AnalysisSession, Long>, JpaSpecificationExecutor<AnalysisSession> {

}
