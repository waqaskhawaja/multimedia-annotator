package pk.waqaskhawaja.ma.repository;
import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AnalysisScenario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalysisScenarioRepository extends JpaRepository<AnalysisScenario, Long>, JpaSpecificationExecutor<AnalysisScenario> {

}
