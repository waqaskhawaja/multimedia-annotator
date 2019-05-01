package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Analyst.
 */
@Entity
@Table(name = "analyst")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Analyst implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "experience")
    private Integer experience;

    @OneToMany(mappedBy = "analyst")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    public Analyst name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExperience() {
        return experience;
    }

    public Analyst experience(Integer experience) {
        this.experience = experience;
        return this;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public Analyst sessions(Set<Session> sessions) {
        this.sessions = sessions;
        return this;
    }

    public Analyst addSession(Session session) {
        this.sessions.add(session);
        session.setAnalyst(this);
        return this;
    }

    public Analyst removeSession(Session session) {
        this.sessions.remove(session);
        session.setAnalyst(null);
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
        Analyst analyst = (Analyst) o;
        if (analyst.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), analyst.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Analyst{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", experience=" + getExperience() +
            "}";
    }
}
