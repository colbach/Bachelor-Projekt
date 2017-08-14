package view.main.runreport;

import utils.structures.tuples.Pair;
import logging.AdditionalErr;
import utils.format.TimeFormat;
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
import logging.AdditionalLogger;
import model.*;
import model.runproject.report.Report;
import model.runproject.stats.Stats;
import reflection.NodeDefinition;
import utils.*;
import utils.structures.*;
import view.*;
import static view.Constants.*;

public class RuntimeChartPanel extends JPanel {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

    public ArrayList<Pair<String, Long>> chartData;
    public long totalRuntime;

    public RuntimeChartPanel() {
    }

    public void setChartData(Report report) {
        Stats stats = report.getStats();
        
        chartData = new ArrayList<Pair<String, Long>>();

        // Daten aus report abfragen...
        Map<Node, Integer> runcountMap;
        Map<Node, Long> runtimeMap;
        long totalRuntimeWithManagmentInNanos;
        synchronized(report) {
            runcountMap = stats.getRuncountPerNodeMap();
            runtimeMap = stats.getRuntimeInNanosPerNodeMap();
            totalRuntimeWithManagmentInNanos = report.getRuntimeInNanos();
        }

        // Gesammtzeit ermitteln...
        totalRuntime = 0;
        for (Long runtime : runtimeMap.values()) {
            totalRuntime += runtime;
        }
        if (totalRuntime == 0) { // Falls Total 0 ist (Abfrage um Teilen durch 0 sicher zu vermeiden)
            AdditionalLogger.err.println("Summe von Runtime ist 0ns. return.");
            return;
        }

        // Einzelne Zeiten analysieren...
        for (Map.Entry<Node, Long> entry : runtimeMap.entrySet()) {
            Long runtime = entry.getValue();
            Node node = entry.getKey();
            Integer runcount = runcountMap.containsKey(node) ? runcountMap.get(node) : -1;
            Pair<String, Long> tuple = new Pair<>(node.getName() + " (" + runcount + " mal ausgeführt)", runtime);
            chartData.add(tuple);
        }
        
        // Daten sortieren...
        Collections.sort(chartData, new Comparator<Pair<String, Long>>() {
            @Override
            public int compare(Pair<String, Long> t1, Pair<String, Long> t2) {
                return t2.r.compareTo(t1.r);
            }
        });
        
        // Daten reduzieren...
        long otherTimeInNanos = 0;
        int otherCount = 0;
        for(int i = chartData.size()-1; i>4; i--) {
            Pair<String, Long> removed = chartData.remove(i);
            otherTimeInNanos += removed.r;
            otherCount++;
        }
        if(otherCount != 0) {
            Pair<String, Long> tuple = new Pair<>("Weitere (" + otherCount + " Elemente)", otherTimeInNanos);
            chartData.add(tuple);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        Drawing.enableAntialiasing(g);

        if (chartData != null) {
            int size = 120;
            
            if(totalRuntime != 0) {
                int i = 0;
                double startAngle = 0;
                for(Pair<String, Long> data : chartData) {
                    
                    // Daten entnehmen...
                    String name = data.l;
                    Long runtime = data.r;
                    
                    // Flaechen zeichnen...
                    double arcAngle = (360/1) * (runtime/(double) totalRuntime);
                    g.setColor(RUNTIME_CHART_DIAGRAM_COLORS[i]);
                    g.fillArc(1, 1, size, size, (int) startAngle, (int) arcAngle + 2);
                    startAngle += arcAngle;
                    
                    // Beschriftungen...
                    Drawing.disableAntialiasing(g);
                    int a = 20;
                    int c = 15;
                    g.setColor(RUNTIME_CHART_DIAGRAM_COLORS[i]);
                    g.fillRect(size + 15, (a-c)/2+i*a, c, c);
                    g.setColor(Color.BLACK);
                    g.setStroke(ONE_PX_LINE_STROKE);
                    g.drawRect(size + 15, (a-c)/2+i*a, c, c);
                    Drawing.enableAntialiasing(g);
                    g.drawString(name + ": " + TimeFormat.formatNanos(runtime) , size + 20 + a, 20 - 6+i*a);
                    
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
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
