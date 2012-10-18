package eu.alertproject.iccs.stardom.simulator.services;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/12/12
 * Time: 2:41 PM
 */
@Service("rarSimulationService")
public class RarSimulatorService implements SimulationService{

    private Logger logger = LoggerFactory.getLogger(RarSimulatorService.class);


    @Override
    public int start(String path, InputStreamVisitor streamIssuesVisitor){

        int entries=0;
        FileOutputStream fos =null;
        FileInputStream inputStream=null;
        try {

            logger.info("Handling path {} ", path);
            Archive archive = new Archive(new File(path));
            FileHeader fh = archive.nextFileHeader();


            while(fh != null){

                File stardom = File.createTempFile("stardom", "rar-simulator");
                stardom.createNewFile();
                if(fh.getFileNameString().toLowerCase().endsWith(".xml")){

                    fos = new FileOutputStream(stardom);
                    archive.extractFile(fh, fos);


                    inputStream = new FileInputStream(stardom);
                    streamIssuesVisitor.handle(inputStream);
                    inputStream.close();


                    entries++;
                }
                stardom.delete();
                fh = archive.nextFileHeader();
            }

            logger.info("void start([path, streamIssuesVisitor]) Handled {} entries ",entries);

        } catch (IOException e) {
            logger.warn("Couldn't extract files from tar entry ",e);
        } catch (RarException e) {
            logger.warn("Couldn't extract files from tar entry ",e);
        }finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(inputStream);
        }

        return entries;

    }

}
