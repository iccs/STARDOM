package eu.alertproject.iccs.stardom.internal;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.stardom.activemqconnector.internal.IssueUpdatedAnnotatedListener;
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
public class IssueUpdateAnnotatedListenerTest extends SpringDbUnitJpaTest {

    @Autowired
    IssueUpdatedAnnotatedListener issueUpdatedAnnotatedListener;

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

        issueUpdatedAnnotatedListener.processXml(
                IOUtils.toString(this.getClass().getResourceAsStream("/connector/ALERT.KEUI.IssueUpdate.Annotated.xml"))
        );

        assertAction();
    }


    @Test
    public void testSendEvent() throws IOException {

        jmsTemplate.send(
                Topics.ALERT_METADATA_IssueUpdate_Updated,
                new TextMessageCreator(
                        IOUtils.toString(this.getClass().getResourceAsStream("/connector/ALERT.KEUI.IssueUpdate.Annotated.xml"))
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
        Assert.assertEquals(5, all.size(), 0);


        Iterator<Profile> profileIterator = all.iterator();
        Profile profile1 = profileIterator.next();

        Assert.assertEquals("S.",profile1.getName());
        Assert.assertEquals("Burmeister",profile1.getLastname());
        Assert.assertEquals("sven burmeister",profile1.getEmail());
        Assert.assertEquals("http://www.alert-project.eu/ontologies/alert_scm.owl#Person1",profile1.getUri());
        Assert.assertEquals("its-comment",profile1.getSource());


        List<Identity> all1 = identityDao.findAll();

        Assert.assertEquals(4,all1.size(),0);

        Identity identity = all1.get(1);//drf;
        List<ItsTemporalMetric> idenity1Metric = metricDao.getForIdentity(identity, ItsTemporalMetric.class);
        Assert.assertNotNull(idenity1Metric);
        Assert.assertEquals(5,idenity1Metric.size(),0);


    }
}
