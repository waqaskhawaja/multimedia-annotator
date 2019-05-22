package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.AnalysisSessionResource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AnalysisSessionResource entity.
 */
public interface AnalysisSessionResourceSearchRepository extends ElasticsearchRepository<AnalysisSessionResource, Long> {
}
