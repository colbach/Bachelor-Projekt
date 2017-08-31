package model.check;

import java.util.ArrayList;
import java.util.HashSet;

public class CheckResult {

    private final ArrayList<CheckWarning> warnings;
    private final HashSet<String> warningsAsSet;

    public CheckResult() {
        this.warnings = new ArrayList<>();
        this.warningsAsSet = new HashSet<>();
    }

    protected void add(CheckWarning warning) {
        if (warningsAsSet.add(warning.getMessage() + warning.getUserMessage() + warning.projectIsStillRunnable()) == true) {
            warnings.add(warning);
        }
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
