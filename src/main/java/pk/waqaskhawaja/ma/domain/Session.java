package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Session.
 */
@Entity
@Table(name = "session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "source_file")
    private byte[] sourceFile;

    @Column(name = "source_file_content_type")
    private String sourceFileContentType;

    @ManyToOne
    @JsonIgnoreProperties("sessions")
    private DataType dataType;

    @ManyToOne
    @JsonIgnoreProperties("sessions")
    private Scenario scenario;

    @OneToMany(mappedBy = "session")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DataRecord> dataRecords = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getSourceFile() {
        return sourceFile;
    }

    public Session sourceFile(byte[] sourceFile) {
        this.sourceFile = sourceFile;
        return this;
    }

    public void setSourceFile(byte[] sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getSourceFileContentType() {
        return sourceFileContentType;
    }

    public Session sourceFileContentType(String sourceFileContentType) {
        this.sourceFileContentType = sourceFileContentType;
        return this;
    }

    public void setSourceFileContentType(String sourceFileContentType) {
        this.sourceFileContentType = sourceFileContentType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Session dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public Session scenario(Scenario scenario) {
        this.scenario = scenario;
        return this;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Set<DataRecord> getDataRecords() {
        return dataRecords;
    }

    public Session dataRecords(Set<DataRecord> dataRecords) {
        this.dataRecords = dataRecords;
        return this;
    }

    public Session addDataRecord(DataRecord dataRecord) {
        this.dataRecords.add(dataRecord);
        dataRecord.setSession(this);
        return this;
    }

    public Session removeDataRecord(DataRecord dataRecord) {
        this.dataRecords.remove(dataRecord);
        dataRecord.setSession(null);
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
        Session session = (Session) o;
        if (session.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), session.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Session{" +
            "id=" + getId() +
            ", sourceFile='" + getSourceFile() + "'" +
            ", sourceFileContentType='" + getSourceFileContentType() + "'" +
            "}";
    }
}
