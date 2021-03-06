package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.*;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.activemqconnector.api.STARDOMActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsCommentEvent;
import eu.alertproject.iccs.stardom.analyzers.its.bus.ItsEvent;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.DefaultItsCommentAction;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsCommentConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.its.connector.ItsConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @Topic eu.alertproject.iccs.events.api.Topics.ALERT_METADATA_IssueNew_Updated
 *
 * User: fotis
 * Date: 25/02/12
 * Time: 15:09
 */
@Component("issueNewAnnotatedListener")
public class IssueNewAnnotatedListener extends STARDOMActiveMQListener {

    private Logger logger = LoggerFactory.getLogger(IssueNewAnnotatedListener.class);

    @Autowired
    PersistanceService persistanceService;

    @Override
    public void processXml(String xml){


        logger.trace("void onMessage() Text to parse {} ",xml);
//        context= mapper.readValue(IOUtils.toInputStream(text),ItsConnectorContext.class);


        IssueNewAnnotatedEnvelope envelope = EventFactory.<IssueNewAnnotatedEnvelope>fromXml(xml, IssueNewAnnotatedEnvelope.class);

        IssueNewAnnotatedPayload.EventData eventData = envelope
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

        persistanceService.recordIssueMetadata(
                kesi.getId(),
                kesi.getProduct() == null ? "None" : kesi.getProduct().getComponentId(),
                kesi.getSummary() == null ? kesi.getDescription() : "None",
                kesi.getDateOpened(),
                mdService.getUri(),
                kesi.getProduct() == null ? "None" : mdService.getProduct().getComponentUri()
        );

        handleIssue(kesi, mdService, keui);
        handleComments(kesi,mdService,keui);


    }



    private void handleIssue(KesiITS kesi, MdServiceITS mdService, Keui keui) {

        if(isIgnoredBasedOnDate(kesi.getDateOpened())){return;}

        ItsConnectorContext context =new ItsConnectorContext();

        context.setProfile(
                ALERTUtils.extractProfile(
                        kesi.getAuthor(),
                        mdService.getAuthorUri(),
                        "its"
                )
        );

        DefaultItsAction itsAction = new DefaultItsAction();
        itsAction.setAssigned(
                ALERTUtils.extractProfile(
                        kesi.getAssignedTo(),
                        mdService.getAssignedToUri(),
                        "its"
                )
        );

        logger.trace("void handleComments() Assigned {}",itsAction.getAssigned());


        itsAction.setSubject(
                kesi.getSummary() ==null ?
                        kesi.getDescription() : "None"
                );
        itsAction.setComponent(
                kesi.getProduct() == null ? "None" : kesi.getProduct().getComponentId());
        itsAction.setBugId(kesi.getId());
        itsAction.setBugStatus(kesi.getStatus());
        itsAction.setDate(kesi.getDateOpened());
        itsAction.setReporter(

                ALERTUtils.extractProfile(
                        kesi.getAuthor(),
                        mdService.getAuthorUri(),
                        "its"
                )
        );
        logger.trace("void handleComments() Reporter {}",itsAction.getReporter());


        itsAction.setResolution(kesi.getResolution());
        itsAction.setSeverity(kesi.getSeverity());

        itsAction.setConcepts(keui.getIssueDescriptionConcepts());
        context.setAction(itsAction);

        fixProfile(context);
        Bus.publish(new ItsEvent(this,context));

    }

    private void handleComments(KesiITS kesi, MdServiceITS mdService, Keui keui) {

        List<KesiITS.Comment> comments = kesi.getComments();
        if(comments == null || comments.size() <=0){
            return;
        }

        List<MdServiceITS.Comment> comment1 = mdService.getComment();
        Iterator<MdServiceITS.Comment> commentIterator = comment1.iterator();


        for(KesiITS.Comment comment : comments){

            if(isIgnoredBasedOnDate(comment.getDate())){
                continue;
            }

            ItsCommentConnectorContext context =new ItsCommentConnectorContext();

            DefaultItsCommentAction commentAction = new DefaultItsCommentAction();


            MdServiceITS.Comment next = commentIterator.next();

            context.setProfile(ALERTUtils.extractProfile(
                    comment.getPerson(),
                    next.getPersonUri(),
                    "its"));

            logger.trace("void handleIssue() Commenter {} ",context.getProfile());
            commentAction.setBugId(kesi.getId());
            commentAction.setDate(comment.getDate());
            commentAction.setText(comment.getText());
            commentAction.setComponent(
                    kesi.getProduct() == null ? "None" : kesi.getProduct().getComponentId());

            commentAction.setSubject(
                    kesi.getSummary() ==null ?
                            kesi.getDescription() : "None"
            );

            commentAction.setConcepts(keui.getCommentTextConcepts());
            context.setAction(commentAction);
            Bus.publish(new ItsCommentEvent(this,context));

        }

    }

}
