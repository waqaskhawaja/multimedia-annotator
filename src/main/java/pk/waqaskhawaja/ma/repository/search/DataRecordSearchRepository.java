package pk.waqaskhawaja.ma.repository.search;

import pk.waqaskhawaja.ma.domain.DataRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DataRecord entity.
 */
public interface DataRecordSearchRepository extends ElasticsearchRepository<DataRecord, Long> {
}
