package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEvent;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.DefaultScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 19:11
 */
@Service("scmNewCommitListener")
public class ScmNewCommitListener extends ALERTActiveMQListener {
;

    private Logger logger = LoggerFactory.getLogger(ScmNewCommitListener.class);

    @Override
    public void processXml(String xml) {

        ScmConnectorContext context =null;

        ObjectMapper mapper = new ObjectMapper();

        logger.trace("void onMessage() Text to parse {} ",xml);
        try {
            context= mapper.readValue(
                    IOUtils.toInputStream(xml)
                    ,ScmConnectorContext.class);

            //fix profile
            fixProfile(context);
            ScmEvent scmEvent = new ScmEvent(this,context);
            logger.trace("void onMessage() {}/{} {} ",new Object[]{getMessageSentCount(),getMessageCount(),scmEvent});

            Bus.publish(scmEvent);

        } catch (IOException e) {
            logger.warn("Couldn't work with commit ",e);
        }

    }

}
