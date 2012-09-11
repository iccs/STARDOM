package eu.alertproject.iccs.stardom.activemqconnector.api;

import eu.alertproject.iccs.events.api.AbstractActiveMQListener;
import eu.alertproject.iccs.stardom.connector.api.ConnectorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

/**
 * User: fotis
 * Date: 16/12/11
 * Time: 22:14
 */
public abstract class STARDOMActiveMQListener extends AbstractActiveMQListener{


    private Logger logger = LoggerFactory.getLogger(STARDOMActiveMQListener.class);

    @Autowired
    Properties systemProperties;

    @PostConstruct
    public void post(){

        setProcessDisabled(
                Boolean.valueOf(systemProperties.getProperty("activemq.processDisabled")));

    }


    @Override
    public final void process(Message message) throws IOException, JMSException {
        
        String text = ((TextMessage) message).getText();
        processXml(text);
    }

    public abstract void processXml(String xml);


    protected boolean isIgnoredBasedOnDate(Date current){

        String filterDate = systemProperties.getProperty("analyzers.filterDate");

        if(!StringUtils.isEmpty(filterDate)){
            Date when = null;
            try {
                when = DateUtils.parseDate(filterDate, new String[]{"yyyy-MM-dd"});
            } catch (ParseException e) {
                //nothing
            }

            if (when != null && current.before(when)) {
                logger.trace("void action() Ignoring action because date {} is before {}", current, when);
                return true;
            }
        }

        return false;
    }
    protected void fixProfile(ConnectorContext<?> context){

        if(StringUtils.trimToEmpty(context.getProfile().getName()).toLowerCase().equals("none")){
            context.getProfile().setName(null);
        }
        if(StringUtils.trimToEmpty(context.getProfile().getLastname()).toLowerCase().equals("none")){
            context.getProfile().setLastname(null);
        }

        if(StringUtils.trimToEmpty(context.getProfile().getUsername()).toLowerCase().equals("none")){
            context.getProfile().setUsername(null);
        }

        if(StringUtils.trimToEmpty(context.getProfile().getEmail()).toLowerCase().equals("none")){
            context.getProfile().setEmail(null);
        }

    }

}
