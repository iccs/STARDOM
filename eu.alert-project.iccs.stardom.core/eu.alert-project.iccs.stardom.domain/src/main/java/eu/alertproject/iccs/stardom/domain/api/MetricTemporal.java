package eu.alertproject.iccs.stardom.domain.api;

import javax.persistence.*;
import java.util.Date;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 21:18
 */
@Entity
@Table(name="metric_temporal")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MetricTemporal extends Metric{

    @Column(name="temporal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date temporal;

    public Date getTemporal() {
        return temporal;
    }

    public void setTemporal(Date temporal) {
        this.temporal = temporal;
    }

    @Override
    public Object getValue() {
        return getTemporal();
    }
}
