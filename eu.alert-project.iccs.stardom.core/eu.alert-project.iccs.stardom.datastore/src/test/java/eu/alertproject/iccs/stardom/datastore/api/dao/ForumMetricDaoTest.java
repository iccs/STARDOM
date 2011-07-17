package eu.alertproject.iccs.stardom.datastore.api.dao;

import eu.alertproject.iccs.stardom.domain.api.metrics.ForumActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ForumTemporalMetric;
import org.joda.time.MutableDateTime;
import org.junit.Test;

/**
 * User: fotis
 * Date: 17/07/11
 * Time: 00:55
 */
public class ForumMetricDaoTest extends MetricDaoTest{



    @Test
    public void findForumActivityMetric(){
        this.<ForumActivityMetric>assertQuantitativeMetric(2, ForumActivityMetric.class, 15, "2007-06-17");
    }

    @Test
    public void noFindForumActivityMetric(){
         this.<ForumActivityMetric>noFindMetric(4,ForumActivityMetric.class);
    }

    @Test
    public void insertForumActivitMetric() {

        ForumActivityMetric fam = new ForumActivityMetric();
        fam.setQuantity(77);

        this.<ForumActivityMetric>assertInsertQuantitativeMetric(
                4,
                fam,
                ForumActivityMetric.class,
                77
        );
    }

    @Test
    public void updateForumActivityMetric() {

        this.<ForumActivityMetric>assertUpdateQuantitativeMetric(3,
                ForumActivityMetric.class,
                10
        );

    }

    @Test
    public void findForumTemporalMetric() {
        this.<ForumTemporalMetric>assertTemporalMetric(2, ForumTemporalMetric.class, "2009-07-17", "2010-06-16");
    }

    @Test
    public void noFindForumTemporalMetric() {
        this.<ForumTemporalMetric>noFindMetric(4, ForumTemporalMetric.class);
    }


    @Test
    public void insertForumTemporalMetric() {

        MutableDateTime mdt = new MutableDateTime();
        mdt.setYear(2000);


        ForumTemporalMetric stm = new ForumTemporalMetric();
        stm.setTemporal(mdt.toDate());

        this.<ForumTemporalMetric>assertInsertTemporalMetric(
                4,
                stm,
                ForumTemporalMetric.class,
                mdt.toDate()
        );

    }

    @Test
    public void updateForumTemporalMetric() {

        MutableDateTime mdt = new MutableDateTime();
        mdt.setYear(2000);

        this.<ForumTemporalMetric>assertUpdateTemporalMetric(
                3,
                ForumTemporalMetric.class,
                mdt.toDate()
        );
    }

}
