package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.events.api.ActiveMQMessageBroker;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
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


    @Autowired
    AnnotationService annotationService;

    @Autowired
    Identifier identifier;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ActiveMQMessageBroker messageBroker;

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

    @RequestMapping(value="/login/authenticate",method = RequestMethod.POST,params = "register")
    public String regiseter(){
        return "redirect:/profile";
    }
    @RequestMapping(value="/login/authenticate",method = RequestMethod.POST,params = "login")
    public ModelAndView login(
                              @ModelAttribute("identity") ProfileBean identity,
                              BindingResult result){

        new LoginValidator().validate(identity,result);

        if(result.hasErrors()){
            return new ModelAndView("login");
        }

        if(!authenticationService.authenticate(identity)){
            result.addError(
                    new FieldError("auth.invalid","code","Invalid Authetication!")
                    );

            return new ModelAndView("login");


        }

        ModelAndView modelAndView = new ModelAndView("redirect");
        modelAndView.addObject("redirect",systemProperties.getProperty("auth.loginUrl")+"?email="+identity.getEmail() );
        return modelAndView;


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


    @RequestMapping(value="/{email}/logout",method = RequestMethod.GET)
    public ModelAndView logout(@PathVariable String email ){

        authenticationService.logout(email);
        ModelAndView modelAndView = new ModelAndView("redirect");
        modelAndView.addObject("redirect",systemProperties.getProperty("auth.logoutUrl"));
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
    public ModelAndView create(
                        @ModelAttribute("identity") ProfileBean identity,
                        BindingResult result){

        new ProfileValidator().validate(identity,result);

        if(result.hasErrors()){
            return new ModelAndView("profile");
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
                p.setLastname(identity.getLastname());
                p.setName(identity.getName());
                p.setUsername(identity.getUsername());
                Identity smIdentity = identifier.find(p, "form");


                IdentityPersons.Persons.Person newPerson = new IdentityPersons.Persons.Person();

                newPerson.setFirstname(identity.getName());
                newPerson.setLastname(identity.getLastname());
                newPerson.setUsername(identity.getUsername());
                newPerson.setEmail(identity.getEmail());

                String stardomIdentityNew = EventFactory.createStardomIdentityNew(
                        messageBroker.requestEventId(),
                        start,
                        System.currentTimeMillis(),
                        messageBroker.requestSequence(),
                        new StardomIdentityNewPayload.EventData.Identity(
                                smIdentity.getUuid(),
                                new IdentityPersons(Arrays.asList(newPerson), null))
                );
                
                logger.trace("String create() Sending event {} ", stardomIdentityNew);

                messageBroker.sendTextMessage(
                        Topics.ALERT_STARDOM_New_Identity,
                        stardomIdentityNew);


                //force authenticate
                authenticationService.initSession(identity.getEmail());

                ModelAndView registred = new ModelAndView("registered");
                registred.addObject("code",smIdentity.getAuthcode());
                registred.addObject("uuid",smIdentity.getAuthcode());
                registred.addObject("redirect",systemProperties.getProperty("auth.loginUrl")+"?email="+identity.getEmail());

                return  registred;

            }else{
                return new ModelAndView("profile");
            }

        }
    }



}
