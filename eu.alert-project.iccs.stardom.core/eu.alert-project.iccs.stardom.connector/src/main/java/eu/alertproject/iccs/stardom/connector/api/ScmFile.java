package eu.alertproject.iccs.stardom.connector.api;

import java.util.List;

/**
 * User: fotis
 * Date: 13/07/11
 * Time: 13:41
 */
public class ScmFile {

    private String name;
    private List<String> functions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFunctions() {
        return functions;
    }

    public void setFunctions(List<String> functions) {
        this.functions = functions;
    }
}
