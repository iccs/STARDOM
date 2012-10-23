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
    private String parentId;

    public Component() {
    }

    public Component(String component, String parentId) {
        this.component = component;
        this.parentId = parentId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Component{" +
                "component='" + component + '\'' +
                ", issueId='" + parentId + '\'' +
                '}';
    }
}
