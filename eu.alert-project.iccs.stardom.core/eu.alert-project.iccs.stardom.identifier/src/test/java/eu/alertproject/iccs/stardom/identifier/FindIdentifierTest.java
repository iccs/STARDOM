package eu.alertproject.iccs.stardom.identifier;

import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: fotis
 * Date: 20/07/11
 * Time: 08:50
 */
public class FindIdentifierTest extends SpringDbUnitJpaTest {

    private Logger logger = LoggerFactory.getLogger(FindIdentifierTest.class);

    @Autowired
    Identifier identifier;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    ProfileDao profileDao;

    @Override
    public void postConstruct() {}

    @Override
    protected String[] getDatasetFiles() {
        return new String[]{
            "/db/empty_identity.xml",
        };
    }

    @Test
    public void testMatch() {

        Profile pA = new Profile("John","Smith","jsmith","jsmith@gmail.com");

        Identity identity = identifier.find(pA,"source-a");
        Assert.assertNotNull(identity);

        Profile pB = new Profile("Juan","Smith","jsmith","jsmith@hotmail.com");
        Identity identity1 = identifier.find(pB,"source-b");

        Assert.assertEquals(identity.getUuid(),identity1.getUuid());

        List<Identity> all = identityDao.findAll();
        Assert.assertEquals(1,all.size(),0);
        

        Profile pC = new Profile(null,null,null,"ervin kde org");
        identifier.find(pC,"its");

        all = identityDao.findAll();
        Assert.assertEquals(2,all.size(),0);


        Profile pD = new Profile(null,null,null,"ervin kde org");
        identifier.find(pD,"its-source");

        all = identityDao.findAll();
        Assert.assertEquals(2,all.size(),0);

        Assert.assertEquals(1,all.get(1).getProfiles().size(),0);


        Profile pE = new Profile(null,null,"ervin kde org",null);
        identifier.find(pE,"its");

        all = identityDao.findAll();
        Assert.assertEquals(3,all.size(),0);


        Profile pF = new Profile(null,null,"ervin kde org",null);
        identifier.find(pF,"its-source");

        all = identityDao.findAll();
        Assert.assertEquals(3,all.size(),0);

        Assert.assertEquals(1,all.get(2).getProfiles().size(),0);



    }

    @Test
    public void testMatchEmailSluggify() {

        Profile pA = new Profile("John","Smith","jsmith","jsmith@gmail.com");

        Identity identity = identifier.find(pA);
        Assert.assertNotNull(identity);

        Profile pB = new Profile("Juan","Smith","jsmith","jsmith hotmail com");
        Identity identity1 = identifier.find(pB);

        Assert.assertEquals(identity.getUuid(),identity1.getUuid());

        List<Identity> all = identityDao.findAll();
        Assert.assertEquals(1,all.size(),0);

    }


    @Test
    public void testMatchWithEmptyUsername(){

        Profile pA = new Profile("Stephan","Kulow","","coolo@kde.org");

        Identity identity = identifier.find(pA);
        Assert.assertNotNull(identity);

        Profile pB = new Profile("Stephan","Kulow","","coolo@kde.org");
        Identity identity1 = identifier.find(pB);

        Assert.assertEquals(identity.getUuid(),identity1.getUuid());

        List<Identity> all = identityDao.findAll();
        Assert.assertEquals(1,all.size(),0);

    }

    @Test
    public void testNullPropertiesNoMatch(){

        Profile pA = new Profile("John","Smith","","jsmith@gmail.com");

        Identity identity = identifier.find(pA);
        Assert.assertNotNull(identity);

        Profile pB = new Profile("Juan","Smith","","jsmith@hotmail.com");
        Identity identity1 = identifier.find(pB);

        Assert.assertFalse(identity.getUuid().equals(identity1.getUuid()));

        List<Identity> all = identityDao.findAll();
        Assert.assertEquals(2,all.size(),0);

    }

