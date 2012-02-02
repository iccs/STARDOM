package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEvent;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.DefaultScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.connector.api.ConstructorConnector;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 23:55
 */
@Controller
public class ScmActionController implements ConstructorConnector<ScmConnectorContext> {

    private Logger logger = LoggerFactory.getLogger(ScmActionController.class);

    private AtomicInteger messageCount = new AtomicInteger();
    
    @Autowired
    Properties systemProperties;


    @Override
    @RequestMapping(value = "/constructor/action/scm", method = RequestMethod.POST)
    public @ResponseBody void action(@RequestBody ScmConnectorContext context) {

        logger.trace("void scmAction() {} {}",context.getProfile(),context.getAction());


        DefaultScmAction action = context.getAction();

        String filterDate = systemProperties.getProperty("analyzers.filterDate");


        Date when = null;
        try {
            when = DateUtils.parseDate(filterDate, new String[]{"yyyy-MM-dd"});
        } catch (ParseException e) {
            //nothing
        }


        if(when != null && action.getDate().before(when)){
            logger.trace("void action() Ignoring action because date {} is before {}",action.getDate(), when);
            return;
        }


        //create the action
        ScmEvent scmEvent = new ScmEvent(this,context);
        messageCount.incrementAndGet();

        Bus.publish(scmEvent);

    }

    @RequestMapping(value = "/constructor/scm/count", method = RequestMethod.GET)
    public @ResponseBody Integer getMessageCount(){

        return messageCount.get();

    }
}
