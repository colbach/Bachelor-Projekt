package main.startuptasks;

import logging.AdditionalLogger;

public abstract class StartupTask implements ProgressIndicator {

    private final String taskName;
    private final int weigth;
    private double progress;
    private StartupTaskBatchRunner tasks;
    private boolean enabled;

    public StartupTask(int weigth, String taskName, boolean enabled) {
        this.weigth = weigth;
        this.taskName = taskName;
        this.enabled = enabled;
        progress = 0F;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public synchronized void setTasks(StartupTaskBatchRunner tasks) {
        this.tasks = tasks;
    }

    public void setProgress(double progress, String message) {
        if (progress > 1) {
            progress = 1;
            AdditionalLogger.err.println("progress > 1 (" + taskName + ")! progress = 1.");
        } else if (progress < 0) {
            progress = 0;
            AdditionalLogger.err.println("progress < 0 (" + taskName + ")! progress = 0.");
        } else {
            this.progress = progress;
        }
        if (message != null) {
            tasks.changed(taskName + ": " + message);
        } else {
            tasks.changed(taskName);
        }
    }

    @Override
    public synchronized void addProgress(double progress, String message) {
        setProgress(getProgress() + progress, message);
    }

    public synchronized int getWeigth() {
        return weigth;
    }

    public String getTaskName() {
        return taskName;
    }

    public synchronized double getWeightedProgress() {
        return weigth * progress;
    }

    public synchronized double getProgress() {
        return progress;
    }

    public abstract void run();

}
