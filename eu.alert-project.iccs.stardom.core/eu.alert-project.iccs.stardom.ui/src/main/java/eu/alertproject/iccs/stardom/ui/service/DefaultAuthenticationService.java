package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.ui.beans.AuthenticationInfo;
import eu.alertproject.iccs.stardom.ui.beans.ProfileBean;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 8/28/12
 * Time: 11:13 PM
 */
@Service("authenticationService")
public class DefaultAuthenticationService implements AuthenticationService {

    private Logger logger = LoggerFactory.getLogger(DefaultAuthenticationService.class);


    @Autowired
    IdentityDao identityDao;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    Properties systemProperties;

    Map<String,Long> authSession;

    private Configuration cfg;


    @PostConstruct
    public void init(){

        authSession = new HashMap<String, Long>();
        cfg = new Configuration();
        cfg.setEncoding(new Locale("el","GR"),"UTF-8");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/freemarker/"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

    }

    @Override
    @Transactional
    public boolean remind(ProfileBean identity) {



        Identity byEmail = identityDao.findByEmail(identity.getEmail());

        if(byEmail == null){
            return false;
        }


        boolean  success = false;

        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // use the true
            // flag to indicate you need a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"UTF-8");

            helper.addTo(identity.getEmail());
//            helper.setFrom("ALERT Authentication System<"+systemProperties.getProperty("mail.username")+">");
            helper.setFrom(systemProperties.getProperty("mail.username"));


            helper.setSubject("Your authentication code reminder");


            Template tmp;
            String ret = "Failed to load the template";
            tmp = cfg.getTemplate("authentication-email.ftl");

            StringWriter stringWriter = new StringWriter();
            Map<String,String> map = new HashMap<String,String>();
            map.put("code",byEmail.getAuthcode());

            tmp.process(map, stringWriter);

            /*
            * Create the messages
            */
            helper.setText(stringWriter.toString(),true);
            mailSender.send(mimeMessage);
            success =true;
        } catch (MessagingException e) {
            logger.error("Couldn't Create the mail message", e);
        } catch (TemplateException e) {
            logger.error("Could not load the message template",e );
        } catch (IOException e) {
            logger.error("Could not read the message template",e );
        }
        return success;
    }


    @Override
    @Transactional
    public boolean authenticate(ProfileBean identity) {
        boolean success = false;

        try{

            //check the session first
            if(authSession.containsKey(identity.getEmail())){

                //check the time
                Long aLong = authSession.get(identity.getEmail());

                if(System.currentTimeMillis() - aLong.intValue() <
                        Long.valueOf(systemProperties.getProperty("auth.sessionTimeout"))){

                    return true;

                }else{

                    authSession.remove(identity.getEmail());
                }

            }



            Identity byEmail = identityDao.findByEmail(identity.getEmail());

            if(byEmail.getAuthcode().equals(identity.getCode())){

                authSession.put(identity.getEmail(),System.currentTimeMillis());
                success = true;
            }


        }catch (Exception e){
            logger.error("Exception occured during authentication",e);
        }


        return success;


    }

    @Override
    public void initSession(String email) {
        authSession.put(email,System.currentTimeMillis());
    }

    @Override
    public void logout(String email) {

        if(authSession.containsKey(email)){
            authSession.remove(email);
        }

    }

    @Override
    @Transactional
    public AuthenticationInfo checkAuthenticationInfo(String email) {

        AuthenticationInfo info = new AuthenticationInfo(email);

        if(!authSession.containsKey(email)){
            info.setAuthenticated(false);
            info.setAdmin(false);
            return info;
        }

        Long aLong = authSession.get(email);

        Identity byEmail = identityDao.findByEmail(email);

        info.setUuid(byEmail.getUuid());
        info.setAdmin(email.equals(systemProperties.getProperty("auth.adminEmail")));
        info.setAuthenticated(true);

        return info;
    }

    @Scheduled(fixedDelay = 5000)
    public void cleanSessions(){

        Set<String> strings = authSession.keySet();

        for(String s :strings){

            Long aLong = authSession.get(s);

            if(System.currentTimeMillis() - aLong.intValue()
                    >= Long.valueOf(systemProperties.getProperty("auth.sessionTimeout"))){

                authSession.remove(s);
            }
        }
    }

}
