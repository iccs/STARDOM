package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.stardom.ui.beans.Concept;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 7/30/12
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockAnnotationService implements AnnotationService {
    @Override
    public List<Concept> annotateText(String text) {
        return new ArrayList<Concept>();
    }
}
