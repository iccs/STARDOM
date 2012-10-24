package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:27
 */
@Entity
//@Table(name="scm_api_usage_count_metric")
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("scm_api_usage_count_metric")
public class ScmApiUsageCountMetric extends MetricQuantitative{

    @Override
    public String getLabel() {
        return "SCM API Api Usage";
    }
}
