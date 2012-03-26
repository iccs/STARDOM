package eu.alertproject.iccs.stardom.analyzers.scm;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.DefaultScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmFile;
import eu.alertproject.iccs.stardom.analyzers.scm.constructor.ScmTemporalAnalyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 10/03/12
 * Time: 00:58
 */
@ContextConfiguration("classpath:testScmTemporalAnalyzerApplicationContext.xml")
public class ScmTemporalAnalyzerTest extends SpringDbUnitJpaTest {

    private Logger logger = LoggerFactory.getLogger(ScmTemporalAnalyzerTest.class);

    @Autowired
    IdentityDao identityDao;
    
    @Autowired
    MetricDao metricDao;

    @Autowired
    Analyzer<ScmAction> scmTemporalAnalyzer;


    @Test
    public void testInsertTemporal(){

        Date date = new Date();

        final Identity[] identities = new Identity[]{
                                    identityDao.findById(1),
                                    identityDao.findById(2)
        };



        int i = 0;
        while(i < 100 ){

            DefaultScmAction scmAction = new DefaultScmAction();

            scmAction.setComment("Nothing");
            scmAction.setDate(DateUtils.addDays(date,i));
            scmAction.setUid(identities[RandomUtils.nextInt(identities.length)].getUuid());
            scmAction.setFiles(new ArrayList<ScmFile>());
            scmAction.setType(ScmAction.RepositoryType.Svn);
            scmTemporalAnalyzer.analyze(identities[RandomUtils.nextInt(identities.length)],scmAction);
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

        };  //To change body of implemented methods use File | Settings | File Templates.
    }
}


