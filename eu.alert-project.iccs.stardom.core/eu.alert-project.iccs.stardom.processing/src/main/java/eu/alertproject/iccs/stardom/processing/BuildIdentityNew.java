package eu.alertproject.iccs.stardom.processing;

import com.sun.tools.doclets.internal.toolkit.util.SourceToHTMLConverter;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.processing.services.IdentityService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/27/12
 * Time: 12:35 AM
 */
public class BuildIdentityNew {


    public static void main(String[] args) throws IOException {

        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");


        IdentityService identityService = (IdentityService) context.getBean("identityService");


        Integer integer = identityService.buildXmlEvents("/tmp/cim");


        System.out.println("Created "+integer+" events into /tmp/cim");

    }
}
