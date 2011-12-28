/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.stardom.lsa.bugmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Kostas Christidis
 */

public class Bugzilla {

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
    private Bug bug;
}
