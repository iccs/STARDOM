package eu.alertproject.iccs.stardom.internal;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.stardom.activemqconnector.internal.IssueNewAnnotatedListener;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsTemporalMetric;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * User: fotis
 * Date: 03/04/12
 * Time: 14:43
 */
@ContextConfiguration("classpath:/connector/applicationContext.xml")
public class IssueNewAnnotatedListenerTest extends SpringDbUnitJpaTest {

    @Autowired
    IssueNewAnnotatedListener issueNewAnnotatedListener;

    @Autowired
    ProfileDao profileDao;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    MetricDao metricDao;

    @Autowired
    JmsTemplate jmsTemplate;

    @Override
    public void postConstruct() {

    }

    @Override
    protected String[] getDatasetFiles() {
        return new String[]{
                "/db/empty_identity.xml",
                "/db/empty_metrics.xml"
        };  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Test
    public void testEvent() throws IOException {

        issueNewAnnotatedListener.processXml(
                IOUtils.toString(this.getClass().getResourceAsStream("/connector/ALERT.KEUI.IssueNew.Annotated.xml"))
        );

        assertAction();
    }


    @Test
    public void testSendEvent() throws IOException {

        jmsTemplate.send(
                Topics.ALERT_METADATA_IssueNew_Updated,
                new TextMessageCreator(
                        IOUtils.toString(this.getClass().getResourceAsStream("/connector/ALERT.KEUI.IssueNew.Annotated.xml"))
                )
        );

        assertAction();

    }

    private void assertAction(){

        CountDownLatch cdl = new CountDownLatch(1);
        //wait
        try{
            cdl.await(7, TimeUnit.SECONDS);
        }catch (InterruptedException e){
        }

        List<Profile> all = profileDao.findAll();
        Assert.assertNotNull(all);
        Assert.assertEquals(2, all.size(), 0);


        Iterator<Profile> profileIterator = all.iterator();
        Profile profile1 = profileIterator.next();
        Profile profile2 = profileIterator.next();

        Assert.assertEquals("Sander",profile1.getName());
        Assert.assertEquals("Pientka",profile1.getLastname());
        Assert.assertEquals("cumulus0007 gmail com",profile1.getEmail());
        Assert.assertEquals("http://www.alert-project.eu/ontologies/alert_scm.owl#Person2",profile1.getUri());
        Assert.assertEquals("its",profile1.getSource());



        Assert.assertEquals("Alex",profile2.getName());
        Assert.assertEquals("Fiestas", profile2.getLastname());
        Assert.assertEquals("afiestas kde org",profile2.getEmail());
        Assert.assertEquals("http://www.alert-project.eu/ontologies/alert_scm.owl#Person1",profile2.getUri());
        Assert.assertEquals("its",profile2.getSource());



        List<Identity> all1 = identityDao.findAll();

        Assert.assertEquals(2,all1.size(),0);

        Iterator<Identity> iterator = all1.iterator();


        List<ItsTemporalMetric> idenity1Metric = metricDao.getForIdentity(iterator.next(), ItsTemporalMetric.class);
        Assert.assertNotNull(idenity1Metric);
        Assert.assertEquals(1,idenity1Metric.size(),0);

        List<ItsTemporalMetric> idenity2Metric = metricDao.getForIdentity(iterator.next(), ItsTemporalMetric.class);
        Assert.assertNotNull(idenity2Metric);
        Assert.assertEquals(2,idenity2Metric.size(),0);


    }
}
