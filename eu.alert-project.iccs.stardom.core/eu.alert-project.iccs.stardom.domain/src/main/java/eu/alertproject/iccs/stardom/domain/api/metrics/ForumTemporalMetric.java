package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:09
 */

@Entity
//@Table(name="forum_temporal_metric")
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("forum_temporal_metric")
public class ForumTemporalMetric extends MetricTemporal{

    @Override
    public String getLabel() {
        return "Form Last Date";
    }
}
