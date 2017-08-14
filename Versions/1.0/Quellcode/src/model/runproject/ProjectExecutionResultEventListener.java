package model.runproject;

import model.runproject.report.Report;

public interface ProjectExecutionResultEventListener {

    /**
     * Diese Methode wird nach Beendigung der Ausfuehrung aufgerufen.
     */
    public void takeFinalReport(Report report);

    /**
     * Diese Methode wird aufgerufen wenn der Debugger Aenderungen zu melden
     * hat. Diese Methode wird nicht bei jeder Aenderung sondern in einem fest
     * definierten Interval aufgerufen (dies hat zum Ziehl die UI nicht unnoetig
     * zu beanspruchen). Diese Methode wird nur aufgerufen wenn der Debugger
     * aktiv ist.
     */
    public void debugViewNeedsUpdate();

}
