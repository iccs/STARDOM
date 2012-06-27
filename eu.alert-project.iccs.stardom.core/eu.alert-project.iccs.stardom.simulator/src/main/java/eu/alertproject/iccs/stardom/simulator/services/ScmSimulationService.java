package eu.alertproject.iccs.stardom.simulator.services;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.alert.CommitNewAnnotatedEnvelope;
import eu.alertproject.iccs.events.api.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 27/06/12
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
@Service("scmSimulationService")
public class ScmSimulationService {


    private Logger logger = LoggerFactory.getLogger(ScmSimulationService.class);

    private JmsTemplate template;
    int counter = 0;
    int errors=0;

    public void start(){


        CommitNewAnnotatedEnvelope  cna= new CommitNewAnnotatedEnvelope();





        /**
         KesiSCM kesi = commitNewAnnotatedEnvelope
         .getBody()
         .getNotify()
         .getNotificationMessage()
         .getMessage()
         .getEvent()
         .getPayload()
         .getEventData()
         .getKesi();

         Keui keui = commitNewAnnotatedEnvelope
         .getBody()
         .getNotify()
         .getNotificationMessage()
         .getMessage()
         .getEvent()
         .getPayload()
         .getEventData()
         .getKeui();

         MdServiceSCM mdService = commitNewAnnotatedEnvelope
         .getBody()
         .getNotify()
         .getNotificationMessage()
         .getMessage()
         .getEvent()
         .getPayload()
         .getEventData()
         .getMdService();


         context.setProfile(
         ALERTUtils.extractProfile(
         kesi.getAuthor(),
         mdService.getAuthorUri(),
         "scm"
         ));

         DefaultScmAction scmAction = new DefaultScmAction();
         scmAction.setComment(kesi.getMessageLog());
         scmAction.setDate(kesi.getDate());
         scmAction.setType(ScmAction.RepositoryType.Git);
         scmAction.setFiles(extractToScmFiles(kesi));
         scmAction.setConcepts(keui.getCommitMessageLogConcepts());
          */



//        template.send(
//                Topics.ALERT_METADATA_CommitNew_Updated,
//                new TextMessageCreator(""));

    }



}
