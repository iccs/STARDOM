package eu.alertproject.iccs.stardom.analyzers.scm;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.DefaultScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmFile;
import eu.alertproject.iccs.stardom.analyzers.scm.constructor.ScmActivityAnalyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmActivityMetric;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * User: fotis
 * Date: 28/01/12
 * Time: 12:45
 */

@ContextConfiguration("classpath:testHistoryAnalyzerApplicationContext.xml")
public class ScmActivityHistoryAnalyzerTest extends SpringDbUnitJpaTest{

    private Logger logger = LoggerFactory.getLogger(ScmActivityHistoryAnalyzerTest.class);

    @Autowired
    IdentityDao identityDao;

    @Autowired
    MetricDao metricDao;

    @Autowired
    Analyzer<ScmAction> scmActivityHistoryAnalyzer;
    private int revission=0;

    private final int min=10;
    private final int max=1000000;
    private final int number_of_threads=5;
    private final int number_of_events = 100;
    

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
    public void testHistory() throws ParseException, InterruptedException {


        final CountDownLatch cdl = new CountDownLatch(number_of_threads);


        final Identity[] identities = new Identity[]{
                                            identityDao.findById(1),
                                            identityDao.findById(2)
        };


        Assert.assertNotNull(identities);

        

        Math.random();

        Runnable r = new Runnable(){
            @Override
            public void run() {
                for(int i =0; i<number_of_events; i++ ) {
                    int loc = getNextRandomSeconds(0,1);
                    logger.trace("void run(identity) Sending event {} to {}",i,loc);
                    scmActivityHistoryAnalyzer.analyze(identities[loc],generateRandomAction(getNextRandomSeconds(min,max)));
                }
                cdl.countDown();

            }
        };

        for(int i =0 ; i< number_of_threads; i++){
            new Thread(r,"Thread - #"+i).start();
        }

        
        logger.trace("void testHistory() Waiting");
        cdl.await();


        /**
         * Retrieve the history and order it by date
         * and make sure that the quantity follows order 
         * 
         */

         for(Identity identity : identities){

            List<ScmActivityMetric> scmActivityMetrics = metricDao.getForIdentity(identity, ScmActivityMetric.class);
            Assert.assertNotNull(scmActivityMetrics);
            logger.trace("void testHistory(identities) {} metrics",scmActivityMetrics.size());
            Iterator<ScmActivityMetric> iterator = scmActivityMetrics.iterator();


            //base your loop on the first
            ScmActivityMetric next = iterator.next();
            int previousQuantity = next.getQuantity();
            while(iterator.hasNext()){
                next = iterator.next();

                if(previousQuantity - next.getQuantity() != 1 ){
                    logger.trace("void testHistory(identity) The difference between the metrics is not 1 ({} - {})= ({}))",
                                                                                new Object[]{
                                                                                        previousQuantity,
                                                                                        next.getQuantity(),
                                                                                        previousQuantity - next.getQuantity()});
                    Assert.fail();
                }
                previousQuantity = next.getQuantity();
            }
        }

    }

    @Test
    public void testHistorySameDate() throws ParseException {

        Identity identity = identityDao.findById(1);
        Assert.assertNotNull(identity);

        Date date = DateUtils.parseDate("2000-01-01",new String[]{"yyyy-MM-dd"});

        scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is the first ", date, "1001"));
        scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is the second", date, "0099"));
        scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is the third", date, "0098"));

        ScmActivityMetric mostRecentMetric = metricDao.getMostRecentMetric(identity, ScmActivityMetric.class);
        assertMetric(mostRecentMetric, 3, "2000-01-01");

        List<ScmActivityMetric> forIdentity = metricDao.getForIdentity(identity, ScmActivityMetric.class);
        Assert.assertNotNull(forIdentity);
        Assert.assertEquals(3, forIdentity.size(), 0);


        Iterator<ScmActivityMetric> iterator = forIdentity.iterator();

        assertMetric(iterator.next(), 3,"2000-01-01");
        assertMetric(iterator.next(), 2,"2000-01-01");
        assertMetric(iterator.next(), 1,"2000-01-01");

        //now add one after
        date = DateUtils.parseDate("2000-01-02",new String[]{"yyyy-MM-dd"});
        scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is fourth",date,"1002"));
        List<ScmActivityMetric> metricList = metricDao.getForIdentity(identity, ScmActivityMetric.class);
        assertMetric(metricList.iterator().next(), 4, "2000-01-02");

        mostRecentMetric = metricDao.getMostRecentMetric(identity, ScmActivityMetric.class);
        assertMetric(mostRecentMetric, 4, "2000-01-02");

    }

