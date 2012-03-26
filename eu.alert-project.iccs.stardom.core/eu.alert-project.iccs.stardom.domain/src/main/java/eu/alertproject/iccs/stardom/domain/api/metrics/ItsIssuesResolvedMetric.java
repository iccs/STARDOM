package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 04/02/12
 * Time: 19:57
 */
@Entity
@Table(name="its_issues_resolved_metric")
public class ItsIssuesResolvedMetric extends MetricQuantitative {

    @Override
    public String getLabel() {
        return "ITS Issues Resolved";
    }
}
