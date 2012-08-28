package eu.alertproject.iccs.stardom.analyzers.its.connector;

import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.stardom.domain.api.Profile;

import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:43
 */
public class DefaultItsAction implements ItsAction{

    private Integer bugId;
    private String bugStatus;
    private String severity;
    private String resolution;
    private Date date;
    private Profile assigned;
    private Profile reporter;
    private List<Keui.Concept> concepts;
    private String component;
    private String subject;

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Integer getBugId() {
        return bugId;
    }

    public void setBugId(Integer bugId) {
        this.bugId = bugId;
    }

    public String getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus(String bugStatus) {
        this.bugStatus = bugStatus;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Profile getAssigned() {
        return assigned;
    }

    public void setAssigned(Profile assigned) {
        this.assigned = assigned;
    }

    public Profile getReporter() {
        return reporter;
    }

    public void setReporter(Profile reporter) {
        this.reporter = reporter;
    }

    public List<Keui.Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Keui.Concept> concepts) {
        this.concepts = concepts;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "DefaultItsAction{" +
                "bugId=" + bugId +
                ", bugStatus='" + bugStatus + '\'' +
                ", severity='" + severity + '\'' +
                ", resolution='" + resolution + '\'' +
                ", date=" + date +
                ", assigned=" + assigned +
                ", reporter=" + reporter +
                '}';
    }

}
