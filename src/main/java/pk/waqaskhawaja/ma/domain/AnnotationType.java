package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AnnotationType.
 */
@Entity
@Table(name = "annotation_type")
@Document(indexName = "annotationtype")
public class AnnotationType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("annotationTypes")
    private AnnotationType parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AnnotationType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnnotationType getParent() {
        return parent;
    }

    public AnnotationType parent(AnnotationType annotationType) {
        this.parent = annotationType;
        return this;
    }

    public void setParent(AnnotationType annotationType) {
        this.parent = annotationType;
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
        AnnotationType annotationType = (AnnotationType) o;
        if (annotationType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), annotationType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnnotationType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
