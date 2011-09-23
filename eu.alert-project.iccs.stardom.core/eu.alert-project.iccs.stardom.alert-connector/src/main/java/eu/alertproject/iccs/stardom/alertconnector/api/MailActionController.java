package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.analyzers.mailing.bus.MailingEvent;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.connector.api.ConstructorConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 23:55
 */
@Controller
public class MailActionController implements ConstructorConnector<MailingListConnectorContext> {

    private Logger logger = LoggerFactory.getLogger(MailingListConnectorContext.class);

    @Override
    @RequestMapping(value = "/constructor/action/mail", method = RequestMethod.POST)
    public @ResponseBody void action(@RequestBody MailingListConnectorContext context) {

        logger.trace("void mailAction() {} {}",context.getAction());

        //create the action


        MailingEvent mailEvent = new MailingEvent(this,context);
        Bus.publish(mailEvent);


    }
}
