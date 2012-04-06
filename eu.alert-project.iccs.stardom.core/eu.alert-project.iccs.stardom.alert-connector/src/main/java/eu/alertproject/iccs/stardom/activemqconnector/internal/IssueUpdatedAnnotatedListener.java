package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.*;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsChangeEvent;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsCommentEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsChangeAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsCommentAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsChangeConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsCommentConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 25/02/12
 * Time: 15:09
 */
@Component("issueUpdatedAnnotatedListener")
public class IssueUpdatedAnnotatedListener extends ALERTActiveMQListener{
    private Logger logger = LoggerFactory.getLogger(IssueUpdatedAnnotatedListener.class);

    @Override
    public void processXml(String xml){

        logger.trace("void processXml() {}",xml);
        IssueUpdateAnnotatedEnvelope envelope =  EventFactory.<IssueUpdateAnnotatedEnvelope>
                fromXml(xml,IssueUpdateAnnotatedEnvelope.class);


        IssueUpdateAnnotatedPayload.EventData eventData = envelope
                .getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData();


        KesiITS kesi = eventData.getKesi();
        MdServiceITS mdService = eventData.getMdService();
        Keui keui = eventData.getKeui();


        handleComments(kesi,mdService,keui);
        handleChange(kesi, mdService, keui);

    }

    private void handleChange(KesiITS kesi, MdServiceITS mdService, Keui keui) {

        if(isIgnoredBasedOnDate(kesi.getDateOpened())){return;}

        Profile p= new Profile();
        p.setEmail(kesi.getActivity().getWho());
        p.setUri(mdService.getActivity().iterator().next().getWhoUri());

        //process the issue here
        List<KesiITS.Activity.ActivityWRA> activityWRAs = kesi.getActivity().getActivityWRAs();

        for(KesiITS.Activity.ActivityWRA activityWRA: activityWRAs){

            ItsChangeConnectorContext context = new ItsChangeConnectorContext();
            context.setProfile(p);

            DefaultItsChangeAction defaultItsChangeAction = new DefaultItsChangeAction();
            defaultItsChangeAction.setWhat(activityWRA.getWhat());
            defaultItsChangeAction.setRemoved(activityWRA.getRemoved());
            defaultItsChangeAction.setAdded(activityWRA.getAdded());
            defaultItsChangeAction.setDate(kesi.getActivity().getDate());
            defaultItsChangeAction.setBugId(kesi.getId());
            fixProfile(context);

            context.setAction(defaultItsChangeAction);
            Bus.publish(new ItsChangeEvent(this,context));

        }

    }

    private void handleComments(KesiITS kesi, MdServiceITS mdService, Keui keui) {

        List<KesiITS.Comment> comments = kesi.getComments();
        if(comments == null || comments.size() <=0){
            return;
        }

        MdServiceITS.Comment mdServiceComment = mdService.getComment();


        for(KesiITS.Comment comment : comments){

            if(isIgnoredBasedOnDate(comment.getDate())){
                continue;
            }

            ItsCommentConnectorContext context =new ItsCommentConnectorContext();

            DefaultItsCommentAction commentAction = new DefaultItsCommentAction();

            context.setProfile(ALERTUtils.extractProfile(
                    comment.getPerson(),
                    mdServiceComment.getPersonUri(),
                    "its"));

            logger.trace("void handleIssue() Commenter {} ",context.getProfile());
            commentAction.setDate(comment.getDate());
            commentAction.setText(comment.getText());
            commentAction.setConcepts(keui.getCommentTextConcepts());

            context.setAction(commentAction);
            Bus.publish(new ItsCommentEvent(this,context));

        }

    }

}
