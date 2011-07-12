package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.bus.api.ItsEvent;
import eu.alertproject.iccs.stardom.bus.api.MailingEvent;
import eu.alertproject.iccs.stardom.bus.api.ScmEvent;
import eu.alertproject.iccs.stardom.connector.api.ConstructorConnector;
import eu.alertproject.iccs.stardom.connector.api.ItsConnectorContext;
import eu.alertproject.iccs.stardom.connector.api.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.connector.api.ScmConnectorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 13:38
 */
@Controller
public class AlertConstructorConnector implements ConstructorConnector{

    private Logger logger = LoggerFactory.getLogger(AlertConstructorConnector.class);


    @Override
    @RequestMapping(value = "/constructor/action/scm", method = RequestMethod.POST)
    public @ResponseBody void scmAction(@RequestBody ScmConnectorContext context) {
        logger.trace("void scmAction() {} {}",context.getProfile(),context.getAction());

        //create the action

        ScmEvent scmEvent = new ScmEvent(this,context);

        Bus.publish(scmEvent);

    }

    @Override
    @RequestMapping(value = "/constructor/action/mail", method = RequestMethod.POST)
    public @ResponseBody void mailingListCommentAction(@RequestBody MailingListConnectorContext context) {

        logger.trace("void mailingListCommentAction() {},{}",context.getAction(),context.getProfile());
        MailingEvent mailingEvent = new MailingEvent(this,context);
        Bus.publish(mailingEvent);


    }

    @Override
    @RequestMapping(value = "/constructor/action/its", method = RequestMethod.POST)
    public @ResponseBody void itsAction(@RequestBody ItsConnectorContext context) {

        logger.trace("void itsAction() {} {}",context.getAction(),context.getProfile());
        ItsEvent itsEvent = new ItsEvent(this,context);
        Bus.publish(itsEvent);

    }
}
