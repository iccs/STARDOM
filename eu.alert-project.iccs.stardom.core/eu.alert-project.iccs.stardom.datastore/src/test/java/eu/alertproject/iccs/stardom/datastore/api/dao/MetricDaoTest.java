package eu.alertproject.iccs.stardom.datastore.api.dao;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

/**
 * User: fotis
 * Date: 16/07/11
 * Time: 14:21
 */
public abstract class MetricDaoTest extends SpringDbUnitJpaTest {

    @Autowired
    MetricDao metricDao;

    @Autowired
    IdentityDao identityDao;

    @Override
    public void postConstruct() {}

    @Override
    protected String[] getDatasetFiles() {
        return new String[]{
                "/db/profile.xml",
                "/db/identity.xml",
                "/db/identity_is_profile.xml",
                "/db/scm_acitivy_metric.xml",
                "/db/scm_temporal_metric.xml",
                "/db/scm_api_introduced_metric.xml",
                "/db/scm_api_usage_count_metric.xml",
                "/db/forum_acitivy_metric.xml",
                "/db/forum_temporal_metric.xml",
                "/db/its_activity_metric.xml",
                "/db/its_temporal_metric.xml",
                "/db/metric_sequence.xml",
        };  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected final <T extends MetricQuantitative> void assertInsertQuantitativeMetric(
            Integer identityId,
            T bean,
            Class<T> metricClass,
            int quantity
    ){

        Identity byId = identityDao.findById(identityId);
        bean.setIdentity(byId);
        metricDao.insert(bean);

        T forIdentity = metricDao.getForIdentity(byId, metricClass);

        Assert.assertEquals(DateFormatUtils.format(forIdentity.getCreatedAt(),"yyyy-MM-dd"),
                            DateFormatUtils.format(new Date(),"yyyy-MM-dd"));

        Assert.assertEquals(quantity, forIdentity.getQuantity(),0);


    }



    protected final <T extends MetricQuantitative> void assertUpdateQuantitativeMetric(
            Integer identityId,
            Class<T> metricClass,
            int updateQuantity
    ){

        Identity byId = identityDao.findById(identityId);
        T forIdentity = metricDao.getForIdentity(byId, metricClass);
        Assert.assertNotNull(forIdentity);


        int previous = forIdentity.getQuantity();
        Assert.assertTrue(updateQuantity != previous);

        forIdentity.setQuantity(updateQuantity);

        metricDao.update(forIdentity);


        T updateForIdentity = metricDao.getForIdentity(byId, metricClass);
        Assert.assertNotNull(updateForIdentity);
        Assert.assertEquals(updateQuantity, updateForIdentity.getQuantity(),0);

    }


    protected final <T extends MetricTemporal> void assertInsertTemporalMetric(
            Integer identityId,
            T bean,
            Class<T> metricClass,
            Date when
    ){

        Identity byId = identityDao.findById(identityId);
        bean.setIdentity(byId);
        metricDao.insert(bean);

        T forIdentity = metricDao.getForIdentity(byId, metricClass);

        Assert.assertEquals(DateFormatUtils.format(forIdentity.getCreatedAt(),"yyyy-MM-dd"),
                            DateFormatUtils.format(new Date(),"yyyy-MM-dd"));

        Assert.assertEquals(DateFormatUtils.format(forIdentity.getTemporal(),"yyyy-MM-dd"),
                    DateFormatUtils.format(when, "yyyy-MM-dd"));

    }

    protected final <T extends MetricTemporal> void assertUpdateTemporalMetric(
            Integer identityId,
            Class<T> metricClass,
            Date updateWhen
    ){

        Identity byId = identityDao.findById(identityId);
        T forIdentity = metricDao.getForIdentity(byId, metricClass);
        Assert.assertNotNull(forIdentity);


        Date date = forIdentity.getTemporal();
        Assert.assertTrue(!updateWhen.equals(date));

        forIdentity.setTemporal(updateWhen);

        metricDao.update(forIdentity);


        T updateForIdentity = metricDao.getForIdentity(byId, metricClass);
        Assert.assertNotNull(updateForIdentity);
        Assert.assertEquals(
                DateUtils.round(updateWhen, Calendar.HOUR),
                DateUtils.round(updateForIdentity.getTemporal(),Calendar.HOUR));

    }




    protected final <T extends MetricQuantitative> void assertQuantitativeMetric(
                    Integer identityId,
                    Class<T> metricClass,
                    int quantity,
                    String created){

        Identity identity = identityDao.findById(identityId);

        T forIdentity = metricDao.getForIdentity(identity, metricClass);

        Assert.assertNotNull(forIdentity);

        Assert.assertEquals(DateFormatUtils.format(forIdentity.getCreatedAt(),"yyyy-MM-dd"),created);
        Assert.assertEquals(quantity, forIdentity.getQuantity(),0);


    }

    protected final <T extends MetricTemporal> void assertTemporalMetric(
                    Integer identityId,
                    Class<T> metricClass,
                    String when,
                    String created){



        Identity identity = identityDao.findById(identityId);

        T forIdentity = metricDao.getForIdentity(identity, metricClass);
        Assert.assertNotNull(forIdentity);

        Assert.assertEquals(DateFormatUtils.format(forIdentity.getCreatedAt(),"yyyy-MM-dd"),created);
        Assert.assertEquals(DateFormatUtils.format(forIdentity.getTemporal(), "yyyy-MM-dd"),when);

    }

    protected final <T extends Metric> void noFindMetric(
                    Integer identityId,
                    Class<T> metricClass){

        Identity identity = identityDao.findById(identityId);

        T forIdentity = metricDao.getForIdentity(identity, metricClass);
        Assert.assertNull(forIdentity);
    }

}
