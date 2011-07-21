package eu.alertproject.iccs.stardom.analyzers.its.internal;


import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: fotis
 * Date: 20/07/11
 * Time: 20:16
 */
public class ProfileFromItsKdeWhoServiceTest {


    private ProfileFromItsKdeWhoService service;

    @Before
    public void init(){

        service = new ProfileFromItsKdeWhoService();

    }


    @Test
    public void testSimpleName(){

        Profile generate = service.generate("Fotis Paras", "fotisp mail ntua gr");

        Assert.assertNotNull(generate);
        Assert.assertEquals("Fotis",generate.getName());
        Assert.assertEquals("Paras",generate.getLastname());
        Assert.assertEquals("fotisp mail ntua gr",generate.getEmail());



    }



}
