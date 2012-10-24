package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:08
 */

@Entity
//@Table(name="forum_activity_metric")
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("forum_activity_metric")
public class ForumActivityMetric extends MetricQuantitative{

    @Override
    public String getLabel() {
        return "Forum Activity";
    }
}
