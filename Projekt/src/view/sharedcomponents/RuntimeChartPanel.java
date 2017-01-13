package view.sharedcomponents;

import view.main.runreport.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import log.*;
import model.*;
import model.runproject.*;
import reflection.NodeDefinition;
import utils.*;
import utils.structures.*;
import view.*;
import static view.Constants.*;

public class RuntimeChartPanel extends JPanel {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

    public ArrayList<Tuple<String, Long>> chartData;
    public long totalRuntime;

    public RuntimeChartPanel() {
    }

    public void setChartData(Debugger debugger) {
        chartData = new ArrayList<>();

        // Daten aus debugger abfragen...
        HashMap<NodeDefinition, Integer> runcountMap;
        HashMap<NodeDefinition, Long> runtimeMap;
        long totalRuntimeWithManagmentInNanos;
        synchronized(debugger) {
            runcountMap = debugger.getRuncountPerNodeMap();
            runtimeMap = debugger.getRuntimeInNanosPerNodeMap();
            totalRuntimeWithManagmentInNanos = debugger.getRuntimeInNanos();
        }

        // Gesammtzeit ermitteln...
        totalRuntime = 0;
        for (Long runtime : runtimeMap.values()) {
            totalRuntime += runtime;
        }
        if (totalRuntime == 0) { // Falls Total 0 ist (Abfrage um Teilen durch 0 sicher zu vermeiden)
            AdditionalErr.println("Summe von Runtime ist 0ns. return.");
            return;
        }

        // Einzelne Zeiten analysieren...
        for (Map.Entry<NodeDefinition, Long> entry : runtimeMap.entrySet()) {
            Long runtime = entry.getValue();
            NodeDefinition node = entry.getKey();
            Integer runcount = runcountMap.containsKey(node) ? runcountMap.get(node) : -1;
            Tuple<String, Long> tuple = new Tuple<>(node.getName() + " (" + runcount + " mal ausgeführt)", runtime);
            chartData.add(tuple);
        }
        
        // Daten sortieren...
        Collections.sort(chartData, new Comparator<Tuple<String, Long>>() {
            @Override
            public int compare(Tuple<String, Long> t1, Tuple<String, Long> t2) {
                return t1.r.compareTo(t2.r);
            }
        });
        
        // Daten reduzieren...
        long otherTimeInNanos = 0;
        int otherCount = 0;
        for(int i = chartData.size()-1; i>4; i--) {
            Tuple<String, Long> removed = chartData.remove(i);
            otherTimeInNanos += removed.r;
            otherCount += 1;
        }
        if(otherCount != 0) {
            Tuple<String, Long> tuple = new Tuple<>("Weitere (" + otherCount + " Elemente)", otherTimeInNanos);
            chartData.add(tuple);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        Drawing.enableAntialiasing(g);

        if (chartData != null && totalRuntime != 0) {
            int size = 100;
            
            {
                int i = 0;
                double startAngle = 0;
                for(Tuple<String, Long> data : chartData) {
                    
                    // Daten entnehmen...
                    String name = data.l;
                    Long runtime = data.r;
                    
                    // Flaechen zeichnen...
                    double arcAngle = (360/1) * (runtime/(double) totalRuntime);
                    g.setColor(Constants.RUNTIME_CHART_DIAGRAM_COLORS[i]);
                    g.fillArc(1, 1, size, size, (int)Math.rint(startAngle), (int)Math.rint(arcAngle));
                    startAngle += arcAngle;
                    
                    // 
                    

                    i++;
                }
            }
            
            // Umrandung zeichnen...
            g.setStroke(ONE_PX_LINE_STROKE);
            g.setColor(Color.BLACK);
            g.drawOval(1, 1, size, size);
            
        } else {
            if(chartData == null) {
                System.err.println("chartData ist null");
            }
            if(totalRuntime == 0) {
                 System.err.println("totalRuntime ist 0");
            }
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
