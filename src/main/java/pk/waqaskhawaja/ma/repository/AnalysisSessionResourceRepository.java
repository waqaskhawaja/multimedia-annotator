package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.AnalysisSession;
import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the AnalysisSessionResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalysisSessionResourceRepository extends JpaRepository<AnalysisSessionResource, Long>, JpaSpecificationExecutor<AnalysisSessionResource> {

    Optional<AnalysisSessionResource> findByAnalysisSessionIdAndResourceTypeId(Long analysisSessionId, Long resourceTypeId);

}
