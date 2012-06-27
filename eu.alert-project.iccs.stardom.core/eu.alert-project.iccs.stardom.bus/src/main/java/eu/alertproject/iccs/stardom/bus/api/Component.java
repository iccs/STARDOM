package eu.alertproject.iccs.stardom.bus.api;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 27/06/12
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class Component {

    private String component;
    private Integer issueId;

    public Component() {
    }

    public Component(String component, Integer issueId) {
        this.component = component;
        this.issueId = issueId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }


    @Override
    public String toString() {
        return "Component{" +
                "component='" + component + '\'' +
                ", issueId='" + issueId + '\'' +
                '}';
    }
}
