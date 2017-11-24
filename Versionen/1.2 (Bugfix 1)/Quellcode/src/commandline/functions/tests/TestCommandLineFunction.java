package commandline.functions.tests;

import commandline.CommandLine;
import commandline.CommandLineFooterHeaderFormat;
import commandline.CommandLineFunction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utils.text.CharacterRepeateHelper.*;

public class TestCommandLineFunction implements CommandLineFunction {

    private static final int EXIT_CODE = 0;

    private HashMap<String, Test> tests;

    public TestCommandLineFunction() {
        tests = new HashMap<>();
        tests.putAll(BunchOfTests.get());
    }

    @Override
    public void execute(String[] param, CommandLine commandLine) {
        if (param.length == 0) {
            for (String testName : tests.keySet()) {
                System.out.println(testName);
            }
        } else {
            Test test = tests.get(param[0]);
            if (test != null) {
                test.doTest();
            } else {
                System.out.println("Test \"" + param[0] + "\" nicht gefunden.");
            }
        }
    }

    @Override
    public String getDescription() {
        return "Testmethode welche spezifische Tests ausfuehrt.";
    }

    @Override
    public String getLongDescription() {
        return getDescription() + "\nOhne Argument wird eine Liste der verfuegbaren Tests ausgegeben. Folgende Tests sind verfuegbar: " + getAllTestsAsOneString();
    }

    public String getAllTestsAsOneString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String testName : tests.keySet()) {
            if (!first) {
                sb.append(" ");
            }
            sb.append(testName);
            first = false;
        }
        return sb.toString();
    }

    @Override
    public String getUsage() {
        return getName() + " [<test>]\n"
                + "@<test> Name des auszufuehrenden Tests.";
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getAliases() {
        return null;
    }
}
