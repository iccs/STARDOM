package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.bus.api.AnnotatedUpdateEvent;
import eu.alertproject.iccs.stardom.bus.api.STARDOMTopics;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import org.bushe.swing.event.annotation.EventTopicSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 11:51
 */
@EventHandler
public class ArtefactUpdateEventHandler {

    @Autowired
    UpdateCiService updateCiService;

    private Logger logger = LoggerFactory.getLogger(ArtefactUpdateEventHandler.class);


    @EventTopicSubscriber(topic= STARDOMTopics.IdentityUpdated)
    public void identityUpdated(String topic, AnnotatedUpdateEvent event){
        updateCiService.updateIndentityCI(event);
    }


    @EventTopicSubscriber(topic= STARDOMTopics.IssueUpdated)
    public void issueUpdated(String topic, AnnotatedUpdateEvent event){
        updateCiService.issueUpdated(event);
    }

    @EventTopicSubscriber(topic= STARDOMTopics.ComponentUpdated)
    public void componentUpdated(String topic, AnnotatedUpdateEvent event){
        updateCiService.componentUpdated(event);
    }


}
