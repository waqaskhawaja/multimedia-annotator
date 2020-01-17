package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.Scenario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Scenario entity.
 */
public interface ScenarioSearchRepository extends ElasticsearchRepository<Scenario, Long> {
}
