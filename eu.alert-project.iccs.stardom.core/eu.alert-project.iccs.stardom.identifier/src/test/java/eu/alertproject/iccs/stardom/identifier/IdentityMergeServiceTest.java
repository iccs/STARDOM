package eu.alertproject.iccs.stardom.identifier;

import com.existanze.libraries.orm.test.JpaTestUtils;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.*;
import eu.alertproject.iccs.stardom.domain.api.metrics.*;
import eu.alertproject.iccs.stardom.identifier.api.IdentityMergeService;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

/**
 * User: fotis
 * Date: 22/01/12
 * Time: 13:58
 */
public class IdentityMergeServiceTest extends SpringDbUnitJpaTest{

    @Autowired
    IdentityDao identityDao;

    @Autowired
    ProfileDao profileDao;

    @Autowired
    MetricDao metricDao;


    @Autowired
    IdentityMergeService identityMergeService;


    @Override
    protected String[] getDatasetFiles() {
        return new String[]{
                "/db/identity.xml",
                "/db/profile.xml",
                "/db/identity_is_profile.xml",
                "/db/scm_acitivy_metric.xml",
                "/db/scm_temporal_metric.xml",
                "/db/its_activity_metric.xml",
                "/db/its_temporal_metric.xml",
                "/db/mailing_list_activity_metric.xml",
                "/db/mailing_list_temporal_metric.xml",
                "/db/scm_api_introduced_metric.xml",
                "/db/metric_sequence.xml",
        };  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void postConstruct() {}

    @Test
    public void testList(){

            //check the info
        List<Identity> all = identityDao.findAll();


        JpaTestUtils.assertIdOrder(
                all,
                new Integer[]{
                    1,2,3,4,5
                });


    }

    @Test
    public void testMerge(){


        //check how many profiles we are working with
        //so that no new ones are created
        List<Profile> profiles= profileDao.findAll();
        
        JpaTestUtils.assertIdOrder(
                profiles,
                new Integer[]{1,2,3,4,5,6,7,8}
        );


        List<Metric> metricList = metricDao.findAll();
        Assert.assertNotNull(metricList);
        Assert.assertEquals(21, metricList.size(), 0);



        /*

        Before


        mysql> select * from scm_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        |  2 |       15 |           2 | 2011-06-17 00:00:00 |
        |  3 |       10 |           3 | 2011-06-20 00:00:00 |
        +----+----------+-------------+---------------------+

        mysql> select * from its_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 14 |       25 |           2 | 2006-06-17 00:00:00 |
        | 15 |       25 |           3 | 2006-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        2 rows in set (0.01 sec)

        mysql> select * from mailing_list_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 26 |       95 |           2 | 1999-06-17 00:00:00 |
        | 27 |       30 |           3 | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        2 rows in set (0.01 sec)





         */

        identityMergeService.merge(
                2, 3, 5
        );

        /*

        After


        mysql> select * from scm_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        |  2 |       25 |           2 | 2011-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)

        mysql> select * from its_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 14 |       50 |           2 | 2006-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)


        mysql> select * from mailing_list_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 26 |      125 |           2 | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)



         */

        //check how many profiles we are working with
        //so that no new ones are created
        profiles= profileDao.findAll();

        JpaTestUtils.assertIdOrder(
                profiles,
                new Integer[]{1,2,3,4,5,6,7,8}
        );


        metricList = metricDao.findAll();
        Assert.assertNotNull(metricList);
        Assert.assertEquals(15, metricList.size(), 0);



        List<Identity> all = identityDao.findAll();
        JpaTestUtils.assertIdOrder(
                all,
                new Integer[]{
                    1,2,4
                }
        );


        //now check the metrics!!

        Identity identity = all.get(1);

        //check profiles
        /*
        mysql> select * from identity_profile_view where id=2;
        +----+----------------------------------+------------+-------+-----------------+----------+----------------------+--------+-----------+
        | id | uuid                             | profile_id | name  | lastname        | username | email                | source | source_id |
        +----+----------------------------------+------------+-------+-----------------+----------+----------------------+--------+-----------+
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          3 | John  | Steward         | jsmiths  | jsmith@gmail.com     | none   | none      |
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          4 | Fotis | Paraskevopoulos | fotisp   | fotisp@superemail.ex | none   | none      |
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          5 | Fotis | Paraskevopoulos | fotakis  | fotisp@superemail.ex | none   | none      |
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          7 | Fotis | Petroul         | fpetroul | petroulf@hotmail.com | none   | none      |
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          8 | Fotis | Petroul         | petroulf | petroulf@gmail.com   | none   | none      |
        +----+----------------------------------+------------+-------+-----------------+----------+----------------------+--------+-----------+

         */


        Iterator<Profile> iterator = identity.getProfiles().iterator();

        assertProfile(iterator.next(), 4, "Fotis", "Paraskevopoulos", "fotisp", "fotisp@superemail.ex");
        assertProfile(iterator.next(), 7, "Fotis", "Petroul", "fpetroul", "petroulf@hotmail.com");
        assertProfile(iterator.next(), 8, "Fotis", "Petroul", "petroulf", "petroulf@gmail.com");
        assertProfile(iterator.next(), 3, "John", "Steward", "jsmiths", "jsmith@gmail.com");
        assertProfile(iterator.next(), 5, "Fotis", "Paraskevopoulos", "fotakis", "fotisp@superemail.ex");


        
        //check metrics
        /*
        mysql> select * from scm_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        |  2 |       25 |           2 | 2011-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)

        mysql> select * from its_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 14 |       50 |           2 | 2006-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)


        mysql> select * from mailing_list_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 26 |      125 |           2 | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+

        mysql> select * from metric_temporal join metric using(id) where identity_id IN (2,3,5);
        +----+---------------------+-------------+---------------------+
        | id | temporal            | identity_id | created_at          |
        +----+---------------------+-------------+---------------------+
        |  5 | 2010-07-17 00:00:00 |           2 | 2011-06-16 00:00:00 |
        | 17 | 2009-07-17 00:00:00 |           2 | 2000-06-16 00:00:00 |
        | 29 | 2014-07-17 00:00:00 |           2 | 1998-06-16 00:00:00 |
        +----+---------------------+-------------+---------------------+
        3 rows in set (0.00 sec)


         */


        
        this.<ScmActivityMetric>assertQuantitativeMetric(identity, ScmActivityMetric.class, 2, 25);
        this.<ItsActivityMetric>assertQuantitativeMetric(identity, ItsActivityMetric.class, 14, 50);
        this.<MailingListActivityMetric>assertQuantitativeMetric(identity, MailingListActivityMetric.class, 26, 125);


        this.<ScmTemporalMetric>assertTemporalMetric(identity, ScmTemporalMetric.class, 5, "2010-07-17");
        this.<ItsTemporalMetric>assertTemporalMetric(identity, ItsTemporalMetric.class, 17, "2009-07-17");
        this.<MailingListTemporalMetric>assertTemporalMetric(identity, MailingListTemporalMetric.class, 29, "2014-07-17");

    }

    @Test
    public void testMergeCrossSection(){


        //check how many profiles we are working with
        //so that no new ones are created
        List<Profile> profiles= profileDao.findAll();

        JpaTestUtils.assertIdOrder(
                profiles,
                new Integer[]{1,2,3,4,5,6,7,8}
        );



        List<Metric> metricList = metricDao.findAll();
        Assert.assertNotNull(metricList);
        Assert.assertEquals(21, metricList.size(), 0);



        /*

        Before


        mysql> select * from scm_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        |  2 |       15 |           2 | 2011-06-17 00:00:00 |
        |  3 |       10 |           3 | 2011-06-20 00:00:00 |
        +----+----------+-------------+---------------------+

        mysql> select * from its_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 14 |       25 |           2 | 2006-06-17 00:00:00 |
        | 15 |       25 |           3 | 2006-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        2 rows in set (0.01 sec)

        mysql> select * from mailing_list_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 26 |       95 |           2 | 1999-06-17 00:00:00 |
        | 27 |       30 |           3 | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        2 rows in set (0.01 sec)


        mysql> select * from scm_api_introduced_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 29 |       10 |           3 | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 rows in set (0.01 sec)
         */

        identityMergeService.merge(
                2, 3, 5
        );

        /*

        After


        mysql> select * from scm_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        |  2 |       25 |           2 | 2011-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)

        mysql> select * from its_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 14 |       50 |           2 | 2006-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)


        mysql> select * from mailing_list_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 26 |      125 |           2 | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)

        mysql> select * from scm_api_introduced_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 29 |       10 |          2  | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 rows in set (0.01 sec)

         */

        //check how many profiles we are working with
        //so that no new ones are created
        profiles= profileDao.findAll();

        JpaTestUtils.assertIdOrder(
                profiles,
                new Integer[]{1,2,3,4,5,6,7,8}
        );


        metricList = metricDao.findAll();
        Assert.assertNotNull(metricList);
        Assert.assertEquals(15, metricList.size(), 0);



        List<Identity> all = identityDao.findAll();
        JpaTestUtils.assertIdOrder(
                all,
                new Integer[]{
                    1,2,4
                }
        );



        Identity identity = all.get(1);

        //check profiles
        /*
        mysql> select * from identity_profile_view where id=2;
        +----+----------------------------------+------------+-------+-----------------+----------+----------------------+--------+-----------+
        | id | uuid                             | profile_id | name  | lastname        | username | email                | source | source_id |
        +----+----------------------------------+------------+-------+-----------------+----------+----------------------+--------+-----------+
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          3 | John  | Steward         | jsmiths  | jsmith@gmail.com     | none   | none      |
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          4 | Fotis | Paraskevopoulos | fotisp   | fotisp@superemail.ex | none   | none      |
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          5 | Fotis | Paraskevopoulos | fotakis  | fotisp@superemail.ex | none   | none      |
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          7 | Fotis | Petroul         | fpetroul | petroulf@hotmail.com | none   | none      |
        |  2 | 26ab0db90d72e28ad0ba1e22ee510510 |          8 | Fotis | Petroul         | petroulf | petroulf@gmail.com   | none   | none      |
        +----+----------------------------------+------------+-------+-----------------+----------+----------------------+--------+-----------+

         */


        Iterator<Profile> iterator = identity.getProfiles().iterator();

        assertProfile(iterator.next(), 4, "Fotis", "Paraskevopoulos", "fotisp", "fotisp@superemail.ex");
        assertProfile(iterator.next(), 7, "Fotis", "Petroul", "fpetroul", "petroulf@hotmail.com");
        assertProfile(iterator.next(), 8, "Fotis", "Petroul", "petroulf", "petroulf@gmail.com");
        assertProfile(iterator.next(), 3, "John", "Steward", "jsmiths", "jsmith@gmail.com");
        assertProfile(iterator.next(), 5, "Fotis", "Paraskevopoulos", "fotakis", "fotisp@superemail.ex");



        //check metrics
        /*
        mysql> select * from scm_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        |  2 |       25 |           2 | 2011-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)

        mysql> select * from its_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 14 |       50 |           2 | 2006-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 row in set (0.00 sec)


        mysql> select * from mailing_list_activity_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 26 |      125 |           2 | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+

        mysql> select * from scm_api_introduced_metric_view where identity_id IN (2,3,4);
        +----+----------+-------------+---------------------+
        | id | quantity | identity_id | created_at          |
        +----+----------+-------------+---------------------+
        | 29 |       10 |          2  | 1999-06-20 00:00:00 |
        +----+----------+-------------+---------------------+
        1 rows in set (0.01 sec)

        mysql> select * from metric_temporal join metric using(id) where identity_id IN (2,3,5);
        +----+---------------------+-------------+---------------------+
        | id | temporal            | identity_id | created_at          |
        +----+---------------------+-------------+---------------------+
        |  5 | 2010-07-17 00:00:00 |           2 | 2011-06-16 00:00:00 |
        | 17 | 2009-07-17 00:00:00 |           2 | 2000-06-16 00:00:00 |
        | 29 | 2014-07-17 00:00:00 |           2 | 1998-06-16 00:00:00 |
        +----+---------------------+-------------+---------------------+
        3 rows in set (0.00 sec)


         */



        this.<ScmActivityMetric>assertQuantitativeMetric(identity, ScmActivityMetric.class, 2, 25);
        this.<ItsActivityMetric>assertQuantitativeMetric(identity, ItsActivityMetric.class, 14, 50);
        this.<MailingListActivityMetric>assertQuantitativeMetric(identity, MailingListActivityMetric.class, 26, 125);


        this.<ScmTemporalMetric>assertTemporalMetric(identity, ScmTemporalMetric.class, 5, "2010-07-17");
        this.<ItsTemporalMetric>assertTemporalMetric(identity, ItsTemporalMetric.class, 17, "2009-07-17");
        this.<MailingListTemporalMetric>assertTemporalMetric(identity, MailingListTemporalMetric.class, 29, "2014-07-17");



    }


    private void assertProfile(Profile p, Integer id, String name, String lastname, String username, String email){
        
        Assert.assertNotNull(p);
        Assert.assertEquals(id, p.getId(), 0);
        Assert.assertEquals(name, p.getName());
        Assert.assertEquals(lastname, p.getLastname());
        Assert.assertEquals(username, p.getUsername());
        Assert.assertEquals(email, p.getEmail());

    }
    
    private <T extends MetricQuantitative> void  assertQuantitativeMetric(Identity identity, Class<? extends MetricQuantitative> clazz, Integer id, Integer quantity){
        
        List<T> forIdentity = (List<T>) metricDao.getForIdentity(identity, clazz);

        Assert.assertNotNull(forIdentity);
        Assert.assertEquals(1,forIdentity.size(),0);
        MetricQuantitative metric = forIdentity.get(0);
        Assert.assertEquals(id, metric.getId(), 0);
        Assert.assertEquals(quantity,metric.getQuantity());

    }


    private <T extends MetricTemporal> void  assertTemporalMetric(Identity identity, Class<? extends MetricTemporal> clazz, Integer id, String temporal) {

        List<T> forIdentity = (List<T>) metricDao.getForIdentity(identity, clazz);

        Assert.assertNotNull(forIdentity);
        Assert.assertEquals(1,forIdentity.size(),0);

        MetricTemporal metric = forIdentity.get(0);
        Assert.assertEquals(id, metric.getId(), 0);

       Assert.assertEquals(temporal, DateFormatUtils.format(metric.getTemporal(),"yyyy-MM-dd"));

    }

}
