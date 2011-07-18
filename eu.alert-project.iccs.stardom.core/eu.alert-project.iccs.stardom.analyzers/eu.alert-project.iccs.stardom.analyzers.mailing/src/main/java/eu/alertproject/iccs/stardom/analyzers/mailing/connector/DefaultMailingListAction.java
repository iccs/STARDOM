package eu.alertproject.iccs.stardom.analyzers.mailing.connector;

import java.util.Date;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:45
 */
public class DefaultMailingListAction implements MailingListAction{

    private Date date;
    private String from;
    private String subject;
    private String text;


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
