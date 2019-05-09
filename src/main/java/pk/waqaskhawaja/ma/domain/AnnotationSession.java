package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A AnnotationSession.
 */
@Entity
@Table(name = "annotation_session")
@Document(indexName = "annotationsession")
public class AnnotationSession implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_start")
    private Instant start;

    @Column(name = "jhi_end")
    private Instant end;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("annotationSessions")
    private Session session;

    @ManyToOne
    @JsonIgnoreProperties("annotationSessions")
    private User annotator;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStart() {
        return start;
    }

    public AnnotationSession start(Instant start) {
        this.start = start;
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public AnnotationSession end(Instant end) {
        this.end = end;
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public AnnotationSession name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public AnnotationSession session(Session session) {
        this.session = session;
        return this;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getAnnotator() {
        return annotator;
    }

    public AnnotationSession annotator(User user) {
        this.annotator = user;
        return this;
    }

    public void setAnnotator(User user) {
        this.annotator = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnnotationSession annotationSession = (AnnotationSession) o;
        if (annotationSession.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), annotationSession.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnnotationSession{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
