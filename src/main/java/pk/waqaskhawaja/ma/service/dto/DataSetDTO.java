package pk.waqaskhawaja.ma.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link pk.waqaskhawaja.ma.domain.DataSet} entity.
 */
public class DataSetDTO implements Serializable {

    private Long id;

    private String title;

    private Instant date;

    private String type;

    @Lob
    private String contents;


    private Long annotationSessionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Long getAnnotationSessionId() {
        return annotationSessionId;
    }

    public void setAnnotationSessionId(Long annotationSessionId) {
        this.annotationSessionId = annotationSessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataSetDTO dataSetDTO = (DataSetDTO) o;
        if (dataSetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataSetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataSetDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", contents='" + getContents() + "'" +
            ", annotationSession=" + getAnnotationSessionId() +
            "}";
    }
}
