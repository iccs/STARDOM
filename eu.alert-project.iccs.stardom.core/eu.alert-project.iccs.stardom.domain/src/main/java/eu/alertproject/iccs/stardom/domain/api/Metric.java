package eu.alertproject.iccs.stardom.domain.api;

import com.existanze.libraries.orm.domain.SimpleBean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 18:18
 */
@Table(name="metric")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Metric implements SimpleBean,Serializable{

    @TableGenerator(
            name="tseq",
            table="sequence",
            pkColumnName="sequence_name",
            valueColumnName="sequence_index",
            pkColumnValue="metric_sequence",
            allocationSize = 1)

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator="tseq")
    private Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id =id;
    }




}
