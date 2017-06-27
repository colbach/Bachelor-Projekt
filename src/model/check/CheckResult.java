package model.check;

import java.util.ArrayList;

public class CheckResult {

    private final ArrayList<CheckWarning> warnings;

    public CheckResult() {
        this.warnings = new ArrayList<>();
    }

    protected void add(CheckWarning warning) {
        warnings.add(warning);
    }

    public int size() {
        return warnings.size();
    }

    public CheckWarning get(int index) {
        return warnings.get(index);
    }

    public boolean isEmpty() {
        return warnings.isEmpty();
    }

    public boolean projectIsStillRunnable() {
        if (!warnings.stream().noneMatch(
                (warning) -> (!warning.projectIsStillRunnable()))) {
            return false;
        }
        return true;
    }

}
