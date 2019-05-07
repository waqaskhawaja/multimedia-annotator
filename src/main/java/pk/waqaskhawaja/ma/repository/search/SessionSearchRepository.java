package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.Session;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Session entity.
 */
public interface SessionSearchRepository extends ElasticsearchRepository<Session, Long> {
}
