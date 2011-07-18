package eu.alertproject.iccs.stardom.bus.api;

import org.bushe.swing.event.EventServiceLocator;

/**
 * The following class acts as a wrapper in case we wish to switch the Bushe's event
 * bus in the future
 * User: fotis
 * Date: 12/07/11
 * Time: 16:52
 */
public class Bus {

    /**
     * @see org.bushe.swing.event.EventService#publish(Object)
     */
    public static void publish(Event event) {

        if (event == null) {
            throw new IllegalArgumentException("Can't publish null.");
        }

        EventServiceLocator.getEventBusService().publish(event);

    }

    /**
     * @see org.bushe.swing.event.EventService#publish(String, Object)
     */
    public static void publish(String topic, Event o) {

        if (topic == null) {
            throw new IllegalArgumentException("Can't publish to null topic.");
        }

        EventServiceLocator.getEventBusService().publish(topic, o);

    }


}
