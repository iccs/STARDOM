/*
 * Created by JFormDesigner on Tue Dec 27 18:06:12 EET 2011
 */

package eu.alertproject.iccs.graph.kde.swing;

import java.awt.event.*;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * @author Fotis Paraskevopoulos
 */
public class GraphViewerPanel extends JPanel {
    private Logger logger = LoggerFactory.getLogger(GraphViewerPanel.class);
    private ResourceBundle bundle = ResourceBundle.getBundle("Resources");;
    private File selectedInputFile;
    private File selectedOutputFile;


    public GraphViewerPanel() {
        initComponents();
        initUI();

    }

    private void initUI() {

        /*
        Just making sure this is done a little tiny bit later
         */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateEdgeTitle();
                updateBtTitle();
            }
        });


        // The BasicVisualizationServer<V,E> is parameterized by the edge types
    }

    private void slider1StateChanged(ChangeEvent e) {

        updateEdgeTitle();

    }

    private void btSliderStateChanged(ChangeEvent e) {
        updateBtTitle();
    }


    private void updateEdgeTitle() {
        Border border = edgePanel.getBorder();
        if(border !=null && border instanceof TitledBorder){
            ((TitledBorder)border).setTitle(bundle.getString("GraphViewerPanel.edgePanel.border")+" "+edgeSlider.getValue());
            edgePanel.repaint();
        }
    }


    private void updateBtTitle() {
        Border border = btPanel.getBorder();

        if(border !=null && border instanceof TitledBorder){
            ((TitledBorder)border).setTitle(bundle.getString("GraphViewerPanel.btPanel.border")+" "+btSlider.getValue());
            btPanel.repaint();
        }
    }

    private void button1ActionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        int i = jFileChooser.showOpenDialog(SwingUtilities.getRoot(this));
        
        if(i == JFileChooser.APPROVE_OPTION){

            selectedInputFile = jFileChooser.getSelectedFile();
            inputFileLabel.setText(selectedInputFile.getAbsolutePath());
        }
    }

    private void button2ActionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();

        int i = jFileChooser.showSaveDialog(SwingUtilities.getRoot(this));
        
        if(i == JFileChooser.APPROVE_OPTION){

            selectedOutputFile = jFileChooser.getSelectedFile();
            outputFilePath.setText(selectedOutputFile.getAbsolutePath());

        }

    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("Resources");
        splitPane1 = new JSplitPane();
        panel2 = new JPanel();
        panel4 = new JPanel();
        cyclicButton = new JRadioButton();
        frButton = new JRadioButton();
        kkButton = new JRadioButton();
        edgePanel = new JPanel();
        edgeSlider = new JSlider();
        btPanel = new JPanel();
        btSlider = new JSlider();
        button3 = new JButton();
        panel3 = new JPanel();
        panel6 = new JPanel();
        button1 = new JButton();
        button2 = new JButton();
        inputFileLabel = new JLabel();
        outputFilePath = new JLabel();
        progressBar = new JProgressBar();

        //======== this ========
        setBorder(new EmptyBorder(5, 5, 5, 5));

        //======== splitPane1 ========
        {
            splitPane1.setDividerLocation(200);
            splitPane1.setDividerSize(6);

            //======== panel2 ========
            {
                panel2.setPreferredSize(new Dimension(200, 506));

                //======== panel4 ========
                {
                    panel4.setBorder(new TitledBorder(bundle.getString("GraphViewerPanel.panel4.border")));

                    //---- cyclicButton ----
                    cyclicButton.setText(bundle.getString("GraphViewerPanel.cyclicButton.text"));
                    cyclicButton.setSelected(true);

                    //---- frButton ----
                    frButton.setText(bundle.getString("GraphViewerPanel.frButton.text"));

                    //---- kkButton ----
                    kkButton.setText(bundle.getString("GraphViewerPanel.kkButton.text"));

                    GroupLayout panel4Layout = new GroupLayout(panel4);
                    panel4.setLayout(panel4Layout);
                    panel4Layout.setHorizontalGroup(
                        panel4Layout.createParallelGroup()
                            .addGroup(panel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel4Layout.createParallelGroup()
                                    .addComponent(cyclicButton)
                                    .addComponent(frButton)
                                    .addComponent(kkButton))
                                .addContainerGap(99, Short.MAX_VALUE))
                    );
                    panel4Layout.setVerticalGroup(
                        panel4Layout.createParallelGroup()
                            .addGroup(panel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cyclicButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(frButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kkButton)
                                .addContainerGap(16, Short.MAX_VALUE))
                    );
                }

                //======== edgePanel ========
                {
                    edgePanel.setBorder(new TitledBorder(bundle.getString("GraphViewerPanel.edgePanel.border")));

                    //---- edgeSlider ----
                    edgeSlider.setMaximum(10);
                    edgeSlider.setMinimum(1);
                    edgeSlider.setValue(5);
                    edgeSlider.setToolTipText(bundle.getString("GraphViewerPanel.edgeSlider.toolTipText"));
                    edgeSlider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            slider1StateChanged(e);
                        }
                    });

                    GroupLayout edgePanelLayout = new GroupLayout(edgePanel);
                    edgePanel.setLayout(edgePanelLayout);
                    edgePanelLayout.setHorizontalGroup(
                        edgePanelLayout.createParallelGroup()
                            .addComponent(edgeSlider, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    );
                    edgePanelLayout.setVerticalGroup(
                        edgePanelLayout.createParallelGroup()
                            .addGroup(edgePanelLayout.createSequentialGroup()
                                .addComponent(edgeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(7, Short.MAX_VALUE))
                    );
                }

                //======== btPanel ========
                {
                    btPanel.setBorder(new TitledBorder(bundle.getString("GraphViewerPanel.btPanel.border")));

                    //---- btSlider ----
                    btSlider.setToolTipText(bundle.getString("GraphViewerPanel.btSlider.toolTipText"));
                    btSlider.setMaximum(10000);
                    btSlider.setValue(5000);
                    btSlider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            btSliderStateChanged(e);
                        }
                    });

                    GroupLayout btPanelLayout = new GroupLayout(btPanel);
                    btPanel.setLayout(btPanelLayout);
                    btPanelLayout.setHorizontalGroup(
                        btPanelLayout.createParallelGroup()
                            .addComponent(btSlider, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    );
                    btPanelLayout.setVerticalGroup(
                        btPanelLayout.createParallelGroup()
                            .addGroup(btPanelLayout.createSequentialGroup()
                                .addComponent(btSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }

                //---- button3 ----
                button3.setText(bundle.getString("GraphViewerPanel.button3.text"));

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addComponent(panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(edgePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button3, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(edgePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btPanel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(button3)
                            .addContainerGap(154, Short.MAX_VALUE))
                );
            }
            splitPane1.setLeftComponent(panel2);

            //======== panel3 ========
            {
                panel3.setBackground(Color.white);
                panel3.setLayout(new BorderLayout());
            }
            splitPane1.setRightComponent(panel3);
        }

        //======== panel6 ========
        {
            panel6.setPreferredSize(new Dimension(890, 50));

            //---- button1 ----
            button1.setText(bundle.getString("GraphViewerPanel.button1.text"));
            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    button1ActionPerformed(e);
                }
            });

            //---- button2 ----
            button2.setText(bundle.getString("GraphViewerPanel.button2.text"));
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    button2ActionPerformed(e);
                }
            });

            //---- inputFileLabel ----
            inputFileLabel.setText(bundle.getString("GraphViewerPanel.inputFileLabel.text"));

            //---- outputFilePath ----
            outputFilePath.setText(bundle.getString("GraphViewerPanel.outputFilePath.text"));

            GroupLayout panel6Layout = new GroupLayout(panel6);
            panel6.setLayout(panel6Layout);
            panel6Layout.setHorizontalGroup(
                panel6Layout.createParallelGroup()
                    .addGroup(panel6Layout.createSequentialGroup()
                        .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(button1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel6Layout.createParallelGroup()
                            .addComponent(outputFilePath, GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE)
                            .addComponent(inputFileLabel, GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE)))
            );
            panel6Layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {button1, button2});
            panel6Layout.setVerticalGroup(
                panel6Layout.createParallelGroup()
                    .addGroup(panel6Layout.createSequentialGroup()
                        .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button1)
                            .addComponent(inputFileLabel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button2)
                            .addComponent(outputFilePath))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addComponent(panel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(splitPane1, GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
                .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(panel6, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(splitPane1, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                    .addGap(10, 10, 10)
                    .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(cyclicButton);
        buttonGroup1.add(frButton);
        buttonGroup1.add(kkButton);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSplitPane splitPane1;
    private JPanel panel2;
    private JPanel panel4;
    private JRadioButton cyclicButton;
    private JRadioButton frButton;
    private JRadioButton kkButton;
    private JPanel edgePanel;
    private JSlider edgeSlider;
    private JPanel btPanel;
    private JSlider btSlider;
    private JButton button3;
    private JPanel panel3;
    private JPanel panel6;
    private JButton button1;
    private JButton button2;
    private JLabel inputFileLabel;
    private JLabel outputFilePath;
    private JProgressBar progressBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public JButton getGoButton() {
        return button3;
    }

    public File getSelectedInputFile() {
        return selectedInputFile;
    }

    public File getSelectedOutputFile() {
        return selectedOutputFile;
    }

    public void setErrors(List<String> validate) {


    }

    public Integer getBetweenness() {
        return btSlider.getValue();
    }

    public Integer getEdge() {
        return edgeSlider.getValue();
    }

    public int getGraphType() {

        if(cyclicButton.isSelected()){
            return 1;
        }
        
        if(frButton.isSelected()){
            return 2;
        }
        
        if(kkButton.isSelected()){
            return 3;
        }

        return 1;

    }

    public void setGraphView(final BasicVisualizationServer<Integer, String> visualizationServer) {

        panel3.removeAll();
        panel3.add(visualizationServer);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                visualizationServer.repaint();
                panel3.repaint();
            }
        });

    }

    public void startLoading() {
        progressBar.setIndeterminate(true);
    }

    public void stopLoading(){
        progressBar.setIndeterminate(false);
    }

    public Dimension getViewDimension() {
        return panel3.getSize();
    }

    public JSlider getEdgeSlider() {
        return edgeSlider;
    }

    public JSlider getBtSlider() {
        return btSlider;
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.edgeSlider.addChangeListener(changeListener);
        this.btSlider.addChangeListener(changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        this.edgeSlider.addChangeListener(changeListener);
        this.btSlider.addChangeListener(changeListener);
    }

    public void setInputEnabled(boolean b) {
        btSlider.setEnabled(b);
        edgeSlider.setEnabled(b);
        button1.setEnabled(b);
        button2.setEnabled(b);
        button3.setEnabled(b);
        cyclicButton.setEnabled(b);
        kkButton.setEnabled(b);
        frButton.setEnabled(b);
    }

    public void addActionListener(ActionListener actionListener) {

        this.cyclicButton.addActionListener(actionListener);
        this.kkButton.addActionListener(actionListener);
        this.frButton.addActionListener(actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {

        this.cyclicButton.addActionListener(actionListener);
        this.kkButton.addActionListener(actionListener);
        this.frButton.addActionListener(actionListener);
    }


}
