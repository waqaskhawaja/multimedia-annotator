package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.ResourceType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ResourceType entity.
 */
public interface ResourceTypeSearchRepository extends ElasticsearchRepository<ResourceType, Long> {
}
