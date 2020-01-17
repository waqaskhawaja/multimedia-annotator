package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Annotation.
 */
@Entity
@Table(name = "annotation")
@Document(indexName = "annotation")
public class Annotation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_start")
    private Instant start;

    @Column(name = "jhi_end")
    private Instant end;

    @Column(name = "annotation_text")
    private String annotationText;

    @ManyToOne
    @JsonIgnoreProperties("annotations")
    private AnnotationSession annotationSession;

    @ManyToMany
    @JoinTable(name = "annotation_interaction_record",
               joinColumns = @JoinColumn(name = "annotation_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "interaction_record_id", referencedColumnName = "id"))
    private Set<InteractionRecord> interactionRecords = new HashSet<>();

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

    public Annotation start(Instant start) {
        this.start = start;
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public Annotation end(Instant end) {
        this.end = end;
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public String getAnnotationText() {
        return annotationText;
    }

    public Annotation annotationText(String annotationText) {
        this.annotationText = annotationText;
        return this;
    }

    public void setAnnotationText(String annotationText) {
        this.annotationText = annotationText;
    }

    public AnnotationSession getAnnotationSession() {
        return annotationSession;
    }

    public Annotation annotationSession(AnnotationSession annotationSession) {
        this.annotationSession = annotationSession;
        return this;
    }

    public void setAnnotationSession(AnnotationSession annotationSession) {
        this.annotationSession = annotationSession;
    }

    public Set<InteractionRecord> getInteractionRecords() {
        return interactionRecords;
    }

    public Annotation interactionRecords(Set<InteractionRecord> interactionRecords) {
        this.interactionRecords = interactionRecords;
        return this;
    }

    public Annotation addInteractionRecord(InteractionRecord interactionRecord) {
        this.interactionRecords.add(interactionRecord);
        interactionRecord.getAnnotations().add(this);
        return this;
    }

    public Annotation removeInteractionRecord(InteractionRecord interactionRecord) {
        this.interactionRecords.remove(interactionRecord);
        interactionRecord.getAnnotations().remove(this);
        return this;
    }

    public void setInteractionRecords(Set<InteractionRecord> interactionRecords) {
        this.interactionRecords = interactionRecords;
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
        Annotation annotation = (Annotation) o;
        if (annotation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), annotation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Annotation{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", annotationText='" + getAnnotationText() + "'" +
            "}";
    }
}
