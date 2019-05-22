package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AnalysisScenario.
 */
@Entity
@Table(name = "analysis_scenario")
@Document(indexName = "analysisscenario")
public class AnalysisScenario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "analysisScenario")
    private Set<AnalysisSession> analysisSessions = new HashSet<>();
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

    public AnalysisScenario name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AnalysisSession> getAnalysisSessions() {
        return analysisSessions;
    }

    public AnalysisScenario analysisSessions(Set<AnalysisSession> analysisSessions) {
        this.analysisSessions = analysisSessions;
        return this;
    }

    public AnalysisScenario addAnalysisSession(AnalysisSession analysisSession) {
        this.analysisSessions.add(analysisSession);
        analysisSession.setAnalysisScenario(this);
        return this;
    }

    public AnalysisScenario removeAnalysisSession(AnalysisSession analysisSession) {
        this.analysisSessions.remove(analysisSession);
        analysisSession.setAnalysisScenario(null);
        return this;
    }

    public void setAnalysisSessions(Set<AnalysisSession> analysisSessions) {
        this.analysisSessions = analysisSessions;
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
        AnalysisScenario analysisScenario = (AnalysisScenario) o;
        if (analysisScenario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), analysisScenario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnalysisScenario{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
