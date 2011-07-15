package eu.alertproject.iccs.stardom.domain.api;

import javax.persistence.*;
import java.util.Date;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 21:18
 */
@Table(name="metric_temporal")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("metric_temporal")
public abstract class MetricTemporal extends Metric{

    @Column(name="when")
    @Temporal(TemporalType.TIMESTAMP)
    private Date when;

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }


}
