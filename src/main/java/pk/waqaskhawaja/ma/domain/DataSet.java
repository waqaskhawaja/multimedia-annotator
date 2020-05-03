package pk.waqaskhawaja.ma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DataSet.
 */
@Entity
@Table(name = "data_set")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dataset")
public class DataSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private Instant date;

    @Column(name = "type")
    private String type;


    @Column(name = "contents")
    private String contents;

    @Column(name = "identifier")
    private String identifier;

    @ManyToOne
    @JsonIgnoreProperties("dataSets")
    private AnnotationSession annotationSession;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public DataSet title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getDate() {
        return date;
    }

    public DataSet date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public DataSet type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContents() {
        return contents;
    }

    public DataSet contents(String contents) {
        this.contents = contents;
        return this;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getIdentifier() {
        return identifier;
    }

    public DataSet identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public AnnotationSession getAnnotationSession() {
        return annotationSession;
    }

    public DataSet annotationSession(AnnotationSession annotationSession) {
        this.annotationSession = annotationSession;
        return this;
    }

    public void setAnnotationSession(AnnotationSession annotationSession) {
        this.annotationSession = annotationSession;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSet)) {
            return false;
        }
        return id != null && id.equals(((DataSet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DataSet{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", contents='" + getContents() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            "}";
    }
}
