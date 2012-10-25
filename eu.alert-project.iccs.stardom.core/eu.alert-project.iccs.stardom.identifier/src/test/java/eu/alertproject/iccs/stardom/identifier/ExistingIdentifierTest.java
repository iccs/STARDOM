package eu.alertproject.iccs.stardom.identifier;

import eu.alertproject.iccs.events.alert.CommitNewAnnotatedEnvelope;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * User: fotis
 * Date: 20/07/11
 * Time: 08:50
 */
public class ExistingIdentifierTest extends SpringDbUnitJpaTest {

    private Logger logger = LoggerFactory.getLogger(ExistingIdentifierTest.class);

    @Autowired
    Identifier identifier;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    ProfileDao profileDao;

    @Override
    public void postConstruct() {}

    @Override
    protected String[] getDatasetFiles() {
        return new String[]{
            "/db/existing_uuids.xml",
        };
    }

    /**
     * The following test checks that the URI of a profile is taken
     * into consideration regardless of the available profiles
     *
     * @throws IOException
     */
    @Test
    public void testMatch() throws IOException {


        Profile p = new Profile();
        p.setName("Dario");
        p.setLastname("Freddi");
        p.setEmail("drf@kde.org");
        p.setUsername("drf@kde.org");
        p.setUri("http://www.alert-project.eu/ontologies/alert_scm.owl#Person1620");
        p.setSource("scm");

        Identity scm = identifier.find(p, "scm");


        List<Profile> all = profileDao.findAll();
        Assert.assertEquals(8,all.size(),0);

        Profile profile = all.get(7);

        Assert.assertEquals("http://www.alert-project.eu/ontologies/alert_scm.owl#Person1620",
                            profile.getUri());

        Assert.assertEquals("Dario",
                            profile.getName());

        Assert.assertEquals("Freddi",
                            profile.getLastname());

        Assert.assertEquals("drf kde org",
                            profile.getEmail());

        Assert.assertEquals("drf kde org",
                            profile.getUsername());




    }


}
