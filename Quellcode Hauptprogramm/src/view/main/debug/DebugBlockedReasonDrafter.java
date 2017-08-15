package view.main.debug;

import java.awt.Color;
import java.awt.Graphics2D;
import projectrunner.debugger.DebuggerRemote;
import static view.Constants.*;

public class DebugBlockedReasonDrafter {

    public static void drawDebugBlockedReason(Graphics2D g, int leftX, int topY, DebuggerRemote debugger) {

        Long blockedContextIdentifier = debugger.getBlockedContextIdentifier();
        
        String reason = debugger.getBlockedReason();
        if (reason != null) {
            reason = debugger.getDescriptionForContextIdentifier(blockedContextIdentifier) + " >> " + debugger.getBlockedReason() + " <<";
            g.setFont(DEBUGGER_BLOCK_REASON_FONT);
            int w = g.getFontMetrics().stringWidth(reason);
            g.setColor(Color.WHITE);
            g.fillRect(leftX, topY, w + 5 + 5, 24);
            g.setColor(Color.BLACK);
            g.drawRect(leftX, topY, w + 5 + 5, 24);
            g.drawString(reason, leftX+5, topY+18);
            
        }

    }

}
