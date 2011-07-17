package eu.alertproject.iccs.stardom.constructor.api;

import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:34
 */
@Service("analyzers")
public class Analyzers {

    private List<Analyzer<ConnectorAction>> analyzers;


    public Analyzers() {
        this.analyzers = new ArrayList<Analyzer<ConnectorAction>>();
    }

    public List<Analyzer<ConnectorAction>> getAnalyzers() {
        return analyzers;
    }

    public boolean add(Analyzer<ConnectorAction> analyzer) {
        return analyzers.add(analyzer);
    }


}
