package eu.alertproject.iccs.stardom.analyzers.its.connector;

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
    private Date date;
    private Profile assigned;
    private Profile reporter;

    private List<Profile> cc;


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

    public List<Profile> getCc() {
        return cc;
    }

    public void setCc(List<Profile> cc) {
        this.cc = cc;
    }
}
