package view.nodecollection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import logging.AdditionalErr;
import logging.AdditionalLogger;
import utils.Drawing;
import view.Constants;
import static view.Constants.*;

public class CategoryPanel extends JPanel {

    private String[] categorys;

    /**
     * Speichert PixelBreite der Kategorien.
     */
    private int[] categoryBounds; // length ist gleich categorys.length+1
    private int actualIndex = 0;

    public CategoryPanel(String[] categorys) {
        setCategorys(categorys);
    }

    public CategoryPanel() {
        setCategorys(new String[0]);
    }

    public final void setCategorys(String[] categorys) {
        this.categorys = categorys;
        this.categoryBounds = new int[categorys.length + 1];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Drawing.enableAntialiasing(g2d);
        int off = 0;
        g.setFont(CATEGORY_LABEL_FONT);
        // Alle...
        {
            String allLabel = "Alle";
            int labelWidth = g2d.getFontMetrics().stringWidth(allLabel);
            int categoryWidth = labelWidth + 10;
            if (actualIndex == 0) {
                g.setColor(CATEGORY_ACTIVE_COLOR);
            } else {
                g.setColor(CATEGORY_NOT_ACTIVE_COLOR);
            }
            g.drawString(allLabel, off + 5, getHeight() / 2 + 5);
            if (this.categoryBounds.length > 0) {
                this.categoryBounds[0] = categoryWidth;
            } else {
                System.err.println("this.categoryBounds.length kleiner 1");
            }
            off += categoryWidth;
        }
        // Kategorien...
        for (int i = 0; i < categorys.length; i++) {
            int labelWidth = g2d.getFontMetrics().stringWidth(categorys[i]);
            int categoryWidth = labelWidth + 10;
            if (actualIndex == i + 1) {
                g.setColor(CATEGORY_ACTIVE_COLOR);
            } else {
                g.setColor(CATEGORY_NOT_ACTIVE_COLOR);
            }
            g.drawString(categorys[i], off + 5, getHeight() / 2 + 5);
            if (this.categoryBounds.length > i + 1) {
                this.categoryBounds[i + 1] = categoryWidth;
            } else {
                System.err.println("this.categoryBounds.length kleiner " + i + " + 1");
            }
            off += categoryWidth;
        }
    }

    public String getCategory() {
        if (actualIndex == 0) {
            return "";
        } else {
            if (actualIndex < categorys.length + 1) {
                return "[" + categorys[actualIndex - 1] + "]";
            } else {
                System.err.println("categorys ist kleiner actual. return \"\"");
                return "";
            }
        }
    }

    public void mouseClick(int x) {
        int off = 0;
        for(int i=0; i<categoryBounds.length; i++) {
            off += categoryBounds[i];
            if(x < off) {
                actualIndex = i;
                return;
            }
        }
        AdditionalLogger.err.println("Click x=" + x + " liegt ausserhalb von Kategorie (max x=" + off + ")");
    }

}
