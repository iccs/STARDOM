package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.domain.api.IssueMetadata;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/15/12
 * Time: 3:13 PM
 */
public interface PersistanceService {
    IssueMetadata getForIssue(String issueUri);
    void recordIssueMetadata(Integer issueId, String componentId, String subject, Date dateCreated, String uri, String componentUri);
}
