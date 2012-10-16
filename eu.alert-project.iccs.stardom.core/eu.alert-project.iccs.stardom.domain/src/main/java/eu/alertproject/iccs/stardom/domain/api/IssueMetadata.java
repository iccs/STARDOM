package eu.alertproject.iccs.stardom.domain.api;

import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/15/12
 * Time: 3:03 PM
 */
@Entity
@Table(name="issue_metadata")
public class IssueMetadata implements SimpleBean {

    @TableGenerator(
            name="tseq",
            table="sequence",
            pkColumnName="sequence_name",
            valueColumnName="sequence_index",
            pkColumnValue="issue_metadata_sequence",
            allocationSize = 1)
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator="tseq")
    private Integer id;


    @Column(name="issue_id")
    private String issueId;

    @Column(name="issue_uri")
    private String issueUri;

    @Column
    private String component;

    @Column(name="component_uri")
    private String componentUri;

    @Column(name="issue_created")
    private Date issueCreated;

    @Column(name="subject")
    private String subject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public Date getIssueCreated() {
        return issueCreated;
    }

    public void setIssueCreated(Date issueCreated) {
        this.issueCreated = issueCreated;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getIssueUri() {
        return issueUri;
    }

    public void setIssueUri(String issueUri) {
        this.issueUri = issueUri;
    }

    public String getComponentUri() {
        return componentUri;
    }

    public void setComponentUri(String componentUri) {
        this.componentUri = componentUri;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueMetadata that = (IssueMetadata) o;

        if (component != null ? !component.equals(that.component) : that.component != null) return false;
        if (componentUri != null ? !componentUri.equals(that.componentUri) : that.componentUri != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (issueId != null ? !issueId.equals(that.issueId) : that.issueId != null) return false;
        if (issueUri != null ? !issueUri.equals(that.issueUri) : that.issueUri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (issueId != null ? issueId.hashCode() : 0);
        result = 31 * result + (issueUri != null ? issueUri.hashCode() : 0);
        result = 31 * result + (component != null ? component.hashCode() : 0);
        result = 31 * result + (componentUri != null ? componentUri.hashCode() : 0);
        return result;
    }
}
