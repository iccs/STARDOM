package eu.alertproject.iccs.stardom.analyzers.scm;

import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEvent;
import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEventHandler;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.DefaultScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmFile;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 10/03/12
 * Time: 01:10
 */
@ContextConfiguration("classpath:testScmTemporalAnalyzerApplicationContext.xml")
public class ScmEventHandlerTest extends SpringDbUnitJpaTest {

    @Autowired
    IdentityDao identityDao;

    @Autowired
    ScmEventHandler scmEventHandler;

    @Autowired
    MetricDao metricDao;


    @Override
    public void initialize() {
        super.initialize();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Test
    public void testEvent(){

        Date date = new Date();

        final Identity[] identities = new Identity[]{
                                    identityDao.findById(1),
                                    identityDao.findById(2)
        };



        int i = 0;
        while(i < 100 ){

            DefaultScmAction scmAction = new DefaultScmAction();

            scmAction.setComment("Nothing");
            scmAction.setDate(DateUtils.addDays(date, i));
            scmAction.setUid(identities[RandomUtils.nextInt(identities.length)].getUuid());
            scmAction.setFiles(new ArrayList<ScmFile>());
            scmAction.setType(ScmAction.RepositoryType.Svn);

            ScmConnectorContext context =new ScmConnectorContext();
            context.setAction(scmAction);

            context.setProfile(identities[RandomUtils.nextInt(identities.length)].getProfiles().iterator().next());

            ScmEvent event = new ScmEvent(this,context);


            scmEventHandler.event(event);
            i++;

        }


        //check how many
        List<ScmTemporalMetric> all = metricDao.findAll(ScmTemporalMetric.class);

        Assert.assertNotNull(all);
        Assert.assertEquals(100,all.size(),0);


    }

    @Override
    public void postConstruct() {

    }

    @Override
    protected String[] getDatasetFiles() {
        return new String[]{
            "/db/identity.xml",
            "/db/profile.xml",
            "/db/identity_is_profile.xml",
            "/db/empty_metrics.xml"

        };
    }
}
