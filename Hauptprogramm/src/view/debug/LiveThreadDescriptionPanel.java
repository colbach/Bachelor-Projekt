package view.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import projectrunner.debugger.DebuggerRemote;
import settings.*;
import utils.Drawing;
import utils.measurements.*;
import static view.Constants.*;

public class LiveThreadDescriptionPanel extends JPanel {
    
    private int threadsOther, threadsCollecting, threadsRunning, threadsStarted, threadsFinished;
    
    public LiveThreadDescriptionPanel() {
        clear();
    }
    
    public final void clear() {
        threadsOther = 0;
        threadsCollecting = 0;
        threadsRunning = 0;
        threadsStarted = 0;
        threadsFinished = 0;
    }
    
    public void capture(DebuggerRemote debugger) {
        threadsStarted = debugger.getTotalThreadCount();
        threadsFinished = debugger.getFinishedThreadCount();
        threadsRunning = debugger.getRunningThreadCount();
        threadsCollecting = debugger.getCollectingThreadCount();
        threadsOther = debugger.getOtherActiveThreadCount();
    }
    
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        
        Drawing.disableAntialiasing(g);
        
        int a = 20;
        int c = 15;
        
        // Rechtecke...
        
        g.setColor(DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR);
        g.fillRect(1, (a-c)/2, c, c);
        g.setColor(Color.BLACK);
        g.setStroke(ONE_PX_LINE_STROKE);
        g.drawRect(1, (a-c)/2, c, c);
        
        g.setColor(DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR);
        g.fillRect(1, (a-c)/2 + a, c, c);
        g.setColor(Color.BLACK);
        g.setStroke(ONE_PX_LINE_STROKE);
        g.drawRect(1, (a-c)/2 + a, c, c);
        
        g.setColor(DEBUGGER_NODE_OTHER_INDICATOR_COLOR);
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
        
        g.drawString("Arbeitende Threads (" + threadsRunning + ")", 15 + 10, 20 - 6);
        g.drawString("Sammelnde Threads (" + threadsCollecting + ")", 15 + 10, 20 - 6 + a);
        g.drawString("Andere Threads (" + threadsOther + ")", 15 + 10, 20 - 6 + a + a);
        
        g.drawString("Gestartete Threads: " + threadsStarted, 208, 20 - 6 + a/2);
        g.drawString("Bereits beendete Threads: " + threadsFinished, 208, 20 - 6 + a + a/2);
        
    }

}
