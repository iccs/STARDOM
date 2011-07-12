package eu.alertproject.iccs.stardom.bus.api;

import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * User: fotis
 * Date: 12/07/11
 * Time: 16:14
 *
 */
public class StardomServiceBus {

    public StardomServiceBus() throws EventServiceExistsException {
        EventServiceLocator.setEventService("StardomEventBus", new StardomEventService());
    }
}
