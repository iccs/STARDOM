package eu.alertproject.iccs.stardom.analyzers.its.bus;

import eu.alertproject.iccs.stardom.bus.api.Event;

/**
 * User: fotis
 * Date: 26/08/11
 * Time: 20:54
 */
public class ItsChangeEvent extends Event {

    public ItsChangeEvent(Object source, Object payload) {
        super(source, payload);
    }
}
