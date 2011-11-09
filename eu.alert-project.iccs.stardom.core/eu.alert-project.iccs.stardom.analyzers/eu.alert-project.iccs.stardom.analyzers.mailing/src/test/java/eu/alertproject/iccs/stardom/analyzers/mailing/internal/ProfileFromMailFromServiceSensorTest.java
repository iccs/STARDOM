package eu.alertproject.iccs.stardom.analyzers.mailing.internal;


import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 17:42
 */
public class ProfileFromMailFromServiceSensorTest {

    private ProfileFromMailFromServiceSensor profileFromMailFromService;



    @Before
    public void init(){
        profileFromMailFromService = new ProfileFromMailFromServiceSensor();
    }


    @Test
    public void getString(){


        Profile profileQuote = profileFromMailFromService.generate(
                "lamarque at gmail.com (Lamarque Vieira Souza)"
        );
        Assert.assertEquals(profileQuote.getName(),"Lamarque");
        Assert.assertEquals(profileQuote.getLastname(),"Vieira Souza");
        Assert.assertEquals(profileQuote.getEmail(),"lamarque gmail.com");



        Profile withQuotes = profileFromMailFromService.generate(
                "teg at jklm.no (Tom Gundersen)"
        );
        Assert.assertEquals(withQuotes.getName(),"Tom");
        Assert.assertEquals(withQuotes.getLastname(),"Gundersen");
        Assert.assertEquals(withQuotes.getEmail(),"teg jklm.no");


        Profile withUtf = profileFromMailFromService.generate(
                "kossebau at kde.org (Friedrich W. H. Kossebau)"
        );
        Assert.assertEquals(withUtf.getName(),"Friedrich");
        Assert.assertEquals(withUtf.getLastname(),"W. H. Kossebau");
        Assert.assertEquals(withUtf.getEmail(),"kossebau kde.org");


        Profile withUTFNullResult = profileFromMailFromService.generate(
                "=?UTF-8?B?UmFmYcWCIE1pxYJlY2tp?= <zajec5@gmail.com>"
        );
        Assert.assertNull(withUTFNullResult);

    }

}
