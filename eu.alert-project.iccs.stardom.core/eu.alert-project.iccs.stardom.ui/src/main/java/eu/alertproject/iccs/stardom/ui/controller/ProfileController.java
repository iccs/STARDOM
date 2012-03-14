package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.ui.beans.Concept;
import eu.alertproject.iccs.stardom.ui.beans.ProfileBean;
import eu.alertproject.iccs.stardom.ui.service.AnnotationService;
import eu.alertproject.iccs.stardom.ui.validator.ProfileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * User: fotis
 * Date: 14/03/12
 * Time: 16:19
 */
@Controller
public class ProfileController {

    private Logger logger = LoggerFactory.getLogger(ProfileController.class);


    @Autowired
    AnnotationService annotationService;


    @RequestMapping(value="/login",method = RequestMethod.GET)
    public ModelAndView login(){

        ModelMap mm = new ModelMap();
        ProfileBean value = new ProfileBean();
        String desc   = "Please Type a description";
        mm.put("identity", value);
        return new ModelAndView("login",mm);

    }

    @RequestMapping(value="/profile/create",method = RequestMethod.POST)
    public String create(
                        @ModelAttribute("identity") ProfileBean identity,
                        BindingResult result){

        new ProfileValidator().validate(identity,result);

        if(result.hasErrors()){
            return "login";
        }else {


            logger.trace("String create() {} ", identity.getDescription());

            List<Concept> concepts = annotationService.annotateText(identity.getDescription());

            for(Concept c :concepts){
                logger.trace("String create() {} ",c);
            }

            return "redirect:/login";

        }
    }



}
