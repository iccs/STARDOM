package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.stardom.ui.beans.LogTailerListener;
import org.apache.commons.collections15.list.FixedSizeList;
import org.apache.commons.io.input.TailerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: fotis
 * Date: 13/11/11
 * Time: 23:15
 */
@Controller
public class LogController {

    private Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    LogTailerListener logTailerListener;

    @RequestMapping(value="/log/dump", method = RequestMethod.GET)
    public @ResponseBody String getLog(){
        String lines = logTailerListener.getLines();

        logger.trace("String getLog() {} ",lines);
        return lines;

    }

}
