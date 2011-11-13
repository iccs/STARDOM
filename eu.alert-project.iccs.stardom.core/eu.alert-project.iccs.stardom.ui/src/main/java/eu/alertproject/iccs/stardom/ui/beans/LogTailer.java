package eu.alertproject.iccs.stardom.ui.beans;

import org.apache.commons.io.input.Tailer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.management.DescriptorKey;
import java.io.File;
import java.util.Properties;

/**
 * User: fotis
 * Date: 13/11/11
 * Time: 23:48
 */
public class LogTailer {

    @Autowired
    Properties systemProperties;

    @Autowired
    LogTailerListener logTailerListener;

    private Tailer tailer;

    public LogTailer() {}

    @PostConstruct
    public void init(){
        tailer = Tailer.create(
        new File(systemProperties.getProperty("log.file")),
        logTailerListener,
        5000,
        true);
    }
    public void stop(){
        tailer.stop();
    }
}
