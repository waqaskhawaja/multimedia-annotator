package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DataType.
 */
@Entity
@Table(name = "data_type")
@Document(indexName = "datatype")
public class DataType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "dataType")
    private Set<Session> sessions = new HashSet<>();
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

    public DataType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public DataType sessions(Set<Session> sessions) {
        this.sessions = sessions;
        return this;
    }

    public DataType addSession(Session session) {
        this.sessions.add(session);
        session.setDataType(this);
        return this;
    }

    public DataType removeSession(Session session) {
        this.sessions.remove(session);
        session.setDataType(null);
        return this;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
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
        DataType dataType = (DataType) o;
        if (dataType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
