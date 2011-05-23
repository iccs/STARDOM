package eu.alertproject.iccs.stardom.identifier;

import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.identifier.api.IdentifierWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.LevelWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.PropertyWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.internal.DefaultIdentifier;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * DISCLAIMER -> The information for the persons is ficticious, it has
 * been made up during coding of this unit test . Please don't be
 * alarmed
 *
 *
 *
 * User: fotis
 * Date: 23/05/11
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */

public class DefaultIdentifierTest {

    private IdentifierWeightConfiguration defaultWeightConfiguration;
    private Identifier identifier;

    @Before
    public void setUp(){

        /**
         * This is set up as per the paper
         * Carmagnola, F. & Cena, F., 2009. User identification for cross-system personalisation. Information Sciences, 179(1-2), p.16–32.

         *
         */
        this.defaultWeightConfiguration= new IdentifierWeightConfiguration(
                0.74,
                new LevelWeightConfiguration(0.45,0.35, 0.20),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.FIRSTNAME,0.2, 0.8,0.2),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.LASTNAME,0.8, 0.8,0.8),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.USERNAME,0.8, 0.2,0.2),
                new PropertyWeightConfiguration(PropertyWeightConfiguration.Property.EMAIL,1, 0.4,0.4)
        );
        this.identifier = new DefaultIdentifier(this.defaultWeightConfiguration);
    }

    @Test
    public void testMatch() {

        Person pA = new Person("John","Smith","jsmith","jsmith@gmail.com");
        Person pB = new Person("Juan","Smith","jsmith","jsmith@hotmail.com");

        boolean match = identifier.match(pA, pB);

        Assert.assertTrue(match);
    }


    @Test
    public void testNotMatch(){

        Person pA = new Person("John","Steward","jsmiths","jsmith@gmail.com");
        Person pB = new Person("John","Smith","jsmith","jsmith@hotmail.com");


        boolean match = identifier.match(pA, pB);
        Assert.assertFalse(match);

    }

    @Test
    public void testIdentify(){


        List<Person> persons  =  new ArrayList<Person>();

        persons.add(new Person("Fotis","Paraskevopoulos","fotisp","fotisp@superemail.ex"));
        persons.add(new Person("Fotis","Paraskevopoulos","fotakis","fotisp@superemail.ex"));
        persons.add(new Person("Fotios","Paraskevopoulos","whatever","fotisp@hotm.ex"));


        List<Identity> identify = identifier.identify(persons);


        Assert.assertNotNull(identify);

        Assert.assertEquals(2, identify.size());


        Identity identity = identify.get(0);
        Identity identityB = identify.get(1);

        Assert.assertEquals(2, identity.getPersons().size());

        assertPerson(
                    persons.get(0),
                    identity.getPersons().get(0));

        assertPerson(
                    persons.get(1),
                    identity.getPersons().get(1));



        Assert.assertEquals(1, identityB.getPersons().size());
        assertPerson(
                    persons.get(2),
                    identityB.getPersons().get(0));



    }


    /**
     * Just in case I need to add more logic in the future
     * @param a
     * @param b
     */
    private void assertPerson(
            Person a,
            Person b
    ){
        Assert.assertEquals(a,b);
    }

}
