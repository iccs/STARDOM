package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.internal.ArtefactUpdated;
import eu.alertproject.iccs.events.internal.IdentityUpdated;
import eu.alertproject.iccs.stardom.bus.api.AnnotatedUpdateEvent;
import eu.alertproject.iccs.stardom.bus.api.STARDOMTopics;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: fotis
 * Date: 06/04/12
 * Time: 16:19
 */
@Service("updateCiService")
public class UpdateCiServiceImpl implements UpdateCiService{

    private Logger logger = LoggerFactory.getLogger(UpdateCiServiceImpl.class);

    @Autowired
    MetricDao metricDao;

    @Autowired
    JmsTemplate jmsTemplate;
    private CI ci;
    private Map<String,MetricValueStrategy<? extends Metric>> metrics;

    @PostConstruct
    public void init() throws IOException {
        ci = CIUtils.loadFromDefaultLocation();

         //get the metrics for each
        metrics = new HashMap<String,MetricValueStrategy<? extends Metric>>();

        metrics.put("ScmActivityMetric", new NumberMetricValueStrategy<ScmTemporalMetric>(ScmTemporalMetric.class));
        metrics.put("ScmApiIntroducedMetric", new NumberMetricValueStrategy<ScmApiIntroducedMetric>(ScmApiIntroducedMetric.class));
        metrics.put("ScmTemporalMetric", new TemporalMetricValueStrategy<ScmTemporalMetric>(ScmTemporalMetric.class));

        metrics.put("ItsActivityMetric", new NumberMetricValueStrategy<ItsTemporalMetric>(ItsTemporalMetric.class));
        metrics.put("ItsIssuesResolvedMetric", new MostRecentMetricValueStrategy<ItsIssuesResolvedMetric>(ItsIssuesResolvedMetric.class));
        metrics.put("ItsTemporalMetric", new TemporalMetricValueStrategy<ItsTemporalMetric>(ItsTemporalMetric.class));

        metrics.put("CommunicationActivityMetric", new TemporalMetricValueStrategy<MailingListTemporalMetric>(MailingListTemporalMetric.class));
        metrics.put("CommunicationTemporalMetric", new NumberMetricValueStrategy<MailingListTemporalMetric>(MailingListTemporalMetric.class));

    }

    @Override
    @Transactional(readOnly = true)
    public void updateIndentityCI(AnnotatedUpdateEvent event) {
        //get the ci
        List<CI.Classifier> classifiers = ci.getClassifiers();

        long start = System.currentTimeMillis();

        Map<String, Double> ciForClass = new HashMap<String, Double>();

        for(CI.Classifier classifier : classifiers){

            List<CI.Classifier.Metric> classifierMetrics = classifier.getMetrics();

            Map<String, Integer> metricValues = new HashMap<String, Integer>();

            for(CI.Classifier.Metric cim: classifierMetrics){

                if(metrics.containsKey(cim.getName())){
                    metricValues.put(cim.getName(), metrics.get(cim.getName()).getValue(metricDao, (Identity) event.getPayload()));
                }
            }
            ciForClass.put(classifier.getName(),calculateCi(metricValues));

        }

        //create the JSON event
        IdentityUpdated id = new IdentityUpdated();
        id.setId(((Identity) event.getPayload()).getUuid());
        id.setConcepts((List<Keui.Concept>) event.getAnnotations());
        id.setCis(ciForClass);
        //send to the

        String updateEvent = null;
        try {
            ObjectMapper om = new ObjectMapper();
            updateEvent= om.writeValueAsString(id);
        } catch (IOException e) {
            logger.error("Can't send the updated event ",e);
        } finally {

            if(updateEvent !=null ){

                jmsTemplate.send(
                        Topics.ICCS_STARDOM_Identity_Updated,
                        new TextMessageCreator(updateEvent)
                );

            }
            logger.trace("void identityUpdated() It took {} millis to complete {}",start-System.currentTimeMillis(),updateEvent);
        }
    }

    @Override
    public void issueUpdated(AnnotatedUpdateEvent event) {

        long start = System.currentTimeMillis();
        ArtefactUpdated au = new ArtefactUpdated();

        au.setConcepts((List<Keui.Concept>) event.getAnnotations());
        au.setId(String.valueOf(event.getPayload()));

        String updateEvent = null;
        try {
            ObjectMapper om = new ObjectMapper();
            updateEvent= om.writeValueAsString(au);
        } catch (IOException e) {
            logger.error("Can't send the updated event ",e);
        } finally {

            if(updateEvent !=null ){

                jmsTemplate.send(
                        Topics.ICCS_STARDOM_Issue_Updated,
                        new TextMessageCreator(updateEvent)
                );

            }
            logger.trace("void identityUpdated() It took {} millis to complete {}",start-System.currentTimeMillis(),updateEvent);
        }
    }

    private Double calculateCi(Map<String, Integer> metricValues) {
        //TODO Kostas here
        return 0.0;
    }





}