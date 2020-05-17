package pk.waqaskhawaja.ma.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import pk.waqaskhawaja.ma.domain.DataSet;


/**
 * Spring Data  repository for the DataSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
	Optional<DataSet> findByIdentifierAndAnalysisScenario(String identifier, AnalysisScenario analysisScenario);
}
