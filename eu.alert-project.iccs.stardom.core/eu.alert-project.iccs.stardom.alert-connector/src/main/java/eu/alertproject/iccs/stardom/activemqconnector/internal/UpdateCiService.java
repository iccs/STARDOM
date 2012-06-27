package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.bus.api.AnnotatedUpdateEvent;
import eu.alertproject.iccs.stardom.bus.api.STARDOMTopics;
import org.bushe.swing.event.annotation.EventTopicSubscriber;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: fotis
 * Date: 06/04/12
 * Time: 16:14
 */
public interface UpdateCiService {
    void updateIndentityCI(AnnotatedUpdateEvent event);
    void issueUpdated(AnnotatedUpdateEvent event);
    void componentUpdated(AnnotatedUpdateEvent event);

}
