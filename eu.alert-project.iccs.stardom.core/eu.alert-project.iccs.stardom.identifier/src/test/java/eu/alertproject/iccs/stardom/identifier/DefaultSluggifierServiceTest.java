package eu.alertproject.iccs.stardom.identifier;

import eu.alertproject.iccs.stardom.identifier.api.SluggifierService;
import eu.alertproject.iccs.stardom.identifier.internal.DefaultSluggifierService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: fotis
 * Date: 22/07/11
 * Time: 18:07
 */
public class DefaultSluggifierServiceTest {

    private SluggifierService defaultSluggifier;

    @Before
    public void init(){
        defaultSluggifier = new DefaultSluggifierService();
    }

    @Test
    public void test(){

        Assert.assertEquals(
            defaultSluggifier.sluggify("f.a@s.d.co"),"f a s d co");

        Assert.assertEquals(
            defaultSluggifier.sluggify("f.b-a_a@s.d.co"),"f b a a s d co");

    }

    @Test
    public void assertTrim(){
        Assert.assertEquals(
            defaultSluggifier.sluggify("  f.a@s.d.co  "),"f a s d co"
        );


    }
}
