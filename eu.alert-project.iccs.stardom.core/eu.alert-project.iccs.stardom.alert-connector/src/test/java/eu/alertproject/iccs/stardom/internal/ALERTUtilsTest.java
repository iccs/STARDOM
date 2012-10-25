package eu.alertproject.iccs.stardom.internal;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.ReaderWrapper;
import com.thoughtworks.xstream.io.xml.XppDomDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import eu.alertproject.iccs.events.alert.CommitNewAnnotatedEnvelope;
import eu.alertproject.iccs.events.alert.CommitNewAnnotatedPayload;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.activemqconnector.internal.ALERTUtils;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.io.Reader;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/25/12
 * Time: 2:10 PM
 */
@ContextConfiguration("classpath:/connector/applicationContext.xml")
public class ALERTUtilsTest {

    @Test
    public void testOk() throws IOException {

        CommitNewAnnotatedEnvelope envelope = EventFactory.<CommitNewAnnotatedEnvelope>fromXml(
                IOUtils.toString(
                        ALERTUtilsTest.class.getResourceAsStream("/connector/ALERT.KEUI.CommitNew.Annotated.xml")
                        ),CommitNewAnnotatedEnvelope.class);


        Profile scm = ALERTUtils.extractProfile(
                envelope.getBody().getNotify().getNotificationMessage().getMessage().getEvent().getPayload().getEventData().getKesi(),
                envelope.getBody().getNotify().getNotificationMessage().getMessage().getEvent().getPayload().getEventData().getMdService(),
                "scm"
        );


        Assert.assertEquals("drf@kde.org",scm.getEmail());
        Assert.assertEquals("Dario",scm.getName());
        Assert.assertEquals("Freddi",scm.getLastname());
        Assert.assertEquals("drf@kde.org",scm.getUsername());


    }


    @Test
    public void testNoAuthor() throws IOException {

        CommitNewAnnotatedEnvelope envelope = EventFactory.<CommitNewAnnotatedEnvelope>fromXml(
                IOUtils.toString(
                        ALERTUtilsTest.class.getResourceAsStream("/connector/ALERT.KEUI.CommitNew-NoAuthor.Annotated.xml")
                ),CommitNewAnnotatedEnvelope.class);


        Profile scm = ALERTUtils.extractProfile(
                envelope.getBody().getNotify().getNotificationMessage().getMessage().getEvent().getPayload().getEventData().getKesi(),
                envelope.getBody().getNotify().getNotificationMessage().getMessage().getEvent().getPayload().getEventData().getMdService(),
                "scm"
        );


        Assert.assertEquals("drf@kde.org",scm.getEmail());
        Assert.assertEquals("Dario",scm.getName());
        Assert.assertEquals("Freddi",scm.getLastname());
        Assert.assertEquals("drf@kde.org",scm.getUsername());
        Assert.assertEquals("http://www.alert-project.eu/ontologies/alert_scm.owl#Person1620",scm.getUri());
        Assert.assertEquals("scm",scm.getSource());


    }

    @Test
    public void testAuthorNone() throws IOException {

        CommitNewAnnotatedEnvelope envelope = EventFactory.<CommitNewAnnotatedEnvelope>fromXml(
                IOUtils.toString(
                        ALERTUtilsTest.class.getResourceAsStream("/connector/ALERT.KEUI.CommitNew-AuthorNone.Annotated.xml")
                ),CommitNewAnnotatedEnvelope.class);


        Profile scm = ALERTUtils.extractProfile(
                envelope.getBody().getNotify().getNotificationMessage().getMessage().getEvent().getPayload().getEventData().getKesi(),
                envelope.getBody().getNotify().getNotificationMessage().getMessage().getEvent().getPayload().getEventData().getMdService(),
                "scm"
        );


        Assert.assertEquals("drf@kde.org",scm.getEmail());
        Assert.assertNull(scm.getName());
        Assert.assertNull(scm.getLastname());
        Assert.assertEquals("drf@kde.org",scm.getUsername());
        Assert.assertEquals("http://www.alert-project.eu/ontologies/alert_scm.owl#Person1621",scm.getUri());
        Assert.assertEquals("scm",scm.getSource());


    }


}
