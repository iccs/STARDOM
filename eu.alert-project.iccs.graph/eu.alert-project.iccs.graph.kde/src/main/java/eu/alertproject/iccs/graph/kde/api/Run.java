package eu.alertproject.iccs.graph.kde.api;


import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.TransformerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * User: fotis
 * Date: 26/12/11
 * Time: 22:15
 */
public class Run {

    private static Logger logger = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) throws IOException {


        File o = new File(args[0]);

        int count = 0;
        LineIterator it = null;

        int max = 106000;
        BufferedWriter fw = null;
        OutputStreamWriter osw = null;
        FileOutputStream fos = null;
        try {
            it = FileUtils.lineIterator(o, "UTF-8");


            Integer previousFile = null;
            Integer previousAuthor = null;

            UndirectedGraph<Integer, String> graph = new UndirectedSparseGraph<Integer, String>();
            Map<String, Number> weights = new HashMap<String, Number>();

            while (it.hasNext() || max > 0) {
                String next = String.valueOf(it.next());


                String[] split = next.split(",");

                Integer fileId = Integer.valueOf(split[0]);
                Integer authorId = Integer.valueOf(split[1]);

                if (previousFile != null && previousFile.equals(fileId)) {


                    String edge = previousFile + "_" + previousAuthor + "_" + authorId;

                    //already added
                    if (!graph.containsEdge(edge)) {
                        graph.addEdge(edge, previousAuthor, authorId);
                        weights.put(edge, 1);
                    } else {
                        weights.put(edge, weights.get(edge).intValue() + 1);
                    }

                    previousAuthor = authorId;

                } else {
                    count++;
                    previousAuthor = authorId;
                    previousFile = fileId;
                }

                max--;


            }


            logger.trace("void main(args) Vertex Count: {} ", graph.getVertexCount());

            BetweennessCentrality<Integer, String> bc = new BetweennessCentrality<Integer, String>(graph, TransformerUtils.mapTransformer(weights));

            logger.trace("void main(args) Betweeness calculated");


            fos = new FileOutputStream(args[1]);
            osw = new OutputStreamWriter(fos, "UTF-8");

            fw = new BufferedWriter(osw);


            List<String> lines = new ArrayList<String>();
            for (Integer vertex : graph.getVertices()) {
                fw.write(vertex + "\t" + bc.getVertexScore(vertex) + "\n");
            }


            logger.trace("void main(args) File created {}",args[2]);

//
//            Layout<Integer, String> layout = new KKLayout<Integer, String>(graph);
//            layout.setSize(new Dimension(800, 800)); // sets the initial size of the space
//            // The BasicVisualizationServer<V,E> is parameterized by the edge types
//            BasicVisualizationServer<Integer, String> vv =
//                    new BasicVisualizationServer<Integer, String>(layout);
//            vv.setPreferredSize(new Dimension(800, 800)); //Sets the viewing area size
//            vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//            vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//
////            DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
////            gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
////            vv.setGraphMouse(gm);
//
//            JFrame frame = new JFrame("Simple Graph View");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.getContentPane().add(vv);
//            frame.pack();
//            logger.trace("void main(args) count {} ", count);
//            frame.setVisible(true);


        } catch (IOException e) {
            logger.error("IO Error ", e);
        } catch (NumberFormatException e) {
            logger.error("Wrong data", e);
        } finally {
            LineIterator.closeQuietly(it);
            fw.close();
            IOUtils.closeQuietly(osw);
            IOUtils.closeQuietly(fos);

        }


    }
}
