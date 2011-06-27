package eu.alertproject.iccs.stardom.domain.api;

import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="person")
public class Person implements SimpleBean{

    @TableGenerator(
            name="tseq",
            table="sequence",
            pkColumnName="sequence_name",
            valueColumnName="sequence_index",
            pkColumnValue="person_sequence",
            allocationSize = 1)

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator="tseq")
    private Integer id;

    @Column
    private String name;

    @Column
    private String lastname;

    @Column
    private String username;

    @Column
    private String email;


    public Person() {
    }

    public Person(String name, String lastname, String username, String email) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
    }


    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (email != null ? !email.equals(person.email) : person.email != null) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (lastname != null ? !lastname.equals(person.lastname) : person.lastname != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (username != null ? !username.equals(person.username) : person.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
