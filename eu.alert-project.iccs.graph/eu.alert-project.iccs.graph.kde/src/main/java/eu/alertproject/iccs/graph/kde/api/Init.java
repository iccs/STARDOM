package eu.alertproject.iccs.graph.kde.api;

import eu.alertproject.iccs.graph.kde.swing.GraphViewerController;
import eu.alertproject.iccs.graph.kde.swing.GraphViewerPanel;

import javax.swing.*;

/**
 * User: fotis
 * Date: 27/12/11
 * Time: 19:00
 */
public class Init {

    public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {


                GraphViewerPanel graphViewerPanel = new GraphViewerPanel();

                JFrame frame = new JFrame("KDE Solid Betweenness");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(graphViewerPanel);
                frame.pack();

                GraphViewerController controller = new GraphViewerController(graphViewerPanel);
                frame.setVisible(true);


            }
        });
    }
}
