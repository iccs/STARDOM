package eu.alertproject.iccs.stardom.internal;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.stardom.activemqconnector.internal.CommitNewAnnotatedListener;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
public class CommitNewAnnotatedListenerTest extends SpringDbUnitJpaTest {

    @Autowired
    CommitNewAnnotatedListener commitNewAnnotatedListener;

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

        commitNewAnnotatedListener.processXml(
                IOUtils.toString(this.getClass().getResourceAsStream("/connector/ALERT.KEUI.CommitNew.Annotated.xml"))
        );

        assertAction();
    }


    @Test
    public void testSendEvent() throws IOException {

        jmsTemplate.send(
                Topics.ALERT_METADATA_CommitNew_Updated,
                new TextMessageCreator(
                        IOUtils.toString(this.getClass().getResourceAsStream("/connector/ALERT.KEUI.CommitNew.Annotated.xml"))
                )
        );

        assertAction();

    }

    private void assertAction(){

        CountDownLatch cdl = new CountDownLatch(1);
        //wait
        try{
            cdl.await(5, TimeUnit.SECONDS);
        }catch (InterruptedException e){
        }

        List<Profile> all = profileDao.findAll();
        Assert.assertNotNull(all);
        Assert.assertEquals(1, all.size(), 0);


        Profile next = all.iterator().next();

        Assert.assertEquals("Sasa",next.getName());
        Assert.assertEquals("Stojanovic",next.getLastname());
        Assert.assertEquals("http://www.alert-project.eu/ontologies/alert_scm.owl#Person1",next.getUri());
        Assert.assertEquals("scm",next.getSource());

        List<Identity> all1 = identityDao.findAll();

        Assert.assertEquals(1,all1.size(),0);

        Iterator<Identity> iterator = all1.iterator();

        List<ScmTemporalMetric> forIdentity = metricDao.getForIdentity(iterator.next(), ScmTemporalMetric.class);

        Assert.assertNotNull(forIdentity);
        Assert.assertEquals(1,forIdentity.size(),0);

        ScmTemporalMetric next1 = forIdentity.iterator().next();


        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime dateTime = dtf.parseDateTime("2012-01-16 16:31");

        Assert.assertEquals(dateTime,new DateTime(next1.getTemporal().getTime()));


    }
}
