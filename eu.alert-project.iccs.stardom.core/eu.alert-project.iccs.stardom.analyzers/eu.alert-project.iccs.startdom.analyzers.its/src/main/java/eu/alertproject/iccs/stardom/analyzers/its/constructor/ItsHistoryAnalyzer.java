package eu.alertproject.iccs.stardom.analyzers.its.constructor;

import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsAction;
import eu.alertproject.iccs.stardom.domain.api.Identity;

/**
 * User: fotis
 * Date: 26/08/11
 * Time: 21:40
 */
public class ItsHistoryAnalyzer extends AbstractItsAnalyzer{
    @Override
    public void analyze(Identity identity, ItsAction action) {

        if(identity ==null){
            return;
        }

    }
}
