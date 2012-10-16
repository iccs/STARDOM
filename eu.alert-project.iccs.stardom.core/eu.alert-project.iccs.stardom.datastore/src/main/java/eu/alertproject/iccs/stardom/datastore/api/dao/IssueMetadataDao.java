package eu.alertproject.iccs.stardom.datastore.api.dao;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.stardom.domain.api.IssueMetadata;
import eu.alertproject.iccs.stardom.domain.api.ml.ItsMl;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/15/12
 * Time: 3:10 PM
 */
public interface IssueMetadataDao extends CommonDao<IssueMetadata> {
    IssueMetadata findByIssueUri(String issueUri);
}
