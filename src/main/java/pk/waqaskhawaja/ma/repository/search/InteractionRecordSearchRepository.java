package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.InteractionRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InteractionRecord entity.
 */
public interface InteractionRecordSearchRepository extends ElasticsearchRepository<InteractionRecord, Long> {
}
