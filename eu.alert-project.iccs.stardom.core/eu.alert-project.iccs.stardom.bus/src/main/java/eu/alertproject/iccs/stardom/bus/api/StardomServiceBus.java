package eu.alertproject.iccs.stardom.bus.api;

import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;

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
