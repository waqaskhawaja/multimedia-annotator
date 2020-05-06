package pk.waqaskhawaja.ma.domain;

import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pk.waqaskhawaja.ma.web.rest.TestUtil;

public class DataSetResourceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataSetResource.class);
        DataSetResource dataSetResource1 = new DataSetResource();
        dataSetResource1.setId(1L);
        DataSetResource dataSetResource2 = new DataSetResource();
        dataSetResource2.setId(dataSetResource1.getId());
        assertThat(dataSetResource1).isEqualTo(dataSetResource2);
        dataSetResource2.setId(2L);
        assertThat(dataSetResource1).isNotEqualTo(dataSetResource2);
        dataSetResource1.setId(null);
        assertThat(dataSetResource1).isNotEqualTo(dataSetResource2);
    }
}
