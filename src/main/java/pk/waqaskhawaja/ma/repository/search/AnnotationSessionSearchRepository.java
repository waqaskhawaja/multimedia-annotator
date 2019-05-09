package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.AnnotationSession;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AnnotationSession entity.
 */
public interface AnnotationSessionSearchRepository extends ElasticsearchRepository<AnnotationSession, Long> {
}
