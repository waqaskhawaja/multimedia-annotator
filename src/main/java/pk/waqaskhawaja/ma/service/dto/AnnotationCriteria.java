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
 * Criteria class for the Annotation entity. This class is used in AnnotationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /annotations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnnotationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter start;

    private InstantFilter end;

    private StringFilter annotationText;

    private LongFilter annotationSessionId;

    private LongFilter dataRecordId;

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

    public StringFilter getAnnotationText() {
        return annotationText;
    }

    public void setAnnotationText(StringFilter annotationText) {
        this.annotationText = annotationText;
    }

    public LongFilter getAnnotationSessionId() {
        return annotationSessionId;
    }

    public void setAnnotationSessionId(LongFilter annotationSessionId) {
        this.annotationSessionId = annotationSessionId;
    }

    public LongFilter getDataRecordId() {
        return dataRecordId;
    }

    public void setDataRecordId(LongFilter dataRecordId) {
        this.dataRecordId = dataRecordId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AnnotationCriteria that = (AnnotationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(start, that.start) &&
            Objects.equals(end, that.end) &&
            Objects.equals(annotationText, that.annotationText) &&
            Objects.equals(annotationSessionId, that.annotationSessionId) &&
            Objects.equals(dataRecordId, that.dataRecordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        start,
        end,
        annotationText,
        annotationSessionId,
        dataRecordId
        );
    }

    @Override
    public String toString() {
        return "AnnotationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (start != null ? "start=" + start + ", " : "") +
                (end != null ? "end=" + end + ", " : "") +
                (annotationText != null ? "annotationText=" + annotationText + ", " : "") +
                (annotationSessionId != null ? "annotationSessionId=" + annotationSessionId + ", " : "") +
                (dataRecordId != null ? "dataRecordId=" + dataRecordId + ", " : "") +
            "}";
    }

}
