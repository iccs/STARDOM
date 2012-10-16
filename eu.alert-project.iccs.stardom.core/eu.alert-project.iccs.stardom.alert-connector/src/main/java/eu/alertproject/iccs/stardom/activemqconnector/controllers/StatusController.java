package eu.alertproject.iccs.stardom.activemqconnector.controllers;

import eu.alertproject.iccs.events.api.ActiveMQMessageBroker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/17/12
 * Time: 12:04 AM
 */
@Controller
public class StatusController {

    @Autowired
    ActiveMQMessageBroker messageBroker;

    @RequestMapping(value="/status", method = RequestMethod.GET)
    public Map<String, AtomicInteger> status(){
        return messageBroker.getListenerCounts();
    }

}
