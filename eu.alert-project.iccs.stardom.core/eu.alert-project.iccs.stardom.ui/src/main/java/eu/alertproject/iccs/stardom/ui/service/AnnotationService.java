package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.events.KeuiEvent;
import eu.alertproject.iccs.events.jsi.TextToAnnotateReplyPayload;
import eu.alertproject.iccs.stardom.ui.beans.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;

/**
 * User: fotis
 * Date: 14/03/12
 * Time: 16:51
 */
public interface AnnotationService {

    public List<Concept> annotateText(String text);

}
