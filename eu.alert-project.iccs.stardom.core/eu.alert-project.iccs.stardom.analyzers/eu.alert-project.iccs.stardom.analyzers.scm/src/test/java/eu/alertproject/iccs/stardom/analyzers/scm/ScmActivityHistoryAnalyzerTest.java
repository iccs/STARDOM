package eu.alertproject.iccs.stardom.analyzers.scm;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.DefaultScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmFile;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: fotis
 * Date: 28/01/12
 * Time: 12:45
 */

@ContextConfiguration("classpath:testHistoryAnalyzerApplicationContext.xml")
public class ScmActivityHistoryAnalyzerTest extends SpringDbUnitJpaTest{

    @Autowired
    IdentityDao identityDao;

    @Autowired
    MetricDao metricDao;

    @Autowired
    Analyzer<ScmAction> scmActivityHistoryAnalyzer;

    
    @Test
    public void canHande(){
        
        Assert.assertTrue(scmActivityHistoryAnalyzer.canHandle(new DefaultScmAction()));

    }
    /**
     * The following method tests wether the
     * tests are properly recorded regardless
     * of the actionDate!
     *
     * @throws java.text.ParseException If the date is not parsed correclty
     */
    @Test
    public void testHistory() throws ParseException {

        Identity identity = identityDao.findById(1);
        Assert.assertNotNull(identity);

        scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is the first ", "2000-01-01", "1001"));
        scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is the second", "1999-01-01", "0099"));
        scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is the third", "1998-01-01", "0098"));

        ScmActivityMetric mostRecentMetric = metricDao.getMostRecentMetric(identity, ScmActivityMetric.class);
        assertMetric(mostRecentMetric, 3, "2000-01-01");

        List<ScmActivityMetric> forIdentity = metricDao.getForIdentity(identity, ScmActivityMetric.class);
        Assert.assertNotNull(forIdentity);
        Assert.assertEquals(3, forIdentity.size(), 0);


        Iterator<ScmActivityMetric> iterator = forIdentity.iterator();

        assertMetric(iterator.next(), 3,"2000-01-01");
        assertMetric(iterator.next(), 2,"1999-01-01");
        assertMetric(iterator.next(), 1,"1998-01-01");

        //now add one after

        scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is fourth","2000-01-02","1002"));


        List<ScmActivityMetric> metricList = metricDao.getForIdentity(identity, ScmActivityMetric.class);
        assertMetric(metricList.iterator().next(), 4, "2000-01-02");

        mostRecentMetric = metricDao.getMostRecentMetric(identity, ScmActivityMetric.class);
        assertMetric(mostRecentMetric, 4, "2000-01-02");

    }
    
    

    private void assertMetric(ScmActivityMetric next, int quantity, String date) throws ParseException {

        Assert.assertEquals(DateUtils.parseDate(date, new String[]{"yyyy-MM-dd"}), next.getCreatedAt());
        Assert.assertEquals(quantity,next.getQuantity(),0);

    }


    private DefaultScmAction generateAction(String comment, String date, String revission) throws ParseException {
        DefaultScmAction as = new DefaultScmAction();

        as.setComment(comment);
        as.setDate(DateUtils.parseDate(date, new String[]{"yyyy-MM-dd"}));
        as.setType(ScmAction.RepositoryType.Svn);
        as.setUid(DigestUtils.md5Hex(date));
        as.setRevission(revission);
        as.setFiles(new ArrayList<ScmFile>());

        return as;
    }


    @Override
    public void postConstruct() {
    }

    @Override
    protected String[] getDatasetFiles() {
        return new String[]{
            "/db/identity.xml",
            "/db/profile.xml",
            "/db/identity_is_profile.xml"
        };  //To change body of implemented methods use File | Settings | File Templates.
    }

}
