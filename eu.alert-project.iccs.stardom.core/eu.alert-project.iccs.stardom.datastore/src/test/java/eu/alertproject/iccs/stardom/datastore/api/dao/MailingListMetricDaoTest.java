package eu.alertproject.iccs.stardom.datastore.api.dao;

import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.MailingListTemporalMetric;
import org.joda.time.MutableDateTime;
import org.junit.Test;

/**
 * User: fotis
 * Date: 17/07/11
 * Time: 00:55
 */
public class MailingListMetricDaoTest extends MetricDaoTest{



    @Test
    public void findMailingActivityMetric(){
        this.<MailingListActivityMetric>assertQuantitativeMetric(2, MailingListActivityMetric.class, 95, "1999-06-17");
    }

    @Test
    public void noFindMailingActivityMetric(){
         this.<MailingListActivityMetric>noFindMetric(4,MailingListActivityMetric.class);
    }

    @Test
    public void insertMailingListActivitMetric() {

        MailingListActivityMetric fam = new MailingListActivityMetric();
        fam.setQuantity(77);

        this.<MailingListActivityMetric>assertInsertQuantitativeMetric(
                4,
                fam,
                MailingListActivityMetric.class,
                77
        );
    }

    @Test
    public void updateMailingListActivityMetric() {

        this.<MailingListActivityMetric>assertUpdateQuantitativeMetric(3,
                MailingListActivityMetric.class,
                10
        );

    }

    @Test
    public void findMailingListTemporalMetric() {
        this.<MailingListTemporalMetric>assertTemporalMetric(2, MailingListTemporalMetric.class, "2014-07-17", "1998-06-16");
    }

    @Test
    public void noFindMailingListTemporalMetric() {
        this.<MailingListTemporalMetric>noFindMetric(4, MailingListTemporalMetric.class);
    }


    @Test
    public void insertMailingListTemporalMetric() {

        MutableDateTime mdt = new MutableDateTime();
        mdt.setYear(1998);


        MailingListTemporalMetric stm = new MailingListTemporalMetric();
        stm.setTemporal(mdt.toDate());

        this.<MailingListTemporalMetric>assertInsertTemporalMetric(
                4,
                stm,
                MailingListTemporalMetric.class,
                mdt.toDate()
        );

    }

    @Test
    public void updateMailingListTemporalMetric() {

        MutableDateTime mdt = new MutableDateTime();
        mdt.setYear(1998);

        this.<MailingListTemporalMetric>assertUpdateTemporalMetric(
                3,
                MailingListTemporalMetric.class,
                mdt.toDate()
        );
    }

}
