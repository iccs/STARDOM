package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.CommitNewAnnotatedEnvelope;
import eu.alertproject.iccs.events.alert.KesiSCM;
import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.alert.MdServiceSCM;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.activemqconnector.api.ALERTActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEvent;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.DefaultScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmFile;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 12:20
 */
@Component("commitNewAnnotatedListener")
public class CommitNewAnnotatedListener extends ALERTActiveMQListener {


    @Autowired
    Properties systemProperties;

    private Logger logger = LoggerFactory.getLogger(CommitNewAnnotatedListener.class);

    @Override
    public void processXml(String xml) {

        ScmConnectorContext context =new ScmConnectorContext();

        CommitNewAnnotatedEnvelope commitNewAnnotatedEnvelope = EventFactory.<CommitNewAnnotatedEnvelope>fromXml(xml,
                CommitNewAnnotatedEnvelope.class);

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


        context.setAction(scmAction);

        if(isIgnoredBasedOnDate(kesi.getDate())){return;}

        fixProfile(context);
        ScmEvent scmEvent = new ScmEvent(this,context);
        logger.trace("void onMessage() {}/{} {} ",new Object[]{getMessageSentCount(),getMessageCount(),scmEvent});
        Bus.publish(scmEvent);

    }

    private List<ScmFile> extractToScmFiles(KesiSCM kesi) {
        List<ScmFile> scmFiles = new ArrayList<ScmFile>();

        List<KesiSCM.File> files = kesi.getFiles();

        for(KesiSCM.File file : files){

            ScmFile scmFile = new ScmFile();
            scmFile.setName(String.valueOf(file.getId()));
                
            List<String> functions = new ArrayList<String>();

            List<KesiSCM.File.Module> modules = file.getModules();
            
            for(KesiSCM.File.Module mod : modules){
                List<KesiSCM.File.Module.Methods> modMethods = mod.getMethods();
                
                for(KesiSCM.File.Module.Methods meth: modMethods ){

                    functions.add(meth.getName());
                }

            }

            scmFile.setFunctions(functions);
           
            scmFiles.add(scmFile);
        }

        return scmFiles;
    }

}
