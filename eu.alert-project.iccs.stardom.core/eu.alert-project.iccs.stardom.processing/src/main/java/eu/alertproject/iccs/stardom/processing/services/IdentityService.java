package eu.alertproject.iccs.stardom.processing.services;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/27/12
 * Time: 12:38 AM
 */
public interface  IdentityService {
    @Transactional(readOnly = true)
    Integer buildXmlEvents(String outputPath) throws IOException;
}
