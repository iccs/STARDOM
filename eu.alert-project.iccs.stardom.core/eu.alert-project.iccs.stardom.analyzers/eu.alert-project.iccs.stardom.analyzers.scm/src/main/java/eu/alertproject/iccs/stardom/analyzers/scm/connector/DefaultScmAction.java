package eu.alertproject.iccs.stardom.analyzers.scm.connector;

import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 14:44
 */
public class DefaultScmAction implements ScmAction {

    private Date date;
    private RepositoryType type;

    private String uid;
    private String revission;

    private List<ScmFile> files;
    private String comment;


    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public RepositoryType getType() {
        return type;
    }

    public void setType(RepositoryType type) {
        this.type = type;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getRevission() {
        return revission;
    }

    public void setRevission(String revission) {
        this.revission = revission;
    }

    @Override
    public List<ScmFile> getFiles() {
        return files;
    }

    public void setFiles(List<ScmFile> files) {
        this.files = files;
    }

    @Override
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
