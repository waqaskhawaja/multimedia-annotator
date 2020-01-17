package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AnalysisSession.
 */
@Entity
@Table(name = "analysis_session")
@Document(indexName = "analysissession")
public class AnalysisSession implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("analysisSessions")
    private AnalysisScenario analysisScenario;

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

    public AnalysisSession name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnalysisScenario getAnalysisScenario() {
        return analysisScenario;
    }

    public AnalysisSession analysisScenario(AnalysisScenario analysisScenario) {
        this.analysisScenario = analysisScenario;
        return this;
    }

    public void setAnalysisScenario(AnalysisScenario analysisScenario) {
        this.analysisScenario = analysisScenario;
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
        AnalysisSession analysisSession = (AnalysisSession) o;
        if (analysisSession.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), analysisSession.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnalysisSession{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
