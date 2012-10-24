package eu.alertproject.iccs.stardom.domain.api.metrics;

import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;

import javax.persistence.*;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 19:08
 */

@Entity
//@Table(name="mailing_list_temporal_metric")
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("mailing_list_temporal_metric")
public class MailingListTemporalMetric extends MetricTemporal{

    
    @Column(name="message_id")
    private String messageId;

    @Column(name = "in_reply_to")
    private String inReplyTo;

    @Override
    public String getLabel() {
        return "ML Last Date";
    }

    public void setInReplyTo(String inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public String getInReplyTo() {
        return inReplyTo;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}

