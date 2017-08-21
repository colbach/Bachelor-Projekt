package main.startuptasks;

import logging.AdditionalLogger;

public class StartupTaskBatchRunner extends Thread {

    private final StartupTask[] tasks;
    private final int totalWeigth;

    private String actualMessage;
    private double actualProgress;

    public StartupTaskBatchRunner(StartupTask[] tasks) {
        this.tasks = tasks;
        int weigth = 0;
        for (StartupTask task : tasks) {
            if (task.isEnabled()) {
                weigth += task.getWeigth();
            }
            task.setTasks(this);
        }
        this.totalWeigth = weigth;
    }

    protected void changed(String message) {
        if (message != null) {
            this.actualMessage = message;
        }
        actualProgress = calculateProgress();
        AdditionalLogger.out.println("[" + getActualProgressInPercent() + "] " + getActualMessage());
    }

    private double calculateProgress() {
        double weightedProgress = 0;
        for (StartupTask task : tasks) {
            if (task.isEnabled()) {
                weightedProgress += task.getWeightedProgress();
            }
        }
        return weightedProgress / totalWeigth;
    }

    public String getActualMessage() {
        return actualMessage;
    }

    public double getActualProgress() {
        return actualProgress;
    }

    public String getActualProgressInPercent() {
        int percent = (int) Math.round(getActualProgress() * 100);
        return percent + "%";
    }

    public void run() {

        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i].isEnabled()) {
                long start = System.currentTimeMillis();
                try {
                    tasks[i].run();
                    tasks[i].setProgress(1, "Beendet");
                    long duration = System.currentTimeMillis() - start;
                    AdditionalLogger.out.println(tasks[i].getTaskName() + " erfolgreich abgeschlossen (" + duration + "ms)");
                } catch (Exception e) {
                    long duration = System.currentTimeMillis() - start;
                    AdditionalLogger.err.println(tasks[i].getTaskName() + " fehlgeschlagen (" + e.getMessage() + ", " + duration + "ms)");
                }
            }
        }

    }

}
