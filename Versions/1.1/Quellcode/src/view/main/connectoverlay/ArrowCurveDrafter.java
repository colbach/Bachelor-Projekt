package view.main.connectoverlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;
import static view.Constants.*;

public class ArrowCurveDrafter {
    
    public static void drawArrowCurve(Graphics2D g, int x1, int y1, int x2, int y2) {
        
        g.setColor(Color.WHITE);
        g.setStroke(TWO_PX_LINE_STROKE);
        
        CubicCurve2D.Double cc = new CubicCurve2D.Double(x1, y1, x1 + 70, y1, x2 - 70, y2, x2-3, y2);
        g.draw(cc);

        g.fillPolygon( //  >
                new int[]{x2 + 1, x2 + 1 - 7, x2 + 1 - 7},
                new int[]{y2, y2 - 5, y2 + 5},
                3);
        
    }
    
}
