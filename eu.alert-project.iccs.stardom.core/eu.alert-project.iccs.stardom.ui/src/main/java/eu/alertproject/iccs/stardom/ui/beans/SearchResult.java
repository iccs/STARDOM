package eu.alertproject.iccs.stardom.ui.beans;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.Profile;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: fotis
 * Date: 18/01/12
 * Time: 20:33
 */
public class SearchResult {


    private Integer identity;
    private List<MetricResult> metrics;
    private Collection<Profile> profiles;


    public SearchResult(Integer identity, List<MetricResult> metrics, Collection<Profile> profiles) {
        this.identity = identity;
        this.metrics = metrics;
        this.profiles = profiles;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public List<MetricResult> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricResult> metrics) {
        this.metrics = metrics;
    }

    public Collection<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Collection<Profile> profiles) {
        this.profiles = profiles;
    }
}