    @Test
    public void testHistoryStatic() throws ParseException, InterruptedException {
        
        final Identity identity = identityDao.findById(1);
        Assert.assertNotNull(identity); 
        
        final CountDownLatch cdl = new CountDownLatch(3);
        
        Thread t1 = new Thread(){
            @Override
            public void run() {


                try {
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 235","2012-02-02 00:25:55","1"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 155","2012-02-02 03:09:50","2"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 26","2012-02-02 03:26:49","3"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 338","2012-02-02 04:39:34","4"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 277","2012-02-02 04:40:20","5"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 473","2012-02-02 05:06:32","6"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 264","2012-02-02 05:17:27","7"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 423","2012-02-02 06:20:17","8"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 226","2012-02-02 07:39:41","9"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 114","2012-02-02 08:25:58","10"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 283","2012-02-02 11:19:42","11"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 160","2012-02-02 12:12:03","12"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 334","2012-02-02 12:24:06","13"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 165","2012-02-02 12:25:41","14"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 263","2012-02-02 13:26:55","15"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 6","2012-02-02 14:04:37","16"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 278","2012-02-02 14:16:05","17"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 61","2012-02-02 16:26:57","18"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 196","2012-02-02 16:32:02","19"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 365","2012-02-02 17:59:16","20"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 180","2012-02-02 18:29:18","21"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 438","2012-02-02 19:31:01","22"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 443","2012-02-02 20:58:28","23"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 312","2012-02-02 22:20:25","24"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 411","2012-02-02 23:12:28","25"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 290","2012-02-02 23:21:38","26"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 172","2012-02-02 23:27:19","27"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 466","2012-02-03 01:18:04","26"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 41","2012-02-03 01:42:28","28"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 88","2012-02-03 02:37:57","29"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 375","2012-02-03 02:45:02","30"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 84","2012-02-03 03:06:17","31"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 325","2012-02-03 05:08:29","32"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 140","2012-02-03 07:50:14","33"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 350","2012-02-03 09:24:53","34"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 381","2012-02-03 09:46:34","35"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 11","2012-02-03 10:02:49","36"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 446","2012-02-03 10:08:12","37"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 111","2012-02-03 11:04:23","38"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 433","2012-02-03 11:09:18","39"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 151","2012-02-03 12:02:48","40"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 94","2012-02-03 12:57:53","41"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 54","2012-02-03 13:01:48","42"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 231","2012-02-03 14:41:20","43"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 424","2012-02-03 16:13:57","44"));
                    scmActivityHistoryAnalyzer.analyze(identity,generateAction("This is id 351","2012-02-03 17:46:18","45"));
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }finally {
                    cdl.countDown();
                }


            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {


                try {

                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 408", "2012-02-03 17:49:01", "46"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 173", "2012-02-03 19:06:59", "47"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 467", "2012-02-03 20:21:49", "48"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 332", "2012-02-03 22:03:12", "49"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 182", "2012-02-04 04:13:35", "50"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 432", "2012-02-04 05:21:24", "51"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 261", "2012-02-04 05:46:29", "52"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 136", "2012-02-04 06:48:55", "53"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 425", "2012-02-04 07:40:46", "54"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 300", "2012-02-04 08:02:52", "55"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 491", "2012-02-04 08:24:48", "56"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 49", "2012-02-04 08:32:54", "57"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 234", "2012-02-04 09:07:26", "58"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 239", "2012-02-04 09:26:42", "59"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 241", "2012-02-04 10:13:17", "60"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 252", "2012-02-04 10:58:47", "61"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 52", "2012-02-04 11:54:00", "62"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 383", "2012-02-04 12:50:55", "63"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 397", "2012-02-04 13:43:45", "64"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 19", "2012-02-04 13:46:05", "65"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 14", "2012-02-04 16:55:32", "66"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 68", "2012-02-04 17:29:47", "67"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 58", "2012-02-04 18:36:56", "68"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 230", "2012-02-04 18:44:50", "69"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 494", "2012-02-04 20:33:46", "69"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 200", "2012-02-04 21:00:06", "70"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 336", "2012-02-05 00:16:02", "71"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 474", "2012-02-05 01:03:20", "72"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 273", "2012-02-05 03:02:43", "73"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 382", "2012-02-05 03:55:32", "74"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 97", "2012-02-05 04:07:37", "75"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 435", "2012-02-05 04:46:33", "76"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 221", "2012-02-05 07:29:43", "77"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 309", "2012-02-05 08:48:21", "78"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 185", "2012-02-05 09:22:01", "79"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 176", "2012-02-05 10:14:40", "80"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 288", "2012-02-05 10:54:38", "81"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 357", "2012-02-05 11:30:52", "82"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 449", "2012-02-05 11:32:50", "83"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 243", "2012-02-05 12:53:46", "84"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 306", "2012-02-05 13:07:59", "85"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 64", "2012-02-05 13:51:38", "86"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 90", "2012-02-05 13:59:42", "87"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 287", "2012-02-05 16:53:44", "88"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 327", "2012-02-05 20:55:03", "89"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 198", "2012-02-05 22:24:07", "90"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 462", "2012-02-05 23:16:05", "91"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 169", "2012-02-05 23:21:33", "92"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 414", "2012-02-06 01:57:01", "93"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 497", "2012-02-06 03:41:07", "94"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 121", "2012-02-06 04:00:32", "95"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 51", "2012-02-06 06:16:36", "96"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 218", "2012-02-06 07:01:25", "97"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 406", "2012-02-06 08:08:29", "98"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 1", "2012-02-06 08:29:01", "99"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 163", "2012-02-06 08:43:28", "100"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 476", "2012-02-06 10:00:03", "101"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 498", "2012-02-06 10:04:26", "101"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 109", "2012-02-06 10:08:59", "102"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 489", "2012-02-06 10:44:51", "103"));
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } finally {
                    cdl.countDown();
                }
            }
        };

