package eu.alertproject.iccs.stardom.simulator;

import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.stardom.simulator.services.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import java.io.File;
import java.util.Properties;

/**
 * User: fotis
 * Date: 19/04/12
 * Time: 17:52
 */
public class Run {

    private static Logger logger = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) {

        if(args == null || args.length < 1 ){
            System.out.println("Please enter either kde, optimis or petals as per you configuration file");
        }

        ApplicationContext context = new ClassPathXmlApplicationContext(
          "/applicationContext.xml"
        );

        Properties systemProperties = (Properties) context.getBean("systemProperties");
        String repo = args[0];


        logger.info("Extracting information for {} ",repo);

        String basePath = systemProperties.getProperty("data.basePath")+File.separator;
        String commitFile = basePath+systemProperties.getProperty(repo+".path.commits");
        String issuesNewFile = basePath+systemProperties.getProperty(repo+".path.issuesNew");
        String issuesUpdateFile = basePath+systemProperties.getProperty(repo+".path.issuesUpdate");
        String mailNewFile = basePath+systemProperties.getProperty(repo+".path.mailnew");
        String forumPostNewFile = basePath+systemProperties.getProperty(repo+".path.forums");


        handleFile(context,Topics.ALERT_METADATA_CommitNew_Updated,commitFile);
        handleFile(context,Topics.ALERT_METADATA_IssueNew_Updated,issuesNewFile);
        handleFile(context,Topics.ALERT_METADATA_IssueUpdate_Updated,issuesUpdateFile);
        handleFile(context,Topics.ALERT_METADATA_MailNew_Updated,mailNewFile);
        handleFile(context,Topics.ALERT_METADATA_ForumPost_Updated,forumPostNewFile);


        System.exit(0);

    }

    private static void handleFile(ApplicationContext context, String topic, String commitFile) {

        Properties systemProperties = (Properties) context.getBean("systemProperties");

        if(StringUtils.isEmpty(commitFile)){
            return;
        }

        File file = new File(commitFile);

        if(file.exists() && file.canRead()){

            //determine service
            String absolutePath = file.getAbsolutePath();
            String service = "zipSimulationService";
            if(absolutePath.toLowerCase().endsWith("tar.gz")){
                service = "targzSimulationService";
            }else if(absolutePath.toLowerCase().endsWith("rar")){
                service = "rarSimulationService";
            }else if(absolutePath.toLowerCase().endsWith("zip")){
                service = "zipSimulationService";
            }else{
                logger.error("Couldn't find service for this file");
                return;
            }

            logger.info("Using service {} ",service);

            ((SimulationService)context.getBean(service)).start(
                    absolutePath,
                    new InputStreamTopicVisitor(topic,
                            Boolean.valueOf(systemProperties.getProperty("activemq.processDisabled")),
                            (JmsTemplate) context.getBean("jmsTemplate")));


        }else{
            logger.error("Couldn't handle {} ",commitFile);
        }
    }

}
