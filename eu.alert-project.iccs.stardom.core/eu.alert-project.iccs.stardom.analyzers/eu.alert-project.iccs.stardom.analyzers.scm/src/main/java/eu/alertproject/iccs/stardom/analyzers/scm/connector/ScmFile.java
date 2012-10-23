package eu.alertproject.iccs.stardom.analyzers.scm.connector;

import eu.alertproject.iccs.events.alert.KesiSCM;

import java.util.List;

/**
 * User: fotis
 * Date: 13/07/11
 * Time: 13:41
 */
public class ScmFile {

    private String name;
    private List<String> functions;
    private List<String> modules;


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

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }
}
