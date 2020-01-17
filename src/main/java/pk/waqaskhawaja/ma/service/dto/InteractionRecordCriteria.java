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
 * Criteria class for the InteractionRecord entity. This class is used in InteractionRecordResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /interaction-records?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InteractionRecordCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter duration;

    private StringFilter text;

    private StringFilter sourceId;

    private IntegerFilter time;

    private LongFilter interactionTypeId;

    private LongFilter analysisSessionResourceId;

    private LongFilter annotationId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public StringFilter getSourceId() {
        return sourceId;
    }

    public void setSourceId(StringFilter sourceId) {
        this.sourceId = sourceId;
    }

    public IntegerFilter getTime() {
        return time;
    }

    public void setTime(IntegerFilter time) {
        this.time = time;
    }

    public LongFilter getInteractionTypeId() {
        return interactionTypeId;
    }

    public void setInteractionTypeId(LongFilter interactionTypeId) {
        this.interactionTypeId = interactionTypeId;
    }

    public LongFilter getAnalysisSessionResourceId() {
        return analysisSessionResourceId;
    }

    public void setAnalysisSessionResourceId(LongFilter analysisSessionResourceId) {
        this.analysisSessionResourceId = analysisSessionResourceId;
    }

    public LongFilter getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(LongFilter annotationId) {
        this.annotationId = annotationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InteractionRecordCriteria that = (InteractionRecordCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(text, that.text) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(time, that.time) &&
            Objects.equals(interactionTypeId, that.interactionTypeId) &&
            Objects.equals(analysisSessionResourceId, that.analysisSessionResourceId) &&
            Objects.equals(annotationId, that.annotationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        duration,
        text,
        sourceId,
        time,
        interactionTypeId,
        analysisSessionResourceId,
        annotationId
        );
    }

    @Override
    public String toString() {
        return "InteractionRecordCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (duration != null ? "duration=" + duration + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (sourceId != null ? "sourceId=" + sourceId + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (interactionTypeId != null ? "interactionTypeId=" + interactionTypeId + ", " : "") +
                (analysisSessionResourceId != null ? "analysisSessionResourceId=" + analysisSessionResourceId + ", " : "") +
                (annotationId != null ? "annotationId=" + annotationId + ", " : "") +
            "}";
    }

}
