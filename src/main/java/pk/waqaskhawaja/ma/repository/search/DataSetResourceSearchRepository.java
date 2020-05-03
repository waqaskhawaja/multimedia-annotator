package pk.waqaskhawaja.ma.repository.search;
import pk.waqaskhawaja.ma.domain.DataSetResource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DataSetResource} entity.
 */
public interface DataSetResourceSearchRepository extends ElasticsearchRepository<DataSetResource, Long> {
}
