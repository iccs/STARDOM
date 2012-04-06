package eu.alertproject.iccs.stardom.classification;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * User: fotis
 * Date: 05/04/12
 * Time: 19:31
 */
public class CIUtils {

    /**
     *
     * @return
     * @throws IOException When /description.xml is not found in the classpath
     */
    public static CI loadFromDefaultLocation() throws IOException {

        XStream x = new XStream();

        x.processAnnotations(CI.class);

        return (CI) x.fromXML(IOUtils.toString(CIUtils.class.getResourceAsStream("/description.xml")));

    }
}
