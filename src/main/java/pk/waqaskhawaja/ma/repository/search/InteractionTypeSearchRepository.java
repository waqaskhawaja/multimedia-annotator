package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.InteractionType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the InteractionType entity.
 */
public interface InteractionTypeSearchRepository extends ElasticsearchRepository<InteractionType, Long> {
}
