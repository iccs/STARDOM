package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.analyzers.forums.bus.ForumEvent;
import eu.alertproject.iccs.stardom.analyzers.forums.connector.ForumConnectorContext;
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
public class ForumActionController implements ConstructorConnector<ForumConnectorContext> {

    private Logger logger = LoggerFactory.getLogger(ForumActionController.class);

    @Override
    @RequestMapping(value = "/constructor/action/forums", method = RequestMethod.POST)
    public @ResponseBody void action(@RequestBody ForumConnectorContext context) {

        logger.trace("void forumAction() {} {}",context.getProfile(),context.getAction());

        //create the action

        ForumEvent scmEvent = new ForumEvent(this,context);
        Bus.publish(scmEvent);


    }
}
