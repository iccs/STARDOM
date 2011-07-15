package eu.alertproject.iccs.stardom.alertconnector.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 13:38
 */
public class AlertConstructorConnector {

    private Logger logger = LoggerFactory.getLogger(AlertConstructorConnector.class);
//
//
//    @Override
//    @RequestMapping(value = "/constructor/action/scm", method = RequestMethod.POST)
//    public @ResponseBody void scmAction(@RequestBody ScmConnectorContext context) {
//        logger.trace("void scmAction() {} {}",context.getProfile(),context.getAction());
//
//        //create the action
//
//        ScmEvent scmEvent = new ScmEvent(this,context);
//
//        Bus.publish(scmEvent);
//
//    }
//
//    @Override
//    @RequestMapping(value = "/constructor/action/mail", method = RequestMethod.POST)
//    public @ResponseBody void mailingListCommentAction(@RequestBody MailingListConnectorContext context) {
//
//        logger.trace("void mailingListCommentAction() {},{}",context.getAction(),context.getProfile());
//        MailingEvent mailingEvent = new MailingEvent(this,context);
//        Bus.publish(mailingEvent);
//
//
//    }
//
//    @Override
//    @RequestMapping(value = "/constructor/action/its", method = RequestMethod.POST)
//    public @ResponseBody void itsAction(@RequestBody ItsConnectorContext context) {
//
//        logger.trace("void itsAction() {} {}",context.getAction(),context.getProfile());
//        ItsEvent itsEvent = new ItsEvent(this,context);
//        Bus.publish(itsEvent);
//
//    }
}
