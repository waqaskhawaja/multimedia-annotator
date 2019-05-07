package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A InteractionType.
 */
@Entity
@Table(name = "interaction_type")
@Document(indexName = "interactiontype")
public class InteractionType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "interactionType")
    private Set<DataRecord> dataRecords = new HashSet<>();
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

    public InteractionType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DataRecord> getDataRecords() {
        return dataRecords;
    }

    public InteractionType dataRecords(Set<DataRecord> dataRecords) {
        this.dataRecords = dataRecords;
        return this;
    }

    public InteractionType addDataRecord(DataRecord dataRecord) {
        this.dataRecords.add(dataRecord);
        dataRecord.setInteractionType(this);
        return this;
    }

    public InteractionType removeDataRecord(DataRecord dataRecord) {
        this.dataRecords.remove(dataRecord);
        dataRecord.setInteractionType(null);
        return this;
    }

    public void setDataRecords(Set<DataRecord> dataRecords) {
        this.dataRecords = dataRecords;
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
        InteractionType interactionType = (InteractionType) o;
        if (interactionType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), interactionType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InteractionType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
