package pk.waqaskhawaja.ma.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the AnalysisScenario entity. This class is used in AnalysisScenarioResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /analysis-scenarios?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnalysisScenarioCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter analysisSessionId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getAnalysisSessionId() {
        return analysisSessionId;
    }

    public void setAnalysisSessionId(LongFilter analysisSessionId) {
        this.analysisSessionId = analysisSessionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnalysisScenarioCriteria that = (AnalysisScenarioCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(analysisSessionId, that.analysisSessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        analysisSessionId
        );
    }

    @Override
    public String toString() {
        return "AnalysisScenarioCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (analysisSessionId != null ? "analysisSessionId=" + analysisSessionId + ", " : "") +
            "}";
    }

}
