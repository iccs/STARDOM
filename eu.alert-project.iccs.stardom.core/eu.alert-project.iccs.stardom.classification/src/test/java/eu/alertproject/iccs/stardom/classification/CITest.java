package eu.alertproject.iccs.stardom.classification;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

/**
 * User: fotis
 * Date: 01/04/12
 * Time: 14:01
 */
public class CITest {

    @Test
    public void deserialize() throws IOException {

        XStream x = new XStream();
        x.processAnnotations(CI.class);
        CI o = (CI) x.fromXML(IOUtils.toString(CITest.class.getResourceAsStream("/description.xml")));
        Assert.assertNotNull(o);
        Assert.assertEquals(3, o.getClassifiers().size(),0);

        
        String[] mls = new String[]{
               "ForumActivityMetric",
                "ForumTemporalMetric",
                "ItsActivityMetric",
                "ItsIssuesResolvedMetric",
                "ItsTemporalMetric",
                "MailingListActivityMetric",
                "MailingListTemporalMetric",
                "ScmActivityMetric",
                "ScmApiIntroducedMetric",
                "ScmApiUsageCountMetric",
                "ScmTemporalMetric"
        };
        
        Iterator<CI.Classifier> iterator = o.getClassifiers().iterator();

        CI.Classifier cd = iterator.next();
        Assert.assertEquals("core developers", cd.getName());
        Assert.assertEquals(11,cd.getMetrics().size(),0);
        assertNames(mls,cd.getMetrics().iterator());
        Assert.assertEquals(0.1234,cd.getMetrics().get(6).getStandardDeviation(),0.0);
        Assert.assertEquals(0.0002,cd.getMetrics().get(6).getMean(),0.0);


        CI.Classifier tr = iterator.next();
        Assert.assertEquals("testers", tr.getName());
        Assert.assertEquals(11,tr.getMetrics().size(),0);
        assertNames(mls,tr.getMetrics().iterator());
        Assert.assertEquals(0.5683,tr.getMetrics().get(4).getStandardDeviation(),0.0);
        Assert.assertEquals(0.0004,tr.getMetrics().get(4).getMean(),0.0);


        CI.Classifier bt = iterator.next();
        Assert.assertEquals("bug triagers", bt.getName());
        Assert.assertEquals(11,bt.getMetrics().size(),0);
        assertNames(mls,bt.getMetrics().iterator());
        Assert.assertEquals(0.0005895,bt.getMetrics().get(10).getStandardDeviation(),0.0);
        Assert.assertEquals(0.2547890,bt.getMetrics().get(10).getMean(),0.0);



    }
    
    private void assertNames(String[] mls, Iterator<CI.Classifier.Metric> metricIterator){
        
        for(String s : mls){
            
            Assert.assertEquals(
                s,
                metricIterator.next().getName()
            );
            
        }
    }

}
