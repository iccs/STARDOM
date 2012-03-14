package eu.alertproject.iccs.stardom.ui.beans;

/**
 * User: fotis
 * Date: 15/03/12
 * Time: 01:02
 */
public class Concept {
    String uri;
    Integer weight;

    public Concept() {
    }

    public Concept(String uri, Integer weight) {
        this.uri = uri;
        this.weight = weight;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Concept{" +
                "uri='" + uri + '\'' +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Concept concept = (Concept) o;

        if (uri != null ? !uri.equals(concept.uri) : concept.uri != null) return false;
        if (weight != null ? !weight.equals(concept.weight) : concept.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }
}
