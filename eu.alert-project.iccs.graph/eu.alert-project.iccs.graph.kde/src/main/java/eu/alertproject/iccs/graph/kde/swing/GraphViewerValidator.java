package eu.alertproject.iccs.graph.kde.swing;

import java.util.ArrayList;
import java.util.List;

/**
 * User: fotis
 * Date: 27/12/11
 * Time: 19:25
 */
public class GraphViewerValidator {

    public List<String> validate(GraphViewerPanel panel){

        List<String> errors = new ArrayList<String>();

        if(panel.getSelectedInputFile() == null){
            errors.add("Please select an input file");
            
        }

        if(panel.getSelectedOutputFile() == null){

            errors.add("Please select an output file");
        }


        return errors;
    }
}
