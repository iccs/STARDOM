package eu.alertproject.iccs.stardom.classification;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * User: fotis
 * Date: 01/04/12
 * Time: 13:57
 */
@XStreamAlias("ci")
public class CI {
    
    @XStreamImplicit(itemFieldName = "classifier")
    private List<Classifier> classifiers;

    public List<Classifier> getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(List<Classifier> classifiers) {
        this.classifiers = classifiers;
    }

    public static class Classifier{

        @XStreamAsAttribute
        private String name;

        @XStreamAsAttribute
        private Double prob = 0.0;

        @XStreamImplicit(itemFieldName = "metric")
        List<Metric> metrics;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getProb() {
            return prob;
        }

        public void setProb(Double prob) {
            this.prob = prob;
        }

        public List<Metric> getMetrics() {
            return metrics;
        }

        public void setMetrics(List<Metric> metrics) {
            this.metrics = metrics;
        }

        public static class Metric{

            @XStreamAsAttribute
            private String name;



            @XStreamAlias("s")
            private Double standardDeviation;

            @XStreamAlias("c")
            private Double mean;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }


            public Double getStandardDeviation() {
                return standardDeviation;
            }

            public void setStandardDeviation(Double standardDeviation) {
                this.standardDeviation = standardDeviation;
            }

            public Double getMean() {
                return mean;
            }

            public void setMean(Double mean) {
                this.mean = mean;
            }
        }
    }

}
