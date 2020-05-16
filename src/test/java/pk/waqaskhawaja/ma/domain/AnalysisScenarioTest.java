package pk.waqaskhawaja.ma.domain;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pk.waqaskhawaja.ma.web.rest.TestUtil;

public class AnalysisScenarioTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalysisScenario.class);
        AnalysisScenario analysisScenario1 = new AnalysisScenario();
        analysisScenario1.setId(1L);
        AnalysisScenario analysisScenario2 = new AnalysisScenario();
        analysisScenario2.setId(analysisScenario1.getId());
        assertThat(analysisScenario1).isEqualTo(analysisScenario2);
        analysisScenario2.setId(2L);
        assertThat(analysisScenario1).isNotEqualTo(analysisScenario2);
        analysisScenario1.setId(null);
        assertThat(analysisScenario1).isNotEqualTo(analysisScenario2);
    }
}
