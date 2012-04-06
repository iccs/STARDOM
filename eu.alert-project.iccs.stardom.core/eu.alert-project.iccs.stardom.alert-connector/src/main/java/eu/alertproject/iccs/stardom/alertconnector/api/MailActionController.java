package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.activemqconnector.internal.MailNewAnnotatedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    MailNewAnnotatedListener mailNewMailListener;

    private AtomicInteger messageCount = new AtomicInteger();


    @RequestMapping(value = "/constructor/ml/count", method = RequestMethod.GET)
    public @ResponseBody Integer action() {

        return mailNewMailListener.getMessageCount();
    }
}
