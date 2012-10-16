package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.datastore.api.dao.IssueMetadataDao;
import eu.alertproject.iccs.stardom.domain.api.IssueMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/15/12
 * Time: 3:13 PM
 */
@Service("persistanceService")
public class PersistanceServiceImpl implements PersistanceService{
    private Logger logger = LoggerFactory.getLogger(PersistanceServiceImpl.class);

    @Autowired
    IssueMetadataDao issueMetadataDao;

    @Override
    @Transactional
    public void recordIssueMetadata(Integer issueId, String componentId, String subject, Date dateCreated, String uri, String componentUri) {

        IssueMetadata byIssueUri = issueMetadataDao.findByIssueUri(uri);
        if(byIssueUri !=null){
            logger.info("Issue metadata already existed for issue {}",uri);
            return;
        }


        logger.info("Creating an issue metadata {} ",uri);
        IssueMetadata issueMetadata = new IssueMetadata();
        issueMetadata.setIssueId(String.valueOf(issueId));
        issueMetadata.setIssueCreated(dateCreated);
        issueMetadata.setSubject(subject);
        issueMetadata.setIssueUri(uri);
        issueMetadata.setComponent(componentId);
        issueMetadata.setComponentUri(componentUri);

        issueMetadataDao.insert(issueMetadata);


    }

    @Override
    @Transactional
    public IssueMetadata getForIssue(String issueUri) {

        return issueMetadataDao.findByIssueUri(issueUri);

    }
}
