package view.onrun.showmathobject;

import view.onrun.shownumberarray.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import logging.AdditionalOut;
import reflection.customdatatypes.SmartIdentifier;
import reflection.customdatatypes.math.MathObject;
import reflection.customdatatypes.math.NumberMathObject;
import utils.images.ImageSaving;
import utils.text.TextSaving;
import view.listener.InputManager;
import view.onrun.DragAreaListener;
import view.onrun.OnRunWindowManager;
import view.onrun.ShowSomethingWindow;
import view.onrun.UnsupportedShowTypeException;

public class ShowMathObjectWindow extends ShowSomethingWindow {

    private MathObject m = null;
    private boolean closed = false;
    private final OnRunWindowManager onRunWindowManager;

    public ShowMathObjectWindow(MathObject m, SmartIdentifier smartIdentifier, OnRunWindowManager onRunWindowManager) {
        initComponents();
        setMathObject(m);
        setTitle(smartIdentifier.getIdentifierName());
        this.onRunWindowManager = onRunWindowManager;
    }

    @Override
    public void releaseMemory() {
        m = null;
        showMathObjectArrayPanel.releaseMemory();
    }

    /**
     * Setzt MathObject und aktualisiert abhaengige View.
     */
    public final void setMathObject(MathObject m) {
        if (m != null && this.m != m) {
            this.m = m;
            showMathObjectArrayPanel.setMathObject(m);
            repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayGroup = new javax.swing.ButtonGroup();
        showMathObjectArrayPanel = new view.onrun.showmathobject.ShowMathObjectPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        javax.swing.GroupLayout showMathObjectArrayPanelLayout = new javax.swing.GroupLayout(showMathObjectArrayPanel);
        showMathObjectArrayPanel.setLayout(showMathObjectArrayPanelLayout);
        showMathObjectArrayPanelLayout.setHorizontalGroup(
            showMathObjectArrayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 483, Short.MAX_VALUE)
        );
        showMathObjectArrayPanelLayout.setVerticalGroup(
            showMathObjectArrayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Als Datei speichern");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Fenster");

        jMenuItem1.setText("Alle Fenster schliessen");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setText("Alle Fenster schliessen (Alle Laufzeiten)");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(showMathObjectArrayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(showMathObjectArrayPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        closed = true;
        releaseMemory();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closed = true;
        releaseMemory();
    }//GEN-LAST:event_formWindowClosing

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        TextSaving.saveTextToFileViaDialog(m.toString(), this, ".");
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (onRunWindowManager != null) {
            onRunWindowManager.disposeAll();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        for (OnRunWindowManager orwm : OnRunWindowManager.getInstances()) {
            orwm.disposeAll();
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup displayGroup;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem saveMenuItem;
    private view.onrun.showmathobject.ShowMathObjectPanel showMathObjectArrayPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean canShow(Object o) {
        return o instanceof MathObject;
    }

    @Override
    public void show(Object o) throws UnsupportedShowTypeException {
        if (o instanceof Number) {
            o = new NumberMathObject((Number) o);
        }
        if (o instanceof MathObject) {
            setMathObject((MathObject) o);
        } else {
            throw new UnsupportedShowTypeException(o.getClass());
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
