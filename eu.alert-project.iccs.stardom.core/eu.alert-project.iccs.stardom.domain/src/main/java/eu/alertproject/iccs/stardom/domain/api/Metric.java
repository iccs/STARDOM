package eu.alertproject.iccs.stardom.domain.api;

import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 18:18
 */
@Entity
@Table(name="metric")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Metric implements SimpleBean,Serializable,MetricDescriptor{

    @TableGenerator(
            name="tseq",
            table="sequence",
            pkColumnName="sequence_name",
            valueColumnName="sequence_index",
            pkColumnValue="metric_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.TABLE, generator="tseq")
    @Id
    private Integer id;


    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @ManyToOne
    private Identity identity;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id =id;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createAt) {
        this.createdAt = createAt;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    @Override
    public String getLabel() {
        return toString();

    }

    @Override
    public String toString() {
        return "Metric{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", identity=" + identity +
                '}';
    }
}
