package view.onrun.showimage;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import logging.AdditionalLogger;
import logging.AdditionalOut;
import reflection.customdatatypes.SmartIdentifier;
import utils.images.ImageSaving;
import view.listener.InputManager;
import view.onrun.DragAreaListener;
import view.onrun.OnRunWindowManager;
import view.onrun.ShowSomethingWindow;
import view.onrun.UnsupportedShowTypeException;

public class ShowBitmapWindow extends ShowSomethingWindow {

    private BufferedImage bitmap = null;
    private boolean closed = false;
    private final InputManager inputManager;
    private final OnRunWindowManager onRunWindowManager;

    public ShowBitmapWindow(BufferedImage bitmap, SmartIdentifier smartIdentifier, OnRunWindowManager onRunWindowManager) {
        this.inputManager = new InputManager();
        initComponents();
        setBitmap(bitmap);
        this.onRunWindowManager = onRunWindowManager;
        inputManager.addListener(showBitmapPanel.getVerticalScrollbar());
        inputManager.addListener(showBitmapPanel.getHorizontalScrollbar());
        inputManager.addListener(new DragAreaListener(showBitmapPanel.getVerticalScrollbar(), showBitmapPanel.getHorizontalScrollbar()));
        setTitle(smartIdentifier.getIdentifierName());
    }

    /**
     * Setzt Bitmap und aktualisiert abhaengige View.
     */
    public final void setBitmap(BufferedImage bitmap) {
        if (bitmap != null && this.bitmap != bitmap) {
            this.bitmap = bitmap;
            showBitmapPanel.setBitmap(bitmap);
            repaint();
        }
    }

    @Override
    public void releaseMemory() {
        bitmap = null;
        showBitmapPanel.releaseMemory();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        showBitmapPanel = new view.onrun.showimage.ShowBitmapPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        scaleUpMenuItem = new javax.swing.JMenuItem();
        scaleDownMenuItem = new javax.swing.JMenuItem();
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

        showBitmapPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                showBitmapPanelMouseDragged(evt);
            }
        });
        showBitmapPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                showBitmapPanelMouseWheelMoved(evt);
            }
        });
        showBitmapPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showBitmapPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                showBitmapPanelMouseReleased(evt);
            }
        });
        showBitmapPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                showBitmapPanelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout showBitmapPanelLayout = new javax.swing.GroupLayout(showBitmapPanel);
        showBitmapPanel.setLayout(showBitmapPanelLayout);
        showBitmapPanelLayout.setHorizontalGroup(
            showBitmapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        showBitmapPanelLayout.setVerticalGroup(
            showBitmapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
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

        jMenu2.setText("Anschicht");

        scaleUpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PLUS, java.awt.event.InputEvent.CTRL_MASK));
        scaleUpMenuItem.setText("Vergrößern");
        scaleUpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleUpMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(scaleUpMenuItem);

        scaleDownMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
        scaleDownMenuItem.setText("Verkleinern");
        scaleDownMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleDownMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(scaleDownMenuItem);

        jMenuBar1.add(jMenu2);

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
            .addComponent(showBitmapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(showBitmapPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void showBitmapPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showBitmapPanelMousePressed
        inputManager.mousePressed(evt.getX(), evt.getY(), evt.getButton() == MouseEvent.BUTTON1);
        repaint();
    }//GEN-LAST:event_showBitmapPanelMousePressed

    private void showBitmapPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_showBitmapPanelComponentResized
        AdditionalLogger.out.println("Scrollbar-Area von LogPanel wird aktualisiert");
        showBitmapPanel.updateScrollbarArea();
        repaint();
    }//GEN-LAST:event_showBitmapPanelComponentResized

    private void showBitmapPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showBitmapPanelMouseDragged
        inputManager.mouseDragged(evt.getX(), evt.getY());
        repaint();
    }//GEN-LAST:event_showBitmapPanelMouseDragged

    private void showBitmapPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showBitmapPanelMouseReleased
        inputManager.mouseReleased(evt.getX(), evt.getY());
        repaint();
    }//GEN-LAST:event_showBitmapPanelMouseReleased

    private void showBitmapPanelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_showBitmapPanelMouseWheelMoved
        //inputManager.mouseMouseWheelMoved(evt.getUnitsToScroll());
        repaint();
    }//GEN-LAST:event_showBitmapPanelMouseWheelMoved

    private void scaleDownMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleDownMenuItemActionPerformed
        showBitmapPanel.scaleMinusMinus();
        repaint();
    }//GEN-LAST:event_scaleDownMenuItemActionPerformed

    private void scaleUpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleUpMenuItemActionPerformed
        showBitmapPanel.scalePlusPlus();
        repaint();
    }//GEN-LAST:event_scaleUpMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        ImageSaving.saveImageToFileViaDialog(bitmap, this, ".");
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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem scaleDownMenuItem;
    private javax.swing.JMenuItem scaleUpMenuItem;
    private view.onrun.showimage.ShowBitmapPanel showBitmapPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean canShow(Object o) {
        return o instanceof BufferedImage;
    }

    @Override
    public void show(Object o) throws UnsupportedShowTypeException {
        if (o instanceof BufferedImage) {
            setBitmap((BufferedImage) o);
        } else {
            throw new UnsupportedShowTypeException(o.getClass());
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}