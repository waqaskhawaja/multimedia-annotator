package pk.waqaskhawaja.ma.domain;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pk.waqaskhawaja.ma.web.rest.TestUtil;

public class DataSetTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataSet.class);
        DataSet dataSet1 = new DataSet();
        dataSet1.setId(1L);
        DataSet dataSet2 = new DataSet();
        dataSet2.setId(dataSet1.getId());
        assertThat(dataSet1).isEqualTo(dataSet2);
        dataSet2.setId(2L);
        assertThat(dataSet1).isNotEqualTo(dataSet2);
        dataSet1.setId(null);
        assertThat(dataSet1).isNotEqualTo(dataSet2);
    }
}
