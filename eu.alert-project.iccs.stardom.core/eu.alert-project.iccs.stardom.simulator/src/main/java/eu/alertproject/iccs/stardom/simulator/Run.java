package eu.alertproject.iccs.stardom.simulator;

import eu.alertproject.iccs.stardom.simulator.services.InputStreamIssuesVisitor;
import eu.alertproject.iccs.stardom.simulator.services.InputStreamMailVisitor;
import eu.alertproject.iccs.stardom.simulator.services.ZipSimulationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import java.io.IOException;
import java.util.Properties;

/**
 * User: fotis
 * Date: 19/04/12
 * Time: 17:52
 */
public class Run {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext(
          "/applicationContext.xml"
        );

        Properties systemProperties = (Properties) context.getBean("systemProperties");
        ZipSimulationService service = (ZipSimulationService) context.getBean("zipSimulationService");
        JmsTemplate template= (JmsTemplate) context.getBean("jmsTemplate");

        try {

            service.start(systemProperties.getProperty("keui.issues.path"),new InputStreamIssuesVisitor(template));
            service.start(systemProperties.getProperty("keui.mailnew.path"),new InputStreamMailVisitor(template));


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}
