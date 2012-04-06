package eu.alertproject.iccs.stardom.bus.api;

import java.util.List;

/**
 * User: fotis
 * Date: 01/04/12
 * Time: 18:54
 */
public class AnnotatedUpdateEvent extends Event{
    private List<?> annotations;

    public AnnotatedUpdateEvent(Object source, Object payload,List<?> annotations) {
        super(source, payload);
        this.annotations = annotations;
    }

    public List<?> getAnnotations() {
        return annotations;
    }
}
