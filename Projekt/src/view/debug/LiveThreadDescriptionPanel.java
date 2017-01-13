package view.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import log.*;
import model.runproject.*;
import settings.*;
import utils.Drawing;
import utils.measurements.*;
import static view.Constants.*;

public class LiveThreadDescriptionPanel extends JPanel {
    
    private int threadsActive, threadsWaiting, threadsWorking, threadsStarted, threadsFinished;
    
    public LiveThreadDescriptionPanel() {
        clear();
    }
    
    public final void clear() {
        threadsActive = 0;
        threadsWaiting = 0;
        threadsWorking = 0;
    }
    
    public void capture(Debugger debugger) {
        threadsActive = debugger.getActiveThreadCount();
        threadsWaiting = debugger.getWaitingThreadCount();
        threadsWorking = debugger.getWorkingThreadCount();
        threadsStarted = debugger.getStartedthreadCount();
        threadsFinished = debugger.getFinishedthreadCount();
    }
    
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        
        Drawing.disableAntialiasing(g);
        
        int a = 20;
        int c = 15;
        
        // Rechtecke...
        
        g.setColor(DEBUGGER_NODE_WORKING_BACKGROUND_COLOR);
        g.fillRect(1, (a-c)/2, c, c);
        g.setColor(Color.BLACK);
        g.setStroke(ONE_PX_LINE_STROKE);
        g.drawRect(1, (a-c)/2, c, c);
        
        g.setColor(DEBUGGER_NODE_WAITING_BACKGROUND_COLOR);
        g.fillRect(1, (a-c)/2 + a, c, c);
        g.setColor(Color.BLACK);
        g.setStroke(ONE_PX_LINE_STROKE);
        g.drawRect(1, (a-c)/2 + a, c, c);
        
        g.setColor(DEBUGGER_NODE_OTHER_BACKGROUND_COLOR);
        g.fillRect(1, (a-c)/2 + a + a, c, c);
        g.setColor(Color.BLACK);
        g.setStroke(ONE_PX_LINE_STROKE);
        g.drawRect(1, (a-c)/2 + a + a, c, c);
        
        // Klammer...
        
        g.drawLine(190 - 5, (a-c)/2, 190, (a-c)/2);
        g.drawLine(190, (a-c)/2, 190, (a-c)/2 + a + a + c);
        g.drawLine(190 - 5, (a-c)/2 + a + a + c, 190, (a-c)/2 + a + a + c);
        
        g.drawLine(190, a + a /2, 190 + 7, a + a /2);
        
        
        // Text...
        
        Drawing.enableAntialiasing(g);
        
        g.drawString("Arbeitende Threads (" + threadsWorking + ")", 15 + 10, 20 - 6);
        g.drawString("Wartende Threads (" + threadsWaiting + ")", 15 + 10, 20 - 6 + a);
        g.drawString("Andere Threads (" + (threadsActive - threadsWorking - threadsWaiting) + ")", 15 + 10, 20 - 6 + a + a);
        
        g.drawString("Gestartete Threads: " + threadsStarted, 208, 20 - 6 + a/2);
        g.drawString("Bereits beendete Threads: " + threadsFinished, 208, 20 - 6 + a + a/2);
        
        
    }

}
