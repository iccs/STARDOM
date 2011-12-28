/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.stardom.lsa.extraction;

import com.thoughtworks.xstream.XStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Exception;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import eu.alertproject.iccs.stardom.lsa.bugmodel.Bug;
import eu.alertproject.iccs.stardom.lsa.bugmodel.Bugzilla;
import eu.alertproject.iccs.stardom.lsa.bugmodel.Cc;
import eu.alertproject.iccs.stardom.lsa.bugmodel.LongDescType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Kostas Christidis
 */
public class CrawlConnector {

    private Logger logger = LoggerFactory.getLogger(CrawlConnector.class);
    private ArrayList<String> bugDescs = new ArrayList<String>();
    private ArrayList bugIds;

    public CrawlConnector(ArrayList<String> bugIds) {
        this.bugIds = bugIds;
    }

    public ArrayList<String> getBugDescs() {
        return bugDescs;
    }

    public void crawl() throws Exception {
        if (bugIds == null) {
            throw new Exception();
        }
        XStream xstream = new XStream();
        xstream.alias("bugzilla", Bugzilla.class);
        xstream.alias("bug", Bug.class);
        xstream.alias("cc", Cc.class);
        xstream.alias("long_desc", LongDescType.class);
        xstream.addImplicitCollection(Bug.class, "ccs", "cc", Cc.class);
        xstream.addImplicitCollection(Bug.class, "long_descs", "long_desc", LongDescType.class);
        
        ListIterator bugIdItr= bugIds.listIterator();
        while (bugIdItr.hasNext()){
        URL url = new URL("http://bugs.kde.org/show_bug.cgi?ctype=xml&id=" + bugIdItr.next());
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                url.openStream()));

        Bugzilla newBugzilla = (Bugzilla) xstream.fromXML(in);//return parsed object
        Bug newBug = newBugzilla.getBug();
        Iterator descsItr =  newBug.getLong_descs().iterator();
        String desc="";
        while (descsItr.hasNext()){
            String currDesc=((LongDescType) descsItr.next()).getTheText();
            logger.trace("\n === Bug Description === \n {} \n ", currDesc);
             desc = desc + currDesc;
        }
        bugDescs.add(desc);
        }
        

    }
}
