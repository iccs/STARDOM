package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.IssueMetadataDao;
import eu.alertproject.iccs.stardom.domain.api.IssueMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/15/12
 * Time: 3:11 PM
 */
@Repository("issueMetadataDao")
public class JpaIssueMetadataDao extends JpaCommonDao<IssueMetadata> implements IssueMetadataDao{

    private Logger logger = LoggerFactory.getLogger(JpaIssueMetadataDao.class);

    protected JpaIssueMetadataDao() {
        super(IssueMetadata.class);
    }

    @Override
    public IssueMetadata findByIssueUri(String issueUri) {

        IssueMetadata meta= null;

        try {
            meta = (IssueMetadata) getEntityManager().createQuery("SELECT i FROM IssueMetadata i WHERE i.issueUri=:uri")
                    .setParameter("uri", issueUri)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            logger.warn("Couldn't find issue metadata for {} ",issueUri);
            logger.trace("Reason is ",e);
        }

        return meta;

    }
}
