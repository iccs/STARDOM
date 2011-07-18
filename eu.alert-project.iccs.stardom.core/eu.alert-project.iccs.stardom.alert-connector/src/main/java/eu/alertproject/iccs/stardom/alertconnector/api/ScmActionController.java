package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEvent;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
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
public class ScmActionController implements ConstructorConnector<ScmConnectorContext> {

    private Logger logger = LoggerFactory.getLogger(ScmActionController.class);

    @Override
    @RequestMapping(value = "/constructor/action/scm", method = RequestMethod.POST)
    public @ResponseBody void action(@RequestBody ScmConnectorContext context) {

        logger.trace("void scmAction() {} {}",context.getProfile(),context.getAction());

        //create the action

        ScmEvent scmEvent = new ScmEvent(this,context);
        Bus.publish(scmEvent);


    }
}
