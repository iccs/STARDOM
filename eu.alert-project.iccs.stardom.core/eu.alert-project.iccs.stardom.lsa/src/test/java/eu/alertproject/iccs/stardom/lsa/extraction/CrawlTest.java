/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.stardom.lsa.extraction;

import org.junit.Assert;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kostas Christidis
 */
public class CrawlTest {
    
    public CrawlTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void testBugzillaCrawling() throws Exception {
             
        
        ArrayList<String> bugIds= new ArrayList<String>();
        bugIds.add("20627");
        
        CrawlConnector crawlConnector = new CrawlConnector(bugIds);
        Assert.assertNotNull(crawlConnector);
        crawlConnector.crawl();
        ArrayList<String> bugdescs= crawlConnector.getBugDescs();
        Assert.assertNotNull(bugdescs);
        
     }
}
