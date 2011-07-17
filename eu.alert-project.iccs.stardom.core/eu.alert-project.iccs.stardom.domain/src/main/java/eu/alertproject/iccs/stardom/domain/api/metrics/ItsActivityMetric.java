package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:08
 */
@Entity
@Table(name="its_activity_metric")
public class ItsActivityMetric extends MetricQuantitative{

    @Override
    public String getLabel() {
        return "ITS Activity";
    }
}
