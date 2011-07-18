package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:08
 */

@Entity
@Table(name="mailing_list_temporal_metric")
public class MailingListTemporalMetric extends MetricTemporal{

    @Override
    public String getLabel() {
        return "ML Last Date";
    }
}
