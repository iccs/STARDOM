package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.activemqconnector.internal.MailNewMailListener;
import eu.alertproject.iccs.stardom.analyzers.mailing.bus.MailingEvent;
import eu.alertproject.iccs.stardom.analyzers.mailing.connector.MailingListConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.connector.api.ConstructorConnector;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 23:55
 */
@Controller
public class MailActionController{

    @Autowired
    MailNewMailListener mailNewMailListener;

    private AtomicInteger messageCount = new AtomicInteger();


    @RequestMapping(value = "/constructor/ml/count", method = RequestMethod.GET)
    public @ResponseBody Integer action() {

        return mailNewMailListener.getMessageCount();
    }
}
