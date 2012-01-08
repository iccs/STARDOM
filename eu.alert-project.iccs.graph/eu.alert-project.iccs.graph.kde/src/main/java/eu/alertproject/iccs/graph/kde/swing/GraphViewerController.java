package eu.alertproject.iccs.graph.kde.swing;

import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import eu.alertproject.iccs.graph.kde.api.GraphHandler;
import org.apache.commons.collections15.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: fotis
 * Date: 27/12/11
 * Time: 19:22
 */
public class GraphViewerController {

    private Logger logger = LoggerFactory.getLogger(GraphViewerController.class);


    private GraphViewerPanel panel;
    private Graph<Integer,String> g;
    private final ExecutorService executorService;
    private final GraphHandler gh;

    public GraphViewerController(GraphViewerPanel panel) {
        this.panel = panel;
        executorService = Executors.newSingleThreadExecutor();
        gh = new GraphHandler();
        initListeners();
    }

    private void initListeners() {


        this.panel.getGoButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //validate

                GraphViewerValidator graphViewerValidator = new GraphViewerValidator();
                List<String> validate = graphViewerValidator.validate(panel);

                if(validate.size() > 0){

                    panel.setErrors(validate);
                    return;

                }

                //continue
                handleGo();

            }
        });
        
        this.panel.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {

                if(!((JSlider)e.getSource()).getValueIsAdjusting()){
                    handleGraphOptionsChanged();
                }

            }
        });

        this.panel.addActionListener(new ActionListener(){


            @Override
            public void actionPerformed(ActionEvent e) {

                handleGraphOptionsChanged();

            }
        });

    }

    private void handleGraphOptionsChanged() {

        if(g == null){
            return;
        }
        panel.setInputEnabled(false);
        panel.startLoading();
        executorService.submit(new Runnable() {
            public Graph<Integer, String> graph;

            @Override
            public void run() {

                graph = filter(g,
                        gh.getWeights(),
                        gh.getBc(),
                        panel.getEdge(),
                        panel.getBetweenness());


                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        panel.stopLoading();
                        panel.setInputEnabled(true);
                        updateGraphView(graph);
                    }
                });

            }
        });

    }

    private void handleGo() {


        final File selectedInputFile = panel.getSelectedInputFile();
        final File selectedOutputFile = panel.getSelectedOutputFile();


        if(selectedInputFile ==null || selectedOutputFile == null){
            return;
        }


        panel.startLoading();


        final Integer maxBt = panel.getBetweenness();
        final Integer maxEdge = panel.getEdge();

        executorService.submit(new Runnable() {


            public Graph<Integer, String> graph;

            @Override
            public void run() {

                g = gh.create(selectedInputFile,selectedOutputFile);

                graph = filter(g,
                        gh.getWeights(),
                        gh.getBc(),
                        maxEdge,
                        maxBt);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        panel.stopLoading();

                        updateGraphView(graph);
                    }
                });


            }
        });
    }

    private void updateGraphView(Graph<Integer,String> graph){

        if(graph== null ){
            return;
        }
        Layout<Integer, String> layout = null;

        switch (panel.getGraphType()){
            case 1:
                layout = new CircleLayout<Integer, String>(graph);
                break;
            case 2:
                layout = new FRLayout<Integer, String>(graph);
                break;
            case 3:
                layout = new KKLayout<Integer, String>(graph);
                break;
            default:
                layout = new CircleLayout<Integer, String>(graph);

        }


        BasicVisualizationServer<Integer,String> basicVisualizationServer = new BasicVisualizationServer<Integer, String>(layout,panel.getViewDimension());
        basicVisualizationServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        basicVisualizationServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        layout.setSize(panel.getViewDimension()); // sets the initial size of the space
        panel.setGraphView(basicVisualizationServer);

        
    }


    
    private Graph<Integer,String> filter(Graph<Integer, String> graph,
                                         final Map<String,Number> weights,
                                         final BetweennessCentrality<Integer, String> bc,
                                         final Integer maxEdge,
                                         final Integer maxBetweenness){

        logger.trace("Graph<Integer,String> filter() BT={}, Edge={} ",maxEdge,maxBetweenness);

        Graph<Integer,String> simpleGraph =
        new VertexPredicateFilter<Integer,String>(new Predicate<Integer>() {
            @Override
            public boolean evaluate(Integer integer) {

                return bc.getVertexScore(integer) >= maxBetweenness;
            }
        }).transform(graph);

        simpleGraph = new EdgePredicateFilter<Integer,String>(new Predicate<String>() {
            @Override
            public boolean evaluate(String s) {
                if(!weights.containsKey(s)){
                    return false;
                }

                return weights.get(s).intValue() >= maxEdge;
            }
        }).transform(simpleGraph);


        return simpleGraph;
    }


}
