package eu.alertproject.iccs.stardom.analyzers.its.connector;

import java.util.Date;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:43
 */
public class DefaultItsCommentAction implements ItsAction{

    private String name;
    private String email;
    private Date date;
    private String text;


    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Date getDate() {
        return date;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
