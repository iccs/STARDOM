package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 18:23
 */

@Entity
@Table(name="scm_temporal_metric")
public class ScmTemporalMetric extends MetricTemporal{

    @Override
    public String getLabel() {
        return "Scm Last Date";
    }
}
