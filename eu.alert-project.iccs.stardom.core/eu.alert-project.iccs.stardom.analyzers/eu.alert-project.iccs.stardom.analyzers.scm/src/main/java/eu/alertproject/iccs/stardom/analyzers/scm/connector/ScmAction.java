package eu.alertproject.iccs.stardom.analyzers.scm.connector;

import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 12/07/11
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
public interface ScmAction extends ConnectorAction {

    RepositoryType getType();

    String getUid();

    String getRevission();

    List<ScmFile> getFiles();

    String getComment();

    enum RepositoryType{Git,Svn,Cvs}

}
