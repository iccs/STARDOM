package eu.alertproject.iccs.stardom.spring;

import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * User: fotis
 * Date: 26/06/12
 * Time: 18:48
 */
public class ALERTDefaultMessageListenerContainer extends DefaultMessageListenerContainer {

    
    @Autowired
    ActiveMQConnectionFactory jmsConnectionFactory;

    @Autowired
    Properties systemProperties;

    public ALERTDefaultMessageListenerContainer(String topic,ALERTActiveMQListener listener) {

        super();

        this.setMessageListener(listener);
        this.setDestination(new ActiveMQTopic(topic));
    }

    
    @PostConstruct
    public void post(){
        this.setConnectionFactory(jmsConnectionFactory);
        this.setRecoveryInterval(Integer.valueOf(systemProperties.getProperty("activemq.recoveryInterval")));
        this.setCacheLevel(Integer.valueOf(systemProperties.getProperty("activemq.cacheLevel")));
    }

}
