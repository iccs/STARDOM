package eu.alertproject.iccs.stardom.simulator.services;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/12/12
 * Time: 2:41 PM
 */
@Service("targzSimulationService")
public class TarGzSimulatorService implements SimulationService{

    private Logger logger = LoggerFactory.getLogger(TarGzSimulatorService.class);


    int count=0;

    @Override
    public void start(String path, InputStreamVisitor streamIssuesVisitor){


        try {

            File tarFile = File.createTempFile("targzSimulator","tarfile");
            tarFile.createNewFile();

            GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(path));

            logger.info("Extracting to tar {}",tarFile.getAbsolutePath());
            IOUtils.copyLarge(gzipInputStream,new FileOutputStream(tarFile));

            logger.info("Reading tar file...");

            TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(tarFile));

            TarArchiveEntry entry = tarArchiveInputStream.getNextTarEntry();

            while (entry != null) {

                if(entry.getName().toLowerCase().endsWith(".xml")){

                    logger.debug("void start([path, streamIssuesVisitor]) Handling {} ",entry.getName());

                    File stardom = File.createTempFile("stardom", "rar-simulator");
                    stardom.createNewFile();

                    final OutputStream outputFileStream = new FileOutputStream(stardom);

                    IOUtils.copy(tarArchiveInputStream, outputFileStream);
                    outputFileStream.close();

                    streamIssuesVisitor.handle(new FileInputStream(stardom));

                    tarFile.delete();

                    count++;
                }

                entry = tarArchiveInputStream.getNextTarEntry();

            }


            tarFile.delete();

            logger.trace("void start([path, streamIssuesVisitor]) Handled {} events ",count);
        } catch (IOException e) {
            logger.warn("Couldn't extract files from tar entry ",e);

        }

    }

}
