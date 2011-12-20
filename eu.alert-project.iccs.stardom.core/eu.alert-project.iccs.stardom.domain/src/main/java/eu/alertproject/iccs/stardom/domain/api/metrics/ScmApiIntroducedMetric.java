package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:07
 */

@Entity
@Table(name="scm_api_introduced_metric")
public class ScmApiIntroducedMetric extends MetricQuantitative{



    @Override
    public String getLabel() {
        return "SCM Api Introduced";
    }
}
