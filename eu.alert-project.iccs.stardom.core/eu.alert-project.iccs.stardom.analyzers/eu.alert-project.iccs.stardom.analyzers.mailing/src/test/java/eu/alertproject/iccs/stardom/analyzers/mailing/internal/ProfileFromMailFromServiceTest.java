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
public class ProfileFromMailFromServiceTest {

    private ProfileFromMailFromServiceWs profileFromMailFromService;



    @Before
    public void init(){
        profileFromMailFromService = new ProfileFromMailFromServiceWs();
    }


    @Test
    public void getString(){


        Profile profileQuote = profileFromMailFromService.generate(
                "Nistor Andrei <coder.tux@gmail.com>"
        );
        Assert.assertEquals(profileQuote.getName(),"Nistor");
        Assert.assertEquals(profileQuote.getLastname(),"Andrei");
        Assert.assertEquals(profileQuote.getEmail(),"coder.tux@gmail.com");



        Profile withQuotes = profileFromMailFromService.generate(
                "\"Mario Bensi\" <nef@ipsquad.net>"
        );
        Assert.assertEquals(withQuotes.getName(),"Mario");
        Assert.assertEquals(withQuotes.getLastname(),"Bensi");
        Assert.assertEquals(withQuotes.getEmail(),"nef@ipsquad.net");


        Profile withUtf = profileFromMailFromService.generate(
                "Sebastian =?ISO-8859-1?Q?K=FCgler?= <sebas@kde.org>"
        );
        Assert.assertEquals(withUtf.getName(),"Sebastian");
        Assert.assertEquals(withUtf.getLastname(),"=?ISO-8859-1?Q?K=FCgler?=");
        Assert.assertEquals(withUtf.getEmail(),"sebas@kde.org");


        Profile withDot = profileFromMailFromService.generate(
                "\"Aaron J. Seigo\" <aseigo@kde.org>"
        );
        Assert.assertEquals(withDot.getName(),"Aaron");
        Assert.assertEquals(withDot.getLastname(),"J. Seigo");
        Assert.assertEquals(withDot.getEmail(),"aseigo@kde.org");


        Profile withUTFNullResult = profileFromMailFromService.generate(
                "=?UTF-8?B?UmFmYcWCIE1pxYJlY2tp?= <zajec5@gmail.com>"
        );
        Assert.assertNull(withUTFNullResult);

    }

}
