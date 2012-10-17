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

        Profile profileQuote2 = profileFromMailFromService.generate(
                "lam_arque at g_mail.com (Lam_arque Vieira Souza)"
        );
        Assert.assertEquals(profileQuote2.getName(),"Lam_arque");
        Assert.assertEquals(profileQuote2.getLastname(),"Vieira Souza");
        Assert.assertEquals(profileQuote2.getEmail(),"lam_arque g_mail.com");



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

    @Test
    public void getMailString(){


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

        Profile withUtf2 = profileFromMailFromService.generate(
                                        "Jérémie Doucy<jdoucy@gmail.com>"
        );
        Assert.assertEquals(withUtf2.getName(),"Jérémie");
        Assert.assertEquals(withUtf2.getLastname(),"Doucy");
        Assert.assertEquals(withUtf2.getEmail(),"jdoucy@gmail.com");

        Profile withUtf3= profileFromMailFromService.generate(
                                        "Jérôme Leclaire<jerome.leclaire@orange.com>"
        );
        Assert.assertEquals(withUtf3.getName(),"Jérôme");
        Assert.assertEquals(withUtf3.getLastname(),"Leclaire");
        Assert.assertEquals(withUtf3.getEmail(),"jerome.leclaire@orange.com");


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


        Profile profileUnderscore = profileFromMailFromService.generate(
                "houda_dd<houda.doumi@gmail.com>"
        );
        Assert.assertEquals(profileUnderscore.getName(),"houda_dd");
        Assert.assertEquals(profileUnderscore.getLastname(),"");
        Assert.assertEquals(profileUnderscore.getEmail(),"houda.doumi@gmail.com");




    }
}
