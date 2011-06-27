package eu.alertproject.iccs.stardom.identifier.internal;

import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.identifier.api.IdentifierWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.LevelWeightConfiguration;
import eu.alertproject.iccs.stardom.identifier.api.PropertyWeightConfiguration;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Person;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * Carmagnola, F. & Cena, F., 2009. User identification for cross-system personalisation. Information Sciences, 179(1-2), p.16–32.
 *
 * User: fotis
 * Date: 23/05/11
 * Time: 10:30
 * To change this template use File | Settings | File Templates.
 */
public class DefaultIdentifier implements Identifier{

    private Logger logger = LoggerFactory.getLogger(DefaultIdentifier.class);

    private IdentifierWeightConfiguration weightConfiguration;

    public DefaultIdentifier() {
        this(new IdentifierWeightConfiguration());
    }

    public DefaultIdentifier(
            double threshold,
            LevelWeightConfiguration levelWeights,
            PropertyWeightConfiguration firstname,
            PropertyWeightConfiguration lastname,
            PropertyWeightConfiguration username,
            PropertyWeightConfiguration email
    ) {

        this(
            new IdentifierWeightConfiguration(
                    threshold,
                    levelWeights,
                    firstname,
                    lastname,
                    username,
                    email
            ));

    }

    public DefaultIdentifier(IdentifierWeightConfiguration weightConfiguration) {
        this.weightConfiguration = weightConfiguration;

    }


    public IdentifierWeightConfiguration getWeightConfiguration() {
        return weightConfiguration;
    }

    public void setWeightConfiguration(IdentifierWeightConfiguration weightConfiguration) {
        this.weightConfiguration = weightConfiguration;
    }

    @Override
    public boolean match(Person a, Person b){
        logger.trace("boolean match() A({}) and B ({})",a,b);

        double anIf = getIf(a, b);

        return anIf > this.weightConfiguration.getThreshold();

    }

    public double getIf(Person a, Person b) {



        //check if any of the information in between them is the same
        List<Double> ifs = new ArrayList<Double>();

        if(StringUtils.endsWithIgnoreCase(a.getName(),b.getName())){


            double v = calculateIf(this.weightConfiguration.getFirstName());
            logger.trace("boolean getIf() Found a first name match {}",v);

            ifs.add(v);
        }

        if(StringUtils.endsWithIgnoreCase(a.getLastname(),b.getLastname())){
            double v = calculateIf(this.weightConfiguration.getLastName());
            logger.trace("boolean getIf() Found a last name match {}",v);

            ifs.add(v);
        }

        if(StringUtils.endsWithIgnoreCase(a.getEmail(),b.getEmail())){

            double v = calculateIf(this.weightConfiguration.getEmail());
            logger.trace("boolean getIf() Found an email match {}",v);
            ifs.add(v);
        }

        if(StringUtils.equalsIgnoreCase(a.getUsername(),b.getUsername())){

            double v = calculateIf(this.weightConfiguration.getUsername());
            logger.trace("boolean getIf() Found a username match {}",v);
            ifs.add(v);

        }


        /**
         *
         */
        if(ifs.size() <= 0){
            //no match
            logger.trace("boolean getIf() No matches found ");
            return 0.0;
        }else if(ifs.size() == 1){

            logger.trace("boolean getIf() Only one property matched with IF {} ",ifs.get(0));
            //check the threshold
            return ifs.get(0);


        }else if(ifs.size() > 1){
            logger.trace("boolean getIf() More that one property matched ");
            //combine and check
            double combinedIf = combineIf(ifs);
            logger.trace("boolean match() combined IF {} ",combinedIf);

            return combinedIf;

        }

        logger.error("boolean match() As is the case with all super algorithms, This line should never happen!");
        return -1.0;

    }

    /**
     * IF= p+(1-p)*q
     * @param ifs
     * @return The combined if
     */
    private double combineIf(List<Double> ifs) {
        if(ifs == null || ifs.size() <=0){
            return 0.0;
        }

        if(ifs.size() <=1){
            return ifs.get(0);
        }


        double combined = ifs.get(0);



        for(int i=1; i< ifs.size(); i++){

            Double aDouble = ifs.get(i);
            combined = aDouble + ( 1 - aDouble) *combined;

            logger.trace("double combineIf() Current combined IF {}",combined);

        }

        return combined;

    }

    /**
     *
     * IF = Value(UL)*uw + Value(1-VpU)*vw + Value(1-ML)*mw
     *
     * @param property
     * @return
     */

    private double calculateIf(PropertyWeightConfiguration property) {

        LevelWeightConfiguration levelWeights = this.weightConfiguration.getLevelWeights();


        double univocity = property.getUvl();
        double valuesPerUser = property.getVpu();
        double misLevel= property.getMl();

        return
                (univocity*levelWeights.getLevelUl())
                +((1-valuesPerUser)*levelWeights.getLevelVpu())
                + ((1-misLevel)*levelWeights.getLevelMl());
    }

    @Override
    public Identity find(Person person) {
        return null;
    }

