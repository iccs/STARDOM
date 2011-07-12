package eu.alertproject.iccs.stardom.connector.api;

import eu.alertproject.iccs.stardom.domain.api.Profile;

import java.awt.*;

/**
 * User: fotis
 * Date: 07/07/11
 * Time: 11:39
 *
 *
 *
 */
public interface ConstructorConnector {

    //http://stackoverflow.com/questions/5726583/spring-rest-multiple-requestbody-parameters-possible
    public void scmAction(ScmConnectorContext context);
    public void mailingListCommentAction(MailingListConnectorContext context);
    public void itsAction(ItsConnectorContext context);

}
