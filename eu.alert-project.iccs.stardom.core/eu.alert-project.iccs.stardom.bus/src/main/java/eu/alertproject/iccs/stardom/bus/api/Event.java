package eu.alertproject.iccs.stardom.bus.api;

/**
 * User: fotis
 * Date: 13/07/11
 * Time: 09:06
 */
public class Event {

    private Object source;
    private Object payload;

    public Event(Object source, Object payload){
        this.source = source;
        this.payload = payload;
    }
}
