package pk.waqaskhawaja.ma.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A DataSetResource.
 */
@Entity
@Table(name = "data_set_resource")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "datasetresource")
public class DataSetResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "source_file")
    private byte[] sourceFile;

    @Column(name = "source_file_content_type")
    private String sourceFileContentType;

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

    public DataSetResource name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSourceFile() {
        return sourceFile;
    }

    public DataSetResource sourceFile(byte[] sourceFile) {
        this.sourceFile = sourceFile;
        return this;
    }

    public void setSourceFile(byte[] sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getSourceFileContentType() {
        return sourceFileContentType;
    }

    public DataSetResource sourceFileContentType(String sourceFileContentType) {
        this.sourceFileContentType = sourceFileContentType;
        return this;
    }

    public void setSourceFileContentType(String sourceFileContentType) {
        this.sourceFileContentType = sourceFileContentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSetResource)) {
            return false;
        }
        return id != null && id.equals(((DataSetResource) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DataSetResource{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sourceFile='" + getSourceFile() + "'" +
            ", sourceFileContentType='" + getSourceFileContentType() + "'" +
            "}";
    }
}
