package eu.alertproject.iccs.stardom.activemqconnector.api;

import eu.alertproject.iccs.events.api.AbstractActiveMQListener;
import eu.alertproject.iccs.stardom.connector.api.ConnectorContext;
import eu.alertproject.iccs.stardom.connector.api.Subscriber;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: fotis
 * Date: 16/12/11
 * Time: 22:14
 */
public abstract class ALERTActiveMQListener extends AbstractActiveMQListener{


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