    /**
     * TODO Make this better than O(n2)
     * @param person
     * @return
     */
    @Override
    public List<Identity> identify(List<Person> person) {

        ArrayList<Identity> identities = new ArrayList<Identity>();

        /**
         * Here you should find a common property with a existing
         * identity, check if the identity matches,
         * if none is found then create one and loop to the next
         * person
         *
         */
        for(Person p : person){

            logger.trace("List<Identity> identify() checking person {} ",p);

            Map<Identity, List<Person>> matches = new HashMap<Identity, List<Person>>();


            for(Identity i : identities){

                logger.trace("List<Identity> identify() find candidate identity  ");
                List<Person> identityMatchedPersons = hasMatchingPoperty(i, p);

                if(identityMatchedPersons.size() <= 0 ){
                    logger.trace("List<Identity> identify() No matching cadidate found, a new one is created");
                    Identity newIdentity = new Identity();
                    newIdentity.addToPerson(p);
                    identities.add(newIdentity);

                }else{

                    /**
                     * Each time an identity is matched we can't just look at the IF and
                     * return the match, because there may be other candidate identities
                     * that will do the work!.
                     *
                     * Store the possible identities in order to check the isPersons array
                     * for all the possible identities
                     *
                     */
                    logger.trace("List<Identity> identify() Possible matches found added to hash");
                    logger.trace("List<Identity> identify() {}",identityMatchedPersons);
                    matches.put(i, identityMatchedPersons);
                }
            }



            Iterator<Identity> iterator = matches.keySet().iterator();
            logger.trace("List<Identity> identify() begin lookup into the map, possible identities {} ",matches.size());

            /**
             *
             * For each of the possible identities, look for matching persons in the isPersons
             * array list.
             *
             * Get the IF of each and store in this array.
             *
             * We are trying to avoid the case where 2 possible matched identities exist, but the
             * IF of one is greater than the other
             *
             */
            List<PossiblePersonIdentity> possibleIdentities = new ArrayList<PossiblePersonIdentity>();


            while(iterator.hasNext()){


                Identity next = iterator.next();
                logger.trace("List<Identity> identify() checking if it is in identity {}",next.getUuid());

                List<Person> possiblePersons = matches.get(next);
                for(Person lookupPerson : possiblePersons){

                    logger.trace("List<Identity> identify() checking {} agains {} ",p, lookupPerson);
                    double anIf = getIf(p, lookupPerson);

                    logger.trace("List<Identity> identify() got an IF of {} ",anIf);
                    if(anIf >= this.getWeightConfiguration().getThreshold()){

                        logger.trace("List<Identity> identify() It is a possible identity keep it");
                        possibleIdentities.add(
                                new PossiblePersonIdentity(
                                        next,
                                        p,
                                        anIf)
                        );
                    }
                }
            }


            /**
             * When none of the possible candidates has been matched then
             * we have exhausted all options and we need to create new
             * identity in our array
             */
            if(possibleIdentities.size() <= 0){
                //no one was matched, create the identity
                logger.trace("List<Identity> identify() No possible identity matched  a new one is created");
                Identity newIdentity = new Identity();
                newIdentity.addToPerson(p);
                identities.add(newIdentity);

            }else{

                /**
                 * We assume there are more than 1 items in this list, so we
                 * sort based on the IF and add to the identity the person with the
                 * greatest IF
                 */
                Collections.<PossiblePersonIdentity>sort(possibleIdentities);
                PossiblePersonIdentity possiblePersonIdentity = possibleIdentities.get(0);
                possiblePersonIdentity.getIdentity().addToPerson(possiblePersonIdentity.getPerson());

            }
        }



        return identities;
    }

    /**
     * Looks inside the identity persons list, and return any persons that
     * have any matching property
     * @param i
     * @param p
     * @return
     */

    private List<Person> hasMatchingPoperty(Identity i, Person p){

        Set<Person> persons = i.getPersons();

        List<Person> ret = new ArrayList<Person>();


        Field[] fields = Person.class.getDeclaredFields();

        /**
         * Check each of the available persons in the current identity,
         * and look to se if there is any person that has a matching
         * property to that of person p!
         *
         */
        for(Person person : persons){
            for(Field f: fields){
                try {
                    Method method = Person.class.getMethod("get" + StringUtils.capitalize(f.getName()));

                    Object personValue = method.invoke(person);
                    Object lookupValue = method.invoke(p);

                    if( (personValue == null && lookupValue == null)
                        || StringUtils.equalsIgnoreCase(personValue.toString(), lookupValue.toString())){
                        ret.add(person);
                        break;
                    }

                } catch (NoSuchMethodException e) {
                    logger.warn("Problem reading field {} ",f.getName());

                } catch (InvocationTargetException e) {
                    logger.warn("Problem calling method");

                } catch (IllegalAccessException e) {
                    logger.warn("Problem calling method, make sure the accessors are public");
                }
            }
        }


        return ret;

    }


    class PossiblePersonIdentity implements Comparable<PossiblePersonIdentity> {

        private Identity identity;
        private Person person;
        private double matchIf;

        PossiblePersonIdentity(Identity identity, Person person, double matchIf) {
            this.identity = identity;
            this.person = person;
            this.matchIf = matchIf;
        }

        @Override
        public int compareTo(PossiblePersonIdentity possiblePersonIdentity) {
            return Double.valueOf(getMatchIf()).compareTo(
                                Double.valueOf(possiblePersonIdentity.getMatchIf()));


        }

        public Identity getIdentity() {
            return identity;
        }

        public Person getPerson() {
            return person;
        }

        public double getMatchIf() {
            return matchIf;
        }
    }

}
