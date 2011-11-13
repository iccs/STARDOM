package eu.alertproject.iccs.stardom.ui.beans;

import org.apache.commons.collections15.buffer.CircularFifoBuffer;
import org.apache.commons.collections15.list.FixedSizeList;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: fotis
 * Date: 13/11/11
 * Time: 23:21
 */
@Component("logTailerListener")
public class LogTailerListener extends TailerListenerAdapter {

    private Logger logger = LoggerFactory.getLogger(LogTailerListener.class);

    private CircularFifoBuffer<String>  lines;

    public LogTailerListener() {
        lines = new CircularFifoBuffer<String>(100);
    }

    @Override
    public void handle(String line) {
        logger.trace("void handle() adding {} ",line);

        lines.add(line);
    }

    public String getLines(){

        List<String> copy = new ArrayList<String>();
        Collections.addAll(copy,lines.toArray(new String[]{}));

        Collections.reverse(copy);

        StringBuffer sb = new StringBuffer();

        for(String c : copy){
            if(sb != null){
                sb.append(c+"<br/>");
            }
        }

        return sb.toString();

    }
}
