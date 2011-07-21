package eu.alertproject.iccs.stardom.analyzers.its.connector;

import java.util.Date;

/**
 * User: fotis
 * Date: 20/07/11
 * Time: 18:58
 */
public class CleanItsAction implements ItsAction{
    Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Date getDate() {
        return date;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
