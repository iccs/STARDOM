package eu.alertproject.iccs.stardom.domain.api.ml;

import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;
import java.util.Date;

/**
 * User: fotis
 * Date: 24/08/11
 * Time: 22:19
 */
@Entity
@Table(name="its_ml")
public class ItsMl implements SimpleBean{

    @TableGenerator(
            name="tseq",
            table="sequence",
            pkColumnName="sequence_name",
            valueColumnName="sequence_index",
            pkColumnValue="its_ml_sequence",
            allocationSize = 1)

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator="tseq")
    private Integer id;

    @Column(name ="bug_id")
    private Integer bugId;

    @Column(name="uuid_who")
    private String uuidWho;
    
    @Column(name = "uuid")
    private String uuid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="status_date")
    private Date when;

    @Column(name="status")
    private String status;


    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getBugId() {
        return bugId;
    }

    public void setBugId(Integer bugId) {
        this.bugId = bugId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuidWho() {
        return uuidWho;
    }

    public void setUuidWho(String uuidWho) {
        this.uuidWho = uuidWho;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItsMl)) return false;

        ItsMl itsMl = (ItsMl) o;

        if (bugId != null ? !bugId.equals(itsMl.bugId) : itsMl.bugId != null) return false;
        if (id != null ? !id.equals(itsMl.id) : itsMl.id != null) return false;
        if (status != null ? !status.equals(itsMl.status) : itsMl.status != null) return false;
        if (uuid != null ? !uuid.equals(itsMl.uuid) : itsMl.uuid != null) return false;
        if (uuidWho != null ? !uuidWho.equals(itsMl.uuidWho) : itsMl.uuidWho != null) return false;
        if (when != null ? !when.equals(itsMl.when) : itsMl.when != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (bugId != null ? bugId.hashCode() : 0);
        result = 31 * result + (uuidWho != null ? uuidWho.hashCode() : 0);
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (when != null ? when.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
