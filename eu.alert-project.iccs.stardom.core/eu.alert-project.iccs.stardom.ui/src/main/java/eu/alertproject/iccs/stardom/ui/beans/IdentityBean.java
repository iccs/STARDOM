package eu.alertproject.iccs.stardom.ui.beans;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;

import java.util.ArrayList;
import java.util.List;

/**
 * User: fotis
 * Date: 27/08/11
 * Time: 00:45
 */
public class IdentityBean {

    private Identity identity;
    private List<Metric> metrics;
    private double ci;

    public IdentityBean() {
        metrics = new ArrayList<Metric>();
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public double getCi() {
        return ci;
    }

    public void setCi(double ci) {
        this.ci = ci;
    }
}
