package eu.alertproject.iccs.stardom.domain.api;

import com.existanze.libraries.orm.dao.CommonDao;
import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="identity")
public class Identity implements SimpleBean{

    @TableGenerator(
            name="tseq",
            table="sequence",
            pkColumnName="sequence_name",
            valueColumnName="sequence_index",
            pkColumnValue="identity_sequence",
            allocationSize = 1)

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator="tseq")
    private Integer id;

    @Column
    private String uuid;


    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "identity_is_person",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name="identity_id")
    )
    private Set<Person> isPersons;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "identity_not_person",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name="identity_id")
    )
    private Set<Person> isNotPersons;

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

        isPersons = new HashSet<Person>();
        isNotPersons = new HashSet<Person>();
    }


    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public Set<Person> getPersons() {
        return isPersons;
    }

    public void setPersons(Set<Person> persons) {
        isPersons = persons;
    }

    public Set<Person> getNotPersons() {
        return isNotPersons;
    }

    public void setNotPersons(Set<Person> notPersons) {
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
