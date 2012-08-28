package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.events.stardom.IdentityPersons;
import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.stardom.StardomIdentityNewPayload;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.ui.beans.Concept;
import eu.alertproject.iccs.stardom.ui.beans.ProfileBean;
import eu.alertproject.iccs.stardom.ui.service.AnnotationService;
import eu.alertproject.iccs.stardom.ui.service.AuthenticationService;
import eu.alertproject.iccs.stardom.ui.service.MessagingService;
import eu.alertproject.iccs.stardom.ui.validator.ProfileValidator;
import eu.alertproject.iccs.stardom.ui.validator.RemindValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * User: fotis
 * Date: 14/03/12
 * Time: 16:19
 */
@Controller
public class ProfileController {

    private Logger logger = LoggerFactory.getLogger(ProfileController.class);

    int id = 0;
    
    @Autowired
    AnnotationService annotationService;

    @Autowired
    Identifier identifier;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    MessagingService messagingService;

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public ModelAndView login(){

        ModelMap mm = new ModelMap();
        ProfileBean value = new ProfileBean();
        String desc   = "Please Type a description";
        mm.put("identity", value);
        return new ModelAndView("login",mm);
    }

    @RequestMapping(value="/login/authenticate",method = RequestMethod.POST,params = "login")
    public ModelAndView login(
                              @ModelAttribute("identity") ProfileBean identity,
                              BindingResult result){


        ModelMap mm = new ModelMap();

        ProfileBean value = new ProfileBean();
        String desc   = "Please Type a description";
        mm.put("identity", value);
        return new ModelAndView("login",mm);
    }

    @RequestMapping(value="/login/authenticate",method = RequestMethod.POST,params = "remind")
    public ModelAndView remind(
            @ModelAttribute("identity") ProfileBean identity,
            BindingResult result){

        new RemindValidator().validate(identity,result);
        ModelAndView modelAndView = new ModelAndView();
        if(result.hasErrors()){
            modelAndView.setViewName("login");
            return modelAndView;
        }

        authenticationService.remind(identity);

        modelAndView.setViewName("remindSuccess");
        modelAndView.addObject("email",identity.getEmail());

        return modelAndView;

    }



    @RequestMapping(value="/profile",method = RequestMethod.GET)
    public ModelAndView profile(){

        ModelMap mm = new ModelMap();
        ProfileBean value = new ProfileBean();
        String desc   = "Please Type a description";
        mm.put("identity", value);
        return new ModelAndView("profile",mm);
    }



    @RequestMapping(value="/profile/create",method = RequestMethod.POST)
    public String create(
                        @ModelAttribute("identity") ProfileBean identity,
                        BindingResult result){

        new ProfileValidator().validate(identity,result);

        if(result.hasErrors()){
            return "profile";
        }else {

            long start = System.currentTimeMillis();

            logger.trace("String create() {} ", identity.getDescription());

            List<Concept> concepts = annotationService.annotateText(identity.getDescription());


            if(concepts != null ){

                for(Concept c :concepts){
                    logger.trace("String create() {} ",c);
                }




                Profile p = new Profile();
                p.setEmail(identity.getEmail());
                p.setLastname(identity.getEmail());
                p.setName(identity.getName());
                p.setUsername(identity.getUsername());
                Identity smIdentity = identifier.find(
                        p
                        , "form");


              

                IdentityPersons.Persons.Person newPerson = new IdentityPersons.Persons.Person();

                newPerson.setFirstname(identity.getName());
                newPerson.setLastname(identity.getLastname());
                newPerson.setUsername(identity.getUsername());
                newPerson.setEmail(identity.getEmail());

                String stardomIdentityNew = EventFactory.createStardomIdentityNew(
                        id++,
                        start,
                        System.currentTimeMillis(),
                        id++,
                        new StardomIdentityNewPayload.EventData.Identity(
                                smIdentity.getUuid(),
                                new IdentityPersons(Arrays.asList(newPerson), null))
                );
                
                logger.trace("String create() Sending event {} ",stardomIdentityNew);

                messagingService.send(
                        Topics.ALERT_STARDOM_New_Identity,
                        stardomIdentityNew);

                return "redirect:/profile";

            }else{


                return "profile";
            }





        }
    }



}
