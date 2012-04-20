package eu.alertproject.iccs.stardom.simulator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * User: fotis
 * Date: 19/04/12
 * Time: 17:57
 */
@Service("zipSimulationService")
public class ZipSimulationService {

    private Logger logger = LoggerFactory.getLogger(ZipSimulationService.class);

    int elementCounter = 0;
    public void start(String path, InputStreamVisitor visitor) throws IOException {

        ZipFile zipFile = new ZipFile(path);


        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while(entries.hasMoreElements()) {

            ZipEntry z = entries.nextElement();

            logger.trace("void start() {} ",z.getName());
            if(z.getName().endsWith(".xml")){
                visitor.handle(zipFile.getInputStream(z));
                elementCounter++;
            }else{
                logger.info("void start() Not an xml file {}", z.getName());
            }


        }


        logger.trace("void start() Handled {} events",elementCounter);





    }

}
