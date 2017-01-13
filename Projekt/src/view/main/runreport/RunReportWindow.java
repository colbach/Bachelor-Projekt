package view.main.runreport;

import model.Node;
import model.runproject.Debugger;

public class RunReportWindow extends javax.swing.JFrame {

    private final Debugger debugger;
    
    /**
     * Creates new form NodeInfoWindow
     */
    public RunReportWindow(Debugger debugger) {
        this.debugger = debugger;
        initComponents();
        generalRuntimePanel.setDebugger(debugger);
        runtimeChartPanel.setChartData(debugger);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        generalRuntimePanel = new view.main.runreport.GeneralRuntimePanel();
        runtimeChartPanel = new view.sharedcomponents.RuntimeChartPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout generalRuntimePanelLayout = new javax.swing.GroupLayout(generalRuntimePanel);
        generalRuntimePanel.setLayout(generalRuntimePanelLayout);
        generalRuntimePanelLayout.setHorizontalGroup(
            generalRuntimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        generalRuntimePanelLayout.setVerticalGroup(
            generalRuntimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout runtimeChartPanelLayout = new javax.swing.GroupLayout(runtimeChartPanel);
        runtimeChartPanel.setLayout(runtimeChartPanelLayout);
        runtimeChartPanelLayout.setHorizontalGroup(
            runtimeChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        runtimeChartPanelLayout.setVerticalGroup(
            runtimeChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 191, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generalRuntimePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(runtimeChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(generalRuntimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runtimeChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private view.main.runreport.GeneralRuntimePanel generalRuntimePanel;
    private view.sharedcomponents.RuntimeChartPanel runtimeChartPanel;
    // End of variables declaration//GEN-END:variables
}
