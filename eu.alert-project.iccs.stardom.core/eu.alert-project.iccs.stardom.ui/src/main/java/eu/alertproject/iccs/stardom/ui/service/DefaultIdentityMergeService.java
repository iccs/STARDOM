package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.events.activemq.TextMessageCreator;
import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.api.Topics;
import eu.alertproject.iccs.events.stardom.IdentityPersons;
import eu.alertproject.iccs.events.stardom.StardomIdentityUpdatePayload;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: fotis
 * Date: 22/01/12
 * Time: 13:41
 */
@Service("mergeService")
public class DefaultIdentityMergeService implements MergeService,MessageListener {


    private Logger logger = LoggerFactory.getLogger(DefaultIdentityMergeService.class);

    
    @Autowired
    IdentityDao identityDao;


    @Autowired
    JmsTemplate jmsTemplate;
    
    private Integer id=0;

    private AtomicReference<String> responseEvent;

    /**
     * This function merges one or more identities into a single one
     * 
     * The result will be an entirely new identity made up of all
     * of them
     * 
     * @param ids
     */
    @Override
    @Transactional
    public void merge(Integer... ids){


        long start = System.currentTimeMillis();

        IdentityPersons add = new IdentityPersons();
        IdentityPersons.Persons is = new IdentityPersons.Persons();
        List<IdentityPersons.Persons.Person> addPersonsArray = new ArrayList<IdentityPersons.Persons.Person>();
        eu.alertproject.iccs.events.stardom.StardomIdentityUpdatePayload.EventData.Identity firstIdentity =
                new eu.alertproject.iccs.events.stardom.StardomIdentityUpdatePayload.EventData.Identity();

        List<eu.alertproject.iccs.events.stardom.StardomIdentityUpdatePayload.EventData.Identity> updateIdentites =
                new ArrayList<eu.alertproject.iccs.events.stardom.StardomIdentityUpdatePayload.EventData.Identity>();


        Identity first = null;
        
        for(Integer id : ids){

            // here we are working against the first identity
            if(first == null ){
                first = identityDao.findById(id);
                continue;
            }

            firstIdentity.setUuid(first.getUuid());


            Identity nextIdentity = identityDao.findById(id);
            eu.alertproject.iccs.events.stardom.StardomIdentityUpdatePayload.EventData.Identity nextEventIdentity =
                            new eu.alertproject.iccs.events.stardom.StardomIdentityUpdatePayload.EventData.Identity();
            nextEventIdentity.setUuid(nextIdentity.getUuid());

            IdentityPersons nextIdentityRemove = new IdentityPersons();

            //join the profilse
            Iterator<Profile> iterator = nextIdentity.getProfiles().iterator();

            IdentityPersons.Persons isnt = new IdentityPersons.Persons();
            

            List<IdentityPersons.Persons.Person> removePersonsArray = new ArrayList<IdentityPersons.Persons.Person>();
            while(iterator.hasNext()){

                Profile p = iterator.next();
                first.getProfiles().add(p);
                IdentityPersons.Persons.Person removePerson = new IdentityPersons.Persons.Person();
                IdentityPersons.Persons.Person addPerson = new IdentityPersons.Persons.Person();

                if(StringUtils.isEmpty(p.getUri())){
                    //create normal person
                    addPerson.setFirstname(p.getName());
                    addPerson.setLastname(p.getLastname());
                    addPerson.setEmail(p.getEmail());
                    addPerson.setUsername(p.getUsername());

                    removePerson.setFirstname(p.getName());
                    removePerson.setLastname(p.getLastname());
                    removePerson.setEmail(p.getEmail());
                    removePerson.setUsername(p.getUsername());
                }else {
                    removePerson.setUri(p.getUri());
                    addPerson.setUri(p.getUri());
                }

                addPersonsArray.add(addPerson);
                removePersonsArray.add(removePerson);
                iterator.remove();
            }

            isnt.setPersons(removePersonsArray);
            nextIdentityRemove.setIsnt(isnt);
            nextEventIdentity.setRemove(nextIdentityRemove);
            updateIdentites.add(nextEventIdentity);

            Iterator<Metric> nextIdentityMetrics = nextIdentity.getMetrics().iterator();
            while(nextIdentityMetrics.hasNext()){
                Metric nextIdentityMetric = nextIdentityMetrics.next();
                
                if(nextIdentityMetric instanceof MetricQuantitative){
                    
                    boolean metricFound = false;
                    for(Metric om : first.getMetrics()){

                        if(nextIdentityMetric.getClass().equals(om.getClass())){
                            
                            ((MetricQuantitative)om).setQuantity(((MetricQuantitative) om).getQuantity()+((MetricQuantitative) nextIdentityMetric).getQuantity());

                            /*
                            Make sure that the date is updated on the quatity
                             */
                            Date omCreatedAt = om.getCreatedAt();
                            Date createdAt = nextIdentityMetric.getCreatedAt();
                            
                            if(createdAt.after(omCreatedAt)){
                                om.setCreatedAt(createdAt);
                            }
                            metricFound = true;
                        }
                    }

                    /*
                    Because we are looking only at the first instance metrics, we need to add
                    the metrics that are not immediately available from the second identity
                     */
                    if(!metricFound){

                        nextIdentityMetric.setIdentity(first);
                        first.getMetrics().add(nextIdentityMetric);
                        
                        nextIdentityMetrics.remove();

                    }

                }else if(nextIdentityMetric instanceof MetricTemporal){

//                    boolean metricFound = false;

                    nextIdentityMetric.setIdentity(first);
                    first.getMetrics().add(nextIdentityMetric);
                    nextIdentityMetrics.remove();
                    //
//                    for(Metric om : first.getMetrics()){
//
//                        if(nextIdentityMetric.getClass().equals(om.getClass())){
//

//                            We changed this for the review, this should be re-enabled
//
//                            Date omTemporal = ((MetricTemporal) om).getTemporal();
//                            
//                            
//                            Date mTemporal = ((MetricTemporal) m).getTemporal();
//
//                            
//                            if(mTemporal.after(omTemporal)){
//                                ((MetricTemporal) om).setTemporal(mTemporal);
//                            }
//                            
//                            Date omCreatedAt = om.getCreatedAt();
//                            Date createdAt = m.getCreatedAt();
//                            if(createdAt.after(omCreatedAt)){
//                                om.setCreatedAt(createdAt);
//                            }
//
//                            metricFound=true;
//                        }
//
//                    }
//
//                    if(!metricFound){
//                        m.setIdentity(first);
//                        first.getMetrics().add(m);
//                        metricIterator.remove();
//                    }
                }
            }

            /*
            This should remove profiles and metrics!!!!
             */
//            deleteIds.add(id);
            identityDao.update(first);
            identityDao.delete(id);
        }



        is.setPersons(addPersonsArray);
        add.setIs(is);
        firstIdentity.setAdd(add);

        Collections.addAll(updateIdentites,firstIdentity);

        String event = EventFactory.createStardomIdentityUpdate(
                id++,
                start,
                System.currentTimeMillis(),
                id,
                updateIdentites.toArray(
                        new StardomIdentityUpdatePayload.EventData.Identity[updateIdentites.size()]

                ));

        //delete the rest

//        for(Integer id : deleteIds){
//            identityDao.delete(id);
//        }
//

        final CountDownLatch countDownLatch = new CountDownLatch(2);
        responseEvent = new AtomicReference<String>();
//

        //wait for the reply
        logger.trace("void merge(ids) Sending {} ",event);

        jmsTemplate.send(
                Topics.ALERT_STARDOM_Identity_Updated,
                new TextMessageCreator(event)
        );

        countDownLatch.countDown();

        Thread t  = new Thread(){
            @Override
            public void run() {


                int i= 0;
                try {
                    //wait for the event
                    while(i < 60 && StringUtils.isEmpty(responseEvent.get())){
                        i++;
                        logger.trace("void run() {} ",i);
                        Thread.sleep(1000);
                    }

                    if(responseEvent.get() !=null && responseEvent.get().equals("error")){
                        //error
                        responseEvent.set(null);
                    }


                } catch (InterruptedException e) {
                    responseEvent.set(null);
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

    }

    @Override
    public void onMessage(Message message) {

        String text="error";
        
        try{
            text=((TextMessage) message).getText();
            logger.trace("void onMessage() Success \n\n{}\n\n ",text);
        } catch (JMSException e) {
            logger.error("Error reading issue updated response {}", e, text);
        }

        responseEvent.set(text);

    }
}
