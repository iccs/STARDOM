package eu.alertproject.iccs.stardom.datastore.api.dao;

import eu.alertproject.iccs.stardom.domain.api.metrics.ItsActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ItsTemporalMetric;
import org.joda.time.MutableDateTime;
import org.junit.Test;

/**
 * User: fotis
 * Date: 17/07/11
 * Time: 00:56
 */
public class ItsMetricDaoTest extends MetricDaoTest {

    @Test
    public void findItsActivityMetric() {
        this.<ItsActivityMetric>assertQuantitativeMetric(2, ItsActivityMetric.class, 25, "2006-06-17");
    }

    @Test
    public void noFindItsActivityMetric() {
        this.<ItsActivityMetric>noFindMetric(4, ItsActivityMetric.class);
    }

    @Test
    public void insertItsActivitMetric() {

        ItsActivityMetric fam = new ItsActivityMetric();
        fam.setQuantity(88);

        this.<ItsActivityMetric>assertInsertQuantitativeMetric(
                4,
                fam,
                ItsActivityMetric.class,
                88
        );
    }

    @Test
    public void updateItsActivityMetric() {

        this.<ItsActivityMetric>assertUpdateQuantitativeMetric(
                3,
                ItsActivityMetric.class,
                10
        );

    }


    @Test
    public void findItsTemporalMetric() {
        this.<ItsTemporalMetric>assertTemporalMetric(2, ItsTemporalMetric.class, "2009-07-17", "2000-06-16");
    }

    @Test
    public void noFindItsTemporalMetric() {
        this.<ItsTemporalMetric>noFindMetric(4, ItsTemporalMetric.class);
    }


    @Test
    public void insertForumTemporalMetric() {

        MutableDateTime mdt = new MutableDateTime();
        mdt.setYear(2000);


        ItsTemporalMetric stm = new ItsTemporalMetric();
        stm.setTemporal(mdt.toDate());

        this.<ItsTemporalMetric>assertInsertTemporalMetric(
                4,
                stm,
                ItsTemporalMetric.class,
                mdt.toDate()
        );

    }

    @Test
    public void updateItsTemporalMetric() {

        MutableDateTime mdt = new MutableDateTime();
        mdt.setYear(2000);

        this.<ItsTemporalMetric>assertUpdateTemporalMetric(
                3,
                ItsTemporalMetric.class,
                mdt.toDate()
        );
    }
}
