package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Scenario.
 */
@Entity
@Table(name = "scenario")
@Document(indexName = "scenario")
public class Scenario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "scenario")
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

    public Scenario name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public Scenario sessions(Set<Session> sessions) {
        this.sessions = sessions;
        return this;
    }

    public Scenario addSession(Session session) {
        this.sessions.add(session);
        session.setScenario(this);
        return this;
    }

    public Scenario removeSession(Session session) {
        this.sessions.remove(session);
        session.setScenario(null);
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
        Scenario scenario = (Scenario) o;
        if (scenario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scenario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Scenario{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
