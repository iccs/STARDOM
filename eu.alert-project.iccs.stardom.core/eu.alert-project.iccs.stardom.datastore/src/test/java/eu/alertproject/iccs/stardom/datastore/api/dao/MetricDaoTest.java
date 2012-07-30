package eu.alertproject.iccs.stardom.datastore.api.dao;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

/**
 * User: fotis
 * Date: 16/07/11
 * Time: 14:21
 */
public abstract class MetricDaoTest extends SpringDbUnitJpaTest {

    private Logger logger = LoggerFactory.getLogger(MetricDaoTest.class);

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
                "/db/mailing_list_activity_metric.xml",
                "/db/mailing_list_temporal_metric.xml",
                "/db/metric_sequence.xml",
        };  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transactional
    protected final <T extends MetricQuantitative> void assertInsertQuantitativeMetric(
            Integer identityId,
            T bean,
            Class<T> metricClass,
            int quantity
    ){

        Identity byId = identityDao.findById(identityId);
        bean.setIdentity(byId);
        metricDao.insert(bean);

        T forIdentity = metricDao.getMostRecentMetric(byId, metricClass);

        Assert.assertNotNull(forIdentity);

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
        T forIdentity = metricDao.getMostRecentMetric(byId, metricClass);
        Assert.assertNotNull(forIdentity);


        int previous = forIdentity.getQuantity();
        Assert.assertTrue(updateQuantity != previous);

        forIdentity.setQuantity(updateQuantity);

        metricDao.update(forIdentity);


        T updateForIdentity = metricDao.getMostRecentMetric(byId, metricClass);
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

        T forIdentity = metricDao.getMostRecentMetric(byId, metricClass);

        Assert.assertNotNull(forIdentity);

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
        T forIdentity = metricDao.getMostRecentMetric(byId, metricClass);
        Assert.assertNotNull(forIdentity);


        Date date = forIdentity.getTemporal();
        Assert.assertTrue(!updateWhen.equals(date));

        forIdentity.setTemporal(updateWhen);

        metricDao.update(forIdentity);


        T updateForIdentity = metricDao.getMostRecentMetric(byId, metricClass);
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

        T forIdentity = metricDao.getMostRecentMetric(identity, metricClass);

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

        T forIdentity = metricDao.getMostRecentMetric(identity, metricClass);
        Assert.assertNotNull(forIdentity);

        Assert.assertEquals(DateFormatUtils.format(forIdentity.getCreatedAt(),"yyyy-MM-dd"),created);
        Assert.assertEquals(DateFormatUtils.format(forIdentity.getTemporal(), "yyyy-MM-dd"),when);

    }

    protected final <T extends Metric> void noFindMetric(
                    Integer identityId,
                    Class<T> metricClass){

        Identity identity = identityDao.findById(identityId);

        T forIdentity = metricDao.getMostRecentMetric(identity, metricClass);
        Assert.assertNull(forIdentity);
    }


    protected final <T extends MetricQuantitative> void assertAfterDate(Integer identityId, Class<T> metricClass) throws ParseException {


        Identity byId = identityDao.findById(identityId);
        
        String[] dates = new String[]{
                "2028-01-01",
                "2028-01-02",
                "2028-01-03",
                "2028-01-04"};
        
        int quantity = 1;
        for(String date : dates){
            metricDao.insert(this.createMetric(metricClass,byId,date,quantity));
            quantity++;
        }

        List<T> forIdentityAfer = metricDao.getForIdentityAfer(byId, DateUtils.parseDate("2028-01-02", new String[]{"yyyy-MM-dd"}), metricClass);
        Assert.assertNotNull(forIdentityAfer);
        Assert.assertEquals(3, forIdentityAfer.size(),0);

        Iterator<T> iterator = forIdentityAfer.iterator();
        quantity = 2;
        for(int i =1; i < dates.length ; i++){

            T next = metricClass.cast(iterator.next());
            Assert.assertEquals(DateUtils.parseDate(dates[i], new String[]{"yyyy-MM-dd"}), next.getCreatedAt());
            Assert.assertEquals(quantity, next.getQuantity(),0);
            quantity++;
        }

    }
    
    
    private <T extends MetricQuantitative>  T createMetric(Class<T> metricClass, Identity identity, String date,Integer quantity){
        //insert a couple of metrics
        T ret = null;
        try {

            Constructor<T> constructor = metricClass.getConstructor();
            ret = constructor.newInstance();
            ret.setCreatedAt(DateUtils.parseDate(date, new String[]{"yyyy-MM-dd"}));
            ret.setIdentity(identity);
            ret.setQuantity(quantity);

        } catch (NoSuchMethodException e) {
            logger.trace("T createMetric() ", e);
        } catch (InvocationTargetException e) {
            logger.trace("T createMetric() ",e);
        } catch (InstantiationException e) {
            logger.trace("T createMetric() ", e);
        } catch (IllegalAccessException e) {
            logger.trace("T createMetric() ", e);
        } catch (ParseException e) {
            logger.trace("T createMetric() ", e);
        }
        
        return ret;
        
    }


}
