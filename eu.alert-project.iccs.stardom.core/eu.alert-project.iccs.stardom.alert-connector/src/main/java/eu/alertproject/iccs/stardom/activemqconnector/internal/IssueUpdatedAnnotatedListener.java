package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.*;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.activemqconnector.api.STARDOMActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsChangeEvent;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsCommentEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsChangeAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsCommentAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsChangeConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsCommentConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @Topic eu.alertproject.iccs.events.api.Topics.ALERT_METADATA_IssueUpdate_Updated
 * @Topic eu.alertproject.iccs.events.api.Topics.ALERT_METADATA_ForumPost_Updated
 *
 * User: fotis
 * Date: 25/02/12
 * Time: 15:09
 */
@Component("issueUpdatedAnnotatedListener")
public class IssueUpdatedAnnotatedListener extends STARDOMActiveMQListener {
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

        List<KesiITS.Activity> activity = kesi.getActivity();

        if(activity == null ){

            return;
        }

        Iterator<MdServiceITS.Activity> mdIterator = null;

        if(mdService !=null){
            mdIterator = mdService.getActivity().iterator();
        }


        String component = kesi.getProduct().getComponentId();

        for(KesiITS.Activity a :activity){


            String whoUri = "owl#none";
            if(mdIterator !=null){
                whoUri = mdIterator.next().getWhoUri();
            }

            Profile p= new Profile();
            p.setUsername(a.getWho());
            p.setEmail(a.getWho());
            p.setUri(whoUri);

            ItsChangeConnectorContext context = new ItsChangeConnectorContext();
            context.setProfile(p);


            DefaultItsChangeAction defaultItsChangeAction = new DefaultItsChangeAction();
            defaultItsChangeAction.setWhat(a.getActivityWhat());
            defaultItsChangeAction.setComponent(component);
            defaultItsChangeAction.setRemoved(a.getActivityRemoved());
            defaultItsChangeAction.setAdded(a.getActivityAdded());
            defaultItsChangeAction.setDate(a.getDate());
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


        Iterator<MdServiceITS.Comment> commentIterator = null;
        if(mdService !=null){
            commentIterator = mdService.getComment().iterator();
        }


        Iterator<Keui.Comment> keuiCommentIterator = keui.getIssueComment().iterator();


        String component = kesi.getProduct().getComponentId();

        for(KesiITS.Comment comment : comments){


            if(isIgnoredBasedOnDate(comment.getDate())){
                continue;
            }

            ItsCommentConnectorContext context =new ItsCommentConnectorContext();

            DefaultItsCommentAction commentAction = new DefaultItsCommentAction();
            
            String personUri =
                    (commentIterator == null ?
                            "owl#none":
                            commentIterator.next().getPersonUri());


            context.setProfile(ALERTUtils.extractProfile(
                    comment.getPerson(),
                    personUri,
                    "its"));

            logger.trace("void handleIssue() Commenter {} ",context.getProfile());
            commentAction.setDate(comment.getDate());
            commentAction.setText(comment.getText());
            commentAction.setComponent(component);
            commentAction.setBugId(kesi.getId());
            
            Keui.Comment keuiComment = keuiCommentIterator.next();
            commentAction.setConcepts(keuiComment.getConcepts());

            context.setAction(commentAction);
            Bus.publish(new ItsCommentEvent(this,context));

        }

    }

}
