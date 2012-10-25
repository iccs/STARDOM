package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.alert.CommitNewAnnotatedEnvelope;
import eu.alertproject.iccs.events.alert.KesiSCM;
import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.alert.MdServiceSCM;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.stardom.activemqconnector.api.STARDOMActiveMQListener;
import eu.alertproject.iccs.stardom.analyzers.scm.bus.ScmEvent;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.DefaultScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmAction;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmFile;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @Topic eu.alertproject.iccs.events.api.Topics.ALERT_METADATA_CommitNew_Updated
 *
 * User: fotis
 * Date: 05/04/12
 * Time: 12:20
 */
@Component("commitNewAnnotatedListener")
public class CommitNewAnnotatedListener extends STARDOMActiveMQListener {


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
                        kesi,
                        mdService,
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

        ScmEvent scmEvent = new ScmEvent(this,context);

        Bus.publish(scmEvent);

    }

    private List<ScmFile> extractToScmFiles(KesiSCM kesi) {
        List<ScmFile> scmFiles = new ArrayList<ScmFile>();

        List<KesiSCM.File> files = kesi.getFiles();


        if(files !=null && files.size() >0){

            for(KesiSCM.File file : files){

                ScmFile scmFile = new ScmFile();
                scmFile.setName(file.getFileName());

                List<String> functions = new ArrayList<String>();
                List<String> fileModules = new ArrayList<String>();
                List<KesiSCM.File.Module> modules = file.getModules();

                //modules may not exist
                if(modules !=null && modules.size() > 0){
                    for(KesiSCM.File.Module mod : modules){

                        if(!fileModules.contains(mod.getName())){
                            fileModules.add(mod.getName());
                        }

                        List<KesiSCM.File.Module.Methods> modMethods = mod.getMethods();

                        if(modMethods !=null && modMethods.size() >0){

                            for(KesiSCM.File.Module.Methods meth: modMethods ){

                                functions.add(meth.getName());
                            }
                        }

                    }

                }

                scmFile.setModules(fileModules);
                scmFile.setFunctions(functions);
                scmFiles.add(scmFile);
            }

        }

        return scmFiles;
    }

}
