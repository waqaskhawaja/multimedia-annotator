package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.AnnotationType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AnnotationType entity.
 */
public interface AnnotationTypeSearchRepository extends ElasticsearchRepository<AnnotationType, Long> {
}
