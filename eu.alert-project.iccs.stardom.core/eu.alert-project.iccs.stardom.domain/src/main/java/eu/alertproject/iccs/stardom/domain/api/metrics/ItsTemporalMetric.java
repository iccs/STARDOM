package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:09
 */

@Entity
@Table(name="its_temporal_metric")
public class ItsTemporalMetric extends MetricTemporal{

    @Override
    public String getLabel() {
        return "ITS Activity";
    }
}
