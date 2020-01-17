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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the AnnotationSession entity. This class is used in AnnotationSessionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /annotation-sessions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnnotationSessionCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter start;

    private InstantFilter end;

    private StringFilter name;

    private LongFilter analysisSessionId;

    private LongFilter annotatorId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getStart() {
        return start;
    }

    public void setStart(InstantFilter start) {
        this.start = start;
    }

    public InstantFilter getEnd() {
        return end;
    }

    public void setEnd(InstantFilter end) {
        this.end = end;
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

    public LongFilter getAnnotatorId() {
        return annotatorId;
    }

    public void setAnnotatorId(LongFilter annotatorId) {
        this.annotatorId = annotatorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnnotationSessionCriteria that = (AnnotationSessionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(start, that.start) &&
            Objects.equals(end, that.end) &&
            Objects.equals(name, that.name) &&
            Objects.equals(analysisSessionId, that.analysisSessionId) &&
            Objects.equals(annotatorId, that.annotatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        start,
        end,
        name,
        analysisSessionId,
        annotatorId
        );
    }

    @Override
    public String toString() {
        return "AnnotationSessionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (start != null ? "start=" + start + ", " : "") +
                (end != null ? "end=" + end + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (analysisSessionId != null ? "analysisSessionId=" + analysisSessionId + ", " : "") +
                (annotatorId != null ? "annotatorId=" + annotatorId + ", " : "") +
            "}";
    }

}
