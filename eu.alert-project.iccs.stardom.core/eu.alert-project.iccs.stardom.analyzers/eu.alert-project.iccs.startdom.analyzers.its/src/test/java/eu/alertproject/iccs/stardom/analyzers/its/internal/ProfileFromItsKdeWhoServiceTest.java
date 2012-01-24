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

        Profile p  = new Profile();
        p.setName("Fotis Paras");
        p.setEmail("fotisp mail ntua gr");
        Profile generate = service.generate(p);

        Assert.assertNotNull(generate);
        Assert.assertEquals("Fotis",generate.getName());
        Assert.assertEquals("Paras",generate.getLastname());
        Assert.assertEquals("fotisp mail ntua gr",generate.getEmail());

    }


    @Test
    public void testNone(){

        Profile p = new Profile();
        p.setName("None");
        p.setLastname("None");
        p.setUsername("None");
        p.setEmail("None");
        p.setSource("None");
        p.setSourceId("None");

        Profile generate = service.generate(p);

        Assert.assertNull(generate.getName());
        Assert.assertNull(generate.getLastname());
        Assert.assertNull(generate.getUsername());
        Assert.assertNull(generate.getEmail());



    }

}
