package eu.alertproject.iccs.stardom.ui.beans;

/**
 * User: fotis
 * Date: 18/01/12
 * Time: 22:54
 */
public class MetricResult {
    private int id;
    private String name;
    private Integer value;

    public MetricResult(int id, String name, Integer value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
