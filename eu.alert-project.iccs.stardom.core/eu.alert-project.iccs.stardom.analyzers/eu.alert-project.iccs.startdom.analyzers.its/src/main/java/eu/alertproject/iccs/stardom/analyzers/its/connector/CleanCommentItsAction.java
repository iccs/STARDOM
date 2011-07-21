package eu.alertproject.iccs.stardom.analyzers.its.connector;

import java.util.Date;

/**
 * User: fotis
 * Date: 20/07/11
 * Time: 18:58
 */
public class CleanCommentItsAction implements ItsAction{

    private Date date;

    @Override
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
