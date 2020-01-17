package pk.waqaskhawaja.ma.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AnalysisSessionResource.
 */
@Entity
@Table(name = "analysis_session_resource")
@Document(indexName = "analysissessionresource")
public class AnalysisSessionResource implements Serializable {

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

    @Size(max = 1000)
    @Column(name = "url", length = 1000)
    private String url;

    @ManyToOne
    @JsonIgnoreProperties("analysisSessionResources")
    private ResourceType resourceType;

    @ManyToOne
    @JsonIgnoreProperties("analysisSessionResources")
    private AnalysisSession analysisSession;

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

    public AnalysisSessionResource name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSourceFile() {
        return sourceFile;
    }

    public AnalysisSessionResource sourceFile(byte[] sourceFile) {
        this.sourceFile = sourceFile;
        return this;
    }

    public void setSourceFile(byte[] sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getSourceFileContentType() {
        return sourceFileContentType;
    }

    public AnalysisSessionResource sourceFileContentType(String sourceFileContentType) {
        this.sourceFileContentType = sourceFileContentType;
        return this;
    }

    public void setSourceFileContentType(String sourceFileContentType) {
        this.sourceFileContentType = sourceFileContentType;
    }

    public String getUrl() {
        return url;
    }

    public AnalysisSessionResource url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public AnalysisSessionResource resourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public AnalysisSession getAnalysisSession() {
        return analysisSession;
    }

    public AnalysisSessionResource analysisSession(AnalysisSession analysisSession) {
        this.analysisSession = analysisSession;
        return this;
    }

    public void setAnalysisSession(AnalysisSession analysisSession) {
        this.analysisSession = analysisSession;
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
        AnalysisSessionResource analysisSessionResource = (AnalysisSessionResource) o;
        if (analysisSessionResource.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), analysisSessionResource.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnalysisSessionResource{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sourceFile='" + getSourceFile() + "'" +
            ", sourceFileContentType='" + getSourceFileContentType() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
