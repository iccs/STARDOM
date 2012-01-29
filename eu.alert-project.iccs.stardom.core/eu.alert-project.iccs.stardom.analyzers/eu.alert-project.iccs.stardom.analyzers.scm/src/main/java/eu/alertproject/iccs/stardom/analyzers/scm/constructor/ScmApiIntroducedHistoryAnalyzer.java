package eu.alertproject.iccs.stardom.analyzers.scm.constructor;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmFile;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.AbstractQuantitativeHistoryAnalyzer;
import eu.alertproject.iccs.stardom.datastore.api.dao.PathSignatureHistoryDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.PathSignatureHistory;
import eu.alertproject.iccs.stardom.domain.api.metrics.ScmApiIntroducedMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: fotis
 * Date: 17/07/11
 * Time: 14:07
 */
public class ScmApiIntroducedHistoryAnalyzer extends AbstractQuantitativeHistoryAnalyzer<ScmAction, ScmApiIntroducedMetric> {

    private Logger logger = LoggerFactory.getLogger(ScmApiIntroducedHistoryAnalyzer.class);
 
    @Autowired
    PathSignatureHistoryDao pathSignatureHistoryDao;


    public ScmApiIntroducedHistoryAnalyzer() {
        super(ScmApiIntroducedMetric.class);
    }


    /**
     * The reason we are overriding this is because there is extra logic to the
     * ScmApiIntroduced metrics
     *
     * @param identity
     * @param action
     */
    @Override
    @Transactional
    public void analyze(Identity identity, ScmAction action) {

        if(identity ==null){
            return;
        }

        List<PathSignatureHistory> newFiles = new ArrayList<PathSignatureHistory>();
        
        //check if the api has been introduced before
        List<ScmFile> files = action.getFiles();
        
        for(ScmFile sf: files){

            String path = sf.getName();



            List<String> functions = sf.getFunctions();
            for(String signature: functions) {

                PathSignatureHistory byIdentityPathAndSignature = pathSignatureHistoryDao.findByIdentityPathAndSignature(
                        identity,
                        path,
                        signature);


                /**
                 * Increase the API introduced metric in CASE a path has not been recorded
                 * in the future
                 */
                if(byIdentityPathAndSignature == null ){


                    PathSignatureHistory pathSignatureHistory = new PathSignatureHistory();
                    pathSignatureHistory.setIdentity(identity);
                    pathSignatureHistory.setPath(path);
                    pathSignatureHistory.setSignature(signature);

                    pathSignatureHistoryDao.insert(pathSignatureHistory);

                    /*
                    Follow the normal logic
                     */
                    super.analyze(identity,action);

                }
            }

        }

    }

    @Override
    public boolean canHandle(ConnectorAction action) {
        return action instanceof ScmAction;
    }
}
