package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A InteractionRecord.
 */
@Entity
@Table(name = "interaction_record")
@Document(indexName = "interactionrecord")
public class InteractionRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "text")
    private String text;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "jhi_time")
    private Integer time;

    @ManyToOne
    @JsonIgnoreProperties("interactionRecords")
    private InteractionType interactionType;

    @ManyToOne
    @JsonIgnoreProperties("interactionRecords")
    private AnalysisSessionResource analysisSessionResource;

    @ManyToMany(mappedBy = "interactionRecords")
    @JsonIgnore
    private Set<Annotation> annotations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public InteractionRecord duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getText() {
        return text;
    }

    public InteractionRecord text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSourceId() {
        return sourceId;
    }

    public InteractionRecord sourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getTime() {
        return time;
    }

    public InteractionRecord time(Integer time) {
        this.time = time;
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public InteractionRecord interactionType(InteractionType interactionType) {
        this.interactionType = interactionType;
        return this;
    }

    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    public AnalysisSessionResource getAnalysisSessionResource() {
        return analysisSessionResource;
    }

    public InteractionRecord analysisSessionResource(AnalysisSessionResource analysisSessionResource) {
        this.analysisSessionResource = analysisSessionResource;
        return this;
    }

    public void setAnalysisSessionResource(AnalysisSessionResource analysisSessionResource) {
        this.analysisSessionResource = analysisSessionResource;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public InteractionRecord annotations(Set<Annotation> annotations) {
        this.annotations = annotations;
        return this;
    }

    public InteractionRecord addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
        annotation.getInteractionRecords().add(this);
        return this;
    }

    public InteractionRecord removeAnnotation(Annotation annotation) {
        this.annotations.remove(annotation);
        annotation.getInteractionRecords().remove(this);
        return this;
    }

    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
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
        InteractionRecord interactionRecord = (InteractionRecord) o;
        if (interactionRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), interactionRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InteractionRecord{" +
            "id=" + getId() +
            ", duration=" + getDuration() +
            ", text='" + getText() + "'" +
            ", sourceId='" + getSourceId() + "'" +
            ", time=" + getTime() +
            "}";
    }
}
