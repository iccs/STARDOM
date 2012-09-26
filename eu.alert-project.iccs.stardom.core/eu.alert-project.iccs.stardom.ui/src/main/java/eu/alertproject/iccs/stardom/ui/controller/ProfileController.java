package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.stardom.IdentityPersons;
import eu.alertproject.iccs.events.stardom.StardomIdentityNewPayload;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import eu.alertproject.iccs.stardom.ui.beans.AuthenticationInfo;
import eu.alertproject.iccs.stardom.ui.beans.Concept;
import eu.alertproject.iccs.stardom.ui.beans.ProfileBean;
import eu.alertproject.iccs.stardom.ui.service.AnnotationService;
import eu.alertproject.iccs.stardom.ui.service.AuthenticationService;
import eu.alertproject.iccs.stardom.ui.service.MessagingService;
import eu.alertproject.iccs.stardom.ui.validator.LoginValidator;
import eu.alertproject.iccs.stardom.ui.validator.ProfileValidator;
import eu.alertproject.iccs.stardom.ui.validator.RemindValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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

    @Autowired
    Properties systemProperties;

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public ModelAndView login(){

        ModelMap mm = new ModelMap();
        ProfileBean value = new ProfileBean();
        String desc   = "Please Type a description";
        mm.put("identity", value);
        return new ModelAndView("login",mm);
    }

    @RequestMapping(value="/login/authenticate",method = RequestMethod.POST,params = "login")
    public String login(
                              @ModelAttribute("identity") ProfileBean identity,
                              BindingResult result){

        new LoginValidator().validate(identity,result);

        if(result.hasErrors()){
            return "login";
        }

        if(!authenticationService.authenticate(identity)){
            result.addError(
                    new FieldError("auth.invalid","code","Invalid Authetication!")
                    );

            return "login";


        }

        String ret = "redirect:"+systemProperties.getProperty("auth.loginUrl")+"/?email="+identity.getEmail();

        logger.trace("String login([identity, result]) {} ",ret);
       return ret;


    }

    @RequestMapping(value="/login/authenticate",method = RequestMethod.POST,params = "remind")
    public ModelAndView remind(
            @ModelAttribute("identity") ProfileBean identity,
            BindingResult result){

        new RemindValidator().validate(identity,result);
        ModelAndView modelAndView = new ModelAndView();
        if(result.hasErrors()){
            modelAndView.addObject("email",identity.getEmail());
            modelAndView.setViewName("login");
            return modelAndView;
        }

        if(authenticationService.remind(identity)){
            modelAndView.setViewName("remindSuccess");
            modelAndView.addObject("email",identity.getEmail());
        }else{
            result.addError(new FieldError("no.user","email","The specified e-mail does not correspond to a user"));
            modelAndView.setViewName("login");
        }
        return modelAndView;

    }


    @RequestMapping(value="/login/authenticated",method = RequestMethod.POST)
    public AuthenticationInfo isAuthenticated(@RequestParam String email, HttpServletResponse response){
        response.setContentType("application/json");



        return authenticationService.checkAuthenticationInfo(email);

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
