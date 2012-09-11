package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.stardom.activemqconnector.api.STARDOMActiveMQListener;
import org.springframework.stereotype.Component;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 15:09
 */
@Component("forumAnnotatedListener")
public class ForumAnnotatedListener extends STARDOMActiveMQListener {

    @Override
    public void processXml(String xml){

        //process the event here here


    }


}
