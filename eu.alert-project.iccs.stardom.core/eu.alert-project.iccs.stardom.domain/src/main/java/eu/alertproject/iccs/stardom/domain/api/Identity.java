package eu.alertproject.iccs.stardom.domain.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class Identity {

    private String uuid;
    private List<Person> isPersons;

    private List<Person> isNotPersons;

    /**
     * This creates an Identiy having a #uuid of {@System.currentTimeMillis}
     */
    public Identity() {

        this(
                String.valueOf(System.currentTimeMillis())
        );
    }

    public Identity(String uuid) {
        this.uuid = uuid;

        isPersons = new ArrayList<Person>();
        isNotPersons = new ArrayList<Person>();
    }


    public String getUuid() {
        return uuid;
    }

    public List<Person> getPersons() {
        return isPersons;
    }

    public void setPersons(List<Person> persons) {
        isPersons = persons;
    }

    public List<Person> getNotPersons() {
        return isNotPersons;
    }

    public void setNotPersons(List<Person> notPersons) {
        isNotPersons = notPersons;
    }

    public void addToPerson(Person p){
        this.isPersons.add(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Identity)) return false;

        Identity that = (Identity) o;

        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