    @Test
    public void testNotMatch(){


        List<Identity> all1 = identityDao.findAll();
        Assert.assertEquals(0,all1.size(),0);

        Profile pA = new Profile("John","Steward","jsmiths","jsmith@gmail.com");

        Identity identity = identifier.find(pA);
        Assert.assertNotNull(identity);


        Profile pB = new Profile("John","Smith","jsmith","jsmith@hotmail.com");
        Identity identity1 = identifier.find(pB);

        Assert.assertNotSame(identity.getUuid(),identity1.getUuid());

        List<Identity> all = identityDao.findAll();
        Assert.assertEquals(2,all.size(),0);

    }

    @Test
    public void testIdentify(){

        List<Identity> all1 = identityDao.findAll();
        Assert.assertEquals(0,all1.size(),0);

        //These 2 should create a single UUID
        Profile profileA1 = new Profile("Fotis", "Paraskevopoulos", "fotisp", "fotisp@superemail.ex");
        Profile profileA2 = new Profile("Fotis", "Paraskevopoulos", "fotakis", "fotisp@superemail.ex");

        //This one should create a single UUID
        Profile profileB1 = new Profile("Fotios", "Paraskevopoulos", "whatever", "fotisp@hotm.ex");


        Identity identityA1 = identifier.find(profileA1);
        Assert.assertNotNull(identityA1);
        Assert.assertEquals(1,profileDao.findAll().size(),0);

        Identity identityB1 = identifier.find(profileB1);
        Assert.assertNotNull(identityB1);

        Assert.assertEquals(2,profileDao.findAll().size(),0);
        Assert.assertNotSame(identityA1.getUuid(),identityB1.getUuid());


        Assert.assertEquals(2, identityDao.findAll().size(), 0);

        Identity identityA2 = identifier.find(profileA2);
        Assert.assertNotNull(identityA2);
        Assert.assertEquals(3,profileDao.findAll().size(),0);

        Assert.assertEquals(identityA1.getUuid(), identityA2.getUuid());
        Assert.assertFalse(identityA2.getUuid().equals(identityB1.getUuid()));

        Assert.assertEquals(2,identityDao.findAll().size(),0);



    }
    @Test
    public void testIdentifyFullMatchProfile(){

        List<Identity> all1 = identityDao.findAll();
        Assert.assertEquals(0,all1.size(),0);

        //These 2 should create a single UUID
        Profile profileA1 = new Profile("Fotis", "Paraskevopoulos", "fotisp", "fotisp@superemail.ex");
        Profile profileA2 = new Profile("Fotis", "Paraskevopoulos", "fotisp", "fotisp@superemail.ex");

        //This one should create a single UUID
        Profile profileB1 = new Profile("Fotios", "Paraskevopoulos", "whatever", "fotisp@hotm.ex");


        Identity identityA1 = identifier.find(profileA1);
        Assert.assertNotNull(identityA1);
        Assert.assertEquals(1,profileDao.findAll().size(),0);

        Identity identityB1 = identifier.find(profileB1);
        Assert.assertNotNull(identityB1);

        Assert.assertEquals(2,profileDao.findAll().size(),0);
        Assert.assertNotSame(identityA1.getUuid(),identityB1.getUuid());


        Assert.assertEquals(2, identityDao.findAll().size(), 0);

        Identity identityA2 = identifier.find(profileA2);
        Assert.assertNotNull(identityA2);
        Assert.assertEquals(2,profileDao.findAll().size(),0);

        Assert.assertEquals(identityA1.getUuid(), identityA2.getUuid());
        Assert.assertFalse(identityA2.getUuid().equals(identityB1.getUuid()));

        Assert.assertEquals(2,identityDao.findAll().size(),0);



    }

    @Test
    public void testIdentityMatchEmptyEmail(){

        Profile pA = new Profile("","","","coolo@kde.org");

        Identity identity = identifier.find(pA);
        Assert.assertNotNull(identity);

        Profile pB = new Profile("","","","coolo@kde.org");
        Identity identity1 = identifier.find(pB);

        Assert.assertEquals(identity.getUuid(),identity1.getUuid());

        List<Identity> all = identityDao.findAll();
        Assert.assertEquals(1,all.size(),0);



    }

}
