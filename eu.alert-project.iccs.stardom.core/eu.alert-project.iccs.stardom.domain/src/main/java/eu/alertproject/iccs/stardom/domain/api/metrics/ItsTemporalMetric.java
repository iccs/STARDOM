package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;

import javax.persistence.*;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:09
 */

@Entity
@Table(name="its_temporal_metric")
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("its_temporal_metric")
public class ItsTemporalMetric extends MetricTemporal{

    
    @Column(name="component")
    private String component;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    @Override
    public String getLabel() {
        return "ITS Activity";
    }
}


