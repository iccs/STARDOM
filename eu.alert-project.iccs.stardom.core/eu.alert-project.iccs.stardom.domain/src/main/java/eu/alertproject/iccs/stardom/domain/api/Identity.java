package eu.alertproject.iccs.stardom.domain.api;

import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User: fotis
 * Date: 23/05/11
 * Time: 10:22
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
            name = "identity_is_profile",
            joinColumns = @JoinColumn(name = "identity_id"),
            inverseJoinColumns = @JoinColumn(name="profile_id")
    )
    private Set<Profile> profiles;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "identity_not_profile",
            joinColumns = @JoinColumn(name = "identity_id"),
            inverseJoinColumns = @JoinColumn(name="profile_id")
    )
    private Set<Profile> notProfiles;


    @OneToMany(fetch=FetchType.LAZY, mappedBy = "identity")
    @OrderBy("createdAt")
    private Set<Metric> metrics;

    /**
     * This constructor creates an Identiy having a #uuid of {@System.currentTimeMillis}
     */
    public Identity() {

        this(
                String.valueOf(System.currentTimeMillis())
        );
    }

    public Identity(String uuid) {
        this.uuid = uuid;

        profiles = new HashSet<Profile>();
        notProfiles = new HashSet<Profile>();
    }


    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public Set<Profile> getNotProfiles() {
        return notProfiles;
    }

    public void setNotProfiles(Set<Profile> notProfiles) {
        this.notProfiles = notProfiles;
    }

    public void addToProfiles(Profile p){
        this.profiles.add(p);
    }

    public Set<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(Set<Metric> metrics) {
        this.metrics = metrics;
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

    @Override
    public String toString() {
        return "Identity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
