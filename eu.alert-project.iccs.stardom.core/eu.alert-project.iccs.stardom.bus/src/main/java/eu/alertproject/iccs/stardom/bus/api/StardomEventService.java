package eu.alertproject.iccs.stardom.bus.api;

import org.bushe.swing.event.ThreadSafeEventService;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 16:25
 */
public class StardomEventService extends ThreadSafeEventService implements SmartLifecycle {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(StardomEventService.class);

    private ExecutorService threadPool;


    public StardomEventService() {
        super();
        logger.trace(" Starting thread pool");
//        threadPool = Executors.newSingleThreadExecutor();
    }

    /**
    * Same as ThreadSafeEventService.publish(), except if the call is coming from a thread that is not the Swing Event
    * Dispatch Thread, the request is put on the EDT through a a call to SwingUtilities.invokeLater(). Otherwise this
    * DOES NOT post a new event on the EDT.  The subscribers are called on the same EDT event, just like addXXXListeners
    * would be.
    */
   protected void publish(final Object event, final String topic, final Object eventObj,
           final List subscribers, final List vetoSubscribers, final StackTraceElement[] callingStack) {

       try{
           StardomEventService.super.publish(event, topic, eventObj, subscribers, vetoSubscribers, callingStack);
       }catch (Exception e){
           logger.warn("A exception occured on your event handle ",e);
       }

//       threadPool.submit(new Runnable() {
//           @Override
//           public void run() {
//               logger.trace("void run(callingStack) Publishing event {} for topic {}",eventObj,topic);
//               StardomEventService.super.publish(event, topic, eventObj, subscribers, vetoSubscribers, callingStack);
//           }
//       });

   }


    @Override
    public boolean isAutoStartup() {
        return false;
    }

    @Override
    public void stop(Runnable callback) {
        logger.debug("void stop()");
//        threadPool.shutdownNow();
    }

    @Override
    public void start() {
        logger.debug("void start()");
    }

    @Override
    public void stop() {
        logger.debug("void stop()");
//        threadPool.shutdownNow();
    }

    @Override
    public boolean isRunning() {
        logger.debug("boolean isRunning()");
        return false;//!threadPool.isTerminated();
    }

    @Override
    public int getPhase() {
        return Integer.MIN_VALUE;
    }
}
