package pk.waqaskhawaja.ma.repository.search;
import pk.waqaskhawaja.ma.domain.DataSet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DataSet} entity.
 */
public interface DataSetSearchRepository extends ElasticsearchRepository<DataSet, Long> {
}
