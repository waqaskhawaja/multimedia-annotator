package pk.waqaskhawaja.ma.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of AnalysisSessionResourceSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AnalysisSessionResourceSearchRepositoryMockConfiguration {

    @MockBean
    private AnalysisSessionResourceSearchRepository mockAnalysisSessionResourceSearchRepository;

}
