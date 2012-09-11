package eu.alertproject.iccs.stardom.activemqconnector.internal;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.stardom.LoginVerifyEnvelope;
import eu.alertproject.iccs.events.stardom.LoginVerifyPayload;
import eu.alertproject.iccs.stardom.activemqconnector.api.STARDOMActiveMQListener;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.identifier.api.SluggifierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @Topic eu.alertproject.iccs.events.api.Topics.ALERT_ALL_STARDOM_LoginVerifyRequest
 * User: fotis
 * Date: 26/03/12
 * Time: 17:05
 *
 *
 */
@Component("loginVerifyListener")
public class LoginVerifyListener extends STARDOMActiveMQListener {

    private Logger logger = LoggerFactory.getLogger(LoginVerifyListener.class);


    @Autowired
    ProfileDao profileDao;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    SluggifierService sluggifierService;

    @Autowired
    JmsTemplate jmsTemplate;
    
    private Integer sequence = 0;




    @Override
    public void processXml(String text){

        logger.trace("void process() Received {} ",text);
        LoginVerifyEnvelope verifyEnvelope = EventFactory.fromXml(
                text,
                LoginVerifyEnvelope.class
        );



        long start = System.currentTimeMillis();
        LoginVerifyPayload.EventData.Login login = verifyEnvelope
                .getBody()
                .getNotify()
                .getNotificationMessage()
                .getMessage()
                .getEvent()
                .getPayload()
                .getEventData()
                .getLogin();


        List<Profile> byAny = profileDao.findByAny(
                sluggifierService.sluggify(login.getEmail()));
        if(byAny == null || byAny.size() <=0 ){
            byAny = profileDao.findByAny(login.getUsername());
        }

        if(byAny != null && byAny.size() >0){

            Identity byProfileId = identityDao.findByProfileId(byAny.get(0).getId());

            logger.trace("void process() Found {} ",byProfileId);

            String stardomLoginVerifyEnvelope = EventFactory.createStardomLoginVerifyEnvelope(
                    verifyEnvelope
                            .getBody()
                            .getNotify()
                            .getNotificationMessage()
                            .getMessage()
                            .getEvent()
                            .getPayload()
                            .getMeta()
                            .getEventId(),
                    start,
                    System.currentTimeMillis(),
                    sequence++,
                    login.getUsername(),
                    login.getEmail(),
                    byProfileId.getUuid()
            );

            logger.trace("void process() Sending {} ",stardomLoginVerifyEnvelope);

            jmsTemplate.send(
                Topics.ALERT_STARDOM_LoginVerify,
                new TextMessageCreator(
                        stardomLoginVerifyEnvelope)
            );
        }


    }
}
