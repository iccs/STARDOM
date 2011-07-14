package eu.alertproject.iccs.stardom.connector.api;

import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:44
 */
public class DefaultScmAction implements ScmAction{

    private Date date;
    private RepositoryType type;

    private String uid;
    private String revission;

    private List<ScmFile> files;
    private String comment;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RepositoryType getType() {
        return type;
    }

    public void setType(RepositoryType type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRevission() {
        return revission;
    }

    public void setRevission(String revission) {
        this.revission = revission;
    }

    public List<ScmFile> getFiles() {
        return files;
    }

    public void setFiles(List<ScmFile> files) {
        this.files = files;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "DefaultScmAction{" +
                "date=" + date +
                ", type=" + type +
                ", uid='" + uid + '\'' +
                ", revission='" + revission + '\'' +
                ", files=" + files +
                ", comment='" + comment + '\'' +
                '}';
    }
}
