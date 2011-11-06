package eu.alertproject.iccs.stardom.connector.api;

import org.apache.commons.lang.StringUtils;

import java.awt.color.ICC_ColorSpace;

/**
 * User: fotis
 * Date: 05/11/11
 * Time: 17:43
 */
public class Topics {

    private static String DOT= ".";
    private static String PARENT=  "ICCS";

    private static String ITS= "Its";
    private static String SCM= "Scm";
    private static String ML = "MailingList";


    public final static String IccsItsNewIssue=PARENT+DOT+ITS+DOT+"NewIssue";
    public final static String IccsItsNewComment=PARENT+DOT+ITS+DOT+"NewComment";

    public final static String IccsScmNewIssue=PARENT+DOT+SCM+DOT+"NewCommit";

    public final static String IccsMlNewMail=PARENT+DOT+ML+DOT+"NewEmail";


}
