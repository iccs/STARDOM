package eu.alertproject.iccs.stardom.identifier.api;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Person;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 10:28
 * To change this template use File | Settings | File Templates.
 */
public interface Identifier {

    public boolean match(Person a, Person b);
    public Identity find(Person person);
    public List<Identity> identify(List<Person> person);

}
