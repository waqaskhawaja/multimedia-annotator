package pk.waqaskhawaja.ma.repository.search;
import pk.waqaskhawaja.ma.domain.AnalysisScenario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AnalysisScenario} entity.
 */
public interface AnalysisScenarioSearchRepository extends ElasticsearchRepository<AnalysisScenario, Long> {
}
