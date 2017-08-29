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

public class LiveThreadPanel extends JPanel {
    
    private int[] threadsActiveRingStack, threadsWaitingRingStack, threadsWorkingRingStack;
    private int max;
    private int stackSize;
    private int pointer;
    private long counter;
    private final int RING_STACK_SIZE = 100;
    
    public LiveThreadPanel() {
        clear();
    }
    
    public final void clear() {
        stackSize = RING_STACK_SIZE;
        threadsActiveRingStack = new int[stackSize];
        threadsWaitingRingStack = new int[stackSize];
        threadsWorkingRingStack = new int[stackSize];
        max = 3;
        pointer = 0;
        counter = 0;
    }
    
    public void capture(DebuggerRemote debugger) {
        threadsActiveRingStack[pointer] = debugger.getActiveThreadCount();
        threadsWaitingRingStack[pointer] = debugger.getCollectingThreadCount();
        threadsWorkingRingStack[pointer] = debugger.getRunningThreadCount();
        max = Math.max(max, threadsActiveRingStack[pointer]);
        
        pointerPlusPlus();
    }
    
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        int width = getWidth();
        int height = getHeight();
        double slicesWidth = width / (double) (stackSize - 1);
        
        boolean dataAvailable = false;
        
        for(int i=0; i<stackSize-1; i++) {
            
            int iPointer1 = (pointer - i - 1 + stackSize) % stackSize;
            int threadsActive1 = threadsActiveRingStack[iPointer1];
            int threadsWaiting1 = threadsWaitingRingStack[iPointer1];
            int threadsWorking1 = threadsWorkingRingStack[iPointer1];
            int threadsOther1 = threadsActive1 - threadsWaiting1 - threadsWorking1;
            if(threadsActive1 != 0) {
                dataAvailable = true;
            }
            
            int iPointer0 = (pointer - i - 2 + stackSize) % stackSize;
            int threadsActive0 = threadsActiveRingStack[iPointer0];
            int threadsWaiting0 = threadsWaitingRingStack[iPointer0];
            int threadsWorking0 = threadsWorkingRingStack[iPointer0];
            int threadsOther0 = threadsActive0 - threadsWaiting0 - threadsWorking0;
            
            int[] xs = new int[] {
                (int) Math.round(width - (i + 1) * slicesWidth),
                (int) Math.round(width - (i) * slicesWidth),
                (int) Math.round(width - (i) * slicesWidth),
                (int) Math.round(width - (i + 1) * slicesWidth)
            };
            
            int threadsOtherColumHeigth0 = (height/max) * threadsOther0;
            int threadsOtherColumHeigth1 = (height/max) * threadsOther1;
            int[] threadsOtherYs = new int[] {
                height - threadsOtherColumHeigth0,
                height - threadsOtherColumHeigth1,
                height,
                height
            };
            g.setColor(DEBUGGER_NODE_OTHER_INDICATOR_COLOR);
            g.fillPolygon(xs, threadsOtherYs, xs.length);
            
            int threadsWaitingColumHeigth0 = (height/max) * threadsWaiting0;
            int threadsWaitingColumHeigth1 = (height/max) * threadsWaiting1;
            int[] threadsWaitingYs = new int[] {
                height - threadsOtherColumHeigth0 - threadsWaitingColumHeigth0,
                height - threadsOtherColumHeigth1 - threadsWaitingColumHeigth1,
                height - threadsOtherColumHeigth1,
                height - threadsOtherColumHeigth0
            };
            g.setColor(DEBUGGER_NODE_COLLECTING_BACKGROUND_COLOR);
            g.fillPolygon(xs, threadsWaitingYs, xs.length);
            
            int threadsWorkingColumHeigth0 = (height/max) * threadsWorking0;
            int threadsWorkingColumHeigth1 = (height/max) * threadsWorking1;
            int[] threadsWorkingYs = new int[] {
                height - threadsOtherColumHeigth0 - threadsWaitingColumHeigth0 - threadsWorkingColumHeigth0,
                height - threadsOtherColumHeigth1 - threadsWaitingColumHeigth1 - threadsWorkingColumHeigth1,
                height - threadsOtherColumHeigth1 - threadsWaitingColumHeigth1,
                height - threadsOtherColumHeigth0 - threadsWaitingColumHeigth0
            };
            g.setColor(DEBUGGER_NODE_RUNNING_BACKGROUND_COLOR);
            g.fillPolygon(xs, threadsWorkingYs, xs.length);
            
        }
        
        // Warnung falls es nichts zum Anzeigen gibt
        if(!dataAvailable) {
            Drawing.enableAntialiasing(g);
            g.setColor(Color.BLACK);
            String text = "Keine Daten zur Anzeige vorhanden";
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, width/2 - textWidth/2, height/2 + 5);
            Drawing.disableAntialiasing(g);
        }
        
        g.setColor(Color.BLACK);
        g.setStroke(TWO_PX_LINE_STROKE);
        g.drawRect(0, 0, getWidth(), getHeight());
        
    }

    private void pointerPlusPlus() {
        pointer = (pointer + 1) % stackSize;
        counter++;
    }


}
