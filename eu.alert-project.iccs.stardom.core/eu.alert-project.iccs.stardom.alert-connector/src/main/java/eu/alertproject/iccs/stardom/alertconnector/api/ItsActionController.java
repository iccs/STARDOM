package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsConnectorContext;
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
 * Date: 20/07/11
 * Time: 12:23
 */
@Controller
public class ItsActionController  implements ConstructorConnector<ItsConnectorContext> {

    private Logger logger = LoggerFactory.getLogger(ItsActionController.class);

    @Override
    @RequestMapping(value = "/constructor/action/its/add", method = RequestMethod.POST)
    public @ResponseBody void action(@RequestBody ItsConnectorContext context) {

        logger.trace("void itsAction() {} {}",context.getAction());

        //create the action
        ItsEvent itsEvent= new ItsEvent(this,context);
        Bus.publish(itsEvent);

    }

}
