package eu.alertproject.iccs.stardom.alertconnector.api;

import eu.alertproject.iccs.stardom.alertconnector.beans.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: fotis
 * Date: 16/03/12
 * Time: 16:51
 */
@Controller
public class AuthenticationController {

    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody void action(@RequestBody AuthRequest context) {

        logger.trace("void action() {} ",context);




    }

}
