package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.AnalysisSession;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AnalysisSession entity.
 */
public interface AnalysisSessionSearchRepository extends ElasticsearchRepository<AnalysisSession, Long> {
}
