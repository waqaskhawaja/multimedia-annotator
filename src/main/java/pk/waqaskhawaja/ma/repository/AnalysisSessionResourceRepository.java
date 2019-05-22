package pk.waqaskhawaja.ma.repository;

import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AnalysisSessionResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalysisSessionResourceRepository extends JpaRepository<AnalysisSessionResource, Long>, JpaSpecificationExecutor<AnalysisSessionResource> {

}
