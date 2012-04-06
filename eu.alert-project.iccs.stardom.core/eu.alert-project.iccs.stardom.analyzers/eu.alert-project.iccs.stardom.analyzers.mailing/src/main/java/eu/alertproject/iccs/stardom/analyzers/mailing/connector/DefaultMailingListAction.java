package eu.alertproject.iccs.stardom.analyzers.mailing.connector;

import eu.alertproject.iccs.events.alert.Keui;

import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:45
 */
public class DefaultMailingListAction implements MailingListAction{

    private Date date;
    private String from;
    private String fromUri;
    private String subject;
    private String text;
    private List<Keui.Concept> concepts;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromUri() {
        return fromUri;
    }

    public void setFromUri(String fromUri) {
        this.fromUri = fromUri;
    }

    public List<Keui.Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Keui.Concept> concepts) {
        this.concepts = concepts;
    }

    @Override
    public String toString() {
        return "DefaultMailingListAction{" +
                "date=" + date +
                ", from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
