package pk.waqaskhawaja.ma.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AnalysisScenario.
 */
@Entity
@Table(name = "analysis_scenario")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "analysisscenario")
public class AnalysisScenario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "analysisScenario")
    private Set<AnalysisSession> analysisSessions = new HashSet<>();

    @OneToMany(mappedBy = "analysisScenario")
    private Set<DataSet> dataSets = new HashSet<>();

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

    public Set<DataSet> getDataSets() {
        return dataSets;
    }

    public AnalysisScenario dataSets(Set<DataSet> dataSets) {
        this.dataSets = dataSets;
        return this;
    }

    public AnalysisScenario addDataSet(DataSet dataSet) {
        this.dataSets.add(dataSet);
        dataSet.setAnalysisScenario(this);
        return this;
    }

    public AnalysisScenario removeDataSet(DataSet dataSet) {
        this.dataSets.remove(dataSet);
        dataSet.setAnalysisScenario(null);
        return this;
    }

    public void setDataSets(Set<DataSet> dataSets) {
        this.dataSets = dataSets;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnalysisScenario)) {
            return false;
        }
        return id != null && id.equals(((AnalysisScenario) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AnalysisScenario{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
