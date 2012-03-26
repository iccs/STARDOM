package eu.alertproject.iccs.stardom.datastore.api.dao;

import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmApiIntroducedMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmApiUsageCountMetric;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmTemporalMetric;
import org.joda.time.MutableDateTime;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * User: fotis
 * Date: 17/07/11
 * Time: 00:45
 */
public class ScmMetricDaoTest extends MetricDaoTest {

    @Test
    public void findScmActivityMetric() {
        this.<ScmActivityMetric>assertQuantitativeMetric(2, ScmActivityMetric.class, 15, "2011-06-17");
    }

    @Test
    public void noFindScmActivityMetric() {
        this.<ScmActivityMetric>noFindMetric(4, ScmActivityMetric.class);
    }

    @Test
    public void insertScmActivitMetric() {

        ScmActivityMetric sam = new ScmActivityMetric();
        sam.setQuantity(15);

        this.<ScmActivityMetric>assertInsertQuantitativeMetric(
                4,
                sam,
                ScmActivityMetric.class,
                15
        );
    }

    @Test
    public void updateScmActivityMetric() {

        this.<ScmActivityMetric>assertUpdateQuantitativeMetric(3,
                ScmActivityMetric.class,
                15
        );

    }


    @Test
    public void findScmTemporalMetric() {
        this.<ScmTemporalMetric>assertTemporalMetric(2, ScmTemporalMetric.class, "2010-07-17", "2011-06-16");
    }

    @Test
    public void noFindScmTemporalMetric() {
        this.<ScmTemporalMetric>noFindMetric(4, ScmTemporalMetric.class);
    }

    @Test
    public void insertScmTemporalMetric() {

        MutableDateTime mdt = new MutableDateTime();
        mdt.setYear(2000);


        ScmTemporalMetric stm = new ScmTemporalMetric();
        stm.setTemporal(mdt.toDate());

        this.<ScmTemporalMetric>assertInsertTemporalMetric(
                4,
                stm,
                ScmTemporalMetric.class,
                mdt.toDate()
        );

    }

    @Test
    public void updateScmTemporalMetric() {

        MutableDateTime mdt = new MutableDateTime();
        mdt.setYear(2000);

        this.<ScmTemporalMetric>assertUpdateTemporalMetric(
                3,
                ScmTemporalMetric.class,
                mdt.toDate()
        );
    }


    @Test
    public void findScmApiIntroducedMetric(){
       this.<ScmApiIntroducedMetric>assertTemporalMetric(
               3,
               ScmApiIntroducedMetric.class,
               "2002-06-20",
               "1998-06-16");
    }

    @Test
    public void noFindScmApiIntroducedMetric(){
        this.<ScmApiIntroducedMetric>noFindMetric(
                2,
                ScmApiIntroducedMetric.class);
    }

    @Test
    public void insertScmApiIntroducedMetric() {

        ScmApiIntroducedMetric sim = new ScmApiIntroducedMetric();
        sim.setAmount(11);
        Date temporal = new Date();
        sim.setTemporal(temporal);
        this.<ScmApiIntroducedMetric>assertInsertTemporalMetric(
                4,
                sim,
                ScmApiIntroducedMetric.class,
                temporal
                );
    }

    @Test
    public void updateScmApiIntroducedMetric() {

        this.<ScmActivityMetric>assertUpdateQuantitativeMetric(3,
                ScmActivityMetric.class,
                15
        );

    }

    @Test
    public void findScmApiUsageCountMetric(){
       this.<ScmApiUsageCountMetric>assertQuantitativeMetric(
               2,
               ScmApiUsageCountMetric.class,
               35,
               "2001-06-17");
    }

    @Test
    public void noFindScmApiUsageCountMetric(){
        this.<ScmApiUsageCountMetric>noFindMetric(
                4,
                ScmApiUsageCountMetric.class);
    }

    @Test
    public void insertScmApiUsageCountMetric() {

        ScmApiUsageCountMetric sim = new ScmApiUsageCountMetric();
        sim.setQuantity(99);
        this.<ScmApiUsageCountMetric>assertInsertQuantitativeMetric(
                4,
                sim,
                ScmApiUsageCountMetric.class,
                99
        );
    }

    @Test
    public void updateScmApiUsageCountMetric() {

        this.<ScmApiUsageCountMetric>assertUpdateQuantitativeMetric(3,
                ScmApiUsageCountMetric.class,
                88
        );

    }


    @Test
    public void findAfterDate() throws ParseException {

        this.assertAfterDate(1, ScmActivityMetric.class);

    }

}
