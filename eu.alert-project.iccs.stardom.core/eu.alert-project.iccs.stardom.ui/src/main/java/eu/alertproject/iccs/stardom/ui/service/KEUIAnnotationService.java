package eu.alertproject.iccs.stardom.ui.service;

import com.thoughtworks.xstream.XStream;
import eu.alertproject.iccs.events.alert.Keui;
import eu.alertproject.iccs.events.alert.TextToAnnotateReplyEnvelope;
import eu.alertproject.iccs.events.alert.TextToAnnotateReplyPayload;
import eu.alertproject.iccs.events.api.AbstractActiveMQHandler;
import eu.alertproject.iccs.events.api.ActiveMQMessageBroker;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.stardom.ui.beans.Concept;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: fotis
 * Date: 14/03/12
 * Time: 16:51
 */
@Service("annotationService")
public class KEUIAnnotationService extends AbstractActiveMQHandler implements AnnotationService{


    private Logger logger = LoggerFactory.getLogger(KEUIAnnotationService.class);
    
    private Integer id =0 ;
    private Integer sequence = 0 ;

    @Autowired
    ActiveMQMessageBroker messageBroker;

    AtomicReference<String> event;
    private TextToAnnotateReplyPayload.EventData eventData;

    @Override
    public List<Concept> annotateText(String text){
        
        
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        long  start = System.currentTimeMillis();
        event= new AtomicReference<String>();


        messageBroker.sendTextMessage(
                Topics.ALERT_STARDOM_TextToAnnotate,
                EventFactory.createTextToAnnotateRequestEvent(
                        messageBroker.requestEventId(),
                        start,
                        System.currentTimeMillis(),
                        messageBroker.requestSequence(),
                        text));


        countDownLatch.countDown();

        Thread t  = new Thread(){
            @Override
            public void run() {


                int i= 0;
                try {
                    //wait for the event
                    while(i < 60 && StringUtils.isEmpty(event.get())){
                        i++;
                        logger.trace("void run() {} ",i);
                        Thread.sleep(1000);
                    }

                    if(event.get() !=null && event.get().equals("error")){
                        //error
                        event.set(null);
                    }


                } catch (InterruptedException e) {
                    event.set(null);
                    logger.error("We timedout here!!! ",e);
                }finally {
                    countDownLatch.countDown();
                }
            }
        };

        t.start();

        try {
            countDownLatch.await(60L, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            logger.error("Count down latch interrupted ",e);
        }


        if(eventData !=null){

            Iterator<Keui.Concept> iterator = eventData.getKeui().getTextConcepts().iterator();


            List<Concept> concepts = new ArrayList<Concept>();

            while (iterator.hasNext()){

                Keui.Concept next = iterator.next();
                concepts.add(new Concept(next.getUri(),next.getWeight()));
            }

            return concepts;
        }


        return null;
    }


    @Override
    public void process(ActiveMQMessageBroker broker, Message message) throws IOException, JMSException {


        String text = null;
        try {
            
            text = ((TextMessage) message).getText();

            XStream xStream = new XStream();
            xStream.processAnnotations(TextToAnnotateReplyEnvelope.class);
            
            TextToAnnotateReplyEnvelope envelope = (TextToAnnotateReplyEnvelope) xStream.fromXML(text);

            eventData = envelope
                    .getBody()
                    .getNotify()
                    .getNotificationMessage()
                    .getMessage()
                    .getEvent()
                    .getPayload()
                    .getEventData();
            
            String source = eventData.getGeneralText().getSource();

            if(source.equals("stardom")){

                event.set(text);
            }
            
            
        } catch (JMSException e) {
            logger.warn("Couldn't retrieve the text message {}",message,e);
            text="error";
        }

        event.set(text);
    }
}
