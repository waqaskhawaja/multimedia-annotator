package pk.waqaskhawaja.ma.domain;

import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pk.waqaskhawaja.ma.web.rest.TestUtil;

public class AnnotationTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnotationType.class);
        AnnotationType annotationType1 = new AnnotationType();
        annotationType1.setId(1L);
        AnnotationType annotationType2 = new AnnotationType();
        annotationType2.setId(annotationType1.getId());
        assertThat(annotationType1).isEqualTo(annotationType2);
        annotationType2.setId(2L);
        assertThat(annotationType1).isNotEqualTo(annotationType2);
        annotationType1.setId(null);
        assertThat(annotationType1).isNotEqualTo(annotationType2);
    }
}
