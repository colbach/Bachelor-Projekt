/*package view.onrun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DecimalFormat;
import javax.swing.JPanel;

import static photopanel.drawing.DrawingConstants.*;
import photopanel.drawing.DrawingTools;

public class ShowBitmap extends JPanel{
    
    private static DecimalFormat format = new DecimalFormat("#0.0");
    private int mouseX = -1;
    private int mouseY = -1;
    
    private Datatype data;
    private long representation = 0; // Jeder Wert moeglich. Wert wird immer % Moeglichkeiten genommen

    public ShowBitmap(Datatype data) {
        this.data = data;
    }
    
    public Dimension getIdealSize() {
        if(data instanceof SingleValue) {
            return new Dimension(380, 40);
        } else if(data instanceof TrueFalse) {
            return new Dimension(200, 40);
        } else if(data instanceof Bitmap) {
            Bitmap b = (Bitmap) data;
            return new Dimension(b.getWidth(), b.getHeight());
        } else if(data instanceof ValueArray) {
            return new Dimension(1000, 500);
        }
        return new Dimension(500, 500);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(data instanceof SingleValue) {
            SingleValue singleValue = (SingleValue) data;
            
            g.setFont(SHOW_SINGLEVALUE_DESCRIPTION_FONT);
            int descriptionWidth = DrawingTools.getTextWidth(g, singleValue.getDescription() + ": ");
            g.setFont(SHOW_SINGLEVALUE_VALUE_FONT);
            int valueWidth = DrawingTools.getTextWidth(g, singleValue.getNumberAndString());
            
            g.setColor(Color.WHITE);
            g.fillRect(5, 5, descriptionWidth+valueWidth + 10, 30);
            
            g.setColor(Color.BLACK);
            
            g.setFont(SHOW_SINGLEVALUE_DESCRIPTION_FONT);
            g.drawString(singleValue.getDescription() + ":", 10, 30);
            g.setFont(SHOW_SINGLEVALUE_VALUE_FONT);
            g.drawString(singleValue.getNumberAndString(), descriptionWidth + 10, 30);
            
        } else if(data instanceof TrueFalse) {
            TrueFalse tf = (TrueFalse) data;
            
            g.setFont(SHOW_SINGLEVALUE_DESCRIPTION_FONT);
            int descriptionWidth = DrawingTools.getTextWidth(g, tf.getDescription() + ": ");
            g.setFont(SHOW_SINGLEVALUE_VALUE_FONT);
            int valueWidth = DrawingTools.getTextWidth(g, tf.toString());
            
            g.setColor(Color.WHITE);
            g.fillRect(5, 5, descriptionWidth+valueWidth + 10, 30);
            
            g.setColor(Color.BLACK);
            
            g.setFont(SHOW_SINGLEVALUE_DESCRIPTION_FONT);
            g.drawString(tf.getDescription() + ":", 10, 30);
            g.setFont(SHOW_SINGLEVALUE_VALUE_FONT);
            g.drawString(tf.getValueAsString(), descriptionWidth + 10, 30);
            
        } else if(data instanceof Bitmap) {
            Bitmap b = (Bitmap) data;
            
            g.drawImage(b.getImage(), 0, 0, null);
        } else if(data instanceof ValueArray) {
            ValueArray va = (ValueArray) data;
            long representation = this.representation%2;
            
            g.setColor(Color.WHITE);
            
            int offX = 60;
            int offY = 30;
            int width = getWidth() - 100;
            int height = getHeight() - 50;
            g.fillRect(offX, offY, width, height);
            g.setColor(Color.BLACK);
            
            g.setFont(SHOW_VALUE_ARRAY_FONT);
            g.drawString(va.getValues().length + " Werte in einem Bereich von " + format.format(va.getMinimum()) + " bis " + format.format(va.getMaximum()) + ". Ansicht: " + (representation == 0 ? "Blöcke" : "Kurve") + " (Klicken um Ansicht umzuschalten)", 2, 18);
            
            double[] v0t1 = va.getValuesNormalized0To1();
            double steps = (double)width / v0t1.length;
            
            //Graphics2D g2d = (Graphics2D) g;
            //g2d.setStroke(new BasicStroke(3));
            
            int lastX = -1;
            int lastSize = -1;
            for(int i=0; i<v0t1.length; i++) {
                int x = (int)(steps * i) + offX;
                int size = (int)(height * v0t1[i]);
                
                if(representation == 0) {
                    g.fillRect(x, offY+height-size, up(steps), size);
                } else {
                    if(lastX != -1 && lastSize != -1) {
                        g.drawLine(lastX+up(steps/2), height-lastSize+offY, x+up(steps/2), height-size+offY);
                    }
                }
                
                lastX = x;
                lastSize = size;
            }
            
            g.drawLine(0, offY, offX, offY);
            g.drawLine(0, offY+height, offX, offY+height);
            
            g.setFont(SHOW_VALUE_ARRAY_FONT);
            g.drawString(format.format(va.getMaximum()), 2, offY+15);
            g.drawString(format.format(va.getMinimum()), 2, offY+height-5);
            
            if(mouseX > offX && mouseX < offX+width && mouseY > offY && mouseY < offY+height) {
                g.setColor(Color.BLACK);
                g.drawLine(0, mouseY, getWidth(), mouseY);
                g.drawLine(mouseX, 0, mouseX, getHeight());
                
                int diaX = mouseX-offX;
                int diaY = mouseY-offY;
                double proX = diaX/(double)width;
                double proY = (height-diaY)/(double)height;
                
                double div = va.getMaximum()-va.getMinimum();
                double vY = proY*div;
                double vX = proX*va.getValues().length;
                
                if(diaY > height/2) {
                    g.drawString(format.format(vY), 2, mouseY - 3);
                } else {
                    g.drawString(format.format(vY), 2, mouseY + 16);
                }
                
                g.drawString(""+Math.round(vX), mouseX+2, offY+height+15);
            }
            //g.drawImage(b.getImage(), 0, 0, null);
        }
        
        
    }

    void mousePositionChanged(int x, int y) {
        mouseX = x;
        mouseY = y;
    }
    
    private static int up(double d) {
        int r = (int)d;
        if(r - d < 0)
            r++;
        return r;
    }

    void mousePressed(int x, int y) {
        representation++;
    }
    
    
}*/
