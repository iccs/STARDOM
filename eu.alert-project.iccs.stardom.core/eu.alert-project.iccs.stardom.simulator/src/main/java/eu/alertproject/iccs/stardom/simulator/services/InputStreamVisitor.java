package eu.alertproject.iccs.stardom.simulator.services;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: fotis
 * Date: 19/04/12
 * Time: 18:03
 */
public interface InputStreamVisitor {

    void handle(InputStream inputStream) throws IOException;

}
