package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.activemqconnector.internal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * User: fotis
 * Date: 13/11/11
 * Time: 22:29
 */
@Controller
public class EventCountController {

    @Autowired
    MailNewMailListener mailNewMailListener;

    @Autowired
    ItsNewCommentListener itsNewCommentListener;

    @Autowired
    ItsNewIssueListener itsNewIssueListener;

    @Autowired
    ItsHistoryListener itsHistoryListener;


    @Autowired
    ScmNewCommitListener scmNewCommitListener;

    @RequestMapping(value = "/constructor/events/count", method = RequestMethod.GET)
    public @ResponseBody
    Map<String,Integer> getCount(){


        Map<String,Integer> ret = new HashMap<String, Integer>();

        ret.put("ml",mailNewMailListener.getMessageCount());
        ret.put("its",itsNewCommentListener.getMessageCount()+itsNewIssueListener.getMessageCount()+itsHistoryListener.getMessageCount());
        ret.put("scm",scmNewCommitListener.getMessageCount());

        return ret;


    }


}