        Thread t3 = new Thread() {
            @Override
            public void run() {
                try {
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 464", "2012-02-06 12:12:25", "104"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 468", "2012-02-06 13:58:27", "105"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 477", "2012-02-06 14:01:24", "106"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 181", "2012-02-06 14:49:20", "107"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 36", "2012-02-06 15:29:32", "108"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 403", "2012-02-06 16:21:41", "109"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 149", "2012-02-06 16:44:00", "110"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 331", "2012-02-06 17:37:51", "111"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 194", "2012-02-06 17:45:29", "112"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 106", "2012-02-06 18:23:58", "113"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 299", "2012-02-06 18:30:04", "114"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 22", "2012-02-06 22:35:53", "115"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 266", "2012-02-06 23:28:40", "116"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 370", "2012-02-07 00:37:05", "117"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 436", "2012-02-07 00:49:59", "118"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 13", "2012-02-07 01:03:02", "119"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 242", "2012-02-07 01:07:32", "120"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 361", "2012-02-07 02:54:11", "121"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 138", "2012-02-07 04:12:26", "122"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 480", "2012-02-07 04:15:55", "123"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 297", "2012-02-07 05:02:51", "124"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 219", "2012-02-07 05:32:27", "125"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 301", "2012-02-07 07:19:58", "126"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 247", "2012-02-07 09:07:10", "127"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 45", "2012-02-07 09:23:21", "128"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 133", "2012-02-07 09:27:02", "129"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 293", "2012-02-07 09:42:04", "130"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 227", "2012-02-07 10:21:26", "131"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 457", "2012-02-07 10:38:08", "132"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 354", "2012-02-07 10:40:08", "133"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 387", "2012-02-07 15:18:12", "134"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 482", "2012-02-07 15:36:41", "135"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 63", "2012-02-07 18:12:39", "136"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 17", "2012-02-07 20:57:55", "137"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 374", "2012-02-07 21:56:40", "138"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 378", "2012-02-07 22:46:02", "139"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 369", "2012-02-07 23:34:38", "140"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 268", "2012-02-07 23:44:41", "141"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 215", "2012-02-08 01:52:21", "142"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 413", "2012-02-08 03:16:50", "143"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 426", "2012-02-08 03:34:22", "144"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 418", "2012-02-08 03:35:24", "145"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 124", "2012-02-08 04:56:34", "146"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 260", "2012-02-08 05:02:29", "147"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 379", "2012-02-08 06:41:54", "148"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 148", "2012-02-08 07:06:02", "149"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 376", "2012-02-08 07:55:20", "150"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 214", "2012-02-08 08:01:17", "151"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 192", "2012-02-08 08:58:51", "152"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 28", "2012-02-08 10:04:39", "153"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 119", "2012-02-08 11:17:50", "154"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 265", "2012-02-08 11:57:12", "155"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 70", "2012-02-08 12:18:49", "156"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 8", "2012-02-08 15:51:50", "157"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 390", "2012-02-08 16:18:58", "158"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 87", "2012-02-08 16:28:49", "159"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 195", "2012-02-08 16:32:39", "160"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 431", "2012-02-08 18:25:27", "161"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 179", "2012-02-08 21:00:43", "162"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 86", "2012-02-08 21:02:22", "163"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 20", "2012-02-08 21:30:55", "164"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 353", "2012-02-08 22:58:08", "165"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 330", "2012-02-08 23:18:26", "166"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 488", "2012-02-09 02:30:31", "167"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 16", "2012-02-09 03:01:54", "168"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 372", "2012-02-09 03:27:34", "169"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 135", "2012-02-09 04:12:23", "170"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 113", "2012-02-09 04:22:10", "171"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 62", "2012-02-09 09:39:03", "172"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 199", "2012-02-09 11:05:18", "173"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 184", "2012-02-09 11:22:51", "174"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 134", "2012-02-09 12:03:02", "175"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 359", "2012-02-09 18:21:42", "176"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 305", "2012-02-09 19:03:38", "177"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 347", "2012-02-09 19:17:28", "178"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 311", "2012-02-09 19:50:58", "179"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 472", "2012-02-09 20:20:32", "180"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 107", "2012-02-09 22:17:19", "181"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 389", "2012-02-09 22:22:06", "182"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 280", "2012-02-10 00:14:58", "183"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 167", "2012-02-10 02:37:23", "184"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 118", "2012-02-10 06:55:07", "185"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 416", "2012-02-10 09:29:57", "186"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 333", "2012-02-10 11:30:32", "187"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 83", "2012-02-10 12:07:54", "188"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 355", "2012-02-10 13:08:43", "189"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 153", "2012-02-10 13:42:56", "190"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 251", "2012-02-10 18:46:11", "191"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 142", "2012-02-10 22:06:06", "192"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 384", "2012-02-10 22:17:10", "193"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 422", "2012-02-10 22:42:47", "194"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 157", "2012-02-10 22:59:04", "195"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 91", "2012-02-11 00:21:03", "196"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 166", "2012-02-11 03:02:32", "197"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 358", "2012-02-11 06:07:00", "198"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 285", "2012-02-11 08:14:46", "199"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 201", "2012-02-11 09:08:44", "200"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 373", "2012-02-11 10:44:13", "201"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 37", "2012-02-11 12:05:08", "202"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 71", "2012-02-11 12:30:02", "203"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 132", "2012-02-11 13:43:42", "204"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 272", "2012-02-11 14:36:12", "205"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 9", "2012-02-11 16:20:53", "206"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 262", "2012-02-11 19:12:53", "207"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 32", "2012-02-11 21:10:47", "208"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 279", "2012-02-11 21:21:29", "209"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 412", "2012-02-11 21:37:22", "210"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 35", "2012-02-11 22:41:03", "211"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 421", "2012-02-11 22:43:22", "212"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 207", "2012-02-11 23:46:07", "213"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 394", "2012-02-12 00:43:14", "214"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 342", "2012-02-12 01:30:55", "215"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 74", "2012-02-12 01:45:17", "216"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 314", "2012-02-12 01:57:58", "217"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 103", "2012-02-12 02:50:54", "218"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 21", "2012-02-12 03:13:11", "219"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 269", "2012-02-12 04:59:05", "220"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 33", "2012-02-12 07:11:08", "221"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 188", "2012-02-12 10:57:23", "222"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 386", "2012-02-12 11:41:26", "223"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 348", "2012-02-12 12:50:27", "224"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 250", "2012-02-12 14:35:49", "225"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 490", "2012-02-12 15:05:13", "226"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 145", "2012-02-12 15:25:10", "227"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 393", "2012-02-12 17:29:45", "228"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 77", "2012-02-12 17:40:38", "229"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 323", "2012-02-12 18:56:55", "230"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 274", "2012-02-12 20:41:16", "231"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 53", "2012-02-13 00:03:22", "232"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 222", "2012-02-13 00:14:48", "233"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 59", "2012-02-13 04:28:55", "234"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 197", "2012-02-13 05:45:09", "235"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 128", "2012-02-13 05:56:11", "236"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 465", "2012-02-13 07:51:23", "237"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 363", "2012-02-13 09:16:35", "238"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 319", "2012-02-13 10:54:24", "239"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 362", "2012-02-13 11:01:03", "240"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 452", "2012-02-13 11:03:35", "241"));
                    scmActivityHistoryAnalyzer.analyze(identity, generateAction("This is id 228", "2012-02-13 13:22:48", "242"));
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } finally {
                    cdl.countDown();
                }
            }
        };

        t1.start();
        t2.start();
        t3.start();

        cdl.await();

        ScmActivityMetric mostRecentMetric = metricDao.getMostRecentMetric(identity, ScmActivityMetric.class);
        Assert.assertNotNull(mostRecentMetric);
        Assert.assertEquals(245, mostRecentMetric.getQuantity(),0);

        List<ScmActivityMetric> forIdentity = metricDao.getForIdentity(identity, ScmActivityMetric.class);
        Assert.assertEquals(245, forIdentity.size(),0);

        Iterator<ScmActivityMetric> iterator = forIdentity.iterator();
        for(int i =245; i >= 1; i--){
            Assert.assertEquals(i, iterator.next().getQuantity(),0);
        }


    }


    private void assertMetric(ScmActivityMetric next, int quantity, String date) throws ParseException {

        Assert.assertEquals(DateUtils.parseDate(date, new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"}), next.getCreatedAt());
        Assert.assertEquals(quantity,next.getQuantity(),0);

    }

    

    private DefaultScmAction generateAction(String comment, String date, String revission) throws ParseException {
            
        return generateAction(comment,DateUtils.parseDate(date,new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"}),revission);
    }
    
    
    private DefaultScmAction generateAction(String comment, Date date, String revission){
        DefaultScmAction as = new DefaultScmAction();

        as.setComment(comment);
        as.setDate(date);
        as.setType(ScmAction.RepositoryType.Svn);
        as.setUid(DigestUtils.md5Hex(date.toString()));
        as.setRevission(revission);
        as.setFiles(new ArrayList<ScmFile>());

        return as;
    }
    
    private synchronized DefaultScmAction generateRandomAction(int seconds){
        
        Date d = new Date();
        Date date = DateUtils.addSeconds(d, seconds);
        
        return generateAction("Action with "+seconds+" in the future", date, StringUtils.leftPad(String.valueOf(revission),4,"0"));



    }

           
    private synchronized int getNextRandomSeconds(int min, int max){
        
        return min + (int)(Math.random() * ((max- min) + 1));
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
