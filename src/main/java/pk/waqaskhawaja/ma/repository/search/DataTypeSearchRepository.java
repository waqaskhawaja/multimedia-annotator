package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.DataType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DataType entity.
 */
public interface DataTypeSearchRepository extends ElasticsearchRepository<DataType, Long> {
}
