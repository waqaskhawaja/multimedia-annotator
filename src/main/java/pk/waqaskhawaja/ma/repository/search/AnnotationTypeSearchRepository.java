package pk.waqaskhawaja.ma.repository.search;
import pk.waqaskhawaja.ma.domain.AnnotationType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AnnotationType} entity.
 */
public interface AnnotationTypeSearchRepository extends ElasticsearchRepository<AnnotationType, Long> {
}
