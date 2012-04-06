package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.internal.ArtefactUpdated;
import eu.alertproject.iccs.events.internal.IdentityUpdated;
import eu.alertproject.iccs.stardom.bus.api.AnnotatedUpdateEvent;
import eu.alertproject.iccs.stardom.bus.api.STARDOMTopics;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import eu.alertproject.iccs.stardom.classification.CI;
import eu.alertproject.iccs.stardom.classification.CIUtils;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.datastore.api.metrics.MetricValueStrategy;
import eu.alertproject.iccs.stardom.datastore.api.metrics.MostRecentMetricValueStrategy;
import eu.alertproject.iccs.stardom.datastore.api.metrics.NumberMetricValueStrategy;
import eu.alertproject.iccs.stardom.datastore.api.metrics.TemporalMetricValueStrategy;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.metrics.*;
import org.bushe.swing.event.annotation.EventTopicSubscriber;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

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
}
