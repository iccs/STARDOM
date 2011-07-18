package eu.alertproject.iccs.stardom.analyzers.forums.connector;

import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 12/07/11
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
public interface ForumAction extends ConnectorAction {

    public Date getDate();


}
