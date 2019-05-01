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
 * A Data.
 */
@Entity
@Table(name = "data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Data implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "source_file")
    private byte[] sourceFile;

    @Column(name = "source_file_content_type")
    private String sourceFileContentType;

    @ManyToOne
    @JsonIgnoreProperties("data")
    private DataType dataType;

    @OneToMany(mappedBy = "data")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DataRecord> dataRecords = new HashSet<>();
    @OneToMany(mappedBy = "data")
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

    public Data name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSourceFile() {
        return sourceFile;
    }

    public Data sourceFile(byte[] sourceFile) {
        this.sourceFile = sourceFile;
        return this;
    }

    public void setSourceFile(byte[] sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getSourceFileContentType() {
        return sourceFileContentType;
    }

    public Data sourceFileContentType(String sourceFileContentType) {
        this.sourceFileContentType = sourceFileContentType;
        return this;
    }

    public void setSourceFileContentType(String sourceFileContentType) {
        this.sourceFileContentType = sourceFileContentType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Data dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Set<DataRecord> getDataRecords() {
        return dataRecords;
    }

    public Data dataRecords(Set<DataRecord> dataRecords) {
        this.dataRecords = dataRecords;
        return this;
    }

    public Data addDataRecord(DataRecord dataRecord) {
        this.dataRecords.add(dataRecord);
        dataRecord.setData(this);
        return this;
    }

    public Data removeDataRecord(DataRecord dataRecord) {
        this.dataRecords.remove(dataRecord);
        dataRecord.setData(null);
        return this;
    }

    public void setDataRecords(Set<DataRecord> dataRecords) {
        this.dataRecords = dataRecords;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public Data sessions(Set<Session> sessions) {
        this.sessions = sessions;
        return this;
    }

    public Data addSession(Session session) {
        this.sessions.add(session);
        session.setData(this);
        return this;
    }

    public Data removeSession(Session session) {
        this.sessions.remove(session);
        session.setData(null);
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
        Data data = (Data) o;
        if (data.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), data.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Data{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sourceFile='" + getSourceFile() + "'" +
            ", sourceFileContentType='" + getSourceFileContentType() + "'" +
            "}";
    }
}
