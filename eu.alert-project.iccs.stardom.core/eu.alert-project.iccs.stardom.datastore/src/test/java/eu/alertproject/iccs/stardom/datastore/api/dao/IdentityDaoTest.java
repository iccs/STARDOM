package eu.alertproject.iccs.stardom.datastore.api.dao;

import com.existanze.libraries.orm.test.JpaTestUtils;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.testdata.api.SpringDbUnitJpaTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 28/06/11
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public class IdentityDaoTest extends SpringDbUnitJpaTest{

    public static int ORIGINAL_NUMBER_OF_ROWS=5;

    @Autowired
    IdentityDao identityDao;

    @Override
    public void postConstruct() {

    }

    @Override
    protected String[] getDatasetFiles() {
        return new String[]{
                "/db/profile.xml",
                "/db/identity.xml",
                "/db/identity_is_profile.xml",
        };  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Test
    public void insert(){

        Identity identity = new Identity();

        Identity insert = identityDao.insert(identity);

        Assert.assertNotNull(insert);
        Assert.assertEquals(ORIGINAL_NUMBER_OF_ROWS + 1, insert.getId(), 0);
        Assert.assertNotNull(insert.getUuid());
        Assert.assertEquals(DigestUtils.md5Hex(String.valueOf(ORIGINAL_NUMBER_OF_ROWS+1)),insert.getUuid());
    }

    @Test
    public void findPossibleMatchesByName(){


        Profile p = new Profile(
                "Fotis",
                "Katsaitis",
                "noname",
                "no@no.com"
        );
        List<Identity> possibleMatches = identityDao.findPossibleMatches(p);

        Assert.assertNotNull(possibleMatches);
        Assert.assertEquals(2,possibleMatches.size(),0);

        JpaTestUtils.assertIdOrder(possibleMatches, new Integer[]{3,5});


    }

    @Test
    public void findPossibleMatchesByEmail(){


        Profile p = new Profile(
                "Stavros",
                "Katsaitis",
                "noname",
                "fotisp@superemail.ex"
        );
        List<Identity> possibleMatches = identityDao.findPossibleMatches(p);

        Assert.assertNotNull(possibleMatches);
        Assert.assertEquals(1,possibleMatches.size(),0);

        JpaTestUtils.assertIdOrder(possibleMatches, new Integer[]{3});


    }

    @Test
    public void findPossibleMatchesByLastname(){


        Profile p = new Profile(
                "Juan",
                "Paraskevopoulos",
                "noname",
                "no@no.com"
        );
        List<Identity> possibleMatches = identityDao.findPossibleMatches(p);

        Assert.assertNotNull(possibleMatches);
        Assert.assertEquals(2,possibleMatches.size(),0);

        JpaTestUtils.assertIdOrder(possibleMatches, new Integer[]{3,4});


    }

    @Test
    public void findPossibleMatchesByUsername(){


        Profile p = new Profile(
                "No",
                "Name",
                "jsmith",
                "no@no.com"
        );
        List<Identity> possibleMatches = identityDao.findPossibleMatches(p);

        Assert.assertNotNull(possibleMatches);
        Assert.assertEquals(1,possibleMatches.size(),0);

        JpaTestUtils.assertIdOrder(possibleMatches, new Integer[]{1});


    }


}
