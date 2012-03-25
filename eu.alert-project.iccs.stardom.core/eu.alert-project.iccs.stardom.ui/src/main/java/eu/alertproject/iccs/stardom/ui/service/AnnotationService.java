package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.stardom.ui.beans.Concept;

import java.util.List;

/**
 * User: fotis
 * Date: 14/03/12
 * Time: 16:51
 */
public interface AnnotationService {

    public List<Concept> annotateText(String text);

}
