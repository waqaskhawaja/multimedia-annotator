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
 * Criteria class for the Session entity. This class is used in SessionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sessions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SessionCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter url;

    private LongFilter dataTypeId;

    private LongFilter scenarioId;

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

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public LongFilter getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(LongFilter dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public LongFilter getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(LongFilter scenarioId) {
        this.scenarioId = scenarioId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SessionCriteria that = (SessionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(url, that.url) &&
            Objects.equals(dataTypeId, that.dataTypeId) &&
            Objects.equals(scenarioId, that.scenarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        url,
        dataTypeId,
        scenarioId
        );
    }

    @Override
    public String toString() {
        return "SessionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (dataTypeId != null ? "dataTypeId=" + dataTypeId + ", " : "") +
                (scenarioId != null ? "scenarioId=" + scenarioId + ", " : "") +
            "}";
    }

}
