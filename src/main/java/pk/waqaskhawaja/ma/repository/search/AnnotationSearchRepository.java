package pk.waqaskhawaja.ma.repository.search;
import pk.waqaskhawaja.ma.domain.Annotation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Annotation} entity.
 */
public interface AnnotationSearchRepository extends ElasticsearchRepository<Annotation, Long> {
}
