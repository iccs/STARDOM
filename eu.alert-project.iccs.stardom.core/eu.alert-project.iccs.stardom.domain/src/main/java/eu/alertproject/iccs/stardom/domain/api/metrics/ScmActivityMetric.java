package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 18:24
 *
 */

@Entity
//@Table(name="scm_activity_metric")
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("scm_activity_metric")
public class ScmActivityMetric extends MetricQuantitative{

    @Override
    public String getLabel() {
        return "SCM Activity";
    }

}
