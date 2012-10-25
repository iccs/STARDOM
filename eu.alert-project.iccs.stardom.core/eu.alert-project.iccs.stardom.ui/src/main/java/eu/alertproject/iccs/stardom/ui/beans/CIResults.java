package eu.alertproject.iccs.stardom.ui.beans;

import eu.alertproject.iccs.stardom.domain.api.Profile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/25/12
 * Time: 5:19 PM
 */
public class CIResults {


    private String uuid;
    private List<MetricResult> metrics;
    private Collection<Profile> profiles;
    private Map<String, Double> ciPerClass;


    public CIResults(String uuid, List<MetricResult> metrics, Collection<Profile> profiles, Map<String, Double> ciPerClass) {
        this.uuid = uuid;
        this.metrics = metrics;
        this.profiles = profiles;
        this.ciPerClass = ciPerClass;
    }

    public String getUuid() {
        return uuid;
    }

    public List<MetricResult> getMetrics() {
        return metrics;
    }

    public Collection<Profile> getProfiles() {
        return profiles;
    }

    public Map<String, Double> getCiPerClass() {
        return ciPerClass;
    }
}
