package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsCommentEvent;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsCommentConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.connector.api.ConstructorConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: fotis
 * Date: 20/07/11
 * Time: 12:23
 */
@Controller
public class ItsCommentActionController implements ConstructorConnector<ItsCommentConnectorContext> {

    private Logger logger = LoggerFactory.getLogger(ItsCommentConnectorContext.class);


    private AtomicInteger messageCount = new AtomicInteger();



    @Override
    @RequestMapping(value = "/constructor/action/its/comment", method = RequestMethod.POST)
    public @ResponseBody void action(@RequestBody ItsCommentConnectorContext context) {

        logger.trace("void istAction() {} {}",context.getAction());

        //create the action
        ItsCommentEvent itsEvent= new ItsCommentEvent(this,context);

        messageCount.incrementAndGet();
        Bus.publish(itsEvent);

    }

    @RequestMapping(value = "/constructor/itscomment/count", method = RequestMethod.GET)
    public @ResponseBody Integer getMessageCount(){

        return messageCount.get();

    }

}
