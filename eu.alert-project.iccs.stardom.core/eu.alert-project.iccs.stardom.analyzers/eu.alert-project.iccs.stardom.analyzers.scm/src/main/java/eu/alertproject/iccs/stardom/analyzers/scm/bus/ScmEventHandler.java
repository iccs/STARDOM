package eu.alertproject.iccs.stardom.analyzers.scm.bus;

import eu.alertproject.iccs.stardom.analyzers.scm.connector.ScmConnectorContext;
import eu.alertproject.iccs.stardom.bus.api.Bus;
import eu.alertproject.iccs.stardom.bus.api.annotation.EventHandler;
import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzers;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.identifier.api.Identifier;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 15:52
 */
@EventHandler
public class ScmEventHandler {

    private int events= 0;

    private Logger logger = LoggerFactory.getLogger(ScmEventHandler.class);


    private LinkedBlockingDeque<ScmConnectorContext> queue;
    private AtomicBoolean run = new AtomicBoolean(Boolean.TRUE);


    @Autowired
    Identifier identifier;

    @Autowired
    MetricDao metricDao;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    Analyzers analyzers;

    @PostConstruct
    protected void start(){

        queue=new LinkedBlockingDeque<ScmConnectorContext>();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while(run.get()){

                    try {
                        Thread.sleep(500);

                        List<ScmConnectorContext> events  = new ArrayList<ScmConnectorContext>();
                        int i = queue.drainTo(events, 10);
                        if(i >0){

                            logger.trace("void run() ************ Flushing {} events ************",i);
                            Iterator<ScmConnectorContext> iterator = events.iterator();
                            while(iterator.hasNext()){

                                ScmConnectorContext context = iterator.next();

                                //do your magic
                                Identity identity = identifier.find(context.getProfile());
                                logger.trace("void event() Identity {}",identity.getUuid());


                                logger.debug("Memory {}/{} ",Runtime.getRuntime().freeMemory(),Runtime.getRuntime().totalMemory());
                                //whatever your do, do it here
                                for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
                                    //if you are wondering how on earth this
                                    //is not breaking, it is because if context.getAction()
                                    // is not an instance of ScmAction, it will throw
                                    // a class cast exception.

                                    // I don't know how correct this is but for now
                                    // I am leaving this as is
                                    a.analyze(identity,context.getAction());
                                }


                            }

                        }
                    } catch (InterruptedException e) {
                        logger.error("ScmEventHandler Interrupted ",e);
                    } catch (Exception e){
                        logger.error("Your event handler thread is broken",e);
                    }

                }


            }
        },"scm-handler-event");

        /*
        Once we get around our threading issues handle this and start the thread
         */
//        t.start();
    }

    @PreDestroy
    protected void stop(){
        logger.trace("void stop()");
        run.set(Boolean.FALSE);
    }




    @EventSubscriber(eventClass = ScmEvent.class)
    public void event(ScmEvent event){

        //add the event to the queue and process one by one
        Object payload = event.getPayload();

        if(payload == null || !(payload instanceof ScmConnectorContext)){
            logger.debug("Ignoring Payload {}",payload);
            return;
        }

        ScmConnectorContext context = (ScmConnectorContext) payload;
        logger.trace("void event() {}",context.getProfile());
        logger.trace("void event() {}",context.getAction());

        logger.trace("Processed {} events ",events++);
//        queue.add(context);
//        logger.trace("Processed {} queue size ",queue.size());

        //do your magic
        Identity identity = identifier.find(context.getProfile());
        logger.trace("void event() Identity {}",identity.getUuid());


        logger.debug("Memory {}/{} ",Runtime.getRuntime().freeMemory(),Runtime.getRuntime().totalMemory());
        //whatever your do, do it here
        for(Analyzer<ConnectorAction> a : analyzers.getAnalyzers()){
            //if you are wondering how on earth this
            //is not breaking, it is because if context.getAction()
            // is not an instance of ScmAction, it will throw
            // a class cast exception.

            // I don't know how correct this is but for now
            // I am leaving this as is
            try{
                a.analyze(identity,context.getAction());
            }catch (ClassCastException e){
                //silence
            }catch (Exception e){
                logger.warn("Error during analyzer action ",e);
            }


        }



    }

}
