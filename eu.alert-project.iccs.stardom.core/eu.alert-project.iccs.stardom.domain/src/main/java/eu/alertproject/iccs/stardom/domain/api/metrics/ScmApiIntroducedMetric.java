package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:07
 */

@Entity
@Table(name="scm_api_introduced_metric")
public class ScmApiIntroducedMetric extends MetricTemporal{


    @Column
    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String getLabel() {
        return "SCM Api Introduced";
    }
}
