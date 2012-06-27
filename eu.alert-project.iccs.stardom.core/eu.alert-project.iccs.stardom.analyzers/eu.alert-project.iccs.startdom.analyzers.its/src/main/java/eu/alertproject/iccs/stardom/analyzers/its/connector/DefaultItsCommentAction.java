package eu.alertproject.iccs.stardom.analyzers.its.connector;

import eu.alertproject.iccs.events.alert.Keui;

import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:43
 */
public class DefaultItsCommentAction implements ItsAction{

    private Integer bugId;
    private Date date;
    private String text;
    private List<Keui.Concept> concepts;
    private String component;


    public Integer getBugId() {
        return bugId;
    }

    public void setBugId(Integer bugId) {
        this.bugId = bugId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Date getDate() {
        return date;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Keui.Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Keui.Concept> concepts) {
        this.concepts = concepts;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }
}
